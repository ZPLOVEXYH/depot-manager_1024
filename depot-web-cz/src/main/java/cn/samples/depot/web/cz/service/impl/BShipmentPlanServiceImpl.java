package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.constant.MqQueueConstant;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.AuditStatus;
import cn.samples.depot.common.model.DropBoxStatus;
import cn.samples.depot.common.model.GoodsConfirmStatus;
import cn.samples.depot.web.bean.BShipmentPlanDTO;
import cn.samples.depot.web.bean.BShipmentPlanQuery;
import cn.samples.depot.web.bean.railwayarrive.ArriveVo;
import cn.samples.depot.web.cz.mapper.BShipmentAuditLogMapper;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.cz.service.event.ContaStatusEvent;
import cn.samples.depot.web.dto.shipment.BShipmentPlanMsg;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.mapper.BShipmentPlanMapper;
import cn.samples.depot.web.service.UserService;
import cn.samples.depot.web.service.event.EventEmitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * @Author majunzi
 * @Date 2019/7/29
 * @Description  发运计划表
 **/
@Slf4j
@Service
public class BShipmentPlanServiceImpl extends ServiceImpl<BShipmentPlanMapper, BShipmentPlan> implements BShipmentPlanService {
    @Autowired
    private BShipmentPlanMapper planMapper;
    @Autowired
    private BShipmentAuditLogMapper auditLogMapper;
    @Autowired
    private BShipmentContainerService containerService;
    @Autowired
    private BShipmentGoodsDetailService goodsDetailService;
    @Autowired
    private BShipmentAuditLogService logService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //落箱
    @Autowired
    private BDropBoxPlanService dropBoxPlanService;
    @Autowired
    private BDropBoxPlanDetailService dropBoxPlanDetailService;
    @Autowired
    private BDropBoxConfirmService dropBoxConfirmService;
    @Autowired
    private BDropBoxConfirmDetailService dropBoxConfirmDetailService;
    @Autowired
    private BGoodsArriveConfirmService goodsArriveConfirmService;
    @Autowired
    private BExRailwayShipmentPlanLogService railwayShipmentPlanLogService;


    @Autowired
    private UserService userService;

    @Autowired
    private EventEmitter emitter;

