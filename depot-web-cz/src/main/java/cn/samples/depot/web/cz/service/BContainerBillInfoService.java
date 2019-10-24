package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.BContainerBillInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * @Author majunzi
 * @Date 2019/9/23
 * @Description 集装箱-发运计划对应关系
 **/
public interface BContainerBillInfoService extends IService<BContainerBillInfo> {

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 根据集装箱状态id获取单货信息
     **/
    List<BContainerBillInfo> listBillInfo(String contaId);

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 根据发运计划id和集装箱id 获取记录
     **/
    BContainerBillInfo getByShipmentIdAndContaNo(String shipmentPlanId, String containerNo);

    /**
     * @Author majunzi
     * @Date 2019/9/24
     * @Description 根据箱状态id 获取集合
     **/
    List<BContainerBillInfo> listByContainerId(String containerId);

    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description
     **/
    BContainerBillInfo getByContainerIdAndContaNo(String containerId, String contaNo);
}