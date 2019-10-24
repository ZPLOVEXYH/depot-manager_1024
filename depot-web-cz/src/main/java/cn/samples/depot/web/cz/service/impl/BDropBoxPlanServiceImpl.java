package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.common.model.DropBoxStatus;
import cn.samples.depot.common.model.GoodsConfirmStatus;
import cn.samples.depot.web.cz.mapper.BDropBoxPlanMapper;
import cn.samples.depot.web.cz.service.BDropBoxConfirmDetailService;
import cn.samples.depot.web.cz.service.BDropBoxConfirmService;
import cn.samples.depot.web.cz.service.BDropBoxPlanDetailService;
import cn.samples.depot.web.cz.service.BDropBoxPlanService;
import cn.samples.depot.web.cz.service.event.ContaStatusEvent;
import cn.samples.depot.web.entity.BDropBoxConfirm;
import cn.samples.depot.web.entity.BDropBoxConfirmDetail;
import cn.samples.depot.web.entity.BDropBoxPlan;
import cn.samples.depot.web.entity.BDropBoxPlanDetail;
import cn.samples.depot.web.service.UserService;
import cn.samples.depot.web.service.event.EventEmitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/8
 * @Description 落箱计划
 **/
@Service
public class BDropBoxPlanServiceImpl extends ServiceImpl<BDropBoxPlanMapper, BDropBoxPlan> implements BDropBoxPlanService {
    @Autowired
    BDropBoxPlanDetailService planDetailService;
    @Autowired
    BDropBoxConfirmService confirmService;
    @Autowired
    BDropBoxConfirmDetailService confirmDetailService;
    @Autowired
    CStationAreaPositionsServiceImpl positionsService;
    @Autowired
    UserService userService;
    @Autowired
    EventEmitter eventEmitter;

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 构建落箱确认的发运计划项
     **/
    public static BDropBoxConfirmDetail buildDropBoxConfirmDetail(BDropBoxPlanDetail planDetail) {
        BDropBoxConfirmDetail confirmDetail = new BDropBoxConfirmDetail();
        BeanUtils.copyProperties(planDetail, confirmDetail);
        confirmDetail.setDropBoxConfirmId(planDetail.getDropBoxPlanId());
        return confirmDetail;
        /*return BDropBoxConfirmDetail.builder()
                .id(planDetail.getId())
                .dropBoxConfirmId(planDetail.getDropBoxPlanId())
                .enterprisesId(planDetail.getEnterprisesId())
                .enterprisesName(planDetail.getEnterprisesName())
                .shipmentPlanId(planDetail.getShipmentPlanId())
                .shipmentPlanNo(planDetail.getShipmentPlanNo())
                .pieceTotal(planDetail.getPieceTotal())
                .weightTotal(planDetail.getWeightTotal())
                .createTime(planDetail.getCreateTime())
                .build();*/

    }

    /**
     * @Author majunzi
     * @Date 2019/8/5
     * @Description 根据落箱计划，及落箱安排数据，构建落箱确认记录。进卡序列号，操作人，操作时间未赋值
     **/
    private BDropBoxConfirm buildDropBoxConfirm(BDropBoxPlan plan, BDropBoxPlan boxArrangement) {
        return BDropBoxConfirm.builder()
                .id(plan.getId())
                .containerNo(plan.getContainerNo())
                .sealNo(plan.getSealNo())
                .contaModelName(plan.getContaModelName())
                .enterprisesId(plan.getEnterprisesId())
                .enterprisesName(plan.getEnterprisesName())
                .shipmentTime(plan.getShipmentTime())
                .stationAreaCode(boxArrangement.getStationAreaCode())
                .stationAreaPositionCode(boxArrangement.getStationAreaPositionCode())
                .entryTime(boxArrangement.getEntryTime())
                .status(DropBoxStatus.PRE_DROP.getValue())
                .entryStatus(GoodsConfirmStatus.NO_GOODS_ARRIVAL.getValue())
                .createTime(System.currentTimeMillis())
                .build();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 落箱计划 1.校验状态为待计划 2.锁定堆区 3.更新落箱计划 4.新增落箱确认
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void arrangement(BDropBoxPlan boxArrangement) throws Throwable {
        BDropBoxPlan plan = getById(boxArrangement.getId());
        //校验状态
        if (!DropBoxStatus.PRE_PLAN.getValue().equals(plan.getStatus()))
            throw new BizException(String.format("仅[%s]的落箱计划可以[落箱安排]", DropBoxStatus.PRE_PLAN.getTitle()));
        //预占用 定推区
        positionsService.onDropBoxPlanArrangement(boxArrangement.getStationAreaCode(), boxArrangement.getStationAreaPositionCode());
        //修改落箱计划状态 更新落箱计划
        updateById(buildArrangementPlan(boxArrangement));
        // 新增一条落箱确认记录
        confirmService.save(buildDropBoxConfirm(plan, boxArrangement));
        confirmDetailService.saveBatch(buildDropBoxConfirmDetails(boxArrangement.getId()));
        // 发出一个事件 todo 集装箱历史记录：落箱计划完成后
        eventEmitter.emit(new ContaStatusEvent(plan, ContaOptionType.DROPBOX_PLAN));
    }

    private BDropBoxPlan buildArrangementPlan(BDropBoxPlan boxArrangement) {
        return BDropBoxPlan.builder()
                .id(boxArrangement.getId())
                .stationAreaCode(boxArrangement.getStationAreaCode())
                .stationAreaPositionCode(boxArrangement.getStationAreaPositionCode())
                .entryTime(boxArrangement.getEntryTime())
                .opUser(userService.currentUserName())
                .opTime(System.currentTimeMillis())
                .status(DropBoxStatus.PRE_DROP.getValue())
                .build();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 构建落箱确认 的发运计划项集合
     **/
    private List<BDropBoxConfirmDetail> buildDropBoxConfirmDetails(String dropBoxPlanId) throws BizException {
        List<BDropBoxPlanDetail> planDetails = planDetailService.list(new LambdaQueryWrapper<BDropBoxPlanDetail>().eq(BDropBoxPlanDetail::getDropBoxPlanId, dropBoxPlanId));
        if (CollectionUtils.isEmpty(planDetails)) throw new BizException("落箱计划的发货计划不能未空");
        List<BDropBoxConfirmDetail> confirmDetails = new ArrayList<>();
        planDetails.forEach(planDetail -> {
            confirmDetails.add(buildDropBoxConfirmDetail(planDetail));
        });
        return confirmDetails;
    }

}