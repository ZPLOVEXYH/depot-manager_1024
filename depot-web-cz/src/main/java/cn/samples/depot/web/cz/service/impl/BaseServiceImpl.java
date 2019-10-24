package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.constant.Constants;
import cn.samples.depot.common.utils.UniqueIdUtil;
import cn.samples.depot.web.cz.mapper.BRailwayTallyDelResponseBillMapper;
import cn.samples.depot.web.cz.mapper.BRailwayTallyDelResponseMapper;
import cn.samples.depot.web.cz.mapper.BRailwayTallyResponseBillMapper;
import cn.samples.depot.web.cz.mapper.BRailwayTallyResponseMapper;
import cn.samples.depot.web.cz.service.BaseService;
import cn.samples.depot.web.entity.BRailwayTallyDelResponse;
import cn.samples.depot.web.entity.BRailwayTallyDelResponseBill;
import cn.samples.depot.web.entity.BRailwayTallyResponse;
import cn.samples.depot.web.entity.BRailwayTallyResponseBill;
import cn.samples.depot.web.entity.xml.RspRailWayBillInfo;
import cn.samples.depot.web.entity.xml.ex.rsp.RspArriveInfo;
import cn.samples.depot.web.entity.xml.load.rsp.RspBillInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 基础实现类
 */
@Service
public class BaseServiceImpl implements BaseService {
    /**
     * 海关回执报文表头
     */
    @Autowired
    BRailwayTallyResponseMapper responseMapper;

    /**
     * 海关回执报文表体
     */
    @Autowired
    BRailwayTallyResponseBillMapper responseBillMapper;

    /**
     * 海关回执报文表头
     */
    @Autowired
    BRailwayTallyDelResponseMapper delResponseMapper;

