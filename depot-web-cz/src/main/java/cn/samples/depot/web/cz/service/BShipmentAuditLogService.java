package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.BShipmentAuditLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 发运计划-审核日志
 **/
public interface BShipmentAuditLogService extends IService<BShipmentAuditLog> {

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 根据发运计划id 获取日志集合
     **/
    List<BShipmentAuditLog> listByShipmentPlanId(String shipmentPlanId);
}