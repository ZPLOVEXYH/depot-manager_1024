package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.BShipmentAuditLogMapper;
import cn.samples.depot.web.cz.service.BShipmentAuditLogService;
import cn.samples.depot.web.entity.BShipmentAuditLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 发运计划-审核日志
 **/
@Service
public class BShipmentAuditLogServiceImpl extends ServiceImpl<BShipmentAuditLogMapper, BShipmentAuditLog> implements BShipmentAuditLogService {
    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 根据发运计划id 获取日志集合
     **/
    @Override
    public List<BShipmentAuditLog> listByShipmentPlanId(String shipmentPlanId) {
        return list(new LambdaQueryWrapper<BShipmentAuditLog>().eq(BShipmentAuditLog::getShipmentPlanId, shipmentPlanId));
    }
}