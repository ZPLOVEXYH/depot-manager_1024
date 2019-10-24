/**
 * @filename:PCustomsCodeServiceImpl 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.PCustomsCode;
import cn.samples.depot.web.mapper.PCustomsCodeMapper;
import cn.samples.depot.web.service.PCustomsCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 海关代码表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Service
public class PCustomsCodeServiceImpl extends ServiceImpl<PCustomsCodeMapper, PCustomsCode> implements PCustomsCodeService {

}