/**
 * @filename:PCustomsCodeServiceImpl 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.model.Status;
import cn.samples.depot.web.cz.mapper.PCustomsCodeMapper;
import cn.samples.depot.web.cz.service.PCustomsCodeService;
import cn.samples.depot.web.entity.PCustomsCode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 海关代码表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Service
public class PCustomsCodeServiceImpl extends ServiceImpl<PCustomsCodeMapper, PCustomsCode> implements PCustomsCodeService {

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 下拉选择：仅code+name
     **/
    @Override
    public List<Map<String, Object>> select() {
        return listMaps(new LambdaQueryWrapper<PCustomsCode>().select(PCustomsCode::getCode, PCustomsCode::getName).eq(PCustomsCode::getEnable, Status.ENABLED.getValue()));
    }
}