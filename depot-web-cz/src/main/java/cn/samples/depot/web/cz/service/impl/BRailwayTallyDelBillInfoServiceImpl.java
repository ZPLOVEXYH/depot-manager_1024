/**
 * @filename:BRailwayTallyDelBillInfoServiceImpl 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.DeclareStatusFlag;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.UniqueIdUtil;
import cn.samples.depot.web.cz.mapper.BRailwayTallyDelBillInfoMapper;
import cn.samples.depot.web.cz.service.BRailwayTallyBillInfoService;
import cn.samples.depot.web.cz.service.BRailwayTallyDelBillInfoService;
import cn.samples.depot.web.cz.service.BRailwayTallyDelReportHeadService;
import cn.samples.depot.web.cz.service.BRailwayTallyReportHeadService;
import cn.samples.depot.web.entity.BRailwayTallyBillInfo;
import cn.samples.depot.web.entity.BRailwayTallyDelBillInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 铁路进口理货作废报文表体——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Service
public class BRailwayTallyDelBillInfoServiceImpl extends ServiceImpl<BRailwayTallyDelBillInfoMapper, BRailwayTallyDelBillInfo> implements BRailwayTallyDelBillInfoService {

    @Autowired
    private BRailwayTallyDelReportHeadService delReportHeadService;

    @Autowired
    private BRailwayTallyDelBillInfoService delBillInfoService;

    @Autowired
    private BRailwayTallyBillInfoService billInfoService;

    @Autowired
    private BRailwayTallyReportHeadService reportHeadService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(String headId, List<BRailwayTallyBillInfo> contas) throws BizException {
        if (CollectionUtils.isEmpty(contas)) return;
        delReportHeadService.checkUpdate(headId);
        Set<String> billNos = list(new LambdaQueryWrapper<BRailwayTallyDelBillInfo>().eq(BRailwayTallyDelBillInfo::getRailwayTallyReportHeadId, headId)).stream().map(c -> c.getBillNo()).collect(Collectors.toSet());
        List<BRailwayTallyDelBillInfo> contaList = new ArrayList<>();

        for (BRailwayTallyBillInfo conta : contas) {
            if (!billNos.add(conta.getBillNo())) throw new BizException(String.format("运单号[%s]已存在", conta.getBillNo()));
            BRailwayTallyDelBillInfo delConta = buildDelContaByConta(headId, conta);
            contaList.add(delConta);
        }

        saveBatch(contaList);
    }

    private BRailwayTallyDelBillInfo buildDelContaByConta(String headId, BRailwayTallyBillInfo conta) throws BizException {
        BRailwayTallyBillInfo conta2 = billInfoService.getById(conta.getId());
        if (null == conta2) throw new BizException(String.format("找不到对应[%s]的集装箱", conta.getId()));
        BeanUtils.copyProperties(conta2, conta, "notes");
        if (!DeclareStatusFlag.DECLARE_PASS.getValue().equals(conta.getAuditStatus()))
            throw new BizException(String.format("运单[%s]未审核通过", conta.getBillNo()));
        BRailwayTallyDelBillInfo delConta = BRailwayTallyDelBillInfo.builder().build();
        BeanUtils.copyProperties(conta, delConta);
        delConta.setId(UniqueIdUtil.getUUID());
        delConta.setRailwayTallyReportHeadId(headId);
        delConta.setAuditStatus(WayBillAuditStatus.WayBillAudit_05.getValue());
        delConta.setCreateTime(System.currentTimeMillis());
        return delConta;
    }

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 根据集装箱id 删除集装箱及对应的运抵编号信息
     **/
    @Override
    @Transactional
    public void deleteByContaId(String contaId) throws BizException {
        BRailwayTallyDelBillInfo conta = cheakUpdate(contaId);
        cheakDelete(conta);
        removeById(contaId);
    }

    private BRailwayTallyDelBillInfo cheakUpdate(String contaId) throws BizException {
        BRailwayTallyDelBillInfo conta = getById(contaId);
        if (null == conta) throw new BizException("找不到对应集装箱");
        // 作废中和作废通过
        if (conta.getAuditStatus().equals(WayBillAuditStatus.WayBillAudit_07.getValue()) || conta.getAuditStatus().equals(WayBillAuditStatus.WayBillAudit_08.getValue()))
            throw new BizException("当前状态不允许删除");
        return conta;
    }

    private void cheakDelete(BRailwayTallyDelBillInfo conta) throws BizException {
        List<BRailwayTallyDelBillInfo> contas = list(new LambdaQueryWrapper<BRailwayTallyDelBillInfo>()
                .eq(BRailwayTallyDelBillInfo::getRailwayTallyReportHeadId, conta.getRailwayTallyReportHeadId())
                .ne(BRailwayTallyDelBillInfo::getBillNo, conta.getBillNo()));
        if (CollectionUtils.isEmpty(contas)) throw new BizException("至少保留一条运单信息");
    }
}