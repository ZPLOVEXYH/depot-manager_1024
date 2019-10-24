/**
 * @filename:BRailwayTallyBillInfoService 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.BRailwayTallyBillInfo;
import cn.samples.depot.web.entity.BRailwayTallyReportHead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 铁路进口理货申请报文表体——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
public interface BRailwayTallyBillInfoService extends IService<BRailwayTallyBillInfo> {

    /**
     * @Author zhangpeng
     * @Date 2019/9/2
     * @Description 添加运单，查询集装箱集合（可作废的）
     **/
    List<BRailwayTallyBillInfo> listContas(String messageId);

    void saveBefore(BRailwayTallyReportHead head, BRailwayTallyBillInfo conta) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description 获取最新一条记录
     **/
    BRailwayTallyBillInfo getLastByContaNo(String contaNo);
}