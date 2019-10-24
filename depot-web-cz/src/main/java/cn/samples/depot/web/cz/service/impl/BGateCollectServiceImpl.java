/**
 * @filename:BGateCollectServiceImpl 2019年08月01日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.BGateCollectMapper;
import cn.samples.depot.web.cz.service.BGateCollectService;
import cn.samples.depot.web.entity.BGateCollect;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 过卡信息采集表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@Service
public class BGateCollectServiceImpl extends ServiceImpl<BGateCollectMapper, BGateCollect> implements BGateCollectService {

}