/**
 * @filename:BRailwayLoadContaServiceImpl 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.constant.Constants;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.cz.mapper.BRailwayLoadContaMapper;
import cn.samples.depot.web.cz.mapper.BRailwayLoadDelListMapper;
import cn.samples.depot.web.cz.service.BRailwayLoadContaService;
import cn.samples.depot.web.cz.service.BRailwayLoadReportHeadService;
import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadDelList;
import cn.samples.depot.web.entity.BRailwayLoadReportHead;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 装车记录单申报报文表集装箱信息——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
@Slf4j
public class BRailwayLoadContaServiceImpl extends ServiceImpl<BRailwayLoadContaMapper, BRailwayLoadConta> implements BRailwayLoadContaService {

    /**
     * 装车记录单作废报文表体运抵单信息
     */
    @Autowired
    private BRailwayLoadDelListMapper loadDelListMapper;
    @Autowired
    private BRailwayLoadReportHeadService headService;

    /**
     * 单个删除装车记录单申报报文表集装箱信息
     *
     * @param id
     * @return
     */
    @Override
    public JsonResult deleteLoadContaById(String id) {
        // 根据id查询得到理货作废信息
        BRailwayLoadConta loadConta = this.baseMapper.selectById(id);
        if (null != loadConta) {
            int deleteLoadConta = this.baseMapper.deleteById(id);
            log.info("删除装车记录单申报报文表集装箱信息：{}", deleteLoadConta);
            if (deleteLoadConta > 0) {
                int deleteList = loadDelListMapper.delete(Wrappers.<BRailwayLoadDelList>lambdaQuery().eq(BRailwayLoadDelList::getRailwayLoadDelContaId, id));
                log.info("删除装车记录单申请报文表体：{}", deleteList);
            } else {
                return JsonResult.error("删除失败，此id号：{}，在表头中不存在", id);
            }
        } else {
            return JsonResult.error("删除失败，此id号：{}，在表头中不存在", id);
        }

        return JsonResult.success();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 根据 集装箱号，运抵单号，查询表头Id集合
     **/
    @Override
    public Set<String> listHeadIdsByLoadQuery(LoadQuery query) {
        return listMaps(new LambdaQueryWrapper<BRailwayLoadConta>()
                .select(BRailwayLoadConta::getRailwayLoadReportHeadId)
                .like(StringUtils.isNotEmpty(query.getContaNo()), BRailwayLoadConta::getContaNo, query.getContaNo())
                .like(StringUtils.isNotEmpty(query.getBillNo()), BRailwayLoadConta::getBillNo, query.getBillNo()))
                .stream().map(map -> map.get("railway_load_report_head_id").toString())
                .collect(Collectors.toSet());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 集装箱保存前的相关工作
     **/
    @Override
    public void saveBefore(BRailwayLoadReportHead head, BRailwayLoadConta conta) throws BizException {
        checkEmpty(conta);
        checkBiz(conta);
        conta.setRailwayLoadReportHeadId(head.getId());
        conta.setSealNum(conta.getSealNo().split(Constants.COMMA).length);
        if (StringUtils.isEmpty(conta.getAuditStatus()))
            conta.setAuditStatus(WayBillAuditStatus.WayBillAudit_01.getValue());
        if (null == conta.getCreateTime() || conta.getCreateTime() <= 0)
            conta.setCreateTime(System.currentTimeMillis());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 非空检查
     **/
    private void checkEmpty(BRailwayLoadConta conta) throws BizException {
        if (null == conta) throw new BizException("集装箱为空");
        if (StringUtils.isEmpty(conta.getBillNo())) throw new BizException("运单号不能为空");
        if (StringUtils.isEmpty(conta.getCarriageNo())) throw new BizException("车皮号不能为空");
        if (StringUtils.isEmpty(conta.getContaNo())) throw new BizException("集装箱号不能为空");
        if (StringUtils.isEmpty(conta.getContaType())) throw new BizException("箱型不能为空");
        if (StringUtils.isEmpty(conta.getSealNo())) throw new BizException("封志号不能为空");
    }

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 1.同一个集装箱，封志号不能重复 2.运单号 全库唯一
     **/
    private void checkBiz(BRailwayLoadConta conta) throws BizException {
        //1.同一个集装箱，封志号不能重复
        String[] sealNos = conta.getSealNo().split(Constants.COMMA);
        Set<String> sealNoSet = new HashSet<>();
        for (String sealNo : sealNos) {
            if (!sealNoSet.add(sealNo))
                throw new BizException(String.format("集装箱[%s]中封志号[%s]重复", conta.getContaNo(), sealNo));
        }
        //2.运单号 全库唯一
        String billNo = conta.getBillNo();
        if (CollectionUtils.isNotEmpty(list(new LambdaQueryWrapper<BRailwayLoadConta>().eq(BRailwayLoadConta::getBillNo, billNo))))
            throw new BizException(String.format("运单号[%s]已存在", billNo));
    }

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 添加运单，查询集装箱集合（可作废的）
     **/
    @Override
    public List<BRailwayLoadConta> listContas(String messageId) {
        BRailwayLoadReportHead head = headService.getByMessageId(messageId);
        if (null == head) return null;
        return list(new LambdaQueryWrapper<BRailwayLoadConta>().eq(BRailwayLoadConta::getRailwayLoadReportHeadId, head.getId()).eq(BRailwayLoadConta::getAuditStatus, WayBillAuditStatus.WayBillAudit_03.getValue()));
    }

    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description 获取最新一条记录
     **/
    @Override
    public BRailwayLoadConta getLastByContaNo(String contaNo) {
        return getOne(new LambdaQueryWrapper<BRailwayLoadConta>().eq(BRailwayLoadConta::getContaNo, contaNo).orderByDesc(BRailwayLoadConta::getCreateTime));
    }
}