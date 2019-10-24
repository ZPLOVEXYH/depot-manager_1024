/**
 * @filename:BRelShipInfoController 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.cz.service.BRelShipInfoService;
import cn.samples.depot.web.entity.BRelShipInfo;
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
import static cn.samples.depot.web.cz.controller.BRelShipInfoController.API;

/**
 * @Description: 放行指令表体运输工具信息接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Api(tags = "海关业务-海关指令-放行指令-运输工具", value = "放行指令表体运输工具信息")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BRelShipInfoController {

    static final String API = "/BRelShipInfo";

    @Autowired
    private BRelShipInfoService service;

    /**
     * @return PageInfo<BRelShipInfo>
     * @explain 分页条件查询放行指令表体运输工具信息
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @GetMapping
    // @Cacheable(value = "bRelShipInfo", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BRelShipInfo>],作者：ZhangPeng")
    public JsonResult index(@Valid BRelShipInfo bRelShipInfo,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BRelShipInfo> wrapper = new QueryWrapper<>();
        // wrapper.lambda()
        // .eq((bShipmentPlanQuery.getEnterprisesId() != null && Strings.EMPTY != bShipmentPlanQuery.getEnterprisesId()), BShipmentPlan::getEnterprisesId, bShipmentPlanQuery.getEnterprisesId());// 发运计划编号


        // 查询第1页，每页返回10条
        Page<BRelShipInfo> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bRelShipInfo).orElseGet(BRelShipInfo::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * Description: 查看指定放行指令表体运输工具信息数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月12日
     **/
    @AddLog
    // @Cacheable(value = "bRelShipInfo", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看指定放行指令表体运输工具信息数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存放行指令表体运输工具信息对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "bRelShipInfo", key = "#bRelShipInfo.id")
    @ApiOperation(value = "保存放行指令表体运输工具信息", notes = "保存放行指令表体运输工具信息[bRelShipInfo],作者：ZhangPeng")
    public JsonResult save(@RequestBody BRelShipInfo bRelShipInfo) {
        return JsonResult.data(service.save(bRelShipInfo));
    }

    /**
     * @return int
     * @explain 单个删除放行指令表体运输工具信息对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "bRelShipInfo", key = "#id")
    @ApiOperation(value = "单个删除放行指令表体运输工具信息", notes = "单个删除放行指令表体运输工具信息,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除放行指令表体运输工具信息对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "bRelShipInfo", allEntries = true)
    @ApiOperation(value = "批量删除放行指令表体运输工具信息", notes = "批量删除放行指令表体运输工具信息,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return bRelShipInfo
     * @explain 更新放行指令表体运输工具信息对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新放行指令表体运输工具信息", notes = "更新放行指令表体运输工具信息[bRelShipInfo],作者：ZhangPeng")
    // @CachePut(value = "bRelShipInfo", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody BRelShipInfo bRelShipInfo) {
        return JsonResult.data(service.update(bRelShipInfo, Wrappers.<BRelShipInfo>lambdaQuery().eq(BRelShipInfo::getId, id)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增放行指令表体运输工具信息", notes = "新增放行指令表体运输工具信息[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BRelShipInfo()));
    }

}