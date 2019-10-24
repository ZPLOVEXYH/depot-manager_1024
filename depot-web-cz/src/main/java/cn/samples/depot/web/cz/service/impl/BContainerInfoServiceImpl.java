package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.IEFlag;
import cn.samples.depot.common.model.ShipmentPlanStatus;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.UniqueIdUtil;
import cn.samples.depot.web.bean.BShipmentPlanDTO;
import cn.samples.depot.web.bean.shipmentStatistics.ContainerDetail;
import cn.samples.depot.web.bean.stations.position.AreaPositionContaSelect;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.cz.service.event.ContaStatusEvent;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.mapper.BContainerInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author majunzi
 * @Date 2019/9/23
 * @Description 箱货信息
 **/
@Service
@Slf4j
public class BContainerInfoServiceImpl extends ServiceImpl<BContainerInfoMapper, BContainerInfo> implements BContainerInfoService {


    @Autowired
    BContainerHistoryService historyService;
    @Autowired
    BContainerBillInfoService billInfoService;
    @Autowired
    BShipmentContainerService shipmentContainerService;
    @Autowired
    BDropBoxConfirmDetailService dropBoxConfirmDetailService;

    //运抵放行，理货放行，装车记录
    @Autowired
    BExRailwayReportHeadService railwayReportHeadService;
    @Autowired
    BExRailwayListService railwayListService;
    @Autowired
    BRailwayTallyBillInfoService tallyBillInfoService;
    @Autowired
    BRailwayLoadContaService loadContaService;
    @Autowired
    CStationAreaPositionsService areaPositionsService;


    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 箱货详情 （b_shipment_container的id） 基本信息+集装箱历史记录
     * 补充 发运计划编号，提运单号，堆区，堆位信息。
     **/
    @Override
    public ContainerDetail contaDetialByShipmentContaId(String shipmentContaId) throws BizException {
        BContainerBillInfo billInfo = getContainerBillInfo(shipmentContaId);
        BContainerInfo contaInfo = getById(billInfo.getContainerId());
        //补充 发运计划编号，提运单号
        contaInfo.setShipmentPlanNo(billInfo.getShipmentPlanNo());
        contaInfo.setBillNo(billInfo.getBillNo());
        //补充 堆区，堆位
        addAreaPostionInfo(contaInfo);
        return ContainerDetail.builder()
                .head(contaInfo)
                .histories(historyService.listHistory(billInfo.getContainerId()))
                .build();

    }

    private void addAreaPostionInfo(BContainerInfo contaInfo) throws BizException {
        AreaPositionContaSelect positionContaSelect = areaPositionsService.getByContano(contaInfo.getContaNo());
        if(null==positionContaSelect) return;
        contaInfo.setAreaName(positionContaSelect.getAreaName());
        contaInfo.setAreaPositionName(positionContaSelect.getPositionName());
    }

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 根据发运计划的集装箱id获取 集装箱状态id
     **/
    private BContainerBillInfo getContainerBillInfo(String shipmentContaId) throws BizException {
        //根据发运计划的集装箱id 查找对应记录
        BShipmentContainer shipmentContainer = shipmentContainerService.getById(shipmentContaId);
        if (null == shipmentContainer) return null;
        //根据集装箱号+发运计划id 获取集装箱状态记录
        BContainerBillInfo billInfo = billInfoService.getByShipmentIdAndContaNo(shipmentContainer.getShipmentPlanId(), shipmentContainer.getContainerNo());
        if (null == billInfo) {
            log.info(String.format("找不到BContainerBillInfo记录，发运计划ID[%s],集装箱号[%s]", shipmentContainer.getShipmentPlanId(), shipmentContainer.getContainerNo()));
            throw new BizException("找不到单货信息");
        }
        //获取集装箱状态id
        return billInfo;
    }

