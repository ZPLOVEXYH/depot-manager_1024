/**
 * @filename:BShipmentAuditLogServiceImpl 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.BShipmentAuditLog;
import cn.samples.depot.web.mapper.BShipmentAuditLogMapper;
import cn.samples.depot.web.service.BShipmentAuditLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 审核日志表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Service
public class BShipmentAuditLogServiceImpl extends ServiceImpl<BShipmentAuditLogMapper, BShipmentAuditLog> implements BShipmentAuditLogService {

}