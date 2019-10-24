/**
 * @filename:CMenusController 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.dto.menu.Menu;
import cn.samples.depot.web.entity.CMenus;
import cn.samples.depot.web.service.CMenusService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.samples.depot.common.utils.Controllers.*;

/**
 * @Description: 菜单表接口层
 * @Author: ZhangPeng
 * @reateDate: 2019年08月20日
 * @Version: V1.0
 */
@Api(tags = "系统管理-菜单管理", value = "菜单表")
@RestController
@RequestMapping(value = URL_PRE_COMMON + CMenusController.API)
@SuppressWarnings(value = {"rawtypes", "unchecked"})
public class CMenusController {

    static final String API = "/CMenus";

    @Autowired
    private CMenusService service;

    /**
     * @return PageInfo<CMenus>
     * @explain 分页条件查询菜单表
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<CMenus>],作者：ZhangPeng")
    public JsonResult index(@Valid CMenus cMenus,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<CMenus> wrapper = Wrappers.<CMenus>lambdaQuery()
                .eq(cMenus.getEnable() != null, CMenus::getEnable, cMenus.getEnable())
                .eq(cMenus.getCode() != null, CMenus::getCode, cMenus.getCode())
                .eq(cMenus.getLevel() != null, CMenus::getLevel, cMenus.getLevel())
                .eq(cMenus.getParentCode() != null, CMenus::getParentCode, cMenus.getParentCode())
                .eq(cMenus.getUrl() != null, CMenus::getUrl, cMenus.getUrl())
                .eq(cMenus.getName() != null, CMenus::getName, cMenus.getName());

        return JsonResult.data(Params.param(M_PAGE, service.page(new Page(pageNum, pageSize), wrapper))
                .set("query", Optional.ofNullable(cMenus).orElseGet(CMenus::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * Description: 查看指定菜单表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月20日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看指定菜单表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * Description: 获取菜单下拉列表
     *
     * @return: JsonResult
     * @author ChenJie
     * @time 2019年08月27日
     **/
    @AddLog
    @GetMapping(value = SELECT)
    @ApiOperation("获取菜单下拉列表")
    public JsonResult select(Integer level) {
        if (level == null)
            return JsonResult.data(null);
        List<CMenus> cMenusList = service.list(Wrappers.<CMenus>lambdaQuery().eq(CMenus::getLevel, level));
        List<Menu> list = cMenusList.stream().sorted(Comparator.comparing(CMenus::getSort)).map(f -> Menu.builder().code(f.getCode()).name(f.getName()).build()).collect(Collectors.toList());
        return JsonResult.data(list);
    }

    /**
     * @return int
     * @explain 保存菜单表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存菜单表", notes = "保存菜单表[cMenus],作者：ZhangPeng")
    public JsonResult save(@RequestBody CMenus cMenus) {
        return service.saveMenu(cMenus) == 0 ? JsonResult.success() : JsonResult.error("该菜单编码已存在");
    }

    /**
     * @return int
     * @explain 单个删除菜单表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "cMenus", key = "#id")
    @ApiOperation(value = "单个删除菜单表", notes = "单个删除菜单表,作者：ChenJie")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.deleteMenu(id));
    }

    /**
     * @return int
     * @explain 批量删除菜单表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping
    @ApiOperation(value = "批量删除菜单表", notes = "批量删除菜单表,作者：ChenJie")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        Arrays.asList(ids).forEach(m -> service.deleteMenu((m)));
        return JsonResult.success();
    }

    /**
     * 更新菜单表对象
     *
     * @return cMenus
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新菜单表", notes = "更新菜单表[cMenus],作者：ZhangPeng")
    public JsonResult update(@PathVariable String id, @RequestBody CMenus cMenus) {
        return JsonResult.data(service.update(cMenus, Wrappers.<CMenus>lambdaQuery().eq(CMenus::getId, id)));
    }

    @GetMapping(value = ALL)
    @ApiOperation(value = "查询所有数据库中所有菜单(有层级关系的菜单列表)", notes = "查询所有数据库中所有菜单(有层级关系的菜单),作者：ChenJie")
    public JsonResult getAllList() {
        return JsonResult.data(Params.param(M_ITEMS, service.getAllMenuList())
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    @GetMapping(value = CURRENT)
    @ApiOperation(value = "获取当前登录用户可用菜单列表(层级关系)", notes = "获取当前登录用户可用菜单列表[cMenus],作者：ChenJie")
    public JsonResult currentList() {
        return JsonResult.data(Params.param(M_ITEMS, service.getCurrentMenuList()));
    }

}