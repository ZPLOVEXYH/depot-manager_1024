package cn.samples.depot.web.service.impl;

import cn.samples.depot.common.config.RootConfig;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.web.entity.CRoles;
import cn.samples.depot.web.entity.CUserRoles;
import cn.samples.depot.web.entity.CUsers;
import cn.samples.depot.web.oauth.jwt.UserContext;
import cn.samples.depot.web.service.CRolesService;
import cn.samples.depot.web.service.CUserRolesService;
import cn.samples.depot.web.service.CUsersService;
import cn.samples.depot.web.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @className: UserServiceImpl
 * @Author: zhangpeng
 * @Date 2019/7/16 14:40
 * @Version 1.0
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    CUsersService cUsersService;
    @Autowired
    CUserRolesService cUserRolesService;
    @Autowired
    CRolesService cRolesService;
    @Autowired
    RootConfig rootConfig;

    @Override
    public CUsers getUser(String userName) {
        if ("root".equals(userName))
            return createRootUser();
        CUserRoles userRole = cUserRolesService.getOne(Wrappers.<CUserRoles>lambdaQuery()
                .eq(CUserRoles::getUserName, userName));
        if (userRole == null)
            return null;
        return cUsersService.getOne(Wrappers.<CUsers>lambdaQuery()
                .eq(CUsers::getUserName, userRole.getUserName())
                .eq(CUsers::getEnable, Status.ENABLED.getValue()));
    }

    @Override
    public CRoles getRole(String userName) {
        if ("root".equals(userName))
            return createRootRole();
        CUserRoles userRole = cUserRolesService.getOne(Wrappers.<CUserRoles>lambdaQuery().eq(CUserRoles::getUserName, userName));
        if (userRole == null)
            return null;
        return cRolesService.getOne(Wrappers.<CRoles>lambdaQuery()
                .eq(CRoles::getCode, userRole.getRoleCode())
                .eq(CRoles::getEnable, Status.ENABLED.getValue()));
    }

    @Override
    public String currentUserName() {
        UserContext userContext = userContext();
        if (userContext == null)
            return null;
        return userContext.getUser().getUserName();
    }

    @Override
    public UserContext userContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return null;
        try {
            return (UserContext) authentication.getPrincipal();
        } catch (Throwable ex) {
            log.error("UserContext转换异常", ex);
            return null;
        }
    }

    @Override
    public String currentRoleCode() {
        UserContext userContext = userContext();
        if (userContext == null)
            return null;
        return userContext.getRole().getCode();
    }

    /**
     * 创建超管用户
     **/
    private CUsers createRootUser() {
        if (rootConfig != null && rootConfig.isDisabled())
            return null;
        return CUsers.builder()
                .userName("root")
                .realName("超级管理员")
                .password(rootConfig.getPassword() != null && !rootConfig.getPassword().isEmpty() ? rootConfig.getPassword() : "$2a$10$1fp0fHLnq26Bajp6d0lK6ebPU9Hn57Du81KlRFORtF.MjOsva9w/m")
                .roleCode("root")
                .roleName("超级管理员")
                .build();

    }

    /**
     * 创建超管角色
     **/
    private CRoles createRootRole() {
        if (rootConfig != null && rootConfig.isDisabled())
            return null;
        return CRoles.builder()
                .code("root")
                .name("超级管理员")
                .build();

    }
}
