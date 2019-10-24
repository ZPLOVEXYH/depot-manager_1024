/**
 * @filename:BRelFormInfoController 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.cz.service.BRelFormInfoService;
import cn.samples.depot.web.entity.BRelFormInfo;
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
import static cn.samples.depot.web.cz.controller.BRelFormInfoController.API;

/**
 * @Description: 放行指令表体单证信息接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Api(tags = "海关业务-海关指令-放行指令-放行单证", value = "放行指令表体单证信息")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BRelFormInfoController {

    static final String API = "/BRelFormInfo";

    @Autowired
    private BRelFormInfoService service;

    /**
     * @return PageInfo<BRelFormInfo>
     * @explain 分页条件查询放行指令表体单证信息
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @GetMapping
    // @Cacheable(value = "bRelFormInfo", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BRelFormInfo>],作者：ZhangPeng")
    public JsonResult index(@Valid BRelFormInfo bRelFormInfo,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BRelFormInfo> wrapper = new QueryWrapper<>();
        // wrapper.lambda()
        // .eq((bShipmentPlanQuery.getEnterprisesId() != null && Strings.EMPTY != bShipmentPlanQuery.getEnterprisesId()), BShipmentPlan::getEnterprisesId, bShipmentPlanQuery.getEnterprisesId());// 发运计划编号


        // 查询第1页，每页返回10条
        Page<BRelFormInfo> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bRelFormInfo).orElseGet(BRelFormInfo::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * Description: 查看指定放行指令表体单证信息数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月12日
     **/
    @AddLog
    // @Cacheable(value = "bRelFormInfo", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看指定放行指令表体单证信息数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存放行指令表体单证信息对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "bRelFormInfo", key = "#bRelFormInfo.id")
    @ApiOperation(value = "保存放行指令表体单证信息", notes = "保存放行指令表体单证信息[bRelFormInfo],作者：ZhangPeng")
    public JsonResult save(@RequestBody BRelFormInfo bRelFormInfo) {
        return JsonResult.data(service.save(bRelFormInfo));
    }

    /**
     * @return int
     * @explain 单个删除放行指令表体单证信息对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "bRelFormInfo", key = "#id")
    @ApiOperation(value = "单个删除放行指令表体单证信息", notes = "单个删除放行指令表体单证信息,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除放行指令表体单证信息对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "bRelFormInfo", allEntries = true)
    @ApiOperation(value = "批量删除放行指令表体单证信息", notes = "批量删除放行指令表体单证信息,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return bRelFormInfo
     * @explain 更新放行指令表体单证信息对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新放行指令表体单证信息", notes = "更新放行指令表体单证信息[bRelFormInfo],作者：ZhangPeng")
    // @CachePut(value = "bRelFormInfo", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody BRelFormInfo bRelFormInfo) {
        return JsonResult.data(service.update(bRelFormInfo, Wrappers.<BRelFormInfo>lambdaQuery().eq(BRelFormInfo::getId, id)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增放行指令表体单证信息", notes = "新增放行指令表体单证信息[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BRelFormInfo()));
    }

}