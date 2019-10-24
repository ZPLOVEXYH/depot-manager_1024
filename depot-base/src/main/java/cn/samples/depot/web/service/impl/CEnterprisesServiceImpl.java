/**
 * @filename:CEnterprisesServiceImpl 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.entity.BShipmentPlan;
import cn.samples.depot.web.entity.CEnterprises;
import cn.samples.depot.web.entity.CUsers;
import cn.samples.depot.web.mapper.BShipmentPlanMapper;
import cn.samples.depot.web.mapper.CEnterprisesMapper;
import cn.samples.depot.web.service.CEnterprisesService;
import cn.samples.depot.web.service.CUsersService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 企业信息表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Service
@SuppressWarnings("rawtypes")
public class CEnterprisesServiceImpl extends ServiceImpl<CEnterprisesMapper, CEnterprises> implements CEnterprisesService {

    @Autowired
    private CUsersService cUsersService;
    @Autowired
    private BShipmentPlanMapper bShipmentPlanMapper;

    @Transactional
    @Override
    public JsonResult updateEnterprise(CEnterprises cEnterprises) {
        if (Status.DISABLED.getValue().equals(cEnterprises.getEnable())) {
            cUsersService.update(Wrappers.<CUsers>lambdaUpdate().eq(CUsers::getEnterpriseCode, cEnterprises.getCode())
                    .set(CUsers::getEnable, Status.DISABLED.getValue()));
        }
        update(cEnterprises, Wrappers.<CEnterprises>lambdaQuery().eq(CEnterprises::getId, cEnterprises.getId()));
        return JsonResult.success();
    }

    @Override
    public JsonResult deleteEnterprise(String id) {
        CEnterprises cEnterprises = getById(id);
        if (cEnterprises == null)
            return JsonResult.error("该企业不存在");
        int count = bShipmentPlanMapper.selectCount(Wrappers.<BShipmentPlan>lambdaQuery().eq(BShipmentPlan::getEnterprisesId, cEnterprises.getCode()));
        if (count > 0)
            return JsonResult.error("该企业下存在发运计划,不予许删除");
        removeById(id);
        return JsonResult.success();

    }
}