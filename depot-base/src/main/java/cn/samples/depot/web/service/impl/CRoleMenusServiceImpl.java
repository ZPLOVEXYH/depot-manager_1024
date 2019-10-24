/**
 * @filename:CRoleMenusServiceImpl 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.dto.menu.Menu;
import cn.samples.depot.web.dto.menu.RoleMenu;
import cn.samples.depot.web.entity.CRoleMenus;
import cn.samples.depot.web.mapper.CRoleMenusMapper;
import cn.samples.depot.web.service.CMenusService;
import cn.samples.depot.web.service.CRoleMenusService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 角色菜单表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
public class CRoleMenusServiceImpl extends ServiceImpl<CRoleMenusMapper, CRoleMenus> implements CRoleMenusService {

    @Autowired
    CMenusService cMenusService;

    @Override
    @Transactional
    public Boolean saveRoleMenuList(List<CRoleMenus> cRoleMenus) {
        if (cRoleMenus == null || cRoleMenus.size() == 0)
            return false;
        remove(Wrappers.<CRoleMenus>lambdaQuery().eq(CRoleMenus::getRoleCode, cRoleMenus.get(0).getRoleCode()));
        saveBatch(cRoleMenus);
        return true;
    }

    @Override
    public List<RoleMenu> getRoleMenuList(String roleCode) {
        List<CRoleMenus> cRoleMenusList = list(Wrappers.<CRoleMenus>lambdaQuery().eq(CRoleMenus::getRoleCode, roleCode));
        List<Menu> menuList = cMenusService.getAllMenuList();
        if (menuList == null || menuList.size() == 0)
            return null;
        return menuList.stream().map(f -> {
            RoleMenu roleMenu = createRoleMenu(f, cRoleMenusList);
            List<Menu> sub_menu = f.getSubMenu();
            if (sub_menu == null || sub_menu.size() == 0) return roleMenu;
            List<RoleMenu> subRoleMenu = sub_menu.stream().map(sub -> createRoleMenu(sub, cRoleMenusList))
                    .sorted(Comparator.comparing(RoleMenu::getSort)).collect(Collectors.toList());
            roleMenu.setSubMenu(subRoleMenu);
            return roleMenu;
        }).sorted(Comparator.comparing(RoleMenu::getSort)).collect(Collectors.toList());
    }

    private RoleMenu createRoleMenu(Menu menu, List<CRoleMenus> cRoleMenusList) {
        boolean select = cRoleMenusList == null || cRoleMenusList.size() == 0 ? false
                : cRoleMenusList.stream().anyMatch(roleMenus -> menu.getCode().equals(roleMenus.getMenuCode()));
        return RoleMenu.builder()
                .code(menu.getCode())
                .name(menu.getName())
                .level(menu.getLevel())
                .parentCode(menu.getParentCode())
                .sort(menu.getSort())
                .select(select ? 1 : 0)
                .build();
    }
}