    /*
     * @Author majunzi
     * @Date 2019/7/29
     * @Description  作废 todo 落箱计划 待落箱，不允许作废 majunzi
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(String id) throws Exception {
        checkCanOption(id, AuditStatus.AUDIT_PASS, AuditStatus.INVALID);
        updateStatus(BShipmentAuditLog.builder().shipmentPlanId(id).auditRemark(AuditStatus.INVALID.getTitle()).auditResult(AuditStatus.INVALID.getValue()).build());
        return true;
    }


    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 审核 1.只有待审核状态的发运计划可以 审核。
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean audit(BShipmentAuditLog log) throws Exception {
        BShipmentPlan plan = checkCanOption(log.getShipmentPlanId(), AuditStatus.PRE_AUDIT, AuditStatus.findByValue(log.getAuditResult()));
        if (AuditStatus.AUDIT_PASS.getValue().equals(log.getAuditResult())) {
            List<BShipmentContainer> containers = auditPass(plan);
            emitter.emit(new ContaStatusEvent(BShipmentPlanDTO.builder().shipmentPlan(plan).containerList(containers).build()));
        }
        updateStatus(log);
        return true;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 状态更新：1.更新发运计划状态，2.记录日志，3.通知企业端
     **/
    private void updateStatus(BShipmentAuditLog log) throws Exception {
        //更新状态
        planMapper.updateById(BShipmentPlan.builder().id(log.getShipmentPlanId()).auditStatus(log.getAuditResult()).build());
        //记录日志
        log.setAuditTime(System.currentTimeMillis());
        log.setAuditor(userService.currentUserName());
        auditLogMapper.insert(log);
        //通知企业端
        sendLog2Mq(log);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/2
     * @Description 根据当前状态 判断是否可以 审核，作废
     **/
    private BShipmentPlan checkCanOption(String id, AuditStatus canOption, AuditStatus option) throws BizException {
        BShipmentPlan plan = planMapper.selectById(id);
        if (null == plan) throw new BizException("找不到相关数据");
        if (!plan.getAuditStatus().equals(canOption.getValue()))
            throw new BizException(String.format("仅[%s]状态可以[%s]", canOption.getTitle(), option.getTitle()));
        return plan;
    }


    /*
     * @Author majunzi
     * @Date 2019/8/2
     * @Description  审核通过
     * 1.落箱计划中不存在集装箱 新增落箱计划  新增落箱计划-发运计划
     * 2.落箱计划中存在集装箱 状态为待计划 添加发运计划
     *                       状态为待落箱  查看落箱确认状态为“已到货” 则不允许审核通过，否则落箱计划增加发运计划，落箱确认增加发运计划
     *                      状态为已落箱 不允许审核通过
     * 3.新增货到确认
     **/
    private List<BShipmentContainer> auditPass(BShipmentPlan plan) throws BizException {
        List<BShipmentContainer> containers = containerService.listByShipmentPlanId(plan.getId());
        if (CollectionUtils.isEmpty(containers)) throw new BizException("找不到相关集装箱数据");
        List<BDropBoxPlan> dropBoxPlans = new ArrayList<>();
        List<BDropBoxPlanDetail> dropBoxPlanDetails = new ArrayList<>();
        List<BDropBoxConfirmDetail> dropBoxConfirmDetails = new ArrayList<>();
        List<BGoodsArriveConfirm> goodsArriveConfirms = new ArrayList<>();
        for (BShipmentContainer container : containers) {
            String containerNo = container.getContainerNo();
            BDropBoxPlan boxPlan = dropBoxPlanService.getOne(new LambdaQueryWrapper<BDropBoxPlan>().eq(BDropBoxPlan::getContainerNo, containerNo));
            BDropBoxPlanDetail planDetail = buildDropBoxPlanDetailWithoutPlanId(plan);
            //落箱计划中不存在集装箱 新增落箱计划  新增落箱计划-发运计划
            if (null == boxPlan) {
                boxPlan = buildDropBoxPlan(plan, container);
                dropBoxPlans.add(boxPlan);
                planDetail.setDropBoxPlanId(boxPlan.getId());
                dropBoxPlanDetails.add(planDetail);
            }
            //落箱计划中存在集装箱
            else {
                planDetail.setDropBoxPlanId(boxPlan.getId());
                DropBoxStatus status = DropBoxStatus.findByValue(boxPlan.getStatus());
                switch (status) {
                    case PRE_PLAN:
                        // 状态为待计划 添加发运计划
                        dropBoxPlanDetails.add(planDetail);
                        break;
                    case PRE_DROP:
                        //状态为待落箱  查看落箱确认状态为“已到货” 则不允许审核通过，否则落箱计划增加发运计划，落箱确认增加发运计划
                        BDropBoxConfirm dropBoxConfirm = dropBoxConfirmService.getOne(new LambdaQueryWrapper<BDropBoxConfirm>().eq(BDropBoxConfirm::getContainerNo, container.getContainerNo()));
                        if (null == dropBoxConfirm)
                            throw new BizException(String.format("集装箱号[%s]对应的落箱计划状态为[待落箱],但不存在落箱确认数据，请联系管理员", containerNo));
                        if (GoodsConfirmStatus.GOODS_ARRIVAL.getValue().equals(dropBoxConfirm.getEntryStatus()))
                            throw new BizException(String.format("集装箱号[%s]对应的落箱确认到货状态为[已到货],不允许审核通过", containerNo));
                        dropBoxPlanDetails.add(planDetail);
                        dropBoxConfirmDetails.add(BDropBoxPlanServiceImpl.buildDropBoxConfirmDetail(planDetail));
                        break;
                    case DROPPED:
                        //状态为已落箱 不允许审核通过 
                        throw new BizException(String.format("集装箱号[%s]对应的落箱计划状态为[已落箱],不允许审核通过", containerNo));
                    default:
                        throw new BizException(String.format("集装箱号[%s]对应的落箱计划状态错误", containerNo));
                }
            }
            //新增货到确认
            goodsArriveConfirms.add(buildGoodsArriveConfirm(plan, container));
        }
        dropBoxPlanService.saveBatch(dropBoxPlans);
        dropBoxPlanDetailService.saveBatch(dropBoxPlanDetails);
        dropBoxConfirmDetailService.saveBatch(dropBoxConfirmDetails);
        goodsArriveConfirmService.saveBatch(goodsArriveConfirms);
        return containers;
    }


    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 接收企业端发过来的发运计划 1.发运计划 2.发运计划集装箱项，3商品项 4日志 5.返回企业端 接受情况
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePlanMsg(BShipmentPlanMsg planMsg) {
        BShipmentPlan plan = planMsg.buildBShipmentPlan();
        plan.setAuditStatus(AuditStatus.PRE_AUDIT.getValue());
        BShipmentAuditLog log = BShipmentAuditLog.builder().shipmentPlanId(plan.getId()).auditor(userService.currentUserName()).auditTime(System.currentTimeMillis()).auditRemark("场站接收成功,待审核").auditResult(AuditStatus.PRE_AUDIT.getValue()).build();
        try {
            List<BShipmentContainer> containers = planMsg.buildBShipmentContainers();
            List<BShipmentGoodsDetail> goodsDetails = planMsg.buildBShipmentGoodsDetail();
            saveOrUpdate(plan);
            containerService.saveOrUpdateBatch(containers);
            goodsDetailService.saveOrUpdateBatch(goodsDetails);
            logService.save(log);
        } catch (BizException be) {
            log.setAuditRemark(be.getMessage());
            log.setAuditResult(AuditStatus.RECEIVE_FAIL.getValue());
        } finally {
            sendLog2Mq(log);
        }

    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 将发运计划日志 发送给企业端
     **/
    private void sendLog2Mq(BShipmentAuditLog log) {
        rabbitTemplate.convertAndSend(MqQueueConstant.AUDIT_EXCHANGE, MqQueueConstant.AUDIT_ROUTING, log, new CorrelationData(log.getId()));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 构建落箱计划
     **/
    private BDropBoxPlan buildDropBoxPlan(BShipmentPlan plan, BShipmentContainer container) {
        return BDropBoxPlan.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .containerNo(container.getContainerNo())
                .sealNo(container.getSealNo())
                .contaModelName(container.getContaModelName())
                .enterprisesId(plan.getEnterprisesId())
                .enterprisesName(plan.getEnterprisesName())
                .shipmentTime(plan.getShipmentTime())
                .status(DropBoxStatus.PRE_PLAN.getValue())
                .opUser(userService.currentUserName())
                .createTime(System.currentTimeMillis())
                .build();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 构建落箱计划的 发运计划项
     **/
    private BDropBoxPlanDetail buildDropBoxPlanDetailWithoutPlanId(BShipmentPlan plan) {
        return BDropBoxPlanDetail.builder()
                //.dropBoxPlanId(dropBoxPlan.getId())
                .enterprisesId(plan.getEnterprisesId())
                .enterprisesName(plan.getEnterprisesName())
                .shipmentPlanId(plan.getId())
                .shipmentPlanNo(plan.getShipmentPlanNo())
                .pieceTotal(plan.getPieceTotal())
                .weightTotal(plan.getWeightTotal())
                .createTime(System.currentTimeMillis())
                .build();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 构建货到确认
     **/
    private BGoodsArriveConfirm buildGoodsArriveConfirm(BShipmentPlan plan, BShipmentContainer container) {
        return BGoodsArriveConfirm.builder()
                .containerNo(container.getContainerNo())
                .stationCode(plan.getStationsCode())
                .enterprisesId(plan.getEnterprisesId())
                .enterprisesName(plan.getEnterprisesName())
                .createTime(System.currentTimeMillis())
                .build();
    }

    @Override
    public BShipmentPlan getByPlanNo(String planNo) {
        return getOne(new LambdaQueryWrapper<BShipmentPlan>().eq(BShipmentPlan::getShipmentPlanNo, planNo));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 查询 运抵报告中 可用的 发运计划；1.发运计划对应的集装箱都已落箱(保存是校验)，2.未生成过运抵报告
     **/
    @Override
    public List<BShipmentPlan> search4arrive(BShipmentPlanQuery bShipmentPlanQuery) {
        List<String> shipmentPlanNos = railwayShipmentPlanLogService.listPlanNos();
        QueryWrapper<BShipmentPlan> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq((StringUtils.isNotEmpty(bShipmentPlanQuery.getEnterprisesId())), BShipmentPlan::getEnterprisesId, bShipmentPlanQuery.getEnterprisesId()) // 发货企业id
                .eq(BShipmentPlan::getAuditStatus, AuditStatus.AUDIT_PASS.getValue()) // 审核通过
                .ge((bShipmentPlanQuery.getStartShipmentTime() != null && bShipmentPlanQuery.getStartShipmentTime() > 0), BShipmentPlan::getShipmentTime, bShipmentPlanQuery.getStartShipmentTime())
                .le((bShipmentPlanQuery.getEndShipmentTime() != null && bShipmentPlanQuery.getEndShipmentTime() > 0), BShipmentPlan::getShipmentTime, bShipmentPlanQuery.getEndShipmentTime())
                .like((StringUtils.isNotEmpty(bShipmentPlanQuery.getShipmentPlanNo())), BShipmentPlan::getShipmentPlanNo, bShipmentPlanQuery.getShipmentPlanNo())
                .notIn(!(CollectionUtils.isEmpty(shipmentPlanNos)), BShipmentPlan::getShipmentPlanNo, shipmentPlanNos);// 发运计划编号
        return list(wrapper);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 运抵-导入发运计划
     **/
    @Override
    public List<ArriveVo> import4arrive(List<String> ids) {
        return listByIds(ids).stream().map(plan -> {
                    return ArriveVo.builder()
                            .arrive(BExRailwayList.builder()
                                    .packNo(plan.getPieceTotal()).grossWt(plan.getWeightTotal())
                                    .enterprisesId(plan.getEnterprisesId()).enterprisesName(plan.getEnterprisesName())
                                    .shipmentPlanNo(plan.getShipmentPlanNo()).shipmentTime(plan.getShipmentTime())
                                    .build())
                            .contaList(containerService.listByShipmentPlanId(plan.getId()).stream().map(
                                    container -> {
                                        return BExRailwayConta.builder().contaNo(container.getContainerNo()).contaType(container.getContaModelName()).build();
                                    }
                            ).collect(Collectors.toList()))
                            .build();
                }
        ).collect(Collectors.toList());


    }

    @Override
    public BShipmentPlanDTO detail(String id) {
        return BShipmentPlanDTO.builder()
                .shipmentPlan(getById(id))
                .containerList(containerService.list(new LambdaQueryWrapper<BShipmentContainer>().eq(BShipmentContainer::getShipmentPlanId, id)))
                .goodsList(goodsDetailService.list(new LambdaQueryWrapper<BShipmentGoodsDetail>().eq(BShipmentGoodsDetail::getShipmentPlanId, id)))
                .build();

    }
}