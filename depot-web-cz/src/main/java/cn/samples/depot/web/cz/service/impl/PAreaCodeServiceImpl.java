/**
 * @filename:PAreaCodeServiceImpl 2019年7月18日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.PAreaCodeMapper;
import cn.samples.depot.web.cz.service.PAreaCodeService;
import cn.samples.depot.web.entity.PAreaCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 监管区域表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月18日
 * @Version: V1.0
 */
@Service
public class PAreaCodeServiceImpl extends ServiceImpl<PAreaCodeMapper, PAreaCode> implements PAreaCodeService {

}