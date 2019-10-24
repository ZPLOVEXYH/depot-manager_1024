/**
 * @filename:BRailwayTallyDelReportHeadServiceImpl 2019年08月12日
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
import cn.samples.depot.web.bean.report.BRailwayTallyDelBillInfoSave;
import cn.samples.depot.web.bean.report.BRailwayTallyDelReportQuery;
import cn.samples.depot.web.bean.report.BRailwayTallyDelReportSave;
import cn.samples.depot.web.bean.report.RspBRailwayTallyDelReport;
import cn.samples.depot.web.cz.mapper.*;
import cn.samples.depot.web.cz.service.BRailwayTallyDelReportHeadService;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.entity.xml.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 铁路进口理货作废报文表头——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Service
@Slf4j
public class BRailwayTallyDelReportHeadServiceImpl extends ServiceImpl<BRailwayTallyDelReportHeadMapper, BRailwayTallyDelReportHead> implements BRailwayTallyDelReportHeadService {

    @Value("${message.file.path:1.0}")
    private String MESSAGE_FILE_PATH;

    @Autowired
    private BRailwayTallyDelBillInfoMapper delBillInfoMapper;

    @Autowired
    private BRailwayTallyDelResponseMapper responseMapper;

    @Autowired
    private BRailwayTallyDelResponseBillMapper responseBillMapper;

    @Autowired
    private BRailwayTallyReportHeadMapper reportHeadMapper;

    /**
     * 理货报告表体信息
     */
    @Autowired
    private BRailwayTallyBillInfoMapper tallyBillInfoMapper;

    /**
     * 装卸货地mapper
     */
    @Autowired
    private CDischargesMapper cDischargesMapper;

    /**
     * 海关代码表mapper
     */
    @Autowired
    private PCustomsCodeMapper customsCodeMapper;

    /**
     * 场站表mapper
     */
    @Autowired
    private CStationsMapper cStationsMapper;


    @Override
    public Page<RspBRailwayTallyDelReport> selectDelReportListPage(BRailwayTallyDelReportQuery query, int pageNum, int pageSize) {
        // 当前页，总条数 构造 page 对象
        Page<RspBRailwayTallyDelReport> page = new Page<>(pageNum, pageSize);
        Page<RspBRailwayTallyDelReport> delReportPages = page.setRecords(this.baseMapper.selectDelReportListPage(page, query));
        if (null != delReportPages) {
            // 获取得到分页的内容信息
            List<RspBRailwayTallyDelReport> delReportList = delReportPages.getRecords();
            if (CollectionUtil.isNotEmpty(delReportList)) {
                delReportList.forEach(delReport -> {
                    // 进出口标识
                    String ieFlag = delReport.getIEFlag();
                    // 卸货地中文名称
                    String unloadingPlaceName;
                    // 装货地中文名称
                    String loadingPlaceName;
                    // 海关中文名称
                    String customsName;
                    if (StringUtils.isNotBlank(ieFlag)) {
                        // 进口
                        if (IEFlag.IMPORT.getValue().equals(ieFlag)) {
                            // 卸货地代码
                            String unloadingPlace = delReport.getUnloadingPlace();
                            if (StringUtils.isNotBlank(unloadingPlace)) {
                                // 根据code获取得到name
                                CDischarges cDischarges = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, unloadingPlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
                                if (null != cDischarges) {
                                    unloadingPlaceName = cDischarges.getName();
                                    delReport.setUnloadingPlaceName(unloadingPlaceName);
                                }
                            }

                            delReport.setRspMessageType(MessageTypeFlag.WLJK_IRTR.getValue());
                        } else {
                            // 出货地代码
                            String loadingPlace = delReport.getLoadingPlace();
                            if (StringUtils.isNotBlank(loadingPlace)) {
                                // 根据code获取得到name
                                CDischarges cDischarges = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, loadingPlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
                                if (null != cDischarges) {
                                    loadingPlaceName = cDischarges.getName();
                                    delReport.setLoadingPlaceName(loadingPlaceName);
                                }
                            }
                            delReport.setRspMessageType(MessageTypeFlag.WLJK_ERTR.getValue());
                        }
                    }

                    // 获取得到海关编号
                    String customsCode = delReport.getCustomsCode();
                    if (StringUtils.isNotBlank(customsCode)) {
                        // 根据code获取得到name
                        PCustomsCode pCustomsCode = customsCodeMapper.selectOne(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, customsCode).eq(PCustomsCode::getEnable, Status.ENABLED.getValue()));
                        if (null != pCustomsCode) {
                            // 获取得到海关中文名称
                            customsName = pCustomsCode.getName();
                            delReport.setCustomsName(customsName);
                        }
                    }
                });
            }
        }

        return delReportPages;
    }

    /**
     * 铁路进出口理货作废
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult declareDel(String id) {
        // 根据理货表头id查询理货表头信息
        BRailwayTallyDelReportHead bRailwayTallyReportHead = this.baseMapper.selectById(id);
        if (null != bRailwayTallyReportHead) {
            // 保存的xml文件名称
            String xmlFileName = Constants.XML_PREFIX;
            // 根据表头id查询得到理货表体信息
            List<BRailwayTallyDelBillInfo> bodyList = delBillInfoMapper.selectList(Wrappers.<BRailwayTallyDelBillInfo>lambdaQuery().eq(BRailwayTallyDelBillInfo::getRailwayTallyReportHeadId, bRailwayTallyReportHead.getId()));
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
//                        .carriageNo(x.getCarriageNo())// 车皮号
//                        .packNo(null != x.getPackNo() ? String.valueOf(x.getPackNo()) : null)// 件数
//                        .grossWt(null != x.getGrossWt() ? String.valueOf(x.getGrossWt()) : null)// 毛重（公斤）
//                        .contaNo(x.getContaNo())// 集装箱号
//                        .contaType(x.getContaType())// 集装箱箱型
//                        .sealIDs(sealIDs)// 封志号码,类型和施加封志人
//                        .notes(x.getNotes())// 备注
                        .build();

                billInfos.add(railWayBilInfos);

            });

            // 铁路进出口理货报告申请表体信息
            MessageList messageList = new MessageList();
            messageList.setBillList(billInfos);
            messageList.setDeclPort(bRailwayTallyReportHead.getCustomsCode());
            // 进出口标记（I 进口,E 出口）
            String ieFlag = bRailwayTallyReportHead.getIEFlag();
            // 获取得到当前用户信息
            String currentStations = "";
            // 查询获取得到数据库场站信息
            CStations cStations = cStationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getEnable, Status.ENABLED.getValue()));
            if (null != cStations) {
                // 当前场站code
                currentStations = cStations.getCode();
            }
            String sender = bRailwayTallyReportHead.getCustomsCode() + currentStations;
            if ("I".equals(ieFlag)) {
                // 卸货地代码
                messageList.setUnloadingPlace(bRailwayTallyReportHead.getUnloadingPlace());
                xmlFileName += XmlTypeConstant.WLJK_ERTA
                        + Constants.XML_BODY
                        + Constants.UNDER_LINE
                        + sender
                        + Constants.UNDER_LINE
                        + DateUtils.getFullTimeStamp()
                        + Constants.XML_SUFFIX;
            } else {
                // 装货地代码
                // 当前场站code
                messageList.setLoadingPlace(bRailwayTallyReportHead.getLoadingPlace());
                xmlFileName += XmlTypeConstant.WLJK_IRTA
                        + Constants.XML_BODY
                        + Constants.UNDER_LINE
                        + sender
                        + Constants.UNDER_LINE
                        + DateUtils.getFullTimeStamp()
                        + Constants.XML_SUFFIX;
            }
            // 装卸开始时间
//            messageList.setActualDateTime(String.valueOf(null != bRailwayTallyReportHead.getActualDatetime() ? DateUtils.longToString(bRailwayTallyReportHead.getActualDatetime()) : DateUtils.longToString(System.currentTimeMillis())));
//            // 装卸结束时间
//            messageList.setCompletedDateTime(String.valueOf(null != bRailwayTallyReportHead.getCompletedDatetime() ? DateUtils.longToString(bRailwayTallyReportHead.getCompletedDatetime()) : DateUtils.longToString(System.currentTimeMillis())));

            // 铁路进出口理货报告申请表头信息
//            MessageHead messageHead = new MessageHead();
            MessageHead messageHead = MessageHead.builder()
                    .messageId(bRailwayTallyReportHead.getMessageId()) // 报文编号
                    .functionCode(FunctionCodeFlag.FUNCTION_CODE_3.getValue())// 功能代码（2新增，3删除）
                    .messageType(bRailwayTallyReportHead.getMessageType())// 消息类型（进口WLJK_IRTD，出口WLJK_ERTD）
                    .auditTime(DateUtils.getFullTimeStamp())// 消息创建时间yyyyMMddHHmmssfff
                    // 监管场所或理货公司(4位海关代码+9位组织结构代码)
                    .sender(sender)
                    .receiver(bRailwayTallyReportHead.getCustomsCode())// 接收者（海关代码）
                    .version(Constants.MESSAGE_VERSION)// 版本号（默认1.0）
                    .build();

            // 消息root
            Message message = new Message();
            message.setMessageHead(Arrays.asList(messageHead));
            message.setMessageList(Arrays.asList(messageList));

            String xmlStr = XstreamUtil.xmlAppendHead(XmlUtil.serializeToStr(message, CharsetUtil.UTF_8));
            log.info("文件xml格式：{}", xmlStr);

            boolean createFile = FileUtils.writeContent(MESSAGE_FILE_PATH, xmlFileName, xmlStr);
            // 根据id申请报文表头id来更新申报时间和审核状态字段
            if (createFile) {
                log.info("文件写入成功：{}", createFile);

                BRailwayTallyDelReportHead head = BRailwayTallyDelReportHead.builder()
                        .id(id)// 申请报文表头id
                        .auditTime(System.currentTimeMillis())// 作废时间
                        .auditStatus(DeclareStatusFlag.PRO_DECLARE.getValue())// 更新理货作废的状态为申报海关
                        .build();

                this.baseMapper.updateById(head);

                // TODO 更新理货作废表体的状态为作废中
                BRailwayTallyDelBillInfo delBillInfo = BRailwayTallyDelBillInfo.builder()
                        .railwayTallyReportHeadId(id)
                        .auditStatus(WayBillAuditStatus.WayBillAudit_07.getValue())
                        .build();
                delBillInfoMapper.update(delBillInfo, Wrappers.<BRailwayTallyDelBillInfo>lambdaQuery().eq(BRailwayTallyDelBillInfo::getRailwayTallyReportHeadId, id));

                BRailwayTallyReportHead reportHead = reportHeadMapper.selectOne(Wrappers.<BRailwayTallyReportHead>lambdaQuery().eq(BRailwayTallyReportHead::getMessageId, bRailwayTallyReportHead.getMessageIdAdd()));
                if (null != reportHead) {
                    bodyList.forEach(body -> {
                        // TODO 更新理货申请表中的运单状态为作废中
                        BRailwayTallyBillInfo billInfo = BRailwayTallyBillInfo.builder()
                                .auditStatus(WayBillAuditStatus.WayBillAudit_07.getValue())
                                .build();

                        // 根据提单号来更新申请表中的状态为作废中
                        tallyBillInfoMapper.update(billInfo, Wrappers.<BRailwayTallyBillInfo>lambdaQuery()
                                .eq(BRailwayTallyBillInfo::getRailwayTallyReportHeadId, reportHead.getId())
                                .eq(BRailwayTallyBillInfo::getBillNo, body.getBillNo()));
                    });
                }
            } else {
                log.error("文件写入失败：{}", createFile);
                return JsonResult.error("文件写入失败");
            }

            return JsonResult.data(createFile);
        }

        return JsonResult.success();
    }

    /**
     * 处理海关响应的报文回执
     *
     * @param rspMessage
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult xmlDelReturnReceiptHandle(RspMessage rspMessage) {
        if (null != rspMessage) {
            RspMessageList rspMessageList = rspMessage.getMessageList();
            // 获取海关返回的消息报文头信息
            MessageHead rspMessageHead = rspMessage.getMessageHead();
            // 申报的报文编号
            String declMsgId = "";
            // 回执类型代码
            String chkFlag = "";
            // 回执描述
            String notes = "";
            // 发送时间（响应报文的操作时间）
            String sendTime = "";
            if (null != rspMessageList) {
                ResponseInfo responseInfo = rspMessageList.getResponseInfo();
                if (null != responseInfo) {
                    // 获取得到申报报文编号
                    declMsgId = responseInfo.getMsgId();
                    // 审核状态（01审核通过，03退单）
                    chkFlag = responseInfo.getChkFlag();
                    // 回执描述
                    notes = responseInfo.getNotes();
                    // 海关响应报文的操作时间
                    sendTime = responseInfo.getPerTime();
                    String auditStatus = Constants.FIX_NUM_01.equals(chkFlag) ? DeclareStatusFlag.DECLARE_PASS.getValue() : DeclareStatusFlag.DECLARE_NO_PASS.getValue();
                    BRailwayTallyDelReportHead head = BRailwayTallyDelReportHead.builder()
                            .auditStatus(auditStatus)
                            .build();

                    // 根据消息id更新申报状态
                    this.baseMapper.update(head, Wrappers.<BRailwayTallyDelReportHead>lambdaQuery().eq(BRailwayTallyDelReportHead::getMessageId, declMsgId));

                }

                // 响应的表体信息
                List<RspRailWayBillInfo> billInfoList = rspMessageList.getRspRailWayBillInfo();
                billInfoList.forEach(bill -> {
                    // 提单号
                    String billNo = bill.getBillNo();
                    // 回执类型代码
                    String billChkFlag = bill.getChkFlag();
                    // 回执描述
                    String bllNotes = bill.getNotes();
                    // 回执报文编号
                    String railwayTallyResponseId = "";
                    if (null != rspMessageHead) {
                        railwayTallyResponseId = rspMessageHead.getMessageId();
                    }

                    BRailwayTallyDelResponseBill responseBill = BRailwayTallyDelResponseBill.builder()
                            .id(UniqueIdUtil.getUUID())
                            .railwayTallyResponseId(railwayTallyResponseId)
                            .billNo(billNo)
                            .chkFlag(billChkFlag)
                            .notes(bllNotes)
                            .createTime(System.currentTimeMillis())
                            .build();

                    responseBillMapper.insert(responseBill);

                    // 表体中单个提单号的申报状态
                    String billAuditStatus = Constants.FIX_NUM_01.equals(billChkFlag) ? DeclareStatusFlag.DECLARE_PASS.getValue() : DeclareStatusFlag.DECLARE_NO_PASS.getValue();
                    BRailwayTallyDelBillInfo billInfo = BRailwayTallyDelBillInfo.builder()
                            .auditStatus(billAuditStatus)
                            .build();

                    // 根据提单号更新申报状态
                    delBillInfoMapper.update(billInfo, Wrappers.<BRailwayTallyDelBillInfo>lambdaQuery().eq(BRailwayTallyDelBillInfo::getBillNo, billNo));
                });
            }

            if (null != rspMessageHead) {
                // 回执报文编号
                String messageId = rspMessageHead.getMessageId();
                String messageType = rspMessageHead.getMessageType();
                String sendId = rspMessageHead.getSender();

                // 铁路进口理货报文回执实体类
                BRailwayTallyDelResponse entity = BRailwayTallyDelResponse.builder()
                        .id(UniqueIdUtil.getUUID())
                        .messageId(messageId)
                        .declMessageId(declMsgId)
                        .messageType(messageType)
                        .sendId(sendId)
                        .chkFlag(chkFlag)
                        .notes(notes)
                        // 发送时间
                        .sendTime(Long.valueOf(sendTime))
                        // 接收时间
                        .createTime(System.currentTimeMillis())
                        .build();

                // 保存铁路进口理货报文回执数据入库
                responseMapper.insert(entity);
            }

            return JsonResult.success();
        }

        return JsonResult.error("");
    }

    /**
     * 铁路进口理货作废报文表头和表体
     *
     * @param id
     * @return
     */
    @Override
    public JsonResult removeDelReportById(String id) {
        // 根据id查询得到理货作废信息
        BRailwayTallyDelReportHead head = this.baseMapper.selectById(id);
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
                log.info("删除铁路进口理货作废报文表头：{}", deleteHead);
                if (deleteHead > 0) {
                    int deleteBill = delBillInfoMapper.delete(Wrappers.<BRailwayTallyDelBillInfo>lambdaQuery().eq(BRailwayTallyDelBillInfo::getRailwayTallyReportHeadId, id));
                    log.info("删除铁路进口理货作废报文表体：{}", deleteBill);
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
     * 保存用户理货作废信息
     *
     * @param delReportSave
     * @return
     */
    @Override
    public JsonResult saveDelReportInfo(BRailwayTallyDelReportSave delReportSave) {
        // 保存前验证参数的有效性
        JsonResult jsonResult = verifyParamValid(delReportSave);
        if (!"0000".equals(jsonResult.getCode())) {
            // 校验不通过就直接返回给前端错误信息
            return jsonResult;
        }

        String delUUID = "";
        if (null != delReportSave) {
            // 获取得到4位海关代码
            String customsCode = delReportSave.getCustomsCode();
            CStations cStations = cStationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getEnable, Status.ENABLED.getValue()));
            // 获取场站信息（也就是企业组织结构代码）
            if (null != cStations) {
                // 场站编码
                String code = cStations.getCode();
                // 自动生成消息id
                String messageId = UniqueIdUtil.getMsgId(customsCode, code);
                delReportSave.setMessageId(messageId);
            }
            delUUID = UniqueIdUtil.getUUID();
            delReportSave.setId(delUUID);
            BRailwayTallyDelReportHead reportHead = new BRailwayTallyDelReportHead();
            BeanUtils.copyProperties(delReportSave, reportHead);
            // 根据进出口标识来判断消息类型
            String ieFlag = reportHead.getIEFlag();
            if (IEFlag.IMPORT.getValue().equals(ieFlag)) {
                reportHead.setMessageType(MessageTypeFlag.WLJK_IRTD.getValue());
            } else {
                reportHead.setMessageType(MessageTypeFlag.WLJK_ERTD.getValue());
            }
            reportHead.setAuditStatus(DeclareStatusFlag.PRE_DECLARE.getValue());
            reportHead.setCreateTime(System.currentTimeMillis());
            // 保存理货申请表头信息
            this.baseMapper.insert(reportHead);

            // 获取得到集装箱
            List<BRailwayTallyDelBillInfoSave> billInfoSaves = delReportSave.getDelBillInfoSaveList();
            String finalDelUUID = delUUID;
            billInfoSaves.forEach(bill -> {
                bill.setId(UniqueIdUtil.getUUID());

                BRailwayTallyDelBillInfo billInfo = new BRailwayTallyDelBillInfo();
                BeanUtils.copyProperties(bill, billInfo);
                billInfo.setAuditStatus(WayBillAuditStatus.WayBillAudit_05.getValue());
                billInfo.setCreateTime(System.currentTimeMillis());
                billInfo.setRailwayTallyReportHeadId(finalDelUUID);
                // 保存理货申请表体信息（集装箱运单信息）
                delBillInfoMapper.insert(billInfo);
            });
        }

        return JsonResult.data(delUUID);
    }

    @Override
    public BRailwayTallyDelReportSave queryByMsgId(String msgId) {
        // 根据消息id查询得到对应理货报文编号
        BRailwayTallyDelReportHead delReportHead = this.baseMapper.selectOne(Wrappers.<BRailwayTallyDelReportHead>lambdaQuery().eq(BRailwayTallyDelReportHead::getId, msgId));
        if (null != delReportHead) {
            // 获取得到表头的id
            String id = delReportHead.getId();
            // 根据申报表头的id查询得到表体的集合信息
            Map<String, Object> maps = MapUtil.buildMap("railway_tally_report_head_id", id, "audit_status", WayBillAuditStatus.WayBillAudit_07.getValue());
            List<BRailwayTallyDelBillInfo> billInfoList = delBillInfoMapper.selectByMap(maps);
            BRailwayTallyDelReportSave delReportSave = BRailwayTallyDelReportSave.builder().build();
            BeanUtils.copyProperties(delReportHead, delReportSave);

            List<BRailwayTallyDelBillInfoSave> saveList = new ArrayList<>();
            billInfoList.forEach(bill -> {
                BRailwayTallyDelBillInfoSave saves = BRailwayTallyDelBillInfoSave.builder().build();
                BeanUtils.copyProperties(bill, saves);
                saveList.add(saves);
            });

            delReportSave.setDelBillInfoSaveList(saveList);

            return delReportSave;
        }

        return null;
    }

    @Override
    public BRailwayTallyDelReportSave editByMsgId(String msgId) {
        // 根据消息id查询得到对应理货报文编号
        BRailwayTallyDelReportHead delReportHead = this.baseMapper.selectOne(Wrappers.<BRailwayTallyDelReportHead>lambdaQuery().eq(BRailwayTallyDelReportHead::getId, msgId));
        if (null != delReportHead) {
            // 获取得到表头的id
            String id = delReportHead.getId();
            // 根据申报表头的id查询得到表体的集合信息
            Map<String, Object> maps = MapUtil.buildMap("railway_tally_report_head_id", id);
            List<BRailwayTallyDelBillInfo> billInfoList = delBillInfoMapper.selectByMap(maps);
            BRailwayTallyDelReportSave delReportSave = BRailwayTallyDelReportSave.builder().build();
            BeanUtils.copyProperties(delReportHead, delReportSave);

            List<BRailwayTallyDelBillInfoSave> saveList = new ArrayList<>();
            billInfoList.forEach(bill -> {
                BRailwayTallyDelBillInfoSave saves = BRailwayTallyDelBillInfoSave.builder().build();
                BeanUtils.copyProperties(bill, saves);
                saveList.add(saves);
            });

            delReportSave.setDelBillInfoSaveList(saveList);

            return delReportSave;
        }

        return null;
    }

    @Override
    public BRailwayTallyDelReportHead checkUpdate(String id) throws BizException {
        if (StringUtils.isEmpty(id)) throw new BizException("表头id不能为空");
        BRailwayTallyDelReportHead head = getById(id);
        if (null == head) throw new BizException("找不到对应理货作废记录");
        if (DeclareStatusFlag.PRE_DECLARE.getValue().equals(head.getAuditStatus()) || DeclareStatusFlag.DECLARE_NO_PASS.getValue().equals(head.getAuditStatus()))
            return head;
        throw new BizException(String.format("仅[%s,%s]状态可操作", DeclareStatusFlag.PRE_DECLARE.getTitle(), DeclareStatusFlag.DECLARE_NO_PASS.getTitle()));

    }

    /**
     * 保存前验证请求参数的有效性
     *
     * @param delReportSave
     * @return
     */
    public JsonResult verifyParamValid(BRailwayTallyDelReportSave delReportSave) {
        if (null != delReportSave) {
            // 获取对应理货报文编号
            String messageAddId = delReportSave.getMessageIdAdd();
            if (StringUtil.isEmpty(messageAddId)) {
                JsonResult.error("9998", "对应的理货报文编号不能为空");
            }

            List<BRailwayTallyDelBillInfoSave> delBillInfoSaves = delReportSave.getDelBillInfoSaveList();
            if (CollectionUtil.isEmpty(delBillInfoSaves)) {
                JsonResult.error("9998", "集装新运单信息至少选中一条");
            }
        }
        return JsonResult.success();
    }
}