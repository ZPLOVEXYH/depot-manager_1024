/**
 * @filename:BRailwayLoadDelContaServiceImpl 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.UniqueIdUtil;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.cz.mapper.BRailwayLoadDelContaMapper;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadDelConta;
import cn.samples.depot.web.entity.BRailwayLoadDelList;
import cn.samples.depot.web.entity.BRailwayLoadList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @Description: 装车记录单作废报文表集装箱信息——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
@Slf4j
public class BRailwayLoadDelContaServiceImpl extends ServiceImpl<BRailwayLoadDelContaMapper, BRailwayLoadDelConta> implements BRailwayLoadDelContaService {


    @Autowired
    private BRailwayLoadDelListService delListService;
    @Autowired
    private BRailwayLoadDelReportHeadService delHeadService;
    @Autowired
    private BRailwayLoadContaService contaService;
    @Autowired
    private BRailwayLoadListService listService;


    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 根据集装箱id 删除集装箱及对应的运抵编号信息
     **/
    @Override
    @Transactional
    public void deleteByContaId(String contaId) throws BizException {
        BRailwayLoadDelConta conta = cheakUpdate(contaId);
        cheakDelete(conta);
        removeById(contaId);
        delListService.remove(new LambdaQueryWrapper<BRailwayLoadDelList>().eq(BRailwayLoadDelList::getRailwayLoadDelContaId, contaId));

    }

    private void cheakDelete(BRailwayLoadDelConta conta) throws BizException {
        List<BRailwayLoadDelConta> contas = list(new LambdaQueryWrapper<BRailwayLoadDelConta>()
                .eq(BRailwayLoadDelConta::getRailwayLoadDelReportHeadId, conta.getRailwayLoadDelReportHeadId())
                .ne(BRailwayLoadDelConta::getBillNo, conta.getBillNo()));
        if (CollectionUtils.isEmpty(contas)) throw new BizException("至少需要一条运单信息");
    }

    private BRailwayLoadDelConta cheakUpdate(String contaId) throws BizException {
        BRailwayLoadDelConta conta = getById(contaId);
        if (null == conta) throw new BizException("找不到对应集装箱");
        if (conta.getAuditStatus().equals(WayBillAuditStatus.WayBillAudit_07.getValue()) || conta.getAuditStatus().equals(WayBillAuditStatus.WayBillAudit_08.getValue()))
            throw new BizException("当前状态不允许删除");
        return conta;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(String headId, List<BRailwayLoadConta> contas) throws BizException {
        if (CollectionUtils.isEmpty(contas)) return;
        delHeadService.checkUpdate(headId);
        Set<String> billNos = list(new LambdaQueryWrapper<BRailwayLoadDelConta>().eq(BRailwayLoadDelConta::getRailwayLoadDelReportHeadId, headId)).stream().map(c -> c.getBillNo()).collect(Collectors.toSet());
        List<BRailwayLoadDelConta> contaList = new ArrayList<>();
        List<BRailwayLoadDelList> listList = new ArrayList<>();

        for (BRailwayLoadConta conta : contas) {
            if (!billNos.add(conta.getBillNo())) throw new BizException(String.format("运单号[%s]已存在", conta.getBillNo()));
            BRailwayLoadDelConta delConta = buildDelContaByConta(headId, conta);
            contaList.add(delConta);
            listList.addAll(buildDelListByContaId(headId, delConta.getId(), conta.getId()));
        }
        saveBatch(contaList);
        delListService.saveBatch(listList);
    }

    private List<BRailwayLoadDelList> buildDelListByContaId(String headId, String delContaId, String contaId) throws BizException {
        List<BRailwayLoadList> lists = listService.list(new LambdaQueryWrapper<BRailwayLoadList>().eq(BRailwayLoadList::getRailwayLoadReportContaId, contaId));
        return lists.stream().map(list -> {
            BRailwayLoadDelList delList = BRailwayLoadDelList.builder().build();
            BeanUtils.copyProperties(list, delList, "id");
            delList.setRailwayLoadDelReportHeadId(headId);
            delList.setRailwayLoadDelContaId(delContaId);
            delList.setCreateTime(System.currentTimeMillis());
            return delList;
        }).collect(Collectors.toList());
    }

    private BRailwayLoadDelConta buildDelContaByConta(String headId, BRailwayLoadConta conta) throws BizException {
        BRailwayLoadConta conta2 = contaService.getById(conta.getId());
        if (null == conta2) throw new BizException(String.format("找不到对应[%s]的集装箱", conta.getId()));
        BeanUtils.copyProperties(conta2, conta, "notes");
        if (!WayBillAuditStatus.WayBillAudit_03.getValue().equals(conta.getAuditStatus()))
            throw new BizException(String.format("运单[%s]未审核通过", conta.getBillNo()));
        BRailwayLoadDelConta delConta = BRailwayLoadDelConta.builder().build();
        BeanUtils.copyProperties(conta, delConta);
        delConta.setId(UniqueIdUtil.getUUID());
        delConta.setRailwayLoadDelReportHeadId(headId);
        delConta.setAuditStatus(WayBillAuditStatus.WayBillAudit_05.getValue());
        delConta.setCreateTime(System.currentTimeMillis());
        return delConta;
    }


    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 根据集装箱号，运抵单号，查询表头id集合
     **/
    @Override
    public Set<String> listHeadIdsByLoadQuery(LoadQuery query) {
        return list(new LambdaQueryWrapper<BRailwayLoadDelConta>()
                .like(StringUtils.isNotEmpty(query.getContaNo()), BRailwayLoadDelConta::getContaNo, query.getContaNo())
                .like(StringUtils.isNotEmpty(query.getBillNo()), BRailwayLoadDelConta::getBillNo, query.getBillNo())).stream().map(conta -> {
            return conta.getRailwayLoadDelReportHeadId();
        }).collect(Collectors.toSet());
    }
}