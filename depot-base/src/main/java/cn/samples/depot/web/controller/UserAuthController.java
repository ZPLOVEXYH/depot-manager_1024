package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.dto.user.LoginRequest;
import cn.samples.depot.web.entity.CRoles;
import cn.samples.depot.web.entity.CUsers;
import cn.samples.depot.web.oauth.jwt.JwtTokenFactory;
import cn.samples.depot.web.oauth.jwt.UserContext;
import cn.samples.depot.web.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static cn.samples.depot.common.utils.Controllers.URL_PRE_COMMON;

/**
 * Description: 用户登录和登出模块
 *
 * @className: UserAuthEndpoint
 * @Author: ChenJie
 * @Date 2019/8/29
 * @Version 1.0
 **/
@RestController
@Api(tags = "系统管理-用户授权", value = "用户登录和登出")
@RequestMapping(value = URL_PRE_COMMON + UserAuthController.API)
@SuppressWarnings(value = {"rawtypes"})
public class UserAuthController {
    static final String API = "/auth";
    @Autowired
    private JwtTokenFactory tokenFactory;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;

    @AddLog
    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录获取Token")
    @ResponseBody
    @CachePut(value = "userLogin", key = "#request.username", condition = "#request.username!=null", unless = "#result.data==null")
    public JsonResult login(@RequestBody LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null)
            return JsonResult.error("参数错误");
        CUsers user = userService.getUser(request.getUsername());
        if (user == null)
            return JsonResult.error("此用户不存在");
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            return JsonResult.error("密码错误");
        }
        CRoles role = userService.getRole(request.getUsername());
        if (role == null)
            return JsonResult.error("此用户未设置任何角色无法登录,请联系管理员修改");
        String accessToken = tokenFactory.createAccessJwtToken(UserContext.create(user, role));
        return JsonResult.data(Params.param("token", accessToken));
    }

    @AddLog
    @PostMapping(value = "/logout")
    @ResponseBody
    @ApiOperation(value = "用户登出注销token")
    @CacheEvict(value = "userLogin", key = "#request.username", condition = "#request.username!=null")
    public JsonResult logout(@RequestBody LoginRequest request) {
        return JsonResult.success();
    }

    @AddLog
    @GetMapping(value = "/me")
    @ResponseBody
    @ApiOperation(value = "获取当前用户基础信息")
    @JsonView({CUsers.View.ME.class})
    public JsonResult me() {
        UserContext userContext = userService.userContext();
        return JsonResult.data(userContext);
    }
}