    /**
     * @Author majunzi
     * @Date 2019/9/20
     * @Description 箱货详情（b_container_info的id）  基本信息+单货信息+集装箱历史记录
     **/
    @Override
    public ContainerDetail contaDetial(String contaId) throws BizException {
        BContainerInfo contaInfo = getById(contaId);
        //补充 堆区，堆位
        addAreaPostionInfo(contaInfo);

        return ContainerDetail.builder()
                .head(contaInfo)
                .billInfo(billInfoService.listBillInfo(contaId))
                .histories(historyService.listHistory(contaId))
                .build();
    }

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 箱状态
     **/
    @Override
    public BContainerInfo lastContaInfo(String contaNo, boolean nullException) throws BizException {
        BContainerInfo contaInfo = getOne(new LambdaQueryWrapper<BContainerInfo>().eq(BContainerInfo::getContaNo, contaNo).orderByDesc(BContainerInfo::getCreateTime));
        if (nullException && null == contaInfo) {
            log.warn(String.format("找不到箱[%s]状态记录", contaNo));
            throw new BizException("找不到集装箱状态记录");
        }
        return contaInfo;
    }


    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description 箱号查询
     **/
    @Override
    public BContainerInfo contaInfo(String contaNo) throws BizException {
        BContainerInfo contaInfo = lastContaInfo(contaNo, false);
        if (null == contaInfo) {
            log.warn(String.format("找不到集装箱号[%s]集装箱状态", contaNo));
        } else if (ShipmentPlanStatus.DROPPED.getValue().equals(contaInfo.getStatus())) {
            setReportStatus(contaInfo);
        }
        return contaInfo;
    }

    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description 设置申报状态 运抵放行，理货放行，装车记录放行信息
     **/
    private void setReportStatus(BContainerInfo contaInfo) {
        log.info("设置申报状态 运抵放行，理货放行，装车记录放行信息");
        if (null == contaInfo) return;
        String contaNo = contaInfo.getContaNo();
        log.info("b_container_bill_info(发运计划编号)->b_ex_railway_list(audit_status)");
        BContainerBillInfo billInfo = billInfoService.getByContainerIdAndContaNo(contaInfo.getId(), contaNo);
        if (null != billInfo) {
            BExRailwayList railwayList = railwayListService.getByShipmentPlanNo(billInfo.getShipmentPlanNo());
            if (null != railwayList) {
                contaInfo.setArriveRel(trans2reportStatus(railwayList.getAuditStatus()));
            }
        }
        log.info("b_railway_tally_bill_info(audit_status)");
        BRailwayTallyBillInfo tallyBillInfo = tallyBillInfoService.getLastByContaNo(contaNo);
        if (null != tallyBillInfo) contaInfo.setTallyRel(trans2reportStatus(tallyBillInfo.getAuditStatus()));
        log.info("b_railway_load_conta(audit_status)");
        BRailwayLoadConta loadConta = loadContaService.getLastByContaNo(contaNo);
        if (null != loadConta) contaInfo.setLoadRel(trans2reportStatus(loadConta.getAuditStatus()));
    }

    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description WayBillAuditStatus 空白：未申报，申报中，作废中；Y：审核通过；N：审核不通过
     **/
    private String trans2reportStatus(String status) {
        WayBillAuditStatus auditStatus = WayBillAuditStatus.findByValue(status);
        switch (auditStatus) {
            case WayBillAudit_03:
                return "Y";
            case WayBillAudit_04:
                return "N";
            default:
                return null;
        }
    }


