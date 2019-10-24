/**
 * @filename:CEnterprisesService 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service;

import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.entity.CEnterprises;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 企业信息表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface CEnterprisesService extends IService<CEnterprises> {
    JsonResult updateEnterprise(CEnterprises cEnterprises);

    JsonResult deleteEnterprise(String id);
}