package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.DropBoxStatus;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.UniqueIdUtil;
import cn.samples.depot.web.cz.mapper.BExRailwayListMapper;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.cz.service.event.RailwayDelReportEvent;
import cn.samples.depot.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author majunzi
 * @Date 2019/8/22
 * @Description 铁路-运抵申报-运抵单
 **/
@Service
public class BExRailwayListServiceImpl extends ServiceImpl<BExRailwayListMapper, BExRailwayList> implements BExRailwayListService {
    @Autowired
    BShipmentPlanService shipmentPlanService;
    @Autowired
    BShipmentContainerService shipmentContainerService;
    @Autowired
    BExRailwayShipmentPlanLogService logService;
    @Autowired
    BDropBoxConfirmService dropBoxConfirmService;
    @Autowired
    BExRailwayContaService contaService;
    @Autowired
    BExRailwayReportHeadService headService;

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 根据运抵单编号（部分，like） 获取 运抵报文表头id集合
     **/
    @Override
    public Set<String> listHeadIdsByPartArriveNo(String partOfArriveNo) {
        Set<String> ids = new HashSet<>();
        List<Map<String, Object>> maps = listMaps(new LambdaQueryWrapper<BExRailwayList>().select(BExRailwayList::getExRailwayReportHeadId).like(BExRailwayList::getArriveNo, partOfArriveNo));
        if (!CollectionUtils.isEmpty(maps)) {
            maps.forEach(map -> {
                ids.add(map.get("ex_railway_report_head_id").toString());
            });
        }
        return ids;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 保存- 校验，保存，设置发运计划运抵标记
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(BExRailwayReportHead head, BExRailwayList arrive) throws BizException {
        saveBefore(head, arrive);
        super.save(arrive);
        if (StringUtils.isNotEmpty(arrive.getShipmentPlanNo()))
            logService.save(BExRailwayShipmentPlanLog.builder().shipmentPlanNo(arrive.getShipmentPlanNo()).build());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description
     **/
    @Override
    public void saveBefore(BExRailwayReportHead head, BExRailwayList arrive) throws BizException {
        checkEmpty(arrive);
        arrive.setExRailwayReportHeadId(head.getId());
        if (StringUtils.isEmpty(arrive.getArriveNo()))
            arrive.setArriveNo(head.getCustomsCode() + head.getDischargePlace().substring(head.getDischargePlace().length() - 2, head.getDischargePlace().length()) + UniqueIdUtil.get6Date6SerialNumber());
        if (StringUtils.isEmpty(arrive.getAuditStatus()))
            arrive.setAuditStatus(WayBillAuditStatus.WayBillAudit_01.getValue());
        if (null == arrive.getCreateTime() || arrive.getCreateTime() <= 0)
            arrive.setCreateTime(System.currentTimeMillis());
        if (StringUtils.isEmpty(arrive.getShipmentPlanNo())) return;
        //发运计划  1.校验
        BShipmentPlan shipmentPlan = checkShipmentPlan(arrive.getShipmentPlanNo());
        arrive.setEnterprisesId(shipmentPlan.getEnterprisesId());
        arrive.setEnterprisesName(shipmentPlan.getEnterprisesName());
        arrive.setShipmentTime(shipmentPlan.getShipmentTime());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 发运计划对应集装箱都已落箱，发运计划未生成过运抵报告
     **/
    private BShipmentPlan checkShipmentPlan(String shipmentPlanNo) throws BizException {
        BShipmentPlan shipmentPlan = shipmentPlanService.getByPlanNo(shipmentPlanNo);
        if (null == shipmentPlan) throw new BizException("发运计划[" + shipmentPlanNo + "]不存在");
        //发运计划未生成过运抵报告
        if (!CollectionUtils.isEmpty(logService.list(new LambdaQueryWrapper<BExRailwayShipmentPlanLog>().eq(BExRailwayShipmentPlanLog::getShipmentPlanNo, shipmentPlanNo)))) {
            throw new BizException(String.format("发运计划[%s]已经生成过运抵单", shipmentPlanNo));
        }
        //发运计划对应集装箱都已落箱
        List<BShipmentContainer> containers = shipmentContainerService.listByShipmentPlanId(shipmentPlan.getId());
        Set<String> contaNos = containers.stream().map(container -> container.getContainerNo()).collect(Collectors.toSet());
        List<BDropBoxConfirm> confirms = dropBoxConfirmService.list(new LambdaQueryWrapper<BDropBoxConfirm>()
                .eq(BDropBoxConfirm::getStatus, DropBoxStatus.DROPPED.getValue())
                .in(BDropBoxConfirm::getContainerNo, contaNos));
        Set<String> contaNosArriveds = confirms.stream().map(confirm -> confirm.getContainerNo()).collect(Collectors.toSet());
        contaNos.removeAll(contaNosArriveds);
        if (CollectionUtils.isEmpty(contaNos)) return shipmentPlan;
        throw new BizException(String.format("发运计划[%s]下的集装箱%s未落箱", shipmentPlanNo, contaNos.toString()));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 非空校验:件数，创建时间不能为空
     **/
    private void checkEmpty(BExRailwayList arrive) throws BizException {
        if (null == arrive) throw new BizException("运抵单不能为空");
        if (null == arrive.getPackNo() || arrive.getPackNo() <= 0) throw new BizException("件数不能为空");
        if (null == arrive.getGrossWt() || arrive.getGrossWt() <= 0) throw new BizException("重量不能为空");
    }

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 运抵作废申报-作废中
     **/
    @EventListener(value = RailwayDelReportEvent.class, condition = "#event.declare")
    public void invalid(RailwayDelReportEvent event) {
        List<String> arriveNos = event.getSource();
        if (CollectionUtils.isEmpty(arriveNos)) return;
        update(new LambdaUpdateWrapper<BExRailwayList>()
                .set(BExRailwayList::getAuditStatus, WayBillAuditStatus.WayBillAudit_07.getValue())
                .in(BExRailwayList::getArriveNo, arriveNos));

    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 根据表头id，删除运抵单，及发运计划 运抵标记
     **/
    @Override
    public void removeByHeadId(String id) {
        LambdaQueryWrapper<BExRailwayList> arriveWrapper = new LambdaQueryWrapper<BExRailwayList>().eq(BExRailwayList::getExRailwayReportHeadId, id);
        List<BExRailwayList> arrives = list(arriveWrapper);
        logService.remove(new LambdaQueryWrapper<BExRailwayShipmentPlanLog>().in(BExRailwayShipmentPlanLog::getShipmentPlanNo, arrives.stream().map(arrive -> arrive.getShipmentPlanNo()).collect(Collectors.toList())));
        remove(arriveWrapper);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 运抵作废 审核通过- 删除运抵单(发运计划运抵日志删除)及集装箱，如果运抵单全部删除，则删除对应的表头
     **/
    @EventListener(value = RailwayDelReportEvent.class, condition = "#event.pass")
    @Transactional(rollbackFor = Exception.class)
    public void pass(RailwayDelReportEvent event) {
        List<String> arriveNos = event.getSource();
        if (CollectionUtils.isEmpty(arriveNos)) return;
        LambdaQueryWrapper<BExRailwayList> arriveWrapper = new LambdaQueryWrapper<BExRailwayList>().in(BExRailwayList::getArriveNo, arriveNos);
        List<BExRailwayList> arriveList = list(arriveWrapper);
        //查询表头
        Set<String> headIds = arriveList.stream().map(arrive -> arrive.getExRailwayReportHeadId()).collect(Collectors.toSet());
        //查询发运计划编号
        Set<String> shipmentPlanNos = arriveList.stream().map(arrive -> arrive.getShipmentPlanNo()).collect(Collectors.toSet());
        //删除运抵单
        remove(arriveWrapper);
        //删除 发运计划运抵标记
        logService.remove(new LambdaQueryWrapper<BExRailwayShipmentPlanLog>().in(BExRailwayShipmentPlanLog::getShipmentPlanNo, shipmentPlanNos));
        //集装箱
        contaService.remove(new LambdaQueryWrapper<BExRailwayConta>().in(BExRailwayConta::getArriveNo, arriveNos));
        //检查表头对应的运抵单，如果无运抵单，则删除表头
        headIds.forEach(id -> {
            if (CollectionUtils.isEmpty(list(new LambdaQueryWrapper<BExRailwayList>().eq(BExRailwayList::getExRailwayReportHeadId, id)))) {
                headService.removeById(id);
            }

        });

    }

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 运抵作废 审核退单-审核通过
     **/
    @EventListener(value = RailwayDelReportEvent.class, condition = "#event.fail")
    public void fail(RailwayDelReportEvent event) {
        List<String> arriveNos = event.getSource();
        if (CollectionUtils.isEmpty(arriveNos)) return;
        update(new LambdaUpdateWrapper<BExRailwayList>()
                .set(BExRailwayList::getAuditStatus, WayBillAuditStatus.WayBillAudit_03.getValue())
                .in(BExRailwayList::getArriveNo, arriveNos));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 根据表头id，获取运抵单集合 (审核通过)
     **/
    @Override
    public List<BExRailwayList> listArrives(String messageAddId) throws BizException {
        BExRailwayReportHead head = headService.getOne(new LambdaQueryWrapper<BExRailwayReportHead>().eq(BExRailwayReportHead::getMessageId, messageAddId));
        if (null == head) throw new BizException(String.format("找不到运抵报文[%s]", messageAddId));
        return list(new LambdaQueryWrapper<BExRailwayList>()
                .eq(BExRailwayList::getExRailwayReportHeadId, head.getId())
                .eq(BExRailwayList::getAuditStatus, WayBillAuditStatus.WayBillAudit_03.getValue()));
    }

    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description
     **/
    @Override
    public BExRailwayList getByShipmentPlanNo(String shipmentPlanNo) {
        return getOne(new LambdaQueryWrapper<BExRailwayList>()
                .eq(BExRailwayList::getShipmentPlanNo, shipmentPlanNo));
    }

    /**
     * @Author majunzi
     * @Date 2019/10/16
     * @Description 下拉选择（不筛状态）
     **/
    @Override
    public List<BExRailwayList> select() {
        return list(new LambdaQueryWrapper<BExRailwayList>().orderByDesc(BExRailwayList::getCreateTime));
    }

    /**
     * @Author majunzi
     * @Date 2019/10/16
     * @Description 根据运抵编号 获取运抵单
     **/
    @Override
    public BExRailwayList getByArriveNo(String arriveNo) {
        return getOne(new LambdaQueryWrapper<BExRailwayList>().eq(BExRailwayList::getArriveNo, arriveNo));
    }

}