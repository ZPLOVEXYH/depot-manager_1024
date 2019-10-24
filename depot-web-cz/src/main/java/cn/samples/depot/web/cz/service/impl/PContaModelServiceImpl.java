/**
 * @filename:PContaModelServiceImpl 2019年7月18日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.PContaModelMapper;
import cn.samples.depot.web.cz.service.PContaModelService;
import cn.samples.depot.web.entity.PContaModel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 箱型表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月18日
 * @Version: V1.0
 */
@Service
public class PContaModelServiceImpl extends ServiceImpl<PContaModelMapper, PContaModel> implements PContaModelService {

}