/**
 * @filename:CUsersService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service;

import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.dto.user.ResetPasswordDTO;
import cn.samples.depot.web.dto.user.UpdatePasswordDTO;
import cn.samples.depot.web.entity.CUsers;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 用户信息表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface CUsersService extends IService<CUsers> {

    int saveUser(CUsers cUsers);

    boolean resetPassword(ResetPasswordDTO reSetPasswordDTO);

    boolean deleteUser(String id);

    boolean updateUser(String id, CUsers cUsers);

    JsonResult updatePassword(UpdatePasswordDTO updatePasswordDTO);
}