    @Autowired
    BShipmentPlanService shipmentPlanService;

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 一：发运计划 审核通过
     * 1.发运计划：更新状态为 待落箱；更新集装箱状态为待落箱
     * 2.生成集装箱状态记录（b_container_info）：箱号，箱型，铅封号，场站编码，场站名称，状态：默认待落箱，发货企业 ，发运计划编号，进出口（E），
     * 3.生成集装箱单货信息记录（b_container_bill_info）：
     **/
    @EventListener(value = ContaStatusEvent.class, condition = "#event.shipmentPass")
    public void shipmentPass(ContaStatusEvent event) {
        log.info("集装箱状态事件-发运计划审核通过 source:" + event.getSource().toString());
        try {
            BShipmentPlanDTO dto = (BShipmentPlanDTO) event.getSource();
            List<BShipmentContainer> containers = dto.getContainerList();
            BShipmentPlan shipmentPlan = dto.getShipmentPlan();
            String shipmentId = shipmentPlan.getId();
            log.info("1.发运计划：更新状态为 待落箱；更新集装箱状态为待落箱");
            shipmentPlanService.updateById(BShipmentPlan.builder().id(shipmentId).status(ShipmentPlanStatus.PRE_DROP.getValue()).build());
            shipmentContainerService.update(new LambdaUpdateWrapper<BShipmentContainer>().set(BShipmentContainer::getStatus, ShipmentPlanStatus.PRE_DROP.getValue()).eq(BShipmentContainer::getShipmentPlanId, shipmentId));
            for (BShipmentContainer conta : containers) {
                String contaNo = conta.getContainerNo();
                BContainerInfo contaInfo = lastContaInfo(contaNo, false);
                if (null == contaInfo || ShipmentPlanStatus.LOADED.getValue().equals(contaInfo.getStatus())) {
                    log.info("2.生成集装箱状态记录（b_container_info）：箱号，箱型，铅封号，场站编码，场站名称，状态：默认待落箱，发货企业 ，进出口（E）");
                    contaInfo = null;
                    contaInfo = BContainerInfo.builder()
                            .id(UniqueIdUtil.getUUID())
                            .contaNo(contaNo)
                            .contaType(conta.getContaModelName())
                            .sealNo(conta.getSealNo())
                            .stationCode(shipmentPlan.getStationsCode())
                            .stationName(shipmentPlan.getStationsName())
                            .status(ShipmentPlanStatus.PRE_DROP.getValue())
                            .shipCompany(shipmentPlan.getEnterprisesName())
                            .iEFlag(IEFlag.EXPORT.getValue())
                            .createTime(System.currentTimeMillis())
                            .build();
                    save(contaInfo);
                }
                log.info("3.生成集装箱单货信息记录（b_container_bill_info）");
                BContainerBillInfo billInfo = BContainerBillInfo.builder()
                        .containerId(contaInfo.getId())
                        .containerNo(contaNo)
                        .shipmentPlanId(shipmentId)
                        .shipmentPlanNo(shipmentPlan.getShipmentPlanNo())
                        .billNo(shipmentPlan.getDeliveryNo())
                        .packNo(shipmentPlan.getPieceTotal())
                        .createTime(System.currentTimeMillis())
                        .build();
                billInfoService.save(billInfo);

            }
        } catch (BizException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 二：落箱计划 落箱安排
     * //1.更新发运计划及集装箱状态 待落箱
     * 2.生成集装箱历史信息 （b_container_history）：计划时间
     **/
    @EventListener(value = ContaStatusEvent.class, condition = "#event.dropBoxPlan")
    @Transactional(rollbackFor = Exception.class)
    public void dropboxPlan(ContaStatusEvent event) {
        log.info("集装箱状态事件-落箱计划 source:" + event.getSource().toString());
        try {
            BDropBoxPlan plan = (BDropBoxPlan) event.getSource();
            log.info("生成集装箱历史信息 （b_container_history）：计划时间");
            BContainerInfo contaInfo = lastContaInfo(plan.getContainerNo(), true);
            BContainerHistory history = historyService.getNew(contaInfo, event.getOptionType());
            history.setPlanTime(plan.getShipmentTime());
            historyService.saveT(history);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 三：货到确认
     * //1.更新发运计划及集装箱状态 待落箱
     * 2.生成集装箱历史信息 （b_container_history）：
     * 3.更新集装箱状态记录（b_container_info）：进场时间：货到确认的时间
     **/
    @EventListener(value = ContaStatusEvent.class, condition = "#event.goodsArr")
    @Transactional(rollbackFor = Exception.class)
    public void goodsArr(ContaStatusEvent event) {
        log.info("集装箱状态事件-货到确认 source:" + event.getSource().toString());
        try {
            BGoodsArriveConfirm confirm = (BGoodsArriveConfirm) event.getSource();
            log.info("1.生成集装箱历史信息 （b_container_history）");
            BContainerInfo contaInfo = lastContaInfo(confirm.getContainerNo(), true);
            BContainerHistory history = historyService.getNew(contaInfo, event.getOptionType());
            history.setWorkTime(confirm.getEntryTime());
            historyService.saveT(history);
            log.info("2.更新集装箱状态记录（b_container_info）：进场时间：货到确认的时间）");
            contaInfo.setInTime(confirm.getEntryTime());
            updateById(contaInfo);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 四：落箱确认 落箱
     * 1.更新发运计划及集装箱状态 已落箱
     * 2.生成集装箱历史信息 （b_container_history）：作业时间
     * 3.更新箱状态：状态：已落箱
     **/
    @EventListener(value = ContaStatusEvent.class, condition = "#event.dropBox")
    @Transactional(rollbackFor = Exception.class)
    public void dropbox(ContaStatusEvent event) {
        log.info("集装箱状态事件-落箱确认 source:" + event.getSource().toString());
        try {
            BDropBoxConfirm confirm = (BDropBoxConfirm) event.getSource();
            String contaNo = confirm.getContainerNo();
            log.info("查找所有对应的发运计划");
            Set<String> shipmentPlanIds = dropBoxConfirmDetailService.listByDropBoxConfirmId(confirm.getId()).stream().map(detail -> detail.getShipmentPlanId()).collect(Collectors.toSet());
            log.info("shipmentPlanIds:" + shipmentPlanIds.toString());
            updateShipmentPlan(shipmentPlanIds, contaNo, ShipmentPlanStatus.DROPPED);
            log.info("生成集装箱历史信息 （b_container_history）");
            BContainerInfo contaInfo = lastContaInfo(confirm.getContainerNo(), true);
            BContainerHistory history = historyService.getNew(contaInfo, event.getOptionType());
            history.setWorkTime(confirm.getOpTime());
            historyService.saveT(history);
            log.info(" 3.更新箱状态：状态：已落箱");
            contaInfo.setStatus(ShipmentPlanStatus.DROPPED.getValue());
            updateById(contaInfo);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 五：移箱确认 移箱
     * //1.更新发运计划及集装箱状态 已落箱
     * 2.生成集装箱历史信息 （b_container_history）：
     **/
    @EventListener(value = ContaStatusEvent.class, condition = "#event.moveBox")
    @Transactional(rollbackFor = Exception.class)
    public void movebox(ContaStatusEvent event) {
        log.info("集装箱状态事件-移箱确认 source:" + event.getSource().toString());
        try {
            BMoveBoxConfirm confirm = (BMoveBoxConfirm) event.getSource();
            log.info("1.生成集装箱历史信息 （b_container_history）");
            BContainerInfo contaInfo = lastContaInfo(confirm.getContainerNo(), true);
            BContainerHistory history = historyService.getNew(contaInfo, event.getOptionType());
            history.setPlanTime(confirm.getOpTime());
            historyService.saveT(history);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 六：发车确认 发车
     * 1.更新发运计划及集装箱状态 已发车
     * 2.生成集装箱历史信息 （b_container_history）：
     * 3.更新集装箱状态记录（b_container_info）：出场时间（发车确认的时间），状态（已发车），运抵放行，理货放行，装车记录放行
     **/
    @EventListener(value = ContaStatusEvent.class, condition = "#event.departure")
    public void departure(ContaStatusEvent event) {
        log.info("集装箱状态事件-发车确认 source:" + event.getSource().toString());
        try {
            BDepartureConfirm confirm = (BDepartureConfirm) event.getSource();
            String contaNo = confirm.getContaNo();
            BContainerInfo contaInfo = lastContaInfo(contaNo, true);
            BContainerHistory history = historyService.getNew(contaInfo, event.getOptionType());
            log.info("1.更新发运计划及集装箱状态 已发车 ,查找所有对应的发运计划");
            Set<String> shipmentPlanIds = billInfoService.listByContainerId(contaInfo.getId()).stream().map(detail -> detail.getShipmentPlanId()).collect(Collectors.toSet());
            log.info("shipmentPlanIds：" + shipmentPlanIds.toString());
            updateShipmentPlan(shipmentPlanIds, contaNo, ShipmentPlanStatus.LOADED);
            log.info("2.生成集装箱历史信息 （b_container_history）：作业时间，出入场工具，车次");
            history.setWorkTime(confirm.getDepartureTime());
            history.setShipName(confirm.getShipName());
            history.setVoyageNo(confirm.getVoyageNo());
            historyService.saveT(history);
            log.info("3.更新集装箱状态记录（b_container_info）：出场时间（发车确认的时间），状态（已发车），运抵放行，理货放行，装车记录放行");
            contaInfo.setOutTime(confirm.getDepartureTime());
            contaInfo.setStatus(ShipmentPlanStatus.LOADED.getValue());
            setReportStatus(contaInfo);
            updateById(contaInfo);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void updateShipmentPlan(Set<String> shipmentPlanIds, String contaNo, ShipmentPlanStatus status) {
        for (String shipmentPlanId : shipmentPlanIds) {
            log.info("更新落箱计划及对应集装箱的状态");
            log.info(String.format("修改集装箱[%s]状态[%s]", contaNo, status.getTitle()));
            shipmentContainerService.updateStatus(shipmentPlanId, contaNo, status);
            log.info("查看所有集装箱状态是否都为已落箱(已发车),是则修改发运计划状态为已落箱（已发车）");
            List<BShipmentContainer> containers = shipmentContainerService.listByShipmentPlanIdAndStatus(shipmentPlanId, ShipmentPlanStatus.beforeCurrent(status));
            if (CollectionUtils.isEmpty(containers)) {
                log.info(String.format("修改落箱计划[%s]状态[%s]", shipmentPlanId, status.getTitle()));
                shipmentPlanService.updateById(BShipmentPlan.builder().id(shipmentPlanId).status(status.getValue()).build());
            }
        }
    }

}