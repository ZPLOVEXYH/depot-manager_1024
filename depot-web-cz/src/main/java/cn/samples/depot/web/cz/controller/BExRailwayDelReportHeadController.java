package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.DeclareStatusFlag;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.railwayarrive.ArriveQuery;
import cn.samples.depot.web.bean.railwayarrive.ArriveReportDel4AddVo;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.entity.BExRailwayDelList;
import cn.samples.depot.web.entity.BExRailwayList;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BExRailwayDelReportHeadController.API;

/**
 * @Author majunzi
 * @Date 2019/8/26
 * @Description 海关业务-运抵作废
 **/
@Api(tags = "海关业务-运抵作废")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BExRailwayDelReportHeadController {

    static final String API = "/BExRailwayDelReportHead";

    @Autowired
    private BExRailwayDelReportHeadService service;
    @Autowired
    private BExRailwayDelListService delListService;
    @Autowired
    private BExRailwayDelContaService contaService;
    @Autowired
    private PCustomsCodeService customsCodeService;
    @Autowired
    private CDischargesService dischargesService;
    @Autowired
    private BExRailwayListService listService;

    /**
     * @Author majunzi
     * @Date 2019/8/26
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
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("customsCode", customsCodeService.select())
                .set("dischargePlace", dischargesService.select()));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 查看-详情
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看-详情")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.detail(id))
                        .set("auditStatus", Lists.newArrayList(DeclareStatusFlag.select()))
//                .set("arriveListAuditStatus", Lists.newArrayList(InvalidStateFlag.values()))
                        .set("arriveListAuditStatus", DeclareStatusFlag.select())
                        .set("wayBillAuditStatus", WayBillAuditStatus.values())
                        .set("customsCode", customsCodeService.select())
                        .set("dischargePlace", dischargesService.select())
        );
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 新增
     **/
    @GetMapping(value = NEW)
    @ApiOperation(value = "新增")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, ArriveReportDel4AddVo.getEmpty())
                .set("auditStatus", DeclareStatusFlag.select())
//                .set("arriveListAuditStatus", Lists.newArrayList(InvalidStateFlag.values()))
                .set("arriveListAuditStatus", DeclareStatusFlag.select())
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("customsCode", customsCodeService.select())
                .set("dischargePlace", dischargesService.select()));
    }


    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 保存
     **/
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存")
    public JsonResult save(@RequestBody ArriveReportDel4AddVo vo) throws Throwable {
        return service.save(vo);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 编辑-详情
     **/
    @AddLog
    @GetMapping(value = "/update/" + ID)
    @ApiOperation("编辑-详情")
    public JsonResult detail4Update(@PathVariable String id) throws Throwable {
        return JsonResult.data(Params.param(M_DETAIL, service.detail4Update(id))
                .set("auditStatus", Lists.newArrayList(DeclareStatusFlag.values())));
    }


    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 编辑-删除运抵单
     **/
    @AddLog
    @PutMapping(value = "/delList/{listId}")
    @ApiOperation(value = "编辑-删除运抵单", notes = "传运抵单id")
    public JsonResult updateDelList(@PathVariable String listId) throws Throwable {
        service.deleteByListId(listId);
        return JsonResult.success();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 编辑-更新作废备注
     **/
    @AddLog
    @PutMapping(value = "/note/{listId}")
    @ApiOperation(value = "编辑-更新作废备注", notes = "传运抵单id")
    public JsonResult updateListNote(@PathVariable String listId, @RequestParam String notes) throws Throwable {
        return JsonResult.data(delListService.update(new LambdaUpdateWrapper<BExRailwayDelList>().set(BExRailwayDelList::getNotes, notes).eq(BExRailwayDelList::getId, listId)));
    }


    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 根据申报id，获取运抵单信息
     **/
    @AddLog
    @GetMapping(value = "/listArrives/{messageAddId}")
    @ApiOperation(value = "查询-运抵单集合", notes = "根据运抵申报单id，查询运抵单（审核通过）集合")
    public JsonResult listArrives(@PathVariable String messageAddId) throws Throwable {
        return JsonResult.data(listService.listArrives(messageAddId));
    }


    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 编辑-增加运抵单
     **/
    @AddLog
    @PutMapping(value = "/addList" + ID)
    @ApiOperation(value = "编辑-增加运抵单", notes = "传表头id")
    public JsonResult updateAddList(@PathVariable String id, @RequestBody List<BExRailwayList> arrives) throws Throwable {
        delListService.add(id, arrives);
        return JsonResult.success();
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
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 删除
     **/
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除")
    public JsonResult deleteSingle(@PathVariable String id) throws Throwable {
        service.deleteById(id);
        return JsonResult.success();
    }

    /**
     * 运抵报告申报
     *
     * @param id
     * @return
     */
    @AddLog
    @PostMapping(value = DECLARE + ID)
    @ApiOperation(value = "运抵报告申报", notes = "运抵报告申报[bExRailwayReportHead],作者：ZhangPeng")
    public JsonResult declare(@PathVariable String id) {
        return service.declare(id);
    }
}