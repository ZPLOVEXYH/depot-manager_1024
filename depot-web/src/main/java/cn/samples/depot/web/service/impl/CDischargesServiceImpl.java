/**
 * @filename:CDischargesServiceImpl 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.CDischarges;
import cn.samples.depot.web.mapper.CDischargesMapper;
import cn.samples.depot.web.service.CDischargesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 装卸货地表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Service
public class CDischargesServiceImpl extends ServiceImpl<CDischargesMapper, CDischarges> implements CDischargesService {

}