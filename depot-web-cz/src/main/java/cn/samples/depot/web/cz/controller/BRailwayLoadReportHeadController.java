/**
 * @filename:BRailwayLoadReportHeadController 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.DeclareStatusFlag;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.bean.load.LoadReportVo;
import cn.samples.depot.web.cz.service.BRailwayLoadListService;
import cn.samples.depot.web.cz.service.BRailwayLoadReportHeadService;
import cn.samples.depot.web.cz.service.CDischargesService;
import cn.samples.depot.web.cz.service.PCustomsCodeService;
import cn.samples.depot.web.entity.BRailwayLoadList;
import cn.samples.depot.web.entity.BRailwayLoadReportHead;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BRailwayLoadReportHeadController.API;

/**
 * @Description: 装车记录单申请报文表头接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Api(tags = "海关业务-装车记录申报")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BRailwayLoadReportHeadController {

    static final String API = "/BRailwayLoadReportHead";

    @Autowired
    private BRailwayLoadReportHeadService service;
    @Autowired
    private BRailwayLoadListService listService;
    @Autowired
    private PCustomsCodeService customsCodeService;
    @Autowired
    private CDischargesService dischargesService;

    /**
     * @Author majunzi
     * @Date 2019/8/29
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
                .set("auditStatus", DeclareStatusFlag.select())
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("customsCode", customsCodeService.select())
                .set("dischargePlace", dischargesService.select())
        );
    }

    /**
     * Description: 查看指定装车记录单申请报文表头数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月20日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看详情")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.queryLoadReportById(id))
                .set("auditStatus", DeclareStatusFlag.select())
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("customsCode", customsCodeService.select())
                .set("dischargePlace", dischargesService.select())
        );
    }

    /**
     * Description: 查看指定装车记录单申请报文表头数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月20日
     **/
    @AddLog
    @GetMapping(value = "/detail" + DECL_MESSAGE_ID)
    @ApiOperation("查看详情")
    public JsonResult detailByCancel(@PathVariable String declMessageId) {
        return JsonResult.data(Params.param(M_DETAIL, service.queryLoadReportByIdForCancel(declMessageId))
                .set("auditStatus", DeclareStatusFlag.select())
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("customsCode", customsCodeService.select())
                .set("dischargePlace", dischargesService.select())
        );
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 新增
     **/
    @GetMapping(value = NEW)
    @ApiOperation(value = "新增")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, LoadReportVo.getEmpty())
                .set("customsCode", customsCodeService.select())
                .set("auditStatus", DeclareStatusFlag.select())
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                .set("customsCode", customsCodeService.select())
                .set("dischargePlace", dischargesService.select())
        );
    }

    /**
     * @Author majunzi
     * @Date 2019/9/16
     * @Description 下拉选择
     **/
    @GetMapping(value = SELECT)
    @ApiOperation(value = "下拉查询")
    @JsonView(BRailwayLoadReportHead.View.SELECT.class)
    public JsonResult select() {
        return JsonResult.data(service.select());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 保存
     **/
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存")
    public JsonResult save(@RequestBody LoadReportVo vo) throws Throwable {
        return service.saveVo(vo);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 编辑-
     **/
    @AddLog
    @GetMapping("/update/" + ID)
    @ApiOperation("编辑-详情")
    public JsonResult detail4Update(@PathVariable String id) throws Throwable {
        return JsonResult.data(Params.param(M_DETAIL, service.detail4Update(id)));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 编辑-保存
     **/
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "编辑-保存")
    public JsonResult update(@PathVariable String id, @RequestBody LoadReportVo vo) throws Throwable {
        service.update(id, vo);
        return JsonResult.success();
    }

    /**
     * @return int
     * @explain 单个删除装车记录单申请报文表头对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除装车记录单申请报文表头", notes = "单个删除装车记录单申请报文表头,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) throws Throwable {
        service.deleteLoadReportById(id);
        return JsonResult.success();
    }


    @AddLog
    @PostMapping(value = DECLARE + ID)
    @ApiOperation(value = "装车记录单申报", notes = "装车记录单理货申报[bRailwayLoadReportHead],作者：ZhangPeng")
    public JsonResult declare(@PathVariable String id) {
        return service.declare(id);
    }


    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 根据集装箱id，获取运抵信息
     **/
    @GetMapping(value = "/listArrives/{contaId}")
    @ApiOperation(value = "根据集装箱id，获取运抵信息")
    public JsonResult listArrives(@PathVariable String contaId) throws BizException {
        return JsonResult.data(Params.param(M_DETAIL, listService.list(new LambdaQueryWrapper<BRailwayLoadList>().eq(BRailwayLoadList::getRailwayLoadReportContaId, contaId))));
    }

}