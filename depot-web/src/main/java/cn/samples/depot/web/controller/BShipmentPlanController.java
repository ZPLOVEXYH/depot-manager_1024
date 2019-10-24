/**
 * @filename:BShipmentPlanController 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.AuditStatus;
import cn.samples.depot.common.utils.ExpAndImpUtil;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.BShipmentPlanDTO;
import cn.samples.depot.web.bean.BShipmentPlanQuery;
import cn.samples.depot.web.dto.shipment.ImportExcelDto;
import cn.samples.depot.web.entity.BShipmentPlan;
import cn.samples.depot.web.service.BShipmentPlanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.BShipmentPlanController.API;

/**
 * @Description: 发运计划表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Api(tags = "发运计划-发运计划申请", value = "发运计划表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class BShipmentPlanController {

    static final String API = "/BShipmentPlan";

    @Autowired
    private BShipmentPlanService service;

    /**
     * 提交计划申请至场站平台
     *
     * @return
     */
    @AddLog
    @GetMapping("/senderForApply")
    @ApiOperation(value = "提交计划申请至场站平台", notes = "提交计划申请至场站平台[PageInfo<BShipmentPlan>],作者：ZhangPeng")
    public JsonResult senderForApply(@RequestParam("ids") String... ids) {
        return service.senderForApply(ids);
    }

    /**
     * @return PageInfo<BShipmentPlan>
     * @explain 分页条件查询发运计划表
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @GetMapping
    // @Cacheable(value = "bShipmentPlan", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BShipmentPlan>],作者：ZhangPeng")
    public JsonResult index(@Valid BShipmentPlanQuery bShipmentPlanQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BShipmentPlan> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq((bShipmentPlanQuery.getEnterprisesId() != null && Strings.EMPTY != bShipmentPlanQuery.getEnterprisesId()), BShipmentPlan::getEnterprisesId, bShipmentPlanQuery.getEnterprisesId()) // 发货企业id
                .eq((bShipmentPlanQuery.getAuditStatus() != null && Strings.EMPTY != bShipmentPlanQuery.getAuditStatus()), BShipmentPlan::getAuditStatus, bShipmentPlanQuery.getAuditStatus()) // 审核状态
                .ge((bShipmentPlanQuery.getStartShipmentTime() != null && bShipmentPlanQuery.getEndShipmentTime() == null), BShipmentPlan::getShipmentTime, bShipmentPlanQuery.getStartShipmentTime())
                .le((bShipmentPlanQuery.getStartShipmentTime() == null && bShipmentPlanQuery.getEndShipmentTime() != null), BShipmentPlan::getShipmentTime, bShipmentPlanQuery.getEndShipmentTime())
                .between((bShipmentPlanQuery.getStartShipmentTime() != null && bShipmentPlanQuery.getEndShipmentTime() != null), BShipmentPlan::getShipmentTime, bShipmentPlanQuery.getStartShipmentTime(), bShipmentPlanQuery.getEndShipmentTime()) // 开始出运时间，结束出运时间
                .like((bShipmentPlanQuery.getShipmentPlanNo() != null && Strings.EMPTY != bShipmentPlanQuery.getShipmentPlanNo()), BShipmentPlan::getShipmentPlanNo, bShipmentPlanQuery.getShipmentPlanNo());// 发运计划编号


        // 查询第1页，每页返回10条
        Page<BShipmentPlan> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bShipmentPlanQuery).orElseGet(BShipmentPlanQuery::new))
                .set("aduitStatus", AuditStatus.values()));// 返回审核状态
    }

    /**
     * Description: 查看指定发运计划表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月24日
     **/
    @AddLog
    // @Cacheable(value = "bShipmentPlan", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看指定发运计划表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存发运计划表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "bShipmentPlan", key = "#bShipmentPlan.id")
    @ApiOperation(value = "保存发运计划表", notes = "保存发运计划表[bShipmentPlan],作者：ZhangPeng")
    public JsonResult save(@RequestBody BShipmentPlanDTO bShipmentPlan) {
        return service.saveBShipmentPlan(bShipmentPlan);
    }

    /**
     * @return int
     * @explain 单个删除发运计划表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "bShipmentPlan", key = "#id")
    @ApiOperation(value = "单个删除发运计划表", notes = "单个删除发运计划表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return service.deleteBShipmentPlan(id);
    }

    /**
     * @return int
     * @explain 批量删除发运计划表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "bShipmentPlan", allEntries = true)
    @ApiOperation(value = "批量删除发运计划表", notes = "批量删除发运计划表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        Arrays.asList(ids).forEach(id -> service.deleteBShipmentPlan(id));
        return JsonResult.success();
    }

    /**
     * @return bShipmentPlan
     * @explain 更新发运计划表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新发运计划表", notes = "更新发运计划表[bShipmentPlan],作者：ZhangPeng")
    // @CachePut(value = "bShipmentPlan", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody BShipmentPlanDTO bShipmentPlanDTO) {
        return service.updateShipmentPlan(id, bShipmentPlanDTO);
    }

    /**
     * 导入excel
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/upload-excel/{stationsCode}")
    @ApiOperation(value = "导入excel", notes = "导入excel[bShipmentPlan],作者：ZhangPeng")
    public void uploadExcel(@PathVariable String stationsCode, @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        service.uploadExcel(stationsCode, file, response);
    }

    /**
     * 导出excel，提供企业导出excel模板
     */
    @GetMapping(value = EXPORT_TEMPLATE)
    @ApiOperation(value = "导出excel模板", notes = "导出excel模板[bShipmentPlan],作者：ZhangPeng")
    public void export(HttpServletResponse response) {
        //导出操作
        ExpAndImpUtil.exportExcel(new ArrayList<>(), null, "发运计划申请单", ImportExcelDto.class, "发运计划申请导入excel模板.xls", response);
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增发运计划单号数据", notes = "新增发运计划单号数据[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BShipmentPlan())
                .set("aduitStatus", AuditStatus.values()));
    }
}