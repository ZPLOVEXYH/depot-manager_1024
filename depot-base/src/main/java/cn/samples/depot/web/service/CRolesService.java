/**
 * @filename:CRolesService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service;

import cn.samples.depot.web.entity.CRoles;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 角色表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
public interface CRolesService extends IService<CRoles> {
    int saveRole(CRoles cRoles);
}