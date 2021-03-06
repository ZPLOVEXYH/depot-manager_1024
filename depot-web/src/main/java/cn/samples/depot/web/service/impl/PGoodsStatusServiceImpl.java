/**
 * @filename:PGoodsStatusServiceImpl 2019年7月17日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.PGoodsStatus;
import cn.samples.depot.web.mapper.PGoodsStatusMapper;
import cn.samples.depot.web.service.PGoodsStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 货物状态表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Service
public class PGoodsStatusServiceImpl extends ServiceImpl<PGoodsStatusMapper, PGoodsStatus> implements PGoodsStatusService {

}