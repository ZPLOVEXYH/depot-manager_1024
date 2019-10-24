/**
 * @filename:PGoodsArrStatusServiceImpl 2019年7月17日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.PGoodsArrStatus;
import cn.samples.depot.web.mapper.PGoodsArrStatusMapper;
import cn.samples.depot.web.service.PGoodsArrStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 到货状态表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Service
public class PGoodsArrStatusServiceImpl extends ServiceImpl<PGoodsArrStatusMapper, PGoodsArrStatus> implements PGoodsArrStatusService {

}