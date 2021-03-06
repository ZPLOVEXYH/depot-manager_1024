/**
 * @filename:BRailwayTallyBillInfoController 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.cz.service.BRailwayTallyBillInfoService;
import cn.samples.depot.web.entity.BRailwayTallyBillInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BRailwayTallyBillInfoController.API;

/**
 * @Description: 铁路进口理货申请报文表体接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Api(tags = "海关业务-理货报告-表体", value = "铁路进口理货申请报文表体")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BRailwayTallyBillInfoController {

    static final String API = "/BRailwayTallyBillInfo";

    @Autowired
    private BRailwayTallyBillInfoService service;

    /**
     * @return PageInfo<BRailwayTallyBillInfo>
     * @explain 分页条件查询铁路进口理货申请报文表体
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @GetMapping
    // @Cacheable(value = "bRailwayTallyBillInfo", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BRailwayTallyBillInfo>],作者：ZhangPeng")
    public JsonResult index(@Valid BRailwayTallyBillInfo bRailwayTallyBillInfo,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BRailwayTallyBillInfo> wrapper = new QueryWrapper<>();
        // wrapper.lambda()
        // .eq((bShipmentPlanQuery.getEnterprisesId() != null && Strings.EMPTY != bShipmentPlanQuery.getEnterprisesId()), BShipmentPlan::getEnterprisesId, bShipmentPlanQuery.getEnterprisesId());// 发运计划编号


        // 查询第1页，每页返回10条
        Page<BRailwayTallyBillInfo> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bRailwayTallyBillInfo).orElseGet(BRailwayTallyBillInfo::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * Description: 查看指定铁路进口理货申请报文表体数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月12日
     **/
    @AddLog
    // @Cacheable(value = "bRailwayTallyBillInfo", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看指定铁路进口理货申请报文表体数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存铁路进口理货申请报文表体对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "bRailwayTallyBillInfo", key = "#bRailwayTallyBillInfo.id")
    @ApiOperation(value = "保存铁路进口理货申请报文表体", notes = "保存铁路进口理货申请报文表体[bRailwayTallyBillInfo],作者：ZhangPeng")
    public JsonResult save(@RequestBody BRailwayTallyBillInfo bRailwayTallyBillInfo) {
        return JsonResult.data(service.save(bRailwayTallyBillInfo));
    }

    /**
     * @return int
     * @explain 单个删除铁路进口理货申请报文表体对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "bRailwayTallyBillInfo", key = "#id")
    @ApiOperation(value = "单个删除铁路进口理货申请报文表体", notes = "单个删除铁路进口理货申请报文表体,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除铁路进口理货申请报文表体对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "bRailwayTallyBillInfo", allEntries = true)
    @ApiOperation(value = "批量删除铁路进口理货申请报文表体", notes = "批量删除铁路进口理货申请报文表体,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return bRailwayTallyBillInfo
     * @explain 更新铁路进口理货申请报文表体对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新铁路进口理货申请报文表体", notes = "更新铁路进口理货申请报文表体[bRailwayTallyBillInfo],作者：ZhangPeng")
    // @CachePut(value = "bRailwayTallyBillInfo", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody BRailwayTallyBillInfo bRailwayTallyBillInfo) {
        return JsonResult.data(service.update(bRailwayTallyBillInfo, Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getId, id)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增铁路进口理货申请报文表体", notes = "新增铁路进口理货申请报文表体[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BRailwayTallyBillInfo()));
    }

}