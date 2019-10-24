/**
 * @filename:PGoodsTypeServiceImpl 2019年7月17日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.PGoodsType;
import cn.samples.depot.web.mapper.PGoodsTypeMapper;
import cn.samples.depot.web.service.PGoodsTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 货物类型表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Service
public class PGoodsTypeServiceImpl extends ServiceImpl<PGoodsTypeMapper, PGoodsType> implements PGoodsTypeService {

}