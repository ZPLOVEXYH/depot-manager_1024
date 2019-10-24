/**
 * @filename:CUserRolesService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service;

import cn.samples.depot.web.dto.user.User;
import cn.samples.depot.web.entity.CUserRoles;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 用户角色关系表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
public interface CUserRolesService extends IService<CUserRoles> {

    boolean saveUserRole(List<CUserRoles> cUserRoles);

    List<User> getUserList(String roleCode);
}