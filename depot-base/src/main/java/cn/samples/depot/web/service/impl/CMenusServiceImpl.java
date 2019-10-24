/**
 * @filename:CMenusServiceImpl 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.common.model.Status;
import cn.samples.depot.web.dto.menu.Menu;
import cn.samples.depot.web.entity.CMenus;
import cn.samples.depot.web.entity.CRoleMenus;
import cn.samples.depot.web.mapper.CMenusMapper;
import cn.samples.depot.web.mapper.CRoleMenusMapper;
import cn.samples.depot.web.service.CMenusService;
import cn.samples.depot.web.service.CRoleMenusService;
import cn.samples.depot.web.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 菜单表——SERVICEIMPL
 * @Author: ChenJie
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
public class CMenusServiceImpl extends ServiceImpl<CMenusMapper, CMenus> implements CMenusService {

    @Autowired
    private CRoleMenusMapper cRoleMenusMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CRoleMenusService cRoleMenusService;

    @Override
    public int saveMenu(CMenus cMenus) {
        int count = count(Wrappers.<CMenus>lambdaQuery().eq(CMenus::getCode, cMenus.getCode()));
        if (count != 0)
            return 1;
        save(cMenus);
        return 0;
    }

    @Override
    public List<Menu> getAllMenuList() {
        return createMenuList(list());
    }

    @Override
    public List<Menu> getCurrentMenuList() {
        String role = userService.currentRoleCode();
        if (role == null)
            return null;
        if ("root".equals(role))
            return getAllMenuList();
        List<CRoleMenus> cRoleMenusList = cRoleMenusMapper.selectList(Wrappers.<CRoleMenus>lambdaQuery().eq(CRoleMenus::getRoleCode, role));
        List<CMenus> cMenusList = list(Wrappers.<CMenus>lambdaQuery().eq(CMenus::getEnable, Status.ENABLED.getValue())
                .in(CMenus::getCode, cRoleMenusList.stream().map(CRoleMenus::getMenuCode).collect(Collectors.toList())));
        return createMenuList(cMenusList);
    }

    @Override
    @Transactional
    public boolean deleteMenu(String id) {
        CMenus menu = getById(id);
        if (menu == null)
            return false;
        removeById(id);
        cRoleMenusService.remove(Wrappers.<CRoleMenus>lambdaQuery().eq(CRoleMenus::getMenuCode, menu.getCode()));
        return true;
    }

    /**
     * 根据一级菜单生成其子菜单
     */
    private List<Menu> createMenuList(List<CMenus> cMenusList) {
        if (cMenusList == null || cMenusList.size() == 0)
            return null;
        List<Menu> menu_list = cMenusList.stream().filter(f -> Integer.valueOf(1).equals(f.getLevel()))
                .map(m -> createMenu(m)).sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList());
        if (menu_list == null || menu_list.size() == 0)
            return null;
        menu_list.forEach(m -> {
            String parentCode = m.getCode();
            if (parentCode == null)
                return;
            List<Menu> sub_menu = cMenusList.stream().filter(f -> parentCode.equals(f.getParentCode()))
                    .map(f -> createMenu(f)).sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList());
            m.setSubMenu(sub_menu);
        });
        return menu_list;
    }

    private Menu createMenu(CMenus m) {
        System.out.println("打印的参数为：" + m.toString());
        return Menu.builder()
                .id(m.getId())
                .code(m.getCode())
                .enable(m.getEnable())
                .createTime(m.getCreateTime())
                .icon(m.getIcon())
                .level(m.getLevel())
                .name(m.getName())
                .remark(m.getRemark())
                .sort(m.getSort())
                .url(m.getUrl())
                .build();
    }
}