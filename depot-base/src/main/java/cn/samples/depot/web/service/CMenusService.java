/**
 * @filename:CMenusService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service;

import cn.samples.depot.web.dto.menu.Menu;
import cn.samples.depot.web.entity.CMenus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 菜单表——SERVICE
 * @Author: ChenJie
 * @CreateDate: 2019年08月21日
 * @Version: V1.0
 */
public interface CMenusService extends IService<CMenus> {
    int saveMenu(CMenus cMenus);

    List<Menu> getAllMenuList();

    /**
     * 根据当前登录用户获取可用菜单列表
     **/
    List<Menu> getCurrentMenuList();

    boolean deleteMenu(String id);

}