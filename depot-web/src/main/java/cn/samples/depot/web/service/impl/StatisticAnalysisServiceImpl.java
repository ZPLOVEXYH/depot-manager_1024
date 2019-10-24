package cn.samples.depot.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.AuditStatus;
import cn.samples.depot.common.model.ShipmentPlanStatus;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.model.UseStatus;
import cn.samples.depot.common.utils.DateUtils;
import cn.samples.depot.common.utils.MapUtil;
import cn.samples.depot.web.bean.analysis.PlanTrendQuery;
import cn.samples.depot.web.bean.analysis.RspPlannedOrderDto;
import cn.samples.depot.web.bean.analysis.RspPlannedTrendBean;
import cn.samples.depot.web.bean.analysis.RspStockAnalysisBean;
import cn.samples.depot.web.entity.BShipmentPlan;
import cn.samples.depot.web.entity.CStationAreaPositions;
import cn.samples.depot.web.service.BShipmentPlanService;
import cn.samples.depot.web.service.CStationAreaPositionsService;
import cn.samples.depot.web.service.StatisticAnalysisService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description 查询统计-统计分析
 * @Author ZhangPeng
 * @Date 2019/9/25
 **/
@Service
public class StatisticAnalysisServiceImpl implements StatisticAnalysisService {

    /**
     * 堆存接口
     */
    @Autowired
    private CStationAreaPositionsService areaPositionsService;
    /**
     * 发运计划接口
     */
    @Autowired
    private BShipmentPlanService bShipmentPlanService;

    /**
     * 查询统计-堆存分析接口
     *
     * @return
     */
    @Override
    public List<RspStockAnalysisBean> stockAnalysis() {
        // 不可用状态的数目
        List<CStationAreaPositions> areaPositionsList = areaPositionsService.getBaseMapper().selectList(Wrappers.<CStationAreaPositions>lambdaQuery()
                .eq(CStationAreaPositions::getEnable, Status.ENABLED.getValue()));

        if (CollectionUtil.isNotEmpty(areaPositionsList)) {
            // 堆存可用数量
            long notUsed = areaPositionsList.stream().filter(cStationAreaPositions -> UseStatus.NOT_USED.getValue() == cStationAreaPositions.getUsed()).count();
            // 堆存占用数量
            long used = areaPositionsList.stream().filter(cStationAreaPositions -> UseStatus.USED.getValue().equals(cStationAreaPositions.getUsed())).count();
            // 堆存锁定数量
            long preUsed = areaPositionsList.stream().filter(cStationAreaPositions -> UseStatus.PRE_USED.getValue() == cStationAreaPositions.getUsed()).count();

            RspStockAnalysisBean notUsedBean = RspStockAnalysisBean.builder()
                    .name(UseStatus.NOT_USED.getTitle()).value(notUsed).build();

            RspStockAnalysisBean usedBean = RspStockAnalysisBean.builder()
                    .name(UseStatus.USED.getTitle()).value(used).build();

            RspStockAnalysisBean preUsedBean = RspStockAnalysisBean.builder()
                    .name(UseStatus.PRE_USED.getTitle()).value(preUsed).build();

            List<RspStockAnalysisBean> dataList = new ArrayList<>();
            dataList.add(notUsedBean);
            dataList.add(usedBean);
            dataList.add(preUsedBean);

            return dataList;
        }

        return null;
    }

    @Override
    public RspPlannedOrderDto plannedOrder() {
        RspPlannedOrderDto plannedOrderDto = RspPlannedOrderDto.builder()
                .planValid(validPlannedOrder())
                .planTotal(plannedOrderTotal())
                .build();

        return plannedOrderDto;
    }

    /**
     * 发运计划单总量
     */
    public List<Map<String, Object>> plannedOrderTotal() {
        // 不可用状态的数目
        List<BShipmentPlan> bShipmentPlanList = bShipmentPlanService.getBaseMapper().selectList(null);

        if (CollectionUtil.isNotEmpty(bShipmentPlanList)) {
            // 发运计划单总量审核中
            long preDrop = bShipmentPlanList.stream().filter(bShipmentPlan -> AuditStatus.PRE_AUDIT.getValue() == bShipmentPlan.getAuditStatus()).count();
            // 发运计划单总量审核通过
            long dropped = bShipmentPlanList.stream().filter(bShipmentPlan -> AuditStatus.AUDIT_PASS.getValue().equals(bShipmentPlan.getAuditStatus())).count();
            // 发运计划单总量审核驳回
            long loaded = bShipmentPlanList.stream().filter(bShipmentPlan -> AuditStatus.AUDIT_REJECT.getValue() == bShipmentPlan.getAuditStatus()).count();

            Map<String, Object> plannedOrderMap = MapUtil.buildMap(
                    AuditStatus.PRE_AUDIT.getQuery(), preDrop,
                    AuditStatus.AUDIT_PASS.getQuery(), dropped,
                    AuditStatus.AUDIT_REJECT.getQuery(), loaded);

            return Arrays.asList(plannedOrderMap);
        }

        return null;
    }

