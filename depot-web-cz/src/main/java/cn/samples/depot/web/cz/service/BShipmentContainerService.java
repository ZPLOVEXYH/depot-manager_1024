package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.model.ShipmentPlanStatus;
import cn.samples.depot.web.entity.BShipmentContainer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 集装箱信息
 **/
public interface BShipmentContainerService extends IService<BShipmentContainer> {
    List<BShipmentContainer> listByShipmentPlanId(String shipmentPlanId);

    /**
     * @Author majunzi
     * @Date 2019/9/24
     * @Description 更新状态
     **/
    boolean updateStatus(String shipmentPlanId, String contaNo, ShipmentPlanStatus status);

    /**
     * @Author majunzi
     * @Date 2019/9/24
     * @Description 根据发运计划id和状态 获取集合
     **/
    List<BShipmentContainer> listByShipmentPlanIdAndStatus(String shipmentPlanId, String... statuses);
}