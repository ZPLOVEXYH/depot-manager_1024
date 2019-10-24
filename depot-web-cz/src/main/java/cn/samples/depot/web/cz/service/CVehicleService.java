/**
 * @filename:CVehicleService 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.CVehicle;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 车辆备案表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
public interface CVehicleService extends IService<CVehicle> {

    void saveVehical(CVehicle vehicle) throws BizException;

    void updateVehical(String id, CVehicle vehicle) throws BizException;
}