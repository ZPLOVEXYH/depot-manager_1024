/**
 * @filename:CRoleMenusService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service;

import cn.samples.depot.web.dto.menu.RoleMenu;
import cn.samples.depot.web.entity.CRoleMenus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 角色菜单表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
public interface CRoleMenusService extends IService<CRoleMenus> {
    Boolean saveRoleMenuList(List<CRoleMenus> cRoleMenus);

    List<RoleMenu> getRoleMenuList(String roleCode);
}