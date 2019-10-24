/**
 * @filename:CRolesServiceImpl 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.CRoles;
import cn.samples.depot.web.mapper.CRolesMapper;
import cn.samples.depot.web.service.CRolesService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 角色表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
public class CRolesServiceImpl extends ServiceImpl<CRolesMapper, CRoles> implements CRolesService {
    @Override
    public int saveRole(CRoles cRoles) {
        if ("root".equals(cRoles.getCode()))
            return 1;
        int count = count(Wrappers.<CRoles>lambdaQuery().eq(CRoles::getCode, cRoles.getCode()));
        if (count != 0)
            return 1;
        save(cRoles);
        return 0;
    }
}