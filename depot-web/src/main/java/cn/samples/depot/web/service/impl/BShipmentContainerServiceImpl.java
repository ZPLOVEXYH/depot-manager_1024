/**
 * @filename:BShipmentContainerServiceImpl 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.BShipmentContainer;
import cn.samples.depot.web.mapper.BShipmentContainerMapper;
import cn.samples.depot.web.service.BShipmentContainerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 集装箱信息表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Service
public class BShipmentContainerServiceImpl extends ServiceImpl<BShipmentContainerMapper, BShipmentContainer> implements BShipmentContainerService {

}