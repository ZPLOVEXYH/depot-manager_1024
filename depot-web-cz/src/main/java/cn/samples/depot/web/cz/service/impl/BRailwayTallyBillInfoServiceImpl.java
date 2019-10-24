/**
 * @filename:BRailwayTallyBillInfoServiceImpl 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.DeclareStatusFlag;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.web.cz.mapper.BRailwayTallyBillInfoMapper;
import cn.samples.depot.web.cz.service.BRailwayTallyBillInfoService;
import cn.samples.depot.web.cz.service.BRailwayTallyReportHeadService;
import cn.samples.depot.web.entity.BRailwayTallyBillInfo;
import cn.samples.depot.web.entity.BRailwayTallyReportHead;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 铁路进口理货申请报文表体——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Service
public class BRailwayTallyBillInfoServiceImpl extends ServiceImpl<BRailwayTallyBillInfoMapper, BRailwayTallyBillInfo> implements BRailwayTallyBillInfoService {

    @Autowired
    private BRailwayTallyReportHeadService reportHeadService;

    /**
     * @Author zhangpeng
     * @Date 2019/9/2
     * @Description 添加运单，查询集装箱集合（可作废的）
     **/
    @Override
    public List<BRailwayTallyBillInfo> listContas(String messageId) {
        BRailwayTallyReportHead head = reportHeadService.getByMessageId(messageId);
        if (null == head) return null;
        return list(new LambdaQueryWrapper<BRailwayTallyBillInfo>().eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, head.getId()).eq(BRailwayTallyBillInfo::getAuditStatus, DeclareStatusFlag.DECLARE_PASS.getValue()));
    }

    /**
     * @Author zhangpeng
     * @Date 2019/8/30
     * @Description 集装箱保存前的相关工作
     **/
    @Override
    public void saveBefore(BRailwayTallyReportHead head, BRailwayTallyBillInfo conta) throws BizException {
        checkEmpty(conta);
        checkBiz(conta);
        conta.setRailwayTallyReportHeadId(head.getId());
        conta.setPackNo(conta.getSealNo().split(",").length);
        if (StringUtils.isEmpty(conta.getAuditStatus()))
            conta.setAuditStatus(WayBillAuditStatus.WayBillAudit_01.getValue());
        if (null == conta.getCreateTime() || conta.getCreateTime() <= 0)
            conta.setCreateTime(System.currentTimeMillis());
    }

    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description 获取最新一条记录
     **/
    @Override
    public BRailwayTallyBillInfo getLastByContaNo(String contaNo) {
        return getOne(new LambdaQueryWrapper<BRailwayTallyBillInfo>().eq(BRailwayTallyBillInfo::getContaNo, contaNo).orderByDesc(BRailwayTallyBillInfo::getCreateTime));
    }

    /**
     * @Author zhangpeng
     * @Date 2019/8/30
     * @Description 非空检查
     **/
    private void checkEmpty(BRailwayTallyBillInfo conta) throws BizException {
        if (null == conta) throw new BizException("集装箱为空");
        if (StringUtils.isEmpty(conta.getBillNo())) throw new BizException("运单号不能为空");
        if (StringUtils.isEmpty(conta.getCarriageNo())) throw new BizException("车皮号不能为空");
        if (StringUtils.isEmpty(conta.getContaNo())) throw new BizException("集装箱号不能为空");
        if (StringUtils.isEmpty(conta.getContaType())) throw new BizException("箱型不能为空");
        if (StringUtils.isEmpty(conta.getSealNo())) throw new BizException("封志号不能为空");
    }

    /**
     * @Author zhangpeng
     * @Date 2019/8/30
     * @Description 1.同一个集装箱，封志号不能重复 2.运单号 全库唯一
     **/
    private void checkBiz(BRailwayTallyBillInfo conta) throws BizException {
        //1.同一个集装箱，封志号不能重复
        String[] sealNos = conta.getSealNo().split(",");
        Set<String> sealNoSet = new HashSet<>();
        for (String sealNo : sealNos) {
            if (!sealNoSet.add(sealNo))
                throw new BizException(String.format("集装箱[%s]中封志号[%s]重复", conta.getContaNo(), sealNo));
        }
        //2.运单号 全库唯一
        String billNo = conta.getBillNo();
        if (CollectionUtils.isNotEmpty(list(new LambdaQueryWrapper<BRailwayTallyBillInfo>().eq(BRailwayTallyBillInfo::getBillNo, billNo))))
            throw new BizException(String.format("运单号[%s]已存在", billNo));
    }
}