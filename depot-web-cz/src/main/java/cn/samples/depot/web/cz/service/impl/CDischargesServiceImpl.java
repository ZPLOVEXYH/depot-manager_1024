/**
 * @filename:CDischargesServiceImpl 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.model.Status;
import cn.samples.depot.web.cz.mapper.CDischargesMapper;
import cn.samples.depot.web.cz.service.CDischargesService;
import cn.samples.depot.web.entity.CDischarges;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 装卸货地表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Service
public class CDischargesServiceImpl extends ServiceImpl<CDischargesMapper, CDischarges> implements CDischargesService {

    @Override
    public List<Map<String, Object>> select() {
        return listMaps(new LambdaQueryWrapper<CDischarges>().select(CDischarges::getCode, CDischarges::getName).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
    }
}