    /**
     * 海关回执报文表体
     */
    @Autowired
    BRailwayTallyDelResponseBillMapper delResponseBillMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveResponseHeadXml(Map<String, String> headRspXml) {
        // TODO 保存到回执报文表头表中
        if (Constants.FIX_NUM_2.equals(headRspXml.get("functinoCode"))) {
            // 铁路进口理货报文回执实体类
            BRailwayTallyResponse entity = BRailwayTallyResponse.builder()
                    .id(headRspXml.get("headUUID"))
                    .messageId(headRspXml.get("messageId"))
                    .declMessageId(headRspXml.get("declMsgId"))
                    .messageType(headRspXml.get("messageType"))
                    .sendId(headRspXml.get("sendId"))
                    .chkFlag(headRspXml.get("chkFlag"))
                    .notes(headRspXml.get("notes"))
                    .sendTime(System.currentTimeMillis())
                    .createTime(System.currentTimeMillis())
                    .build();

            // 保存铁路理货报文回执数据入库
            responseMapper.insert(entity);
        } else {
            BRailwayTallyDelResponse delResponse = BRailwayTallyDelResponse.builder()
                    .id(headRspXml.get("headUUID"))
                    .messageId(headRspXml.get("messageId"))
                    .declMessageId(headRspXml.get("declMsgId"))
                    .messageType(headRspXml.get("messageType"))
                    .sendId(headRspXml.get("sendId"))
                    .chkFlag(headRspXml.get("chkFlag"))
                    .notes(headRspXml.get("notes"))
                    .sendTime(System.currentTimeMillis())
                    .createTime(System.currentTimeMillis())
                    .build();

            // 保存铁路理货报文报废回执的数据入库
            delResponseMapper.insert(delResponse);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveResponseBodyXml(List<RspBillInfo> rspBillInfoList, String functinoCode, String headUUID) {
        // TODO 保存海关回执的表体信息
        if (CollectionUtils.isNotEmpty(rspBillInfoList)) {
            // 2新增，3删除
            if (Constants.FIX_NUM_2.equals(functinoCode)) {
                rspBillInfoList.forEach(billInfo -> {
                    BRailwayTallyResponseBill responseBill = BRailwayTallyResponseBill.builder()
                            .id(UniqueIdUtil.getUUID())
                            .railwayTallyResponseId(headUUID)
                            .billNo(billInfo.getBillNo())
                            .chkFlag(billInfo.getChkFlag())
                            .notes(billInfo.getNotes())
                            .createTime(System.currentTimeMillis())
                            .build();

                    responseBillMapper.insert(responseBill);
                });
            } else {
                rspBillInfoList.forEach(billInfo -> {
                    BRailwayTallyDelResponseBill delResponseBill = BRailwayTallyDelResponseBill.builder()
                            .id(UniqueIdUtil.getUUID())
                            .railwayTallyResponseId(headUUID)
                            .billNo(billInfo.getBillNo())
                            .chkFlag(billInfo.getChkFlag())
                            .notes(billInfo.getNotes())
                            .createTime(System.currentTimeMillis())
                            .build();

                    delResponseBillMapper.insert(delResponseBill);
                });
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveExResponseBodyXml(List<RspArriveInfo> rspArriveInfoList, String functinoCode, String headUUID) {
        // TODO 保存海关回执的表体信息
        if (CollectionUtils.isNotEmpty(rspArriveInfoList)) {
            // 2新增，3删除
            if (Constants.FIX_NUM_2.equals(functinoCode)) {
                rspArriveInfoList.forEach(billInfo -> {
                    BRailwayTallyResponseBill responseBill = BRailwayTallyResponseBill.builder()
                            .id(UniqueIdUtil.getUUID())
                            .railwayTallyResponseId(headUUID)
                            .billNo(billInfo.getArriveNo())// TODO 此运抵编号字段插入到了提单号字段中
                            .chkFlag(billInfo.getChkFlag())
                            .notes(billInfo.getNotes())
                            .createTime(System.currentTimeMillis())
                            .build();

                    responseBillMapper.insert(responseBill);
                });
            } else {
                rspArriveInfoList.forEach(billInfo -> {
                    BRailwayTallyDelResponseBill delResponseBill = BRailwayTallyDelResponseBill.builder()
                            .id(UniqueIdUtil.getUUID())
                            .railwayTallyResponseId(headUUID)
                            .billNo(billInfo.getArriveNo())// TODO 此运抵编号字段插入到了提单号字段中
                            .chkFlag(billInfo.getChkFlag())
                            .notes(billInfo.getNotes())
                            .createTime(System.currentTimeMillis())
                            .build();

                    delResponseBillMapper.insert(delResponseBill);
                });
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRailResponseBodyXml(List<RspRailWayBillInfo> rspRailWayBillInfoList, String functinoCode, String headUUID) {
        // TODO 保存海关回执的表体信息
        if (CollectionUtils.isNotEmpty(rspRailWayBillInfoList)) {
            // 2新增，3删除
            if (Constants.FIX_NUM_2.equals(functinoCode)) {
                rspRailWayBillInfoList.forEach(billInfo -> {
                    BRailwayTallyResponseBill responseBill = BRailwayTallyResponseBill.builder()
                            .id(UniqueIdUtil.getUUID())
                            .railwayTallyResponseId(headUUID)
                            .billNo(billInfo.getBillNo())
                            .chkFlag(billInfo.getChkFlag())
                            .notes(billInfo.getNotes())
                            .createTime(System.currentTimeMillis())
                            .build();

                    responseBillMapper.insert(responseBill);
                });
            } else {
                rspRailWayBillInfoList.forEach(billInfo -> {
                    BRailwayTallyDelResponseBill delResponseBill = BRailwayTallyDelResponseBill.builder()
                            .id(UniqueIdUtil.getUUID())
                            .railwayTallyResponseId(headUUID)
                            .billNo(billInfo.getBillNo())
                            .chkFlag(billInfo.getChkFlag())
                            .notes(billInfo.getNotes())
                            .createTime(System.currentTimeMillis())
                            .build();

                    delResponseBillMapper.insert(delResponseBill);
                });
            }
        }
    }
}