    /**
     * 有效发运计划单
     */
    public List<Map<String, Object>> validPlannedOrder() {
        // 不可用状态的数目
        List<BShipmentPlan> bShipmentPlanList = bShipmentPlanService.getBaseMapper().selectList(null);

        if (CollectionUtil.isNotEmpty(bShipmentPlanList)) {
            // 有效发运计划单待落箱数量
            long preDrop = bShipmentPlanList.stream().filter(bShipmentPlan -> ShipmentPlanStatus.PRE_DROP.getValue() == bShipmentPlan.getStatus()).count();
            // 有效发运计划单已落箱数量
            long dropped = bShipmentPlanList.stream().filter(bShipmentPlan -> ShipmentPlanStatus.DROPPED.getValue().equals(bShipmentPlan.getStatus())).count();
            // 有效发运计划单已发车数量
            long loaded = bShipmentPlanList.stream().filter(bShipmentPlan -> ShipmentPlanStatus.LOADED.getValue() == bShipmentPlan.getStatus()).count();

            Map<String, Object> preDropMap = MapUtil.buildMap(
                    ShipmentPlanStatus.PRE_DROP.getQuery(), preDrop,
                    ShipmentPlanStatus.DROPPED.getQuery(), dropped,
                    ShipmentPlanStatus.LOADED.getQuery(), loaded);


            return Arrays.asList(preDropMap);
        }

        return null;
    }

    /**
     * 发运计划趋势
     * 等价于此sql结果：
     * select left(from_unixtime(b.create_time / 1000, '%Y-%m-%d %H:%m:%s'), 7), count(1), sum(b.piece_total)
     * from b_shipment_plan b group by left(from_unixtime(b.create_time / 1000, '%Y-%m-%d %H:%m:%s'), 7);
     *
     * @param query
     * @return
     */
    @Override
    public List<RspPlannedTrendBean> plannedTrend(PlanTrendQuery query) {
        QueryWrapper<BShipmentPlan> wrapper = new QueryWrapper<>();
        if (null == query.getStartDate() && null == query.getEndDate()) {
            // 设置开始时间为当前时间往前6个月
            wrapper.lambda()
                    .ge(BShipmentPlan::getCreateTime, DateUtils.getMonthStartTime(-5))
                    .le(BShipmentPlan::getCreateTime, DateUtils.getMonthStartTime(0))
                    .orderByAsc(BShipmentPlan::getCreateTime);
        } else if (null != query.getStartDate() && null != query.getEndDate()) {
            if (query.getEndDate() < query.getStartDate()) {
                new BizException("开始时间不能大于结束时间！");
            }
            wrapper.apply("left(from_unixtime(create_time / 1000, '%Y-%m-%d %H:%m:%s'), 7) >= {0} and left(from_unixtime(create_time / 1000, '%Y-%m-%d %H:%m:%s'), 7) <= {1}",
                    DateUtils.getYearMonth(query.getStartDate()),
                    DateUtils.getYearMonth(query.getEndDate()))
                    .orderByAsc();
        }

        // 根据开始时间和结束时间获取得到发运计划集合
        List<BShipmentPlan> planList = bShipmentPlanService.getBaseMapper().selectList(wrapper);
        if (CollectionUtil.isNotEmpty(planList)) {
            Map<String, LongSummaryStatistics> collect = planList.stream().collect(
                    Collectors.groupingBy((plan -> DateUtils.getYearMonth(plan.getCreateTime())),
                            Collectors.summarizingLong(plan -> (null != plan.getContainerNum() ? Long.valueOf(plan.getPieceTotal()) : 0))
                    ));

            List<RspPlannedTrendBean> plannedTrendBeanList = new ArrayList<>();
            for (Map.Entry<String, LongSummaryStatistics> entry : collect.entrySet()) {
                LongSummaryStatistics longSummaryStatistics = entry.getValue();
                RspPlannedTrendBean trendBean = RspPlannedTrendBean.builder()
                        .date(entry.getKey())
                        .containerNum(longSummaryStatistics.getSum())
                        .orderTotal(longSummaryStatistics.getCount())
                        .build();

                plannedTrendBeanList.add(trendBean);
            }

            return plannedTrendBeanList;
        }

        return null;
    }
}