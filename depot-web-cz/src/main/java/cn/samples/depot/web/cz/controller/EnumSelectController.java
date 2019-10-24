package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.model.AuditStatus;
import cn.samples.depot.common.model.EnstaveMentFlag;
import cn.samples.depot.common.model.SealTypeFlag;
import cn.samples.depot.common.utils.JsonResult;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.samples.depot.common.utils.Controllers.URL_PRE_STATION;
import static cn.samples.depot.web.cz.controller.EnumSelectController.API;


/**
 * @author majunzi
 * @Description 一些枚举 下拉选择框
 * @time 2019-09-04 14:52
 */
@Api(tags = "下拉 一些枚举的下拉选择框")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
public class EnumSelectController {
    static final String API = "/EnumSelect";

    /**
     * @Author majunzi
     * @Date 2019/9/4
     * @Description 铅封类型
     **/
    @GetMapping("/sealType")
    @ApiOperation(value = "封志号-铅封类型")
    public JsonResult sealType() {
        return JsonResult.data(SealTypeFlag.values());
    }

    /**
     * @Author majunzi
     * @Date 2019/9/4
     * @Description 施加封志人
     **/
    @GetMapping("/enstaveMent")
    @ApiOperation(value = "封志号-施加封志人")
    public JsonResult enstaveMent() {
        return JsonResult.data(EnstaveMentFlag.values());
    }

    /**
     * @Author majunzi
     * @Date 2019/9/4
     * @Description 发运计划审核-审核意见
     **/
    @GetMapping("/shipmentPlanAuditStatus")
    @ApiOperation(value = "发运计划审核-审核意见")
    public JsonResult shipmentPlanAuditStatus() {
        return JsonResult.data(Lists.newArrayList(AuditStatus.AUDIT_PASS, AuditStatus.AUDIT_REJECT));
    }

}
