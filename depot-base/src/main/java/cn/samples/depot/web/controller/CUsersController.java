/**
 * @filename:CUsersController 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.dto.user.ResetPasswordDTO;
import cn.samples.depot.web.dto.user.UpdatePasswordDTO;
import cn.samples.depot.web.entity.CEnterprises;
import cn.samples.depot.web.entity.CUsers;
import cn.samples.depot.web.service.CEnterprisesService;
import cn.samples.depot.web.service.CUsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;

/**
 * @Description: 用户信息表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Api(tags = "系统管理-用户管理", value = "用户信息表")
@RestController
@RequestMapping(value = URL_PRE_COMMON + CUsersController.API)
@SuppressWarnings(value = {"rawtypes", "unchecked"})
public class CUsersController {

    static final String API = "/CUsers";

    @Autowired
    private CUsersService service;

    @Autowired
    private CEnterprisesService enterprisesService;

    /**
     * @return PageInfo<CUsers>
     * @explain 分页条件查询用户信息表
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<CUsers>],作者：ZhangPeng")
    public JsonResult index(@Valid CUsers cUsers,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CUsers> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(cUsers.getUserName() != null && StringUtils.isNotEmpty(cUsers.getUserName()), CUsers::getUserName, cUsers.getUserName())
                .like(cUsers.getContactPhone() != null && StringUtils.isNotEmpty(cUsers.getContactPhone()), CUsers::getContactPhone, cUsers.getContactPhone())
                .eq(cUsers.getEnable() != null, CUsers::getEnable, cUsers.getEnable())
                .like(cUsers.getRealName() != null && StringUtils.isNotEmpty(cUsers.getRealName()), CUsers::getRealName, cUsers.getRealName())
                .eq(cUsers.getRoleCode() != null && StringUtils.isNotEmpty(cUsers.getRoleCode()), CUsers::getRoleCode, cUsers.getRoleCode())
                .eq(cUsers.getEnterpriseCode() != null && StringUtils.isNotEmpty(cUsers.getEnterpriseCode()), CUsers::getEnterpriseCode, cUsers.getEnterpriseCode())
                .orderByDesc(CUsers::getCreateTime);


        IPage<CUsers> iPage = service.page(new Page(pageNum, pageSize), wrapper);
        if (null != iPage) {
            List<CUsers> cUsersList = iPage.getRecords();
            if (CollectionUtil.isNotEmpty(cUsersList)) {
                cUsersList.forEach(user -> {
                    String enterpriseCode = user.getEnterpriseCode();
                    CEnterprises cEnterprises = enterprisesService.getOne(Wrappers.<CEnterprises>lambdaQuery().eq(CEnterprises::getCode, enterpriseCode).eq(CEnterprises::getEnable, Status.ENABLED.getValue()));
                    if (null != cEnterprises) {
                        user.setEnterpriseName(cEnterprises.getName());
                    }
                });
            }
        }

        return JsonResult.data(Params.param(M_PAGE, iPage)
                .set("query", Optional.ofNullable(cUsers).orElseGet(CUsers::new))
                .set("status", Lists.newArrayList(Status.values())));
    }

    /**
     * Description: 查看指定用户信息表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月20日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看指定用户信息表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存用户信息表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存用户信息表", notes = "保存用户信息表[cUsers],作者：ZhangPeng")
    public JsonResult save(@Valid @RequestBody CUsers cUsers) {
        int result = service.saveUser(cUsers);
        return result == 0 ? JsonResult.success() : JsonResult.error("该用户已被注册");
    }

    /**
     * @return int
     * @explain 单个删除用户信息表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除用户信息表", notes = "单个删除用户信息表,作者：ChenJie")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.deleteUser(id));
    }

    /**
     * @explain 批量删除用户信息表对象
     * @author ChenJie
     * @time 2019年08月20日
     */
    @AddLog
    @DeleteMapping
    @ApiOperation(value = "批量删除用户信息表", notes = "批量删除用户信息表,作者：ChenJie")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        Arrays.asList(ids).forEach(id -> service.deleteUser(id));
        return JsonResult.success();
    }

    /**
     * @return cUsers
     * @explain 更新用户信息表对象
     * @author ZhangPeng
     * @time 2019年08月20日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新用户信息表", notes = "更新用户信息表[cUsers],作者：ZhangPeng")
    public JsonResult update(@PathVariable String id, @RequestBody CUsers cUsers) {
        return JsonResult.data(service.updateUser(id, cUsers));
    }

    /**
     * @explain 重置用户密码
     * @author ChenJie
     * @time 2019年08月26日
     */
    @AddLog
    @PostMapping("/resetPassword")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码[cUsers],作者：ChenJie")
    public JsonResult resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return JsonResult.data(service.resetPassword(resetPasswordDTO));
    }

    @AddLog
    @PostMapping("/updatePassword")
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码[cUsers],作者：ChenJie")
    public JsonResult updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return service.updatePassword(updatePasswordDTO);
    }
}