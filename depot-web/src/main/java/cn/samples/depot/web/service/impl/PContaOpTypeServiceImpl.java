/**
 * @filename:PContaOpTypeServiceImpl 2019年7月18日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.PContaOpType;
import cn.samples.depot.web.mapper.PContaOpTypeMapper;
import cn.samples.depot.web.service.PContaOpTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 集装箱操作类型表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月18日
 * @Version: V1.0
 */
@Service
public class PContaOpTypeServiceImpl extends ServiceImpl<PContaOpTypeMapper, PContaOpType> implements PContaOpTypeService {

}