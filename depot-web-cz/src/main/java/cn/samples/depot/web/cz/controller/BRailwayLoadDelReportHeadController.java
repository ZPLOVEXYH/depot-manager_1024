/**
 * @filename:BRailwayLoadDelReportHeadController 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.DeclareStatusFlag;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.load.LoadDelAddVo;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadDelConta;
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
import static cn.samples.depot.web.cz.controller.BRailwayLoadDelReportHeadController.API;

/**
 * @Description: 装车记录单作废报文表头接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Api(tags = "海关业务-装车记录作废")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BRailwayLoadDelReportHeadController {

    static final String API = "/BRailwayLoadDelReportHead";

    @Autowired
    private BRailwayLoadDelReportHeadService service;
    @Autowired
    private BRailwayLoadDelContaService delContaService;
    @Autowired
    private BRailwayLoadContaService contaService;
    @Autowired
    private BRailwayLoadDelListService delListService;
    @Autowired
    private PCustomsCodeService customsCodeService;
    @Autowired
    private CDischargesService dischargesService;

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 分页查询
     **/
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询")
    public JsonResult index(@Valid LoadQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return JsonResult.data(Params.param(M_PAGE, service.page(query, pageNum, pageSize))
                .set("query", Optional.ofNullable(query).orElseGet(LoadQuery::new))
                .set("auditStatus", Lists.newArrayList(DeclareStatusFlag.values()))
                .set("customsCode", customsCodeService.select())
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("dischargePlace", dischargesService.select())

        );
    }

    /**
     * Description: 查看指定装车记录单作废报文表头数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月20日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.queryLoadDelReportById(id))
                .set("auditStatus", Lists.newArrayList(DeclareStatusFlag.select()))
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("customsCode", customsCodeService.select())
                .set("dischargePlace", dischargesService.select()));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 新增-空对象
     **/
    @GetMapping(value = NEW)
    @ApiOperation(value = "新增")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, LoadDelAddVo.getEmpty())
                .set("auditStatus", Lists.newArrayList(DeclareStatusFlag.values()))
                .set("customsCode", customsCodeService.select())
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("dischargePlace", dischargesService.select()));
    }


    /**
     * @return int
     * @explain 保存装车记录单作废报文表头对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存")
    public JsonResult save(@RequestBody LoadDelAddVo vo) throws Throwable {
        return service.saveVo(vo);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 编辑-删除集装箱
     **/
    @AddLog
    @PutMapping(value = "/delConta/{contaId}")
    @ApiOperation(value = "编辑-删除集装箱", notes = "传运集装箱id")
    public JsonResult updateDelConta(@PathVariable String contaId) throws Throwable {
        delContaService.deleteByContaId(contaId);
        return JsonResult.success();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 编辑-更新作废备注
     **/
    @AddLog
    @PutMapping(value = "/note/{contaId}")
    @ApiOperation(value = "编辑-更新作废备注", notes = "传集装箱id")
    public JsonResult updateContaNote(@PathVariable String contaId, @RequestParam String notes) throws Throwable {
        return JsonResult.data(delContaService.update(new LambdaUpdateWrapper<BRailwayLoadDelConta>().set(BRailwayLoadDelConta::getNotes, notes).eq(BRailwayLoadDelConta::getId, contaId)));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 编辑-增加运单
     **/
    @AddLog
    @PutMapping(value = "/addList" + ID)
    @ApiOperation(value = "编辑-增加运单", notes = "传表头id")
    public JsonResult updateAddList(@PathVariable String id, @RequestBody List<BRailwayLoadConta> contas) throws Throwable {
        delContaService.add(id, contas);
        return JsonResult.success();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 根据集装箱id，获取运抵明细表
     **/
    @AddLog
    @GetMapping(value = "/listArrives/{contaId}")
    @ApiOperation(value = "根据集装箱id，获取运抵明细表")
    public JsonResult listArrives(@PathVariable String contaId) {
        return JsonResult.data(delListService.listArrives(contaId));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 根据申报id，获取集装箱集合
     **/
    @AddLog
    @GetMapping(value = "/listAddContas/{messageAddId}")
    @ApiOperation(value = "添加运单", notes = "查询-集装箱集合（可作废的）")
    public JsonResult listContas(@PathVariable String messageAddId) throws Throwable {
        return JsonResult.data(contaService.listContas(messageAddId));
    }

    /**
     * @return int
     * @explain 单个删除装车记录单作废报文表头对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除装车记录单作废报文表头", notes = "单个删除装车记录单作废报文表头,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return service.deleteLoadDelReportById(id);
    }


    @AddLog
    @PostMapping(value = DECLARE + ID)
    @ApiOperation(value = "装车记录单作废", notes = "装车记录单作废[bRailwayLoadDelReportHead],作者：ZhangPeng")
    public JsonResult declare(@PathVariable String id) {
        return service.declare(id);
    }
}