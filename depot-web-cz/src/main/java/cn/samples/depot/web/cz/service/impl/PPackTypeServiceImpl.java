/**
 * @filename:PPackTypeServiceImpl 2019年7月17日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.PPackTypeMapper;
import cn.samples.depot.web.cz.service.PPackTypeService;
import cn.samples.depot.web.entity.PPackType;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 包装类型表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Service
public class PPackTypeServiceImpl extends ServiceImpl<PPackTypeMapper, PPackType> implements PPackTypeService {

}