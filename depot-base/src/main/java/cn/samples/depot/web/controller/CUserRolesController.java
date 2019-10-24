/**
 * @filename:CUserRolesController 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.entity.CUserRoles;
import cn.samples.depot.web.service.CUserRolesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @Description: 用户角色关系表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Api(tags = "系统管理-用户角色关系", value = "用户角色关系表")
@RestController
@RequestMapping(value = URL_PRE_COMMON + CUserRolesController.API)
@SuppressWarnings(value = {"rawtypes", "unchecked"})
public class CUserRolesController {

    static final String API = "/CUserRoles";

    @Autowired
    private CUserRolesService service;

    /**
     * @return PageInfo<CUserRoles>
     * @explain 分页条件查询用户角色关系表
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<CUserRoles>],作者：ZhangPeng")
    public JsonResult index(@Valid CUserRoles cUserRoles,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CUserRoles> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(cUserRoles.getUserName() != null, CUserRoles::getUserName, cUserRoles.getUserName())
                .eq(cUserRoles.getRoleCode() != null, CUserRoles::getRoleCode, cUserRoles.getRoleCode());

        return JsonResult.data(Params.param(M_PAGE, service.page(new Page(pageNum, pageSize), wrapper)));
    }

    /**
     * Description: 查看角色下所有的用户
     *
     * @return: JsonResult
     * @author ChenJie
     * @time 2019年08月26日
     **/
    @AddLog
    @GetMapping(value = CODE)
    @ApiOperation("查看角色下所有的用户")
    public JsonResult userList(@PathVariable String code) {
        return JsonResult.data(Params.param(M_ITEMS, service.getUserList(code)));
    }

    /**
     * @return int
     * @explain 保存用户角色关系表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "cUserRoles", key = "#cUserRoles.id")
    @ApiOperation(value = "保存用户角色关系表", notes = "保存用户角色关系表[cUserRoles],作者：ZhangPeng")
    public JsonResult save(@RequestBody List<CUserRoles> cUserRoles) {
        return JsonResult.data(service.saveUserRole(cUserRoles));
    }

    /**
     * @return int
     * @explain 单个删除用户角色关系表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "cUserRoles", key = "#id")
    @ApiOperation(value = "单个删除用户角色关系表", notes = "单个删除用户角色关系表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除用户角色关系表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "cUserRoles", allEntries = true)
    @ApiOperation(value = "批量删除用户角色关系表", notes = "批量删除用户角色关系表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return cUserRoles
     * @explain 更新用户角色关系表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新用户角色关系表", notes = "更新用户角色关系表[cUserRoles],作者：ZhangPeng")
    // @CachePut(value = "cUserRoles", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody CUserRoles cUserRoles) {
        return JsonResult.data(service.update(cUserRoles, Wrappers.<CUserRoles>lambdaQuery().eq(CUserRoles::getId, id)));
    }

}