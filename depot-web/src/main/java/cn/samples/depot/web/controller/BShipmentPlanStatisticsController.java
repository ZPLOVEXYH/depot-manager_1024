package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.AuditStatus;
import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.common.model.IEFlag;
import cn.samples.depot.common.model.ShipmentPlanStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.BShipmentPlanQuery;
import cn.samples.depot.web.entity.BShipmentPlan;
import cn.samples.depot.web.service.BContainerInfoService;
import cn.samples.depot.web.service.BShipmentPlanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.BShipmentPlanStatisticsController.API;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 作业管理-发运计划
 **/
@Api(tags = "发运计划-发运计划查询")
@RestController
@RequestMapping(value = API)
@Slf4j
public class BShipmentPlanStatisticsController {

    static final String API = URL_PRE_ENTERPRICE + "/BShipmentPlanStatistics";

    @Autowired
    private BShipmentPlanService service;

    @Autowired
    private BContainerInfoService containerInfoService;


    /**
     * @Author majunzi
     * @Date 2019/9/19
     * @Description 分页查询
     **/
    @AddLog
    @GetMapping
    // @Cacheable(value = "bShipmentPlan", sync = true)
    @ApiOperation(value = "分页查询")
    public JsonResult index(@Valid BShipmentPlanQuery bShipmentPlanQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BShipmentPlan> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(BShipmentPlan::getAuditStatus, AuditStatus.AUDIT_PASS.getValue())
                .eq((StringUtils.isNotEmpty(bShipmentPlanQuery.getEnterprisesId())), BShipmentPlan::getEnterprisesId, bShipmentPlanQuery.getEnterprisesId()) // 发货企业id
                .eq((StringUtils.isNotEmpty(bShipmentPlanQuery.getStatus())), BShipmentPlan::getStatus, bShipmentPlanQuery.getStatus()) // 状态
                .ge((bShipmentPlanQuery.getStartShipmentTime() != null && bShipmentPlanQuery.getStartShipmentTime() > 0), BShipmentPlan::getShipmentTime, bShipmentPlanQuery.getStartShipmentTime())
                .le((bShipmentPlanQuery.getEndShipmentTime() != null && bShipmentPlanQuery.getEndShipmentTime() > 0), BShipmentPlan::getShipmentTime, bShipmentPlanQuery.getEndShipmentTime())
                .like((StringUtils.isNotEmpty(bShipmentPlanQuery.getShipmentPlanNo())), BShipmentPlan::getShipmentPlanNo, bShipmentPlanQuery.getShipmentPlanNo());// 发运计划编号


        // 查询第1页，每页返回10条
        Page<BShipmentPlan> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bShipmentPlanQuery).orElseGet(BShipmentPlanQuery::new))
                .set("status", ShipmentPlanStatus.values()));// 返回状态
    }

    /**
     * @Author majunzi
     * @Date 2019/9/19
     * @Description 查看
     **/
    @AddLog
    // @Cacheable(value = "bShipmentPlan", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("发运计划详情")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.detail(id))
                .set("aduitStatus", AuditStatus.values())
                .set("status", ShipmentPlanStatus.values())
        );
    }

    /**
     * @Author majunzi
     * @Date 2019/9/20
     * @Description 查看集装箱详情
     **/
    @AddLog
    @GetMapping(value = "/contaDetial/{contaId}")
    @ApiOperation("箱货详情")
    public JsonResult contaDetail(@PathVariable String contaId) throws Throwable {
        return JsonResult.data(Params.param(M_DETAIL, containerInfoService.contaDetialByShipmentContaId(contaId))
                .set("status", ShipmentPlanStatus.values())
                .set("contaOptionType", ContaOptionType.values())
                .set("ieFlag", IEFlag.values()));
    }
}