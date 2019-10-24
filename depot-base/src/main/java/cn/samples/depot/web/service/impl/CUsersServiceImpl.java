/**
 * @filename:CUsersServiceImpl 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.dto.user.ResetPasswordDTO;
import cn.samples.depot.web.dto.user.UpdatePasswordDTO;
import cn.samples.depot.web.entity.CRoles;
import cn.samples.depot.web.entity.CUserRoles;
import cn.samples.depot.web.entity.CUsers;
import cn.samples.depot.web.mapper.CUsersMapper;
import cn.samples.depot.web.service.CRolesService;
import cn.samples.depot.web.service.CUserRolesService;
import cn.samples.depot.web.service.CUsersService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 用户信息表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
@SuppressWarnings("rawtypes")
public class CUsersServiceImpl extends ServiceImpl<CUsersMapper, CUsers> implements CUsersService {

    @Autowired
    private CUserRolesService cUserRolesService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private CRolesService cRolesService;

    @Override
    @Transactional
    public int saveUser(CUsers cUsers) {
        if (cUsers.getUserName().equals("root")) {
            return 1; //超管用户默认不允许创建
        }
        int count = count(Wrappers.<CUsers>lambdaQuery().eq(CUsers::getUserName, cUsers.getUserName()));
        if (count != 0) {
            return 1;
        }
        CRoles cRoles = cRolesService.getOne(Wrappers.<CRoles>lambdaQuery().eq(CRoles::getCode, cUsers.getRoleCode()));
        if (cRoles == null)
            return 1;
        if (cUsers.getPassword() != null)
            cUsers.setPassword(encoder.encode(cUsers.getPassword()));
        cUsers.setRoleName(cRoles.getName());
        save(cUsers);
        cUserRolesService.save(CUserRoles.builder()
                .roleCode(cUsers.getRoleCode())
                .userName(cUsers.getUserName())
                .createTime(System.currentTimeMillis())
                .build());
        return 0;
    }

    @Transactional
    public boolean updateUser(String id, CUsers cUsers) {
        if (cUsers.getUserName().equals("root"))
            return false; //超管用户默认不允许修改
        CUserRoles cUserRoles = cUserRolesService.getOne(Wrappers.<CUserRoles>lambdaQuery().eq(CUserRoles::getUserName, cUsers.getUserName()));
        CRoles cRoles = cRolesService.getOne(Wrappers.<CRoles>lambdaQuery().eq(CRoles::getCode, cUsers.getRoleCode()));
        if (cUserRoles == null || cRoles == null)
            return false;
        update(Wrappers.<CUsers>lambdaUpdate()
                .eq(CUsers::getId, id)
                .set(CUsers::getRoleCode, cUsers.getRoleCode())
                .set(CUsers::getRealName, cUsers.getRealName())
                .set(CUsers::getEnterpriseCode, cUsers.getEnterpriseCode())
                .set(CUsers::getEnable, cUsers.getEnable())
                .set(CUsers::getContactPhone, cUsers.getContactPhone())
                .set(CUsers::getRoleName, cRoles.getName()));
        if (!cUserRoles.getRoleCode().equals(cUsers.getRoleCode())) {
            cUserRoles.setRoleCode(cUsers.getRoleCode());
            cUserRolesService.updateById(cUserRoles);
        }
        return true;
    }

    @Override
    public boolean resetPassword(ResetPasswordDTO reSetPasswordDTO) {
        return update(Wrappers.<CUsers>lambdaUpdate()
                .eq(CUsers::getId, reSetPasswordDTO.getId())
                .set(CUsers::getPassword, encoder.encode(reSetPasswordDTO.getPassword())));
    }


    @Override
    public JsonResult updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        CUsers cUsers = getOne(Wrappers.<CUsers>lambdaQuery().eq(CUsers::getUserName, updatePasswordDTO.getUsername()));
        if (null == cUsers)
            return JsonResult.error("此用户不存在");
        if (!encoder.matches(updatePasswordDTO.getPasswordOld(), cUsers.getPassword()))
            return JsonResult.error("原密码错误");
        update(Wrappers.<CUsers>lambdaUpdate()
                .eq(CUsers::getUserName, updatePasswordDTO.getUsername())
                .set(CUsers::getPassword, encoder.encode(updatePasswordDTO.getPassword())));
        return JsonResult.success();
    }

    @Override
    @Transactional
    public boolean deleteUser(String id) {
        CUsers cUsers = getById(id);
        if (cUsers == null)
            return false;
        removeById(id);
        cUserRolesService.remove(Wrappers.<CUserRoles>lambdaQuery().eq(CUserRoles::getUserName, cUsers.getUserName()));
        return true;
    }

}