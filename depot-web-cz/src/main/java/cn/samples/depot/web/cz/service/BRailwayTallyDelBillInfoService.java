/**
 * @filename:BRailwayTallyDelBillInfoService 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.BRailwayTallyBillInfo;
import cn.samples.depot.web.entity.BRailwayTallyDelBillInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 铁路进口理货作废报文表体——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
public interface BRailwayTallyDelBillInfoService extends IService<BRailwayTallyDelBillInfo> {

    void add(String headId, List<BRailwayTallyBillInfo> contas) throws BizException;

    void deleteByContaId(String contaId) throws BizException;
}