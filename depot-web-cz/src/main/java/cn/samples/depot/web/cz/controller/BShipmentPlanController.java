package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.AuditStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.BShipmentPlanQuery;
import cn.samples.depot.web.cz.service.BShipmentPlanService;
import cn.samples.depot.web.entity.BShipmentAuditLog;
import cn.samples.depot.web.entity.BShipmentPlan;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BShipmentPlanController.API;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 作业管理-发运计划
 **/
@Api(tags = "作业管理-发运计划审核")
@RestController
@RequestMapping(value = API)
@Slf4j
public class BShipmentPlanController {

    static final String API = URL_PRE_STATION + "/BShipmentPlan";

    @Autowired
    private BShipmentPlanService service;


    /**
     * @Author majunzi
     * @Date 2019/8/22
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
                .eq((StringUtils.isNotEmpty(bShipmentPlanQuery.getEnterprisesId())), BShipmentPlan::getEnterprisesId, bShipmentPlanQuery.getEnterprisesId()) // 发货企业id
                .eq((StringUtils.isNotEmpty(bShipmentPlanQuery.getAuditStatus())), BShipmentPlan::getAuditStatus, bShipmentPlanQuery.getAuditStatus()) // 审核状态
                .ge((bShipmentPlanQuery.getStartShipmentTime() != null && bShipmentPlanQuery.getStartShipmentTime() > 0), BShipmentPlan::getShipmentTime, bShipmentPlanQuery.getStartShipmentTime())
                .le((bShipmentPlanQuery.getEndShipmentTime() != null && bShipmentPlanQuery.getEndShipmentTime() > 0), BShipmentPlan::getShipmentTime, bShipmentPlanQuery.getEndShipmentTime())
                .like((StringUtils.isNotEmpty(bShipmentPlanQuery.getShipmentPlanNo())), BShipmentPlan::getShipmentPlanNo, bShipmentPlanQuery.getShipmentPlanNo())
                .orderByDesc(BShipmentPlan::getCreateTime);// 发运计划编号


        // 查询第1页，每页返回10条
        Page<BShipmentPlan> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bShipmentPlanQuery).orElseGet(BShipmentPlanQuery::new))
                .set("aduitStatus", Lists.newArrayList(AuditStatus.PRE_AUDIT, AuditStatus.AUDIT_PASS, AuditStatus.AUDIT_REJECT, AuditStatus.INVALID)));// 返回审核状态
    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 查看
     **/
    @AddLog
    // @Cacheable(value = "bShipmentPlan", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.detail(id))
                .set("aduitStatus", AuditStatus.values())
        );
    }


    /**
     * @Author majunzi
     * @Description 单个作废
     * @Date 2019/7/29
     **/
    @AddLog
    @PutMapping(value = "/cancel" + ID)
    @ApiOperation(value = "单个作废", notes = "单个作废")
    public JsonResult cancelSingle(@PathVariable String id) throws Throwable {
        return JsonResult.data(service.cancel(id));
    }

    /**
     * @Author majunzi
     * @Description 单个审核
     * @Date 2019/7/29
     **/
    @AddLog
    @PutMapping(value = "/audit" + ID)
    @ApiOperation(value = "单个审核", notes = "单个审核")
    public JsonResult auditSingle(@PathVariable String id, @RequestBody BShipmentAuditLog log) throws Throwable {
        log.setShipmentPlanId(id);
        return JsonResult.data(service.audit(log));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 分页查询
     **/
    @AddLog
    @GetMapping("/search4arrive")
    // @Cacheable(value = "bShipmentPlan", sync = true)
    @ApiOperation(value = "运抵申报-查询")
    @JsonView(BShipmentPlan.View.ARRIVESELECT.class)
    public JsonResult search4arrive(@Valid BShipmentPlanQuery bShipmentPlanQuery) {
        return JsonResult.data(Params.param(M_PAGE, service.search4arrive(bShipmentPlanQuery))
                .set("query", Optional.ofNullable(bShipmentPlanQuery).orElseGet(BShipmentPlanQuery::new)));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 运抵-导入发运计划
     **/
    @AddLog
    @PostMapping("/import4arrive")
    @ApiOperation(value = "运抵申报-导入", notes = "运抵导入发运计划")
//    @CacheEvict(value = "cDischarges", allEntries = true)
    public JsonResult import4arrive(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.import4arrive(Arrays.asList(ids)));
    }
}