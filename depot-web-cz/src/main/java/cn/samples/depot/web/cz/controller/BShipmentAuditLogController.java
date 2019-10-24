package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.cz.service.BShipmentAuditLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.samples.depot.common.utils.Controllers.M_DETAIL;
import static cn.samples.depot.common.utils.Controllers.URL_PRE_STATION;
import static cn.samples.depot.web.cz.controller.BShipmentAuditLogController.API;

/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 作业管理-发运计划-审核日志
 **/
@Api(tags = "作业管理-发运计划-审核日志", value = "审核日志表")
@RestController
@RequestMapping(value = API)
@Slf4j
public class BShipmentAuditLogController {

    static final String API = URL_PRE_STATION + "/BShipmentAuditLog";

    @Autowired
    private BShipmentAuditLogService service;

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 查看
     **/
    @AddLog
    // @Cacheable(value = "bShipmentAuditLog", key = "#id")
    @GetMapping(value = "/{shipmentPlanId}")
    @ApiOperation("查看")
    public JsonResult detail(@PathVariable String shipmentPlanId) {
        return JsonResult.data(Params.param(M_DETAIL, service.listByShipmentPlanId(shipmentPlanId)));
    }

}