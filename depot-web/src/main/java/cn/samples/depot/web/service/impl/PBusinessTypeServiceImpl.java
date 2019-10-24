/**
 * @filename:PBusinessTypeServiceImpl 2019年7月17日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.PBusinessType;
import cn.samples.depot.web.mapper.PBusinessTypeMapper;
import cn.samples.depot.web.service.PBusinessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 业务类型表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Service
public class PBusinessTypeServiceImpl extends ServiceImpl<PBusinessTypeMapper, PBusinessType> implements PBusinessTypeService {

}