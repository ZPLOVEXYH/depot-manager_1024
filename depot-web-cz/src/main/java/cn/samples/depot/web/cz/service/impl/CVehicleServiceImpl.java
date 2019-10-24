/**
 * @filename:CVehicleServiceImpl 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.cz.mapper.CVehicleMapper;
import cn.samples.depot.web.cz.service.CVehicleService;
import cn.samples.depot.web.entity.CVehicle;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @Description: 车辆备案表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Service
public class CVehicleServiceImpl extends ServiceImpl<CVehicleMapper, CVehicle> implements CVehicleService {

    @Override
    public void saveVehical(CVehicle vehicle) throws BizException {
        CVehicle newV = beforeSave(vehicle);
        save(newV);
    }

    @Override
    public void updateVehical(String id, CVehicle vehicle) throws BizException {
        CVehicle newV = beforeUpdate(id, vehicle);
        updateById(newV);
    }

    private CVehicle beforeUpdate(String id, CVehicle vehicle) throws BizException {
        //车牌号全局唯一
        CVehicle newV = CVehicle.builder().build();
        BeanUtils.copyProperties(vehicle, newV, "auditStatus", "auditTime", "auditUser", "enable", "createTime", "vehicleNumber");
        return newV;
    }

    private CVehicle beforeSave(CVehicle vehicle) throws BizException {
        //车牌号全局唯一
        CVehicle v = getByVehicleNumber(vehicle.getVehicleNumber());
        if (null != v) throw new BizException(String.format("车牌号[%s]已经存在", vehicle.getVehicleNumber()));
        CVehicle newV = CVehicle.builder().build();
        BeanUtils.copyProperties(vehicle, newV, "id", "auditStatus", "auditTime", "auditUser", "enable");
        newV.setCreateTime(System.currentTimeMillis());
        return newV;
    }

    private CVehicle getByVehicleNumber(String vehicleNumber) {
        return getOne(new LambdaQueryWrapper<CVehicle>().eq(CVehicle::getVehicleNumber, vehicleNumber));
    }

}