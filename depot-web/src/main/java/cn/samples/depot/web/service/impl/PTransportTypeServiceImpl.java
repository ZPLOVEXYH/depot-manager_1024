/**
 * @filename:PTransportTypeServiceImpl 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.PTransportType;
import cn.samples.depot.web.mapper.PTransportTypeMapper;
import cn.samples.depot.web.service.PTransportTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 运输方式类型表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Service
public class PTransportTypeServiceImpl extends ServiceImpl<PTransportTypeMapper, PTransportType> implements PTransportTypeService {

}