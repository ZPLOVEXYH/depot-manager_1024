package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.CreateType;
import cn.samples.depot.common.model.DeclareStatusFlag;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.railwayarrive.ArriveQuery;
import cn.samples.depot.web.bean.railwayarrive.ArriveReport4UpdateVo;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.entity.BExRailwayList;
import cn.samples.depot.web.entity.BExRailwayReportHead;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BExRailwayReportHeadController.API;

/**
 * @Author majunzi
 * @Date 2019/8/21
 * @Description 铁路-运抵报告
 **/
@Api(tags = "海关业务-运抵报告")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BExRailwayReportHeadController {

    static final String API = "/BExRailwayReportHead";

    @Autowired
    private BExRailwayReportHeadService service;

    @Autowired
    private BExRailwayContaService contaService;
    @Autowired
    private BExRailwayListService listService;
    @Autowired
    private PCustomsCodeService customsCodeService;
    @Autowired
    private CDischargesService dischargesService;

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 分页查询
     **/
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询")
    public JsonResult index(@Valid ArriveQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return JsonResult.data(Params.param(M_PAGE, service.page(query, pageNum, pageSize))
                .set("query", Optional.ofNullable(query).orElseGet(ArriveQuery::new))
                .set("auditStatus", DeclareStatusFlag.select())
                .set("messageMode", Lists.newArrayList(CreateType.values()))
                .set("customsCode", customsCodeService.select())
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("dischargePlace", dischargesService.select())
        );
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 查看
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看-详情")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.detail(id))
                .set("auditStatus", DeclareStatusFlag.select())
                .set("messageMode", Lists.newArrayList(CreateType.values()))
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("customsCode", customsCodeService.select())
                .set("dischargePlace", dischargesService.select())
        );
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 新增
     **/
    @GetMapping(value = NEW)
    @ApiOperation(value = "新增")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, ArriveReport4UpdateVo.getEmpty())
                .set("messageMode", Lists.newArrayList(CreateType.MANUAL, CreateType.SHIP_PLAN))
                .set("auditStatus", DeclareStatusFlag.select())
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("customsCode", customsCodeService.select())
                .set("dischargePlace", dischargesService.select()));
    }


    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 保存
     **/
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存")
    public JsonResult save(@RequestBody ArriveReport4UpdateVo vo) throws Throwable {
        return service.save(vo);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 查看
     **/
    @AddLog
    @GetMapping(value = "/update/" + ID)
    @ApiOperation("编辑-详情")
    public JsonResult detail4Update(@PathVariable String id) throws Throwable {
        return JsonResult.data(Params.param(M_DETAIL, service.detail4Update(id))
                .set("auditStatus", Lists.newArrayList(DeclareStatusFlag.values()))
                .set("messageMode", Lists.newArrayList(CreateType.values())));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 编辑
     **/
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "编辑-保存", notes = "编辑")
    public JsonResult update(@PathVariable String id, @RequestBody ArriveReport4UpdateVo vo) throws Throwable {
        service.update(id, vo);
        return JsonResult.success();
    }


    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 删除
     **/
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除", notes = "单个删除")
    public JsonResult deleteSingle(@PathVariable String id) throws Throwable {
        service.deleteById(id);
        return JsonResult.success();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 下拉列表
     **/
    @GetMapping(value = SELECT)
    @ApiOperation(value = "下拉查询-运抵申报（表头）")
    @JsonView(value = BExRailwayReportHead.View.SELECT.class)
    public JsonResult select() {
        return JsonResult.data(service.select());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 下拉列表
     **/
    @GetMapping(value = "/selectArrives")
    @ApiOperation(value = "下拉查询-运抵单（表体）")
    @JsonView(BExRailwayList.View.SELECT.class)
    public JsonResult selectArrive() {
        return JsonResult.data(listService.select());
    }


    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 根据运抵单id，获取集装箱信息
     **/
    @AddLog
    @GetMapping(value = "/listContas/{listId}")
    @ApiOperation(value = "查询-集装箱集合", notes = "根据运抵申报单id，查询运抵单（审核通过）集合")
    public JsonResult listContas(@PathVariable String listId) {
        return JsonResult.data(contaService.listContas(listId));
    }

    /**
     * 运抵报告申报
     *
     * @param id
     * @return
     */
    @AddLog
    @PostMapping(value = DECLARE + ID)
    @ApiOperation(value = "申报", notes = "运抵报告申报[bExRailwayReportHead],作者：ZhangPeng")
    public JsonResult declare(@PathVariable String id) {
        return service.declare(id);
    }

}