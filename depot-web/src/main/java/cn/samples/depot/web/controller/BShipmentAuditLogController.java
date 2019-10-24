/**
 * @filename:BShipmentAuditLogController 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.entity.BShipmentAuditLog;
import cn.samples.depot.web.service.BShipmentAuditLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.BShipmentAuditLogController.API;

/**
 * @Description: 审核日志表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Api(tags = "发运计划-发运计划申请-审核日志", value = "审核日志表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class BShipmentAuditLogController {

    static final String API = "/BShipmentAuditLog";

    @Autowired
    private BShipmentAuditLogService service;

    /**
     * @return PageInfo<BShipmentAuditLog>
     * @explain 分页条件查询审核日志表
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @GetMapping
    // @Cacheable(value = "bShipmentAuditLog", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BShipmentAuditLog>],作者：ZhangPeng")
    public JsonResult index(@Valid BShipmentAuditLog bShipmentAuditLog,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BShipmentAuditLog> wrapper = new QueryWrapper<>();
        if (null != bShipmentAuditLog.getShipmentPlanId()) {
            wrapper.lambda().eq(BShipmentAuditLog::getShipmentPlanId, bShipmentAuditLog.getShipmentPlanId());
        }

        // 查询第1页，每页返回10条
        Page<BShipmentAuditLog> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bShipmentAuditLog).orElseGet(BShipmentAuditLog::new)));
    }

    /**
     * Description: 查看指定审核日志表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月24日
     **/
    @AddLog
    // @Cacheable(value = "bShipmentAuditLog", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看指定审核日志表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存审核日志表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "bShipmentAuditLog", key = "#bShipmentAuditLog.id")
    @ApiOperation(value = "保存审核日志表", notes = "保存审核日志表[bShipmentAuditLog],作者：ZhangPeng")
    public JsonResult save(@RequestBody BShipmentAuditLog bShipmentAuditLog) {
        return JsonResult.data(service.save(bShipmentAuditLog));
    }

    /**
     * @return int
     * @explain 单个删除审核日志表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "bShipmentAuditLog", key = "#id")
    @ApiOperation(value = "单个删除审核日志表", notes = "单个删除审核日志表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除审核日志表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "bShipmentAuditLog", allEntries = true)
    @ApiOperation(value = "批量删除审核日志表", notes = "批量删除审核日志表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return bShipmentAuditLog
     * @explain 更新审核日志表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新审核日志表", notes = "更新审核日志表[bShipmentAuditLog],作者：ZhangPeng")
    // @CachePut(value = "bShipmentAuditLog", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody BShipmentAuditLog bShipmentAuditLog) {
        return JsonResult.data(service.update(bShipmentAuditLog, Wrappers.<BShipmentAuditLog>lambdaQuery().eq(BShipmentAuditLog::getId, id)));
    }

}