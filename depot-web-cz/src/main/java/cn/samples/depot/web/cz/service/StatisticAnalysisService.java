package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.bean.analysis.PlanTrendQuery;
import cn.samples.depot.web.bean.analysis.RspPlannedOrderDto;
import cn.samples.depot.web.bean.analysis.RspPlannedTrendBean;
import cn.samples.depot.web.bean.analysis.RspStockAnalysisBean;

import java.util.List;


/**
 * @Description 查询统计-统计分析
 * @Author ZhangPeng
 * @Date 2019/9/25
 **/
public interface StatisticAnalysisService {

    /**
     * 查询统计-堆存分析接口
     *
     * @return
     */
    List<RspStockAnalysisBean> stockAnalysis();

    /**
     * 查询统计-计划单总量和有效发运计划单
     *
     * @return
     */
    RspPlannedOrderDto plannedOrder();

    /**
     * 发运计划趋势
     *
     * @param query
     * @return
     */
    List<RspPlannedTrendBean> plannedTrend(PlanTrendQuery query);
}