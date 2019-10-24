/**
 * @filename:CStationsServiceImpl 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.CStations;
import cn.samples.depot.web.mapper.CStationsMapper;
import cn.samples.depot.web.service.CStationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 场站表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Service
public class CStationsServiceImpl extends ServiceImpl<CStationsMapper, CStations> implements CStationsService {

}