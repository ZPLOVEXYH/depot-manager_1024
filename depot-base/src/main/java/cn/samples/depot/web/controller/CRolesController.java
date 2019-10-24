/**
 * @filename:CRolesController 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.entity.CRoles;
import cn.samples.depot.web.service.CRolesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;

/**
 * @Description: 角色表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Api(tags = "系统管理-角色管理", value = "角色表")
@RestController
@RequestMapping(value = URL_PRE_COMMON + CRolesController.API)
@SuppressWarnings(value = {"rawtypes", "unchecked"})
public class CRolesController {

    static final String API = "/CRoles";

    @Autowired
    private CRolesService service;

    /**
     * @return PageInfo<CRoles>
     * @explain 分页条件查询角色表
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<CRoles>],作者：ChenJie")
    public JsonResult index(@Valid CRoles cRoles,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CRoles> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(cRoles.getCode() != null && StringUtils.isNotEmpty(cRoles.getCode()), CRoles::getCode, cRoles.getCode())
                .like(cRoles.getName() != null && StringUtils.isNotEmpty(cRoles.getName()), CRoles::getName, cRoles.getName())
                .eq(cRoles.getEnable() != null, CRoles::getEnable, cRoles.getEnable());

        return JsonResult.data(Params.param(M_PAGE, service.page(new Page(pageNum, pageSize), wrapper))
                .set("query", Optional.ofNullable(cRoles).orElseGet(CRoles::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    @GetMapping(value = SELECT)
    @ApiOperation("角色下拉列表")
    @JsonView(CRoles.View.SELECT.class)
    public JsonResult select() {
        return JsonResult.data(service.list(Wrappers.<CRoles>lambdaQuery().eq(CRoles::getEnable, Status.ENABLED.getValue())));
    }

    /**
     * Description: 查看指定角色表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月20日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看指定角色表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存角色表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "cRoles", key = "#cRoles.id")
    @ApiOperation(value = "保存角色表", notes = "保存角色表[cRoles],作者：ZhangPeng")
    public JsonResult save(@RequestBody CRoles cRoles) {
        return service.saveRole(cRoles) == 0 ? JsonResult.success() : JsonResult.error("该角色已存在");
    }

    /**
     * @return int
     * @explain 单个删除角色表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "cRoles", key = "#id")
    @ApiOperation(value = "单个删除角色表", notes = "单个删除角色表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除角色表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "cRoles", allEntries = true)
    @ApiOperation(value = "批量删除角色表", notes = "批量删除角色表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return cRoles
     * @explain 更新角色表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新角色表", notes = "更新角色表[cRoles],作者：ZhangPeng")
    // @CachePut(value = "cRoles", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody CRoles cRoles) {
        return JsonResult.data(service.update(cRoles, Wrappers.<CRoles>lambdaQuery().eq(CRoles::getId, id)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增角色表", notes = "新增角色表[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new CRoles()));
    }

}