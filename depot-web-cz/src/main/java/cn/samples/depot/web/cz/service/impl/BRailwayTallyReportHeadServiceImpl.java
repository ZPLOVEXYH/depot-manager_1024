/**
 * @filename:BRailwayTallyReportHeadServiceImpl 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.samples.depot.common.constant.Constants;
import cn.samples.depot.common.constant.XmlTypeConstant;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.*;
import cn.samples.depot.common.utils.*;
import cn.samples.depot.web.bean.report.*;
import cn.samples.depot.web.cz.mapper.*;
import cn.samples.depot.web.cz.service.BRailwayTallyBillInfoService;
import cn.samples.depot.web.cz.service.BRailwayTallyReportHeadService;
import cn.samples.depot.web.cz.service.BaseService;
import cn.samples.depot.web.cz.service.CStationsService;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.entity.xml.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.samples.depot.common.model.DeclareStatusFlag.DECLARE_PASS;

/**
 * @Description: 铁路进口理货申请报文表头——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Service
@Slf4j
public class BRailwayTallyReportHeadServiceImpl extends ServiceImpl<BRailwayTallyReportHeadMapper, BRailwayTallyReportHead> implements BRailwayTallyReportHeadService {

    @Autowired
    BRailwayTallyBillInfoMapper billInfoMapper;

    /**
     * 海关回执报文表头
     */
    @Autowired
    BRailwayTallyResponseMapper responseMapper;

    /**
     * 基础实现类
     */
    @Autowired
    BaseService baseService;

    /**
     * 基础实现类
     */
    @Autowired
    CStationsService stationsService;

    /**
     * 海关回执报文表体
     */
    @Autowired
    BRailwayTallyResponseBillMapper responseBillMapper;

    /**
     * 海关申请报废报文回执表体
     */
    @Autowired
    BRailwayTallyDelReportHeadMapper delReportHeadMapper;

    /**
     * 海关申请报废报文回执表体
     */
    @Autowired
    BRailwayTallyDelResponseMapper delResponseMapper;

    /**
     * 海关申请报废报文回执表体
     */
    @Autowired
    BRailwayTallyDelResponseBillMapper delResponseBillMapper;

    /**
     * 海关申请报废报文表体
     */
    @Autowired
    BRailwayTallyDelBillInfoMapper delBillInfoMapper;

    /**
     * 场站表
     */
    @Autowired
    CStationsMapper stationsMapper;

    /**
     * 海关代码表
     */
    @Autowired
    PCustomsCodeMapper customsCodeMapper;

    /**
     * 堆位表
     */
    @Autowired
    CStationAreaPositionsMapper cStationAreaPositionsMapper;

    @Autowired
    CDischargesMapper cDischargesMapper;

    @Value("${message.file.path:1.0}")
    private String MESSAGE_FILE_PATH;

    /**
     * 理货报告列表多表关联分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Page<RspBRailwayTallyReport> selectReportListPage(BRailwayTallyReportQuery query, int pageNum, int pageSize) {
        // 当前页，总条数 构造 page 对象
        Page<RspBRailwayTallyReport> page = new Page<>(pageNum, pageSize);
        List<RspBRailwayTallyReport> list = this.baseMapper.selectReportListPage(page, query);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(report -> {
                String customsCode = report.getCustomsCode();
                // 根据code获取得到name
                PCustomsCode pCustomsCode = customsCodeMapper.selectOne(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, customsCode).eq(PCustomsCode::getEnable, Status.ENABLED.getValue()));
                if (null != pCustomsCode) {
                    // 获取得到海关中文名称
                    String customsName = pCustomsCode.getName();
                    report.setCustomsName(customsName);
                }

//                String unloadingPlace = report.getUnloadingPlace();
//                CDischarges unloadingObj = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, unloadingPlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
//                if(null != unloadingObj){
//                    report.setUnloadingPlaceName(unloadingObj.getName());
//                }
//
//                String loadingPlace = report.getLoadingPlace();
//                CDischarges loadingObj = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, loadingPlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
//                if(null != loadingObj){
//                    report.setLoadingPlaceName(loadingObj.getName());
//                }

                String ieFlag = report.getIEFlag();
                // 卸货地中文名称
                String unloadingPlaceName;
                // 装货地中文名称
                String loadingPlaceName;
                if (org.apache.commons.lang3.StringUtils.isNotBlank(ieFlag)) {
                    // 进口
                    if (IEFlag.IMPORT.getValue().equals(ieFlag)) {
                        // 卸货地代码
                        String unloadingPlace = report.getUnloadingPlace();
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(unloadingPlace)) {
                            // 根据code获取得到name
                            CDischarges cDischarges = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, unloadingPlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
                            if (null != cDischarges) {
                                unloadingPlaceName = cDischarges.getName();
                                report.setUnloadingPlaceName(unloadingPlaceName);
                            }
                        }

                        report.setRspMessageType(MessageTypeFlag.WLJK_IRTR.getValue());
                    } else {
                        // 出货地代码
                        String loadingPlace = report.getLoadingPlace();
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(loadingPlace)) {
                            // 根据code获取得到name
                            CDischarges cDischarges = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, loadingPlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
                            if (null != cDischarges) {
                                loadingPlaceName = cDischarges.getName();
                                report.setLoadingPlaceName(loadingPlaceName);
                            }
                        }
                        report.setRspMessageType(MessageTypeFlag.WLJK_ERTR.getValue());
                    }
                }
            });
        }
        return page.setRecords(list);
    }

    /**
     * 铁路进出口理货申报
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult declare(String id) {
        // 根据理货表头id查询理货表头信息
        BRailwayTallyReportHead bRailwayTallyReportHead = this.baseMapper.selectById(id);
        if (null != bRailwayTallyReportHead) {
            // 保存的xml文件名称
            String xmlFileName = Constants.XML_PREFIX;
            // 根据表头id查询得到理货表体信息
            List<BRailwayTallyBillInfo> bodyList = billInfoMapper.selectList(Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, bRailwayTallyReportHead.getId()));
            List<RailWayBillInfos> billInfos = new ArrayList<>();
            // 循环遍历铁路进出口申请标体内容
            bodyList.forEach(x -> {
                List<SealID> sealIDs = new ArrayList<>();
                String sealNo = x.getSealNo();
                if (StringUtils.isNotEmpty(sealNo)) {
                    String[] strs = sealNo.split(Constants.COMMA);
                    Arrays.stream(strs).forEach(y -> {
                        SealID sealID = new SealID();
                        if (y.indexOf(Constants.SPRIT) > 0) {
                            String agencyCode = y.substring(y.lastIndexOf(Constants.SPRIT) + 1, y.length());
                            String text = y.substring(0, y.lastIndexOf(Constants.SPRIT));

                            sealID.setAgencyCode(agencyCode);
                            sealID.setText(text);
                        } else {
                            sealID.setAgencyCode(y);
                        }

                        sealIDs.add(sealID);
                    });
                }

                // 铁路运单信息集合信息
                RailWayBillInfos railWayBilInfos = RailWayBillInfos.builder()
                        .billNo(x.getBillNo())// 运单号
                        .carriageNo(x.getCarriageNo())// 车皮号
                        .packNo(null != x.getPackNo() ? String.valueOf(x.getPackNo()) : null)// 件数
                        .grossWt(null != x.getGrossWt() ? String.valueOf(x.getGrossWt()) : null)// 毛重（公斤）
                        .contaNo(x.getContaNo())// 集装箱号
                        .contaType(x.getContaType())// 集装箱箱型
                        .sealIDs(sealIDs)// 封志号码,类型和施加封志人
                        .notes(x.getNotes())// 备注
                        .build();

                billInfos.add(railWayBilInfos);

            });

            // 铁路进出口理货报告申请表体信息
            MessageList messageList = new MessageList();
            messageList.setBillList(billInfos);
            messageList.setDeclPort(bRailwayTallyReportHead.getCustomsCode());
            // 获取得到当前用户信息
            String currentStations = "";
            // 查询获取得到数据库场站信息
            CStations cStations = stationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getEnable, Status.ENABLED.getValue()));
            if (null != cStations) {
                // 当前场站code
                currentStations = cStations.getCode();
            }
            // 进出口标记（I 进口,E 出口）
            String ieFlag = bRailwayTallyReportHead.getIEFlag();
            // 发送者
            String sender = bRailwayTallyReportHead.getCustomsCode() + currentStations;
            if ("I".equals(ieFlag)) {
                // 卸货地代码
                messageList.setUnloadingPlace(bRailwayTallyReportHead.getUnloadingPlace());
                xmlFileName += XmlTypeConstant.WLJK_IRTA
                        + Constants.XML_BODY
                        + Constants.UNDER_LINE
                        + sender
                        + Constants.UNDER_LINE
                        + DateUtils.getFullTimeStamp()
                        + Constants.XML_SUFFIX;
            } else {
                // 装货地代码
                messageList.setLoadingPlace(bRailwayTallyReportHead.getLoadingPlace());
                xmlFileName += XmlTypeConstant.WLJK_ERTA
                        + Constants.XML_BODY
                        + Constants.UNDER_LINE
                        + sender
                        + Constants.UNDER_LINE
                        + DateUtils.getFullTimeStamp()
                        + Constants.XML_SUFFIX;
            }

            // 装卸开始时间
            messageList.setActualDateTime(String.valueOf(null != bRailwayTallyReportHead.getActualDatetime() ? DateUtils.longToString(bRailwayTallyReportHead.getActualDatetime()) : DateUtils.longToString(System.currentTimeMillis())));
            // 装卸结束时间
            messageList.setCompletedDateTime(String.valueOf(null != bRailwayTallyReportHead.getCompletedDatetime() ? DateUtils.longToString(bRailwayTallyReportHead.getCompletedDatetime()) : DateUtils.longToString(System.currentTimeMillis())));
//            messageList.setCompletedDateTime(String.valueOf(bRailwayTallyReportHead.getCompletedDatetime()));

            // 铁路进出口理货报告申请表头信息
            MessageHead messageHead = MessageHead.builder().build();
            // 报文编号
            messageHead.setMessageId(bRailwayTallyReportHead.getMessageId());
            // 功能代码（2新增，3删除）
            messageHead.setFunctionCode(FunctionCodeFlag.FUNCTION_CODE_2.getValue());
            // 消息类型（进口WLJK_IRTA，出口WLJK_ERTA）
            messageHead.setMessageType(bRailwayTallyReportHead.getMessageType());
            // 消息创建时间yyyyMMddHHmmssfff
            messageHead.setAuditTime(DateUtils.getFullTimeStamp());
            // 监管场所或理货公司(4位海关代码+9位组织结构代码)
            messageHead.setSender(sender);
            // 接收者（海关代码）
            messageHead.setReceiver(bRailwayTallyReportHead.getCustomsCode());
            // 版本号（默认1.0）
            messageHead.setVersion(Constants.MESSAGE_VERSION);

            // 消息root
            Message message = new Message();
            message.setMessageHead(Arrays.asList(messageHead));
            message.setMessageList(Arrays.asList(messageList));

            String xmlStr = XstreamUtil.xmlAppendHead(XmlUtil.serializeToStr(message, CharsetUtil.UTF_8));
            log.info("文件xml格式：{}", xmlStr);

            boolean createFile = FileUtils.writeContent(MESSAGE_FILE_PATH, xmlFileName, xmlStr);
            log.info("文件写入成功：{}", createFile);

            // 根据id申请报文表头id来更新申报时间和审核状态字段
            if (createFile) {
                BRailwayTallyReportHead head = BRailwayTallyReportHead.builder()
                        .id(id)// 申请报文表头id
                        .auditTime(System.currentTimeMillis())// 申报时间
                        .auditStatus(DeclareStatusFlag.PRO_DECLARE.getValue())// 申报状态:申报海关
                        .build();
                this.baseMapper.updateById(head);

                BRailwayTallyBillInfo billInfo = BRailwayTallyBillInfo.builder()
                        .railwayTallyReportHeadId(id)
                        .auditStatus(WayBillAuditStatus.WayBillAudit_02.getValue())// 申报状态:申报海关
                        .build();

                billInfoMapper.update(billInfo, Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, id));
            } else {
                log.error("文件写入失败：{}", createFile);
                return JsonResult.error("9998", "铁路进出口理货申报，文件写入失败");
            }

            return JsonResult.data(createFile);
        }

        return JsonResult.error("9998", "铁路进出口理货申报失败");
    }

    /**
     * 多个属性拼接出一个组合属性（运单号+集装箱号）
     *
     * @param save
     * @return
     */
    private static String fetchGroupKey(BRailwayTallyBillInfoSave save) {
        // 运单号+集装箱号拼接
        return save.getBillNo() + save.getContaNo();
    }

    /**
     * 处理海关响应的报文回执
     *
     * @param rspMessage
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult xmlReturnReceiptHandle(RspMessage rspMessage) {
        if (null != rspMessage) {
            RspMessageList rspMessageList = rspMessage.getMessageList();
            // 获取海关返回的消息报文头信息
            MessageHead rspMessageHead = rspMessage.getMessageHead();
            // 申报的报文编号
            String declMsgId = "";
            // 回执类型代码 01 审核通过 03 退单
            String chkFlag = "";
            // 回执描述
            String notes = "";

            // 获取得到功能代码 2新增 3删除
            String functinoCode = "";
            String messageId = "";
            String messageType = "";
            String sendId = "";
            if (null != rspMessageHead) {
                functinoCode = rspMessageHead.getFunctionCode();
                // 回执报文编号
                messageId = rspMessageHead.getMessageId();
                messageType = rspMessageHead.getMessageType();
                sendId = rspMessageHead.getSender();
            }

            if (null != rspMessageList) {
                // 生成报文表头的id
                String headUUID = UniqueIdUtil.getUUID();
                // 响应的表体信息
                List<RspRailWayBillInfo> billInfoList = rspMessageList.getRspRailWayBillInfo();
                // TODO 保存海关回执的表体信息
                baseService.saveRailResponseBodyXml(billInfoList, functinoCode, headUUID);

                ResponseInfo responseInfo = rspMessageList.getResponseInfo();
                if (null != responseInfo) {
                    // 01 审核通过 03 退单
                    chkFlag = responseInfo.getChkFlag();
                    declMsgId = responseInfo.getMsgId();
                    notes = responseInfo.getNotes();
                    // 回执状态判断：审核通过或者审核不通过
                    String auditStatus = Constants.FIX_NUM_01.equals(chkFlag) ? DECLARE_PASS.getValue() : DeclareStatusFlag.DECLARE_NO_PASS.getValue();
                    // 2新增，3删除
                    if (Constants.FIX_NUM_2.equals(functinoCode)) {
                        BRailwayTallyReportHead head = BRailwayTallyReportHead.builder()
                                // 申报状态：01待申报、02申报海关、03审核通过、04审核不通过
                                .auditStatus(auditStatus)
                                .build();

                        // 根据消息id更新申报状态
                        this.baseMapper.update(head, Wrappers.<BRailwayTallyReportHead>lambdaQuery().eq(BRailwayTallyReportHead::getMessageId, declMsgId));

                        BRailwayTallyReportHead reportHead = this.baseMapper.selectOne(Wrappers.<BRailwayTallyReportHead>lambdaQuery().eq(BRailwayTallyReportHead::getMessageId, declMsgId));
                        if (null != reportHead) {
                            // 理货报告运单的审核状态（01待作废/02退单/03理货审核通过/04作废中/05作废通过）
                            String billAuditStatus = Constants.FIX_NUM_01.equals(chkFlag) ? WayBillAuditStatus.WayBillAudit_03.getValue() : WayBillAuditStatus.WayBillAudit_04.getValue();
                            BRailwayTallyBillInfo billInfo = BRailwayTallyBillInfo.builder()
                                    //（01待作废/02退单/03理货审核通过/04作废中/05作废通过）
                                    .auditStatus(billAuditStatus)
                                    .build();

                            // 根据提单号更新申报状态
                            billInfoMapper.update(billInfo, Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, reportHead.getId()));
                        }
                    } else {
                        BRailwayTallyDelReportHead reportHead = BRailwayTallyDelReportHead.builder()
                                // 申报状态：01待申报、02申报海关、03审核通过、04审核不通过
                                .auditStatus(auditStatus)
                                .build();

                        delReportHeadMapper.update(reportHead, Wrappers.<BRailwayTallyDelReportHead>lambdaQuery().eq(BRailwayTallyDelReportHead::getMessageId, declMsgId));

                        // 更新理货作废报文表体的审核状态
                        String delStatus = Constants.FIX_NUM_01.equals(chkFlag) ? WayBillAuditStatus.WayBillAudit_08.getValue() : WayBillAuditStatus.WayBillAudit_06.getValue();
                        BRailwayTallyDelBillInfo delBillInfo = BRailwayTallyDelBillInfo.builder()
                                .auditStatus(delStatus)
                                .build();

                        BRailwayTallyDelReportHead delReportHead = delReportHeadMapper.selectOne(Wrappers.<BRailwayTallyDelReportHead>lambdaQuery().eq(BRailwayTallyDelReportHead::getMessageId, declMsgId));
                        if (delReportHead != null) {
                            delBillInfoMapper.update(delBillInfo,
                                    Wrappers.<BRailwayTallyDelBillInfo>lambdaQuery()
                                            .eq(BRailwayTallyDelBillInfo::getRailwayTallyReportHeadId, delReportHead.getId()));
                        }
                        // 如果是作废通过
                        int billCount = 0;
                        String id = "";
                        if (StringUtils.isNotEmpty(declMsgId)) {
                            BRailwayTallyDelReportHead delReport = delReportHeadMapper.selectOne(Wrappers.<BRailwayTallyDelReportHead>lambdaQuery().eq(BRailwayTallyDelReportHead::getMessageId, declMsgId));
                            if (null != delReport) {
                                BRailwayTallyReportHead head = this.baseMapper.selectOne(Wrappers.<BRailwayTallyReportHead>lambdaQuery().eq(BRailwayTallyReportHead::getMessageId, delReport.getMessageIdAdd()));
                                if (null != head) {
                                    id = head.getId();
                                    billCount = billInfoMapper.selectCount(Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, id));
                                }
                            }
                        }
                        if (Constants.FIX_NUM_01.equals(chkFlag)) {
                            if (billInfoList.size() == billCount && StringUtils.isNotEmpty(id)) {
                                // 如果理货报告申请的所有运单号全都作废了，那么将之前的理货报告申请里面的运单号全都删除
                                this.baseMapper.deleteById(id);

                                // 审核作废通过，那么将申报的所有表体信息删除
                                billInfoMapper.delete(Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, id));
                            } else {
                                // 部分删除申请表表体数据
                                billInfoList.forEach(billInfo -> {
                                    // 运单号
                                    String billNo = billInfo.getBillNo();

                                    // TODO 根据运单号来删除申请表体信息
                                    billInfoMapper.delete(Wrappers.<BRailwayTallyBillInfo>lambdaQuery()
                                            .eq(BRailwayTallyBillInfo::getBillNo, billNo));
                                });
                            }
                        } else {
                            // 如果是作废退单，那么修改申报状态为审核通过
                            BRailwayTallyReportHead head = BRailwayTallyReportHead.builder()
                                    .auditStatus(WayBillAuditStatus.WayBillAudit_03.getValue())
                                    .build();

                            this.baseMapper.update(head, Wrappers.<BRailwayTallyReportHead>lambdaQuery().eq(BRailwayTallyReportHead::getMessageId, declMsgId));

                            BRailwayTallyBillInfo billInfo = BRailwayTallyBillInfo.builder()
                                    .auditStatus(WayBillAuditStatus.WayBillAudit_03.getValue())
                                    .build();

                            billInfoMapper.update(billInfo, Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, id));
                        }
                    }

                    // TODO 保存到回执报文表头表中
                    Map<String, String> headRspXml = new HashMap<>();
                    headRspXml.put("headUUID", headUUID);
                    headRspXml.put("messageId", messageId);
                    headRspXml.put("declMsgId", declMsgId);
                    headRspXml.put("messageType", messageType);
                    headRspXml.put("sendId", sendId);
                    headRspXml.put("chkFlag", chkFlag);
                    headRspXml.put("notes", notes);
                    headRspXml.put("functinoCode", functinoCode);

                    baseService.saveResponseHeadXml(headRspXml);
                }
            }

            return JsonResult.success();
        }

        return JsonResult.error("解析得到的理货回执报文为空");
    }

    /**
     * 保存理货报告信息
     *
     * @param headSave
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult saveReportInfo(BRailwayTallyReportHeadSave headSave) {
        // 保存前验证参数的有效性
        JsonResult jsonResult = verifyParamValid(headSave);
        if (!"0000".equals(jsonResult.getCode())) {
            // 校验不通过就直接返回给前端错误信息
            return jsonResult;
        }

        if (null != headSave) {
            String ieFlag = headSave.getIEFlag();
            if ("I".equals(ieFlag)) {
                headSave.setMessageType(MessageTypeFlag.WLJK_IRTA.getValue());
            } else {
                headSave.setMessageType(MessageTypeFlag.WLJK_ERTA.getValue());
            }

            // 获取得到4位海关代码
            String customsCode = headSave.getCustomsCode();
            CStations cStations = stationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getEnable, Status.ENABLED.getValue()));
            // 获取场站信息（也就是企业组织结构代码）
            if (null != cStations) {
                // 场站编码
                String code = cStations.getCode();
                // 自动生成消息id
                String messageId = UniqueIdUtil.getMsgId(customsCode, code);
                headSave.setMessageId(messageId);
            }
            String headId = UniqueIdUtil.getUUID();
            headSave.setId(headId);
            BRailwayTallyReportHead reportHead = new BRailwayTallyReportHead();
            BeanUtils.copyProperties(headSave, reportHead);
            // 保存理货申请表头信息
            reportHead.setAuditStatus(DeclareStatusFlag.PRE_DECLARE.getValue());
            this.baseMapper.insert(reportHead);

            // 获取得到集装箱
            List<BRailwayTallyBillInfoSave> billInfoSaves = headSave.getBillInfoSaveList();
            billInfoSaves.forEach(bill -> {
                bill.setId(UniqueIdUtil.getUUID());

                BRailwayTallyBillInfo billInfo = new BRailwayTallyBillInfo();
                BeanUtils.copyProperties(bill, billInfo);
                billInfo.setRailwayTallyReportHeadId(headId);
                // 待申报
                billInfo.setAuditStatus(WayBillAuditStatus.WayBillAudit_01.getValue());
                // 保存理货申请表体信息（集装箱运单信息）
                billInfoMapper.insert(billInfo);
            });

            return JsonResult.data(headId);
        }

        return JsonResult.success();
    }

    /**
     * 保存前验证请求参数的有效性
     *
     * @param headSave
     * @return
     */
    public JsonResult verifyParamValid(BRailwayTallyReportHeadSave headSave) {
        if (null != headSave) {
            // 进出口类型（I进、E出）
            String ieFlag = headSave.getIEFlag();
            if (IEFlag.IMPORT.getValue().equals(ieFlag)) {
                // 卸货地代码
                String unloadingPlace = headSave.getUnloadingPlace();
                if (StringUtils.isEmpty(unloadingPlace)) {
                    JsonResult.error("9998", "卸货地代码不能为空");
                }
            } else {
                // 装货地代码
                String loadingPlace = headSave.getLoadingPlace();
                if (StringUtils.isEmpty(loadingPlace)) {
                    JsonResult.error("9998", "装货地代码不能为空");
                }
            }

            // 获取得到理货申请添加表体集合
            List<BRailwayTallyBillInfoSave> billInfoList = headSave.getBillInfoSaveList();
            if (CollectionUtil.isNotEmpty(billInfoList)) {
                billInfoList.forEach(bill -> {
                    // 校验运单号不能在全库中重复
                    int billCount = billInfoMapper.selectCount(Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getBillNo, bill.getBillNo()));
                    if (billCount > 0) {
                        JsonResult.error("9998", "校验运单号不能在全库中重复");
                    }

                    if (IEFlag.EXPORT.getValue().equals(ieFlag)) {
                        // 获取得到集装箱号
                        String contaNo = bill.getContaNo();
                        // 获取得到集装箱型号
                        String contaType = bill.getContaType();
                        // 根据集装箱编号查询堆位表
                        int count = cStationAreaPositionsMapper.selectCount(Wrappers.<CStationAreaPositions>lambdaQuery().eq(CStationAreaPositions::getContaNo, contaNo).eq(CStationAreaPositions::getContaType, contaType));
                        if (count == 0) {
                            JsonResult.error("9998", "集装箱号" + contaNo + "，箱型" + contaType + "在堆存查询中检索不到");
                        }
                    }
                });

                // 校验同一个集装箱是否存在多个运单号
                Map<String, List<BRailwayTallyBillInfoSave>> maps = billInfoList.stream().collect(Collectors.groupingBy(e -> fetchGroupKey(e)));
                maps.values().stream().forEach(x -> {
                    int contaSize = x.size();
                    if (contaSize > 1) {
                        JsonResult.error("9998", "集装箱号不能重复");
                    }

                    // 校验同一个集装箱， 封志号不能重复
                    x.forEach(y -> {
                        // 封志号
                        String sealNo = y.getSealNo();
                        // 如果封志号有多个
                        if (sealNo.contains(Constants.COMMA)) {
                            String[] sealNos = sealNo.split(Constants.COMMA);
                            Map<String, List<String>> mapStrs = Arrays.stream(sealNos).collect(Collectors.groupingBy(e -> e));
                            mapStrs.keySet().stream().forEach(s -> {
                                List<String> stringList = mapStrs.get(s);
                                if (stringList.size() > 1) {
                                    JsonResult.error("9998", "该集装箱号码" + y.getContaNo() + "，封志号不能重复");
                                }
                            });
                        }
                    });
                });
            }
        }

        return JsonResult.success();
    }

    @Autowired
    BRailwayTallyBillInfoService billInfoService;

    /**
     * 单个删除铁路进口理货申请报文表头
     *
     * @param id
     * @return
     */
    @Override
    public JsonResult removeReportById(String id) {
        // 根据id查询得到理货申请信息
        BRailwayTallyReportHead head = this.baseMapper.selectById(id);
        if (null != head) {
            // 获取得到审核状态类型
            String auditStatus = head.getAuditStatus();
            // 如果审核状态：待申报、审核不通过的数据
            if (!DeclareStatusFlag.PRE_DECLARE.getValue().equals(auditStatus)
                    && !DeclareStatusFlag.DECLARE_NO_PASS.getValue().equals(auditStatus)) {
                log.error("只能删除待申报和审核不通过状态的数据:{}", id);
                return JsonResult.error("只能删除待申报和审核不通过状态的数据", id);
            } else {
                int deleteHead = this.baseMapper.deleteById(id);
                log.info("删除铁路进口理货申请报文表头：{}", deleteHead);
                if (deleteHead > 0) {
                    int deleteBill = billInfoMapper.delete(Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, id));
                    log.info("删除铁路进口理货申请报文表体：{}", deleteBill);
                    if (0 == deleteBill) {
                        return JsonResult.error("删除失败，此id号：{}，在表体中不存在", id);
                    }
                } else {
                    return JsonResult.error("删除失败，此id号：{}，在表头中不存在", id);
                }
            }
        } else {
            return JsonResult.error("删除失败，此id号：{}，在表头中不存在", id);
        }

        return JsonResult.success();
    }

    /**
     * 根据消息id查询得到对应理货报文编号
     *
     * @param declMessageId
     * @return
     */
    @Override
    public BRailwayTallyReportHeadSave queryByMsgId(String declMessageId) {
        // 根据消息id查询得到对应理货报文编号
        BRailwayTallyReportHead head = this.baseMapper.selectOne(Wrappers.<BRailwayTallyReportHead>lambdaQuery().eq(BRailwayTallyReportHead::getMessageId, declMessageId));
        if (null != head) {
            // 获取得到表头的id
            String id = head.getId();
            // 根据申报表头的id查询得到表体的集合信息
            // TODO 如果是从理货报告中查看明细，那么不需要过滤条件，如果是从理货作废中选择理货报告信息，那么就需要带上审核通过的条件
            Map<String, Object> maps = MapUtil.buildMap("railway_tally_report_head_id", id, "audit_status", DeclareStatusFlag.DECLARE_PASS.getValue());
            List<BRailwayTallyBillInfo> billInfoList = billInfoMapper.selectByMap(maps);
            BRailwayTallyReportHeadSave headSave = BRailwayTallyReportHeadSave.builder().build();
            BeanUtils.copyProperties(head, headSave);

            List<BRailwayTallyBillInfoSave> saveList = new ArrayList<>();
            billInfoList.forEach(bill -> {
                BRailwayTallyBillInfoSave saves = BRailwayTallyBillInfoSave.builder().build();
                BeanUtils.copyProperties(bill, saves, "notes");
                saveList.add(saves);
            });

            headSave.setBillInfoSaveList(saveList);

            return headSave;
        }

        return null;
    }

    /**
     * 根据理货申请id获取得到理货申请明细
     *
     * @param id
     * @return
     */
    @Override
    public BRailwayTallyReportHeadSave queryDetailById(String id) {
        // 根据消息id查询得到对应理货报文编号
        BRailwayTallyReportHead head = this.baseMapper.selectOne(Wrappers.<BRailwayTallyReportHead>lambdaQuery().eq(BRailwayTallyReportHead::getId, id));
        if (null != head) {
            // 根据申报表头的id查询得到表体的集合信息
            // TODO 如果是从理货报告中查看明细，那么不需要过滤条件，如果是从理货作废中选择理货报告信息，那么就需要带上审核通过的条件
            Map<String, Object> maps = MapUtil.buildMap("railway_tally_report_head_id", id);
            List<BRailwayTallyBillInfo> billInfoList = billInfoMapper.selectByMap(maps);
            BRailwayTallyReportHeadSave headSave = BRailwayTallyReportHeadSave.builder().build();
            BeanUtils.copyProperties(head, headSave);

            List<BRailwayTallyBillInfoSave> saveList = new ArrayList<>();
            billInfoList.forEach(bill -> {
                BRailwayTallyBillInfoSave saves = BRailwayTallyBillInfoSave.builder().build();
                BeanUtils.copyProperties(bill, saves);
                saveList.add(saves);
            });

            headSave.setBillInfoSaveList(saveList);

            return headSave;
        }

        return null;
    }

    /**
     * @Author zhangpeng
     * @Date 2019/9/2
     * @Description 根据报文编号获取实体
     **/
    @Override
    public BRailwayTallyReportHead getByMessageId(String messageId) {
        return getOne(new LambdaQueryWrapper<BRailwayTallyReportHead>().eq(BRailwayTallyReportHead::getMessageId, messageId));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 编辑
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, TallyReportVo vo) throws BizException {
        deleteTallyReportById(id);
        saveVo(vo);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 保存   1.同一装车记录单，集装箱号不能重复 2.同一个集装箱，可以有多个封志号，封志号不能重复 3.同一集装箱的运抵编号不能重复
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVo(TallyReportVo vo) throws BizException {
        //表头
        BRailwayTallyReportHead head = vo.getHead();
        saveBefore(head);
        super.save(head);
        //标题
        List<BRailwayTallyBillInfo> contaVos = vo.getBillInfoList();
        if (CollectionUtils.isEmpty(contaVos)) throw new BizException("集装箱不能为空");
        List<BRailwayTallyBillInfo> contas = new ArrayList<>();//集装箱
        Set<String> contaNos = new HashSet<>();
        Set<String> billNos = new HashSet<>();
        for (BRailwayTallyBillInfo conta : contaVos) {
            //集装箱
            if (!contaNos.add(conta.getContaNo()))
                throw new BizException(String.format("集装箱号[%s]重复", conta.getContaNo()));
            if (!billNos.add(conta.getBillNo()))
                throw new BizException(String.format("运单号[%s}重复", conta.getBillNo()));
            if (StringUtils.isEmpty(conta.getId()))
                conta.setId(UniqueIdUtil.getUUID());

            billInfoService.saveBefore(head, conta);
            contas.add(conta);
        }
        billInfoService.saveBatch(contas);

    }

    private void saveBefore(BRailwayTallyReportHead head) throws BizException {
        checkEmpty(head);
        if (StringUtils.isEmpty(head.getMessageId()))
            head.setMessageId(UniqueIdUtil.getMsgId(head.getCustomsCode(), stationsService.getCode()));
        if (null == head.getCreateTime() || head.getCreateTime() <= 0) head.setCreateTime(System.currentTimeMillis());
        if (StringUtils.isEmpty(head.getAuditStatus())) head.setAuditStatus(DeclareStatusFlag.PRE_DECLARE.getValue());
        if (StringUtils.isEmpty(head.getMessageType())) head.setMessageType(MessageTypeFlag.WLJK_ERTA.getValue());
    }

    private void checkEmpty(BRailwayTallyReportHead head) throws BizException {
        if (null == head) throw new BizException("海关代码不能为空");
        if (StringUtils.isEmpty(head.getCustomsCode())) throw new BizException("海关代码不能为空");
        String ieFlag = head.getIEFlag();
        if (StringUtil.isNotEmpty(ieFlag) && "I".equals(ieFlag)) {
            if (StringUtils.isEmpty(head.getUnloadingPlace())) throw new BizException("卸货地代码不能为空");
        } else if ("E".equals(ieFlag)) {
            if (StringUtils.isEmpty(head.getLoadingPlace())) throw new BizException("装货地代码不能为空");
        }
    }

    /**
     * 单个删除装车记录单申请报文
     *
     * @param id
     * @return
     */
    @Override
    public void deleteTallyReportById(String id) throws BizException {
        // 根据id查询得到理货作废信息
        BRailwayTallyReportHead head = checkUpdate(id);

        int deleteHead = this.baseMapper.deleteById(id);
        log.info("删除装车记录单申请报文表头：{}", deleteHead);

        int deleteLoadConta = billInfoMapper.delete(Wrappers.<BRailwayTallyBillInfo>lambdaQuery().eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, id));
        log.info("删除装车记录单申请报文表体集装箱信息：{}", deleteLoadConta);
    }

    private BRailwayTallyReportHead checkUpdate(String id) throws BizException {
        BRailwayTallyReportHead head = getById(id);
        if (null == head) throw new BizException("找不到对应装车记录");
        if (DeclareStatusFlag.PRE_DECLARE.getValue().equals(head.getAuditStatus()) || DeclareStatusFlag.DECLARE_NO_PASS.getValue().equals(head.getAuditStatus()))
            return head;
        throw new BizException(String.format("仅[%s,%s]状态可编辑", DeclareStatusFlag.PRE_DECLARE.getTitle(), DeclareStatusFlag.DECLARE_NO_PASS.getTitle()));

    }
}