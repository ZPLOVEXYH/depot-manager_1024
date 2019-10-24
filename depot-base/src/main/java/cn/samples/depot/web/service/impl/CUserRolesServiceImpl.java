/**
 * @filename:CUserRolesServiceImpl 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.dto.user.User;
import cn.samples.depot.web.entity.CUserRoles;
import cn.samples.depot.web.entity.CUsers;
import cn.samples.depot.web.mapper.CUserRolesMapper;
import cn.samples.depot.web.service.CUserRolesService;
import cn.samples.depot.web.service.CUsersService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 用户角色关系表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
public class CUserRolesServiceImpl extends ServiceImpl<CUserRolesMapper, CUserRoles> implements CUserRolesService {

    @Autowired
    private CUsersService cUsersService;

    public List<User> getUserList(String roleCode) {
        List<CUsers> cUsersList = cUsersService.list(Wrappers.<CUsers>lambdaQuery().eq(CUsers::getRoleCode, roleCode));
        if (cUsersList == null || cUsersList.size() == 0)
            return null;
        return cUsersList.stream().map(f -> User.builder()
                .id(f.getId())
                .userName(f.getUserName())
                .contactPhone(f.getContactPhone())
                .createTime(f.getCreateTime())
                .enable(f.getEnable())
                .enterpriseCode(f.getEnterpriseCode())
                .realName(f.getRealName())
                .roleCode(f.getRoleCode())
                .roleName(f.getRoleName())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public boolean saveUserRole(List<CUserRoles> cUserRoles) {
        if (cUserRoles == null || cUserRoles.size() == 0)
            return false;
        int count = count(Wrappers.<CUserRoles>lambdaQuery().eq(CUserRoles::getRoleCode, cUserRoles.get(0).getRoleCode())
                .in(CUserRoles::getUserName, cUserRoles
                        .stream()
                        .map(f -> f.getUserName())
                        .collect(Collectors.toList())));
        if (count == 0)
            saveBatch(cUserRoles);
        return true;
    }

}