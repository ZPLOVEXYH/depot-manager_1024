package cn.samples.depot.web.service;


import cn.samples.depot.web.entity.CRoles;
import cn.samples.depot.web.entity.CUsers;
import cn.samples.depot.web.oauth.jwt.UserContext;

/**
 * Description:
 *
 * @className: UserService
 * @Author: zhangpeng
 * @Date 2019/7/16 14:40
 * @Version 1.0
 **/
public interface UserService {

    CUsers getUser(String userName);

    CRoles getRole(String userName);

    String currentUserName();

    String currentRoleCode();

    UserContext userContext();
}
