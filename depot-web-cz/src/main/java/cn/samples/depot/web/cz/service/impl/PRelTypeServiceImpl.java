/**
 * @filename:PRelTypeServiceImpl 2019年10月17日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.PRelTypeMapper;
import cn.samples.depot.web.cz.service.PRelTypeService;
import cn.samples.depot.web.entity.PRelType;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 放行方式表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年10月17日
 * @Version: V1.0
 */
@Service
public class PRelTypeServiceImpl extends ServiceImpl<PRelTypeMapper, PRelType> implements PRelTypeService {

}