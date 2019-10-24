/**
 * @filename:PContaModelServiceImpl 2019年7月18日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.PContaModel;
import cn.samples.depot.web.mapper.PContaModelMapper;
import cn.samples.depot.web.service.PContaModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 箱型表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月18日
 * @Version: V1.0
 */
@Service("pContaModelService")
public class PContaModelServiceImpl extends ServiceImpl<PContaModelMapper, PContaModel> implements PContaModelService {

}