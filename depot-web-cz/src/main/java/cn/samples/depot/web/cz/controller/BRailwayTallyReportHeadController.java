/**
 * @filename:BRailwayTallyReportHeadController 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.*;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.report.BRailwayTallyReportHeadSave;
import cn.samples.depot.web.bean.report.BRailwayTallyReportQuery;
import cn.samples.depot.web.bean.report.TallyReportVo;
import cn.samples.depot.web.cz.service.BRailwayTallyReportHeadService;
import cn.samples.depot.web.entity.BRailwayTallyReportHead;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BRailwayTallyReportHeadController.API;

/**
 * @Description: 理货报告
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Api(tags = "海关业务-理货报告-表头", value = "铁路理货报告")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BRailwayTallyReportHeadController {

    static final String API = "/BRailwayTallyReportHead";

    @Autowired
    private BRailwayTallyReportHeadService service;

    /**
     * @return PageInfo<BRailwayTallyReportHead>
     * @explain 分页条件查询铁路进口理货申请报文表头
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "铁路理货报告->分页查询")
    public JsonResult index(@Valid BRailwayTallyReportQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return JsonResult.data(Params.param(M_PAGE, service.selectReportListPage(query, pageNum, pageSize))
                .set("query", Optional.ofNullable(query).orElseGet(BRailwayTallyReportQuery::new))
                // 申报状态
                .set("declareStatusFlag", DeclareStatusFlag.select())
                // 运单状态
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                // 进出口
                .set("ieFlag", IEFlag.values()));
    }

    /**
     * Description: 查看明细页
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月12日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("铁路理货报告->查看明细页")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.queryDetailById(id))// 申报状态
                .set("declareStatusFlag", DeclareStatusFlag.select())
                // 运单状态
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                // 进出口
                .set("ieFlag", IEFlag.values()));
    }

    /**
     * Description: 理货作废中，根据报文编号查询得到理货申请信息
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月12日
     **/
    @AddLog
    @GetMapping(value = "/detail" + DECL_MESSAGE_ID)
    @ApiOperation("铁路理货作废->根据理货申请编号得到理货申请信息")
    public JsonResult detailMessage(@PathVariable String declMessageId) {
        return JsonResult.data(Params.param("detailMsg", service.queryByMsgId(declMessageId))
                .set("messageType", MessageTypeFlag.values())
                .set("declareStatusFlag", DeclareStatusFlag.select())// 运单状态
                .set("wayBillAuditStatus", WayBillAuditStatus.values())
                // 进出口
                .set("ieFlag", IEFlag.values()));
    }

    /**
     * @return int
     * @explain 理货报告->保存
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "铁路理货报告->添加")
    public JsonResult save(@Valid @RequestBody BRailwayTallyReportHeadSave headSave) {
        return service.saveReportInfo(headSave);
    }

    /**
     * @return int
     * @explain 理货报告->删除
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "铁路理货报告->删除", notes = "理货报告->删除")
    public JsonResult deleteSingle(@PathVariable String id) {
        return service.removeReportById(id);
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "铁路理货报告->新增，返回空报文")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BRailwayTallyReportHeadSave())
                // 封志号类型
                .set("sealTypeFlag", SealTypeFlag.values())
                // 施加封志人类型
                .set("enstaveMentFlag", EnstaveMentFlag.values())
                // 进出口类型
                .set("ieFlag", IEFlag.values())
                // 申报状态：待申报、申报海关、审核通过、审核不通过
                .set("declareStatusFlag", DeclareStatusFlag.select())
                // 申报状态：空值/退单/理货审核通过/作废中/作废通过
                .set("wayBillAuditStatus", WayBillAuditStatus.values()));
    }

    @AddLog
    @PostMapping(value = DECLARE + ID)
    @ApiOperation(value = "铁路理货报告->申报")
    public JsonResult declare(@PathVariable String id) {
        return service.declare(id);
    }

    /**
     * 筛选审核通过的理货报文编号
     *
     * @return
     */
    @AddLog
    @GetMapping(value = AUDIT_PASS_SELECT)
    @ApiOperation(value = "铁路理货报告->筛选审核通过的理货报文编号")
    public JsonResult auditPassSelect() {
        QueryWrapper<BRailwayTallyReportHead> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BRailwayTallyReportHead::getAuditStatus, DeclareStatusFlag.DECLARE_PASS.getValue());
        List<BRailwayTallyReportHead> list = service.list(wrapper);

        return JsonResult.data(list);
    }

    /**
     * @Author zhangpeng
     * @Date 2019/8/29
     * @Description 编辑-保存
     **/
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "铁路理货报告->编辑保存")
    public JsonResult update(@PathVariable String id, @RequestBody TallyReportVo vo) throws Throwable {
        service.update(id, vo);
        return JsonResult.success();
    }
}