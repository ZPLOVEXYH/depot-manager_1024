package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.analysis.PlanTrendQuery;
import cn.samples.depot.web.service.StatisticAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.samples.depot.common.utils.Controllers.URL_PRE_ENTERPRICE;
import static cn.samples.depot.web.controller.StatisticAnalysisController.API;

/**
 * 查询统计-统计分析
 *
 * @Author ZhangPeng
 * @Date 2019/9/25
 * @Description 统计分析
 **/
@Api(tags = "查询统计-统计分析", value = "查询统计-统计分析")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class StatisticAnalysisController {

    static final String API = "/StatisticAnalysis";

    @Autowired
    private StatisticAnalysisService service;


    /**
     * 统计分析-堆存分析
     *
     * @Description 堆存分析
     * @Author ZhangPeng
     * @Date 2019/9/25
     **/
    @AddLog
    @GetMapping(value = "/stockAnalysis")
    @ApiOperation(value = "统计分析-堆存分析")
    public JsonResult stockAnalysis() {
        return JsonResult.data(service.stockAnalysis());
    }

    /**
     * 统计分析-发运计划单总量和有效发运计划单
     *
     * @Description 发运计划单总量和有效发运计划单
     * @Author ZhangPeng
     * @Date 2019/9/25
     **/
    @AddLog
    @GetMapping(value = "/plannedOrder")
    @ApiOperation("统计分析-发运计划单总量和有效发运计划单")
    public JsonResult plannedOrder() {
        return JsonResult.data(service.plannedOrder());
    }

    /**
     * 统计分析-发运计划趋势
     *
     * @Description 发运计划趋势
     * @Author ZhangPeng
     * @Date 2019/9/25
     **/
    @AddLog
    @GetMapping(value = "/plannedTrend")
    @ApiOperation("统计分析-发运计划趋势")
    public JsonResult plannedTrend(PlanTrendQuery query) {
        return JsonResult.data(service.plannedTrend(query));
    }
}