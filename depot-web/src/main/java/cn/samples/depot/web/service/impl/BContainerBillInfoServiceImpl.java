package cn.samples.depot.web.service.impl;


import cn.samples.depot.web.entity.BContainerBillInfo;
import cn.samples.depot.web.mapper.BContainerBillInfoMapper;
import cn.samples.depot.web.service.BContainerBillInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author majunzi
 * @Date 2019/9/23
 * @Description 集装箱-发运计划对应，单货信息
 **/
@Service
public class BContainerBillInfoServiceImpl extends ServiceImpl<BContainerBillInfoMapper, BContainerBillInfo> implements BContainerBillInfoService {

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 根据集装箱状态id获取单货信息
     **/
    @Override
    public List<BContainerBillInfo> listBillInfo(String contaId) {
        return list(new LambdaQueryWrapper<BContainerBillInfo>().eq(BContainerBillInfo::getContainerId, contaId).orderByDesc(BContainerBillInfo::getCreateTime));
    }

    @Override
    public BContainerBillInfo getByShipmentIdAndContaNo(String shipmentPlanId, String containerNo) {
        return getOne(new LambdaQueryWrapper<BContainerBillInfo>()
                .eq(BContainerBillInfo::getContainerNo, containerNo)
                .eq(BContainerBillInfo::getShipmentPlanId, shipmentPlanId));
    }

}