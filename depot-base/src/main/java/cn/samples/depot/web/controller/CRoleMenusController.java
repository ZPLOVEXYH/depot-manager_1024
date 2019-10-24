/**
 * @filename:CRoleMenusController 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.entity.CRoleMenus;
import cn.samples.depot.web.service.CRoleMenusService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static cn.samples.depot.common.utils.Controllers.*;

/**
 * @Description: 角色菜单表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Api(tags = "系统管理-角色菜单管理", value = "角色菜单表")
@RestController
@RequestMapping(value = URL_PRE_COMMON + CRoleMenusController.API)
@SuppressWarnings(value = {"rawtypes", "unchecked"})
public class CRoleMenusController {

    static final String API = "/CRoleMenus";

    @Autowired
    private CRoleMenusService service;

    /**
     * @return PageInfo<CRoleMenus>
     * @explain 分页条件查询角色菜单表
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<CRoleMenus>],作者：ZhangPeng")
    public JsonResult index(@Valid CRoleMenus cRoleMenus,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        LambdaQueryWrapper<CRoleMenus> wrapper = Wrappers.<CRoleMenus>lambdaQuery()
                .eq(cRoleMenus.getRoleCode() != null, CRoleMenus::getRoleCode, cRoleMenus.getRoleCode())
                .eq(cRoleMenus.getMenuCode() != null, CRoleMenus::getMenuCode, cRoleMenus.getMenuCode());

        return JsonResult.data(Params.param(M_PAGE, service.page(new Page(pageNum, pageSize), wrapper)));
    }

    @AddLog
    @GetMapping(QUERY)
    @ApiOperation(value = "根据角色查询其所有的菜单code", notes = "根据角色查询其所有的菜单code[List<CRoleMenus>],作者：ChenJie")
    public JsonResult list(String roleCode) {
        return JsonResult.data(Params.param(M_ITEMS, service.getRoleMenuList(roleCode)));
    }

    /**
     * Description: 查看指定角色菜单表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月20日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看指定角色菜单表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存角色菜单表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存角色菜单表", notes = "保存角色菜单表[cRoleMenus],作者：ZhangPeng")
    public JsonResult save(@RequestBody List<CRoleMenus> cRoleMenus) {
        return JsonResult.data(service.saveRoleMenuList(cRoleMenus));
    }

    /**
     * @return int
     * @explain 单个删除角色菜单表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除角色菜单表", notes = "单个删除角色菜单表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除角色菜单表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping
    @ApiOperation(value = "批量删除角色菜单表", notes = "批量删除角色菜单表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return cRoleMenus
     * @explain 更新角色菜单表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新角色菜单表", notes = "更新角色菜单表[cRoleMenus],作者：ZhangPeng")
    public JsonResult update(@PathVariable String id, @RequestBody CRoleMenus cRoleMenus) {
        return JsonResult.data(service.update(cRoleMenus, Wrappers.<CRoleMenus>lambdaQuery().eq(CRoleMenus::getId, id)));
    }
}