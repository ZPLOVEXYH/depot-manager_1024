package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.common.model.IEFlag;
import cn.samples.depot.common.model.ShipmentPlanStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.cz.service.BContainerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BContainerInfoController.API;

/**
 * @Author majunzi
 * @Date 2019/9/23
 * @Description 箱号查询
 **/
@Api(tags = "查询统计-箱号查询")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BContainerInfoController {

    static final String API = "/BContainerInfo";

    @Autowired
    private BContainerInfoService service;

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 箱号查询
     **/
    @AddLog
    @GetMapping(value = "/{contaNo}")
    @ApiOperation(value = "箱号查询")
    public JsonResult contaInfo(@PathVariable String contaNo) throws Throwable {
        return JsonResult.data(Params.param(M_PAGE, service.contaInfo(contaNo))
                .set("status", ShipmentPlanStatus.values()));
    }

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 箱货信息
     **/
    @AddLog
    @GetMapping(value = "/contaDetial/{contaId}")
    @ApiOperation("箱货信息")
    public JsonResult contaDetail(@PathVariable String contaId) throws Throwable {
        return JsonResult.data(Params.param(M_DETAIL, service.contaDetial(contaId))
                .set("status", ShipmentPlanStatus.values())
                .set("contaOptionType", ContaOptionType.values())
                .set("ieFlag", IEFlag.values()));
    }
}