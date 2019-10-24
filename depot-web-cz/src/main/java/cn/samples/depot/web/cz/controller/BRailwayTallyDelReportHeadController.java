/**
 * @filename:BRailwayTallyDelReportHeadController 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.DeclareStatusFlag;
import cn.samples.depot.common.model.IEFlag;
import cn.samples.depot.common.model.MessageTypeFlag;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.report.BRailwayTallyDelReportQuery;
import cn.samples.depot.web.bean.report.BRailwayTallyDelReportSave;
import cn.samples.depot.web.cz.service.BRailwayTallyBillInfoService;
import cn.samples.depot.web.cz.service.BRailwayTallyDelBillInfoService;
import cn.samples.depot.web.cz.service.BRailwayTallyDelReportHeadService;
import cn.samples.depot.web.entity.BRailwayTallyBillInfo;
import cn.samples.depot.web.entity.BRailwayTallyDelBillInfo;
import cn.samples.depot.web.entity.BRailwayTallyDelReportHead;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BRailwayTallyDelReportHeadController.API;

/**
 * @Description: 铁路理货作废
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Api(tags = "海关业务-理货作废-表头", value = "铁路理货作废")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BRailwayTallyDelReportHeadController {

    static final String API = "/BRailwayTallyDelReportHead";

    @Autowired
    private BRailwayTallyDelReportHeadService service;

    @Autowired
    private BRailwayTallyDelBillInfoService delBillInfoService;

    @Autowired
    private BRailwayTallyBillInfoService billInfoService;

    /**
     * @return PageInfo<BRailwayTallyDelReportHead>
     * @explain 铁路理货作废分页查询
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "铁路理货作废->分页查询")
    public JsonResult index(@Valid BRailwayTallyDelReportQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return JsonResult.data(Params.param(M_PAGE, service.selectDelReportListPage(query, pageNum, pageSize))
                .set("query", Optional.ofNullable(query).orElseGet(BRailwayTallyDelReportQuery::new))
                // 申报状态
                .set("declareStatusFlag", DeclareStatusFlag.select())
                // 理货报告运单的审核状态
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                // 进出口
                .set("ieFlag", IEFlag.values())
                // 报文类型
                .set("messageTypeFlag", MessageTypeFlag.values()));
    }

    /**
     * Description: 查看指定铁路进口理货作废报文表头数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月12日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("铁路理货作废->查看理货作废明细")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.queryByMsgId(id))// 申报状态
                .set("declareStatusFlag", DeclareStatusFlag.select())
                // 理货报告运单的审核状态
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                // 进出口
                .set("ieFlag", IEFlag.values())
                // 报文类型
                .set("messageTypeFlag", MessageTypeFlag.values()));
    }

    /**
     * Description: 查看指定铁路进口理货作废报文表头数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月12日
     **/
    @AddLog
    @GetMapping(value = "/edit" + ID)
    @ApiOperation("铁路理货作废->编辑理货作废明细")
    public JsonResult edit(@PathVariable String id) {
        return JsonResult.data(Params.param("edit", service.editByMsgId(id))// 申报状态
                .set("declareStatusFlag", DeclareStatusFlag.select())
                // 理货报告运单的审核状态
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                // 进出口
                .set("ieFlag", IEFlag.values())
                // 报文类型
                .set("messageTypeFlag", MessageTypeFlag.values()));
    }

    /**
     * @return int
     * @explain 添加理货作废
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "铁路理货作废->添加理货作废")
    public JsonResult save(@RequestBody BRailwayTallyDelReportSave delReportSave) {
        return service.saveDelReportInfo(delReportSave);
    }

    /**
     * @return int
     * @explain 删除理货作废
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "铁路理货作废->删除理货作废")
    public JsonResult deleteSingle(@PathVariable String id) {
        return service.removeDelReportById(id);
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增铁路进口理货作废报文表头", notes = "新增铁路进口理货作废报文表头[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BRailwayTallyDelReportHead())
                // 作废表头状态
                .set("declareStatusFlag", DeclareStatusFlag.select())
                // 理货报告运单的审核状态
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                // 进出口
                .set("ieFlag", IEFlag.values())
                // 报文类型
                .set("messageTypeFlag", MessageTypeFlag.values()));
    }

    @AddLog
    @PostMapping(value = DECLARE + ID)
    @ApiOperation(value = "铁路理货作废->申报")
    public JsonResult declareDel(@PathVariable String id) {
        return service.declareDel(id);
    }

    /**
     * @Author zhangpeng
     * @Date 2019/9/3
     * @Description 编辑-增加运单
     **/
    @AddLog
    @PutMapping(value = "/addList" + ID)
    @ApiOperation(value = "铁路理货作废编辑->增加运单", notes = "传表头id")
    public JsonResult updateAddList(@PathVariable String id, @RequestBody List<BRailwayTallyBillInfo> contas) throws Throwable {
        delBillInfoService.add(id, contas);
        return JsonResult.success();
    }

    /**
     * @Author zhangpeng
     * @Date 2019/9/3
     * @Description 根据申报id，获取集装箱集合
     **/
    @AddLog
    @GetMapping(value = "/listAddContas/{messageAddId}")
    @ApiOperation(value = "铁路理货作废->编辑->添加运单展示的运单列表", notes = "查询-集装箱集合（可作废的）")
    public JsonResult listContas(@PathVariable("messageAddId") String messageAddId) throws Throwable {
        return JsonResult.data(billInfoService.listContas(messageAddId));
    }

    /**
     * @Author zhangpeng
     * @Date 2019/8/26
     * @Description 编辑-更新作废备注
     **/
    @AddLog
    @PutMapping(value = "/note/{contaId}")

    @ApiOperation(value = "铁路理货作废->编辑->更新作废备注", notes = "传集装箱id")
    public JsonResult updateContaNote(@PathVariable String contaId, @RequestParam String notes) throws Throwable {
        return JsonResult.data(delBillInfoService.update(BRailwayTallyDelBillInfo.builder().notes(notes).build(), Wrappers.<BRailwayTallyDelBillInfo>lambdaQuery().eq(BRailwayTallyDelBillInfo::getId, contaId)));
    }

    /**
     * @Author zhangpeng
     * @Date 2019/8/26
     * @Description 编辑-删除集装箱
     **/
    @AddLog
    @PutMapping(value = "/delConta/{contaId}")
    @ApiOperation(value = "铁路理货作废->编辑->删除集装箱", notes = "传运集装箱id")
    public JsonResult updateDelConta(@PathVariable String contaId) throws Throwable {
        delBillInfoService.deleteByContaId(contaId);
        return JsonResult.success();
    }
}