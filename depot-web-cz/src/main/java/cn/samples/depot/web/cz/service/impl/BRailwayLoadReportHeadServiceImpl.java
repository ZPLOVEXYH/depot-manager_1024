/**
 * @filename:BRailwayLoadReportHeadServiceImpl 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.samples.depot.common.constant.Constants;
import cn.samples.depot.common.constant.XmlTypeConstant;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.*;
import cn.samples.depot.common.utils.*;
import cn.samples.depot.web.bean.load.ContaVo;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.bean.load.LoadReportVo;
import cn.samples.depot.web.bean.load.add.RspLoadReport;
import cn.samples.depot.web.bean.load.add.RspLoadReportConta;
import cn.samples.depot.web.cz.mapper.*;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.entity.xml.MessageHead;
import cn.samples.depot.web.entity.xml.ResponseInfo;
import cn.samples.depot.web.entity.xml.SealID;
import cn.samples.depot.web.entity.xml.ex.req.ReqArriveInfo;
import cn.samples.depot.web.entity.xml.load.req.ReqContaInfo;
import cn.samples.depot.web.entity.xml.load.req.ReqLoadMessage;
import cn.samples.depot.web.entity.xml.load.req.ReqLoadMessageList;
import cn.samples.depot.web.entity.xml.load.rsp.LoadMessage;
import cn.samples.depot.web.entity.xml.load.rsp.LoadMessageList;
import cn.samples.depot.web.entity.xml.load.rsp.RspBillInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 装车记录单申请报文表头——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
@Slf4j
public class BRailwayLoadReportHeadServiceImpl extends ServiceImpl<BRailwayLoadReportHeadMapper, BRailwayLoadReportHead> implements BRailwayLoadReportHeadService {

    /**
     * 装车记录单申报报文表体运抵单信息
     */
    @Autowired
    BRailwayLoadListMapper loadListMapper;

    /**
     * 装车记录单申报报文表集装箱信息
     */
    @Autowired
    BRailwayLoadContaMapper loadContaMapper;

    /**
     * 装车记录单作废报文表体运抵单信息
     */
    @Autowired
    BRailwayLoadDelReportHeadMapper loadDelReportHeadMapper;

    /**
     * 装车记录单作废报文表集装箱信息
     */
    @Autowired
    BRailwayLoadDelContaMapper loadDelContaMapper;

    /**
     * 场站信息
     */
    @Autowired
    CStationsMapper cStationsMapper;

    @Autowired
    PCustomsCodeMapper customsCodeMapper;

    @Autowired
    CDischargesMapper cDischargesMapper;
    /**
     * 基础实现类
     */
    @Autowired
    BaseService baseService;

    @Autowired
    BRailwayLoadContaService contaService;
    @Autowired
    BRailwayLoadListService listService;
    @Autowired
    CStationsService stationsService;

    /**
     * 发送海关报文的文件放置路径
     */
    @Value("${message.file.path:1.0}")
    private String MESSAGE_FILE_PATH;

    /**
     * 单个删除装车记录单申请报文
     *
     * @param id
     * @return
     */
    @Override
    public void deleteLoadReportById(String id) throws BizException {
        // 根据id查询得到理货作废信息
        BRailwayLoadReportHead head = checkUpdate(id);

        int deleteHead = this.baseMapper.deleteById(id);
        log.info("删除装车记录单申请报文表头：{}", deleteHead);

        int deleteLoadConta = loadContaMapper.delete(Wrappers.<BRailwayLoadConta>lambdaQuery().eq(BRailwayLoadConta::getRailwayLoadReportHeadId, id));
        log.info("删除装车记录单申请报文表体集装箱信息：{}", deleteLoadConta);

        int deleteList = loadListMapper.delete(Wrappers.<BRailwayLoadList>lambdaQuery().eq(BRailwayLoadList::getRailwayLoadReportHeadId, id));
        log.info("删除装车记录单申请报文表体：{}", deleteList);


    }

    /**
     * 根据装车记录申报表头id查询得到申报信息
     *
     * @param id
     * @return
     */
    @Override
    public RspLoadReport queryLoadReportById(String id) {
        BRailwayLoadReportHead reportHead = this.baseMapper.selectById(id);
        if (null != reportHead) {
            RspLoadReport rspLoadReport = RspLoadReport.builder().build();
            BeanUtils.copyProperties(reportHead, rspLoadReport);

            // 根据装车记录表头id查询得到集装箱信息集合
            List<BRailwayLoadConta> loadContaList = loadContaMapper.selectList(Wrappers.<BRailwayLoadConta>lambdaQuery()
                    .eq(BRailwayLoadConta::getRailwayLoadReportHeadId, id));
//                    .eq(BRailwayLoadConta::getAuditStatus, WayBillAuditStatus.WayBillAudit_03.getValue()));

            List<RspLoadReportConta> contaList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(loadContaList)) {
                loadContaList.forEach(loadConta -> {
                    RspLoadReportConta rspLoadReportConta = RspLoadReportConta.builder().build();
                    BeanUtils.copyProperties(loadConta, rspLoadReportConta);

                    contaList.add(rspLoadReportConta);
                });
            }

            rspLoadReport.setContaList(contaList);

            return rspLoadReport;
        }

        return null;
    }

    /**
     * 根据装车记录申报表头id查询得到申报信息
     *
     * @param id
     * @return
     */
    @Override
    public RspLoadReport queryLoadReportByIdForCancel(String id) {
        BRailwayLoadReportHead reportHead = this.baseMapper.selectById(id);
        if (null != reportHead) {
            RspLoadReport rspLoadReport = RspLoadReport.builder().build();
            BeanUtils.copyProperties(reportHead, rspLoadReport);

            // 根据装车记录表头id查询得到集装箱信息集合
            List<BRailwayLoadConta> loadContaList = loadContaMapper.selectList(Wrappers.<BRailwayLoadConta>lambdaQuery()
                    .eq(BRailwayLoadConta::getRailwayLoadReportHeadId, id)
                    .eq(BRailwayLoadConta::getAuditStatus, WayBillAuditStatus.WayBillAudit_03.getValue()));

            List<RspLoadReportConta> contaList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(loadContaList)) {
                loadContaList.forEach(loadConta -> {
                    RspLoadReportConta rspLoadReportConta = RspLoadReportConta.builder().build();
                    // 排除备注字段
                    BeanUtils.copyProperties(loadConta, rspLoadReportConta, "notes");

                    contaList.add(rspLoadReportConta);
                });
            }

            rspLoadReport.setContaList(contaList);

            return rspLoadReport;
        }

        return null;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 分页查询
     **/
    @Override
    public IPage<BRailwayLoadReportHead> page(LoadQuery query, Integer pageNum, Integer pageSize) {
        Set<String> ids = new HashSet<>();
        int i = 0; //计数器
        //运抵单
        if (StringUtils.isNotEmpty(query.getArriveNo())) {
            i++;
            ids.addAll(listService.listHeadIdsByPartArriveNo(query.getArriveNo()));
        }

        //集装箱
        if (StringUtils.isNotEmpty(query.getContaNo()) || StringUtils.isNotEmpty(query.getBillNo())) {
            i++;
            if (i == 1) {
                ids.addAll(contaService.listHeadIdsByLoadQuery(query));
            } else if (i == 2) {
                ids.retainAll(contaService.listHeadIdsByLoadQuery(query));
            }
        }
        if (i > 0 && CollectionUtils.isEmpty(ids)) return null;
        //表头
        Page<BRailwayLoadReportHead> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BRailwayLoadReportHead> wrapper = new LambdaQueryWrapper<>();
        //装卸地代码
        wrapper.eq(StringUtils.isNotEmpty(query.getDischargePlace()), BRailwayLoadReportHead::getDischargePlace, query.getDischargePlace())
                //报文编号
                .like(StringUtils.isNotEmpty(query.getMessageId()), BRailwayLoadReportHead::getMessageId, query.getMessageId())
                //创建时间
                .ge((null != query.getStartCreateTime() && query.getStartCreateTime() > 0), BRailwayLoadReportHead::getCreateTime, query.getStartCreateTime())
                .le((null != query.getEndCreateTime() && query.getEndCreateTime() > 0), BRailwayLoadReportHead::getCreateTime, query.getEndCreateTime())
                .in((!CollectionUtils.isEmpty(ids)), BRailwayLoadReportHead::getId, ids)
                .orderByDesc(BRailwayLoadReportHead::getCreateTime);

        IPage<BRailwayLoadReportHead> loadReportHeadIPage = super.page(page, wrapper);
        if (null != loadReportHeadIPage) {
            List<BRailwayLoadReportHead> reportHeadList = loadReportHeadIPage.getRecords();
            if (null != reportHeadList) {
                reportHeadList.forEach(reportHead -> {
                    // 海关代码
                    String customsCode = reportHead.getCustomsCode();
                    // 卸货地代码
                    String dischargePlace = reportHead.getDischargePlace();
                    if (null != customsCode) {
                        // 根据code获取得到name
                        PCustomsCode pCustomsCode = customsCodeMapper.selectOne(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, customsCode).eq(PCustomsCode::getEnable, Status.ENABLED.getValue()));
                        if (null != pCustomsCode) {
                            // 获取得到海关中文名称
                            String customsName = pCustomsCode.getName();
                            reportHead.setCustomsName(customsName);
                        }
                    }
                    if (!StringUtils.isEmpty(dischargePlace)) {
                        // 根据卸货地代码查询得到已启用的卸货地中文名称
                        CDischarges cDischarges = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, dischargePlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
                        if (null != cDischarges) {
                            // 获取得到卸货地中文名称
                            String dischargesName = cDischarges.getName();
                            reportHead.setDischargePlaceName(dischargesName);
                        }
                    }
                });
            }
        }

        return loadReportHeadIPage;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 保存   1.同一装车记录单，集装箱号不能重复 2.同一个集装箱，可以有多个封志号，封志号不能重复 3.同一集装箱的运抵编号不能重复
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult saveVo(LoadReportVo vo) throws BizException {
        //表头
        BRailwayLoadReportHead head = vo.getHead();
        saveBefore(head);
        super.save(head);
        //标题
        List<ContaVo> contaVos = vo.getContaVoList();
        if (CollectionUtils.isEmpty(contaVos)) throw new BizException("集装箱不能为空");
        List<BRailwayLoadList> arrives = new ArrayList<>();//运抵单
        List<BRailwayLoadConta> contas = new ArrayList<>();//集装箱
        Set<String> contaNos = new HashSet<>();
        Set<String> billNos = new HashSet<>();
        for (ContaVo contaVo : contaVos) {
            //集装箱
            BRailwayLoadConta conta = contaVo.getConta();
            if (!contaNos.add(conta.getContaNo()))
                throw new BizException(String.format("集装箱号[%s]重复", conta.getContaNo()));
            if (!billNos.add(conta.getBillNo()))
                throw new BizException(String.format("运单号[%s}重复", conta.getBillNo()));
            if (StringUtils.isEmpty(conta.getId()))
                conta.setId(UniqueIdUtil.getUUID());
            contaService.saveBefore(head, conta);
            contas.add(conta);
            //运抵单
            List<BRailwayLoadList> lists = contaVo.getArrayList();
            Set<String> arriveNos = new HashSet<>();
            if (CollectionUtils.isEmpty(lists))
                throw new BizException(String.format("集装箱[%s]对应的运抵单不能为空", conta.getContaNo()));
            for (BRailwayLoadList list : lists) {
                if (!arriveNos.add(list.getArriveNo()))
                    throw new BizException(String.format("集装箱[%s]中运抵编号[%s]重复", conta.getContaNo(), list.getArriveNo()));
                listService.saveBefore(head, conta, list);
                arrives.add(list);
            }
        }
        contaService.saveBatch(contas);
        listService.saveBatch(arrives);

        return JsonResult.data(head.getId());
    }

    private void saveBefore(BRailwayLoadReportHead head) throws BizException {
        checkEmpty(head);
        if (StringUtils.isEmpty(head.getMessageId()))
            head.setMessageId(UniqueIdUtil.getMsgId(head.getCustomsCode(), stationsService.getCode()));
        if (null == head.getCreateTime() || head.getCreateTime() <= 0) head.setCreateTime(System.currentTimeMillis());
        if (StringUtils.isEmpty(head.getAuditStatus())) head.setAuditStatus(DeclareStatusFlag.PRE_DECLARE.getValue());
        if (StringUtils.isEmpty(head.getMessageType())) head.setMessageType(MessageTypeFlag.WLJK_TLA.getValue());
    }

    private void checkEmpty(BRailwayLoadReportHead head) throws BizException {
        if (null == head) throw new BizException("海关代码不能为空");
        if (StringUtils.isEmpty(head.getCustomsCode())) throw new BizException("海关代码不能为空");
        if (StringUtils.isEmpty(head.getDischargePlace())) throw new BizException("装货地代码不能为空");
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 编辑-详情
     **/
    @Override
    public LoadReportVo detail4Update(String id) throws BizException {
        return LoadReportVo.builder()
                .head(checkUpdate(id))
                .contaVoList(getContaVosByHeadId(id))
                .build();
    }

    private List<ContaVo> getContaVosByHeadId(String headId) {
        return contaService.list(new LambdaQueryWrapper<BRailwayLoadConta>().eq(BRailwayLoadConta::getRailwayLoadReportHeadId, headId))
                .stream().map(conta -> {
                    return ContaVo.builder()
                            .conta(conta)
                            .arrayList(listService.list(new LambdaQueryWrapper<BRailwayLoadList>().eq(BRailwayLoadList::getRailwayLoadReportContaId, conta.getId())))
                            .build();
                }).collect(Collectors.toList());
    }

    private BRailwayLoadReportHead checkUpdate(String id) throws BizException {
        BRailwayLoadReportHead head = getById(id);
        if (null == head) throw new BizException("找不到对应装车记录");
        if (DeclareStatusFlag.PRE_DECLARE.getValue().equals(head.getAuditStatus()) || DeclareStatusFlag.DECLARE_NO_PASS.getValue().equals(head.getAuditStatus()))
            return head;
        throw new BizException(String.format("仅[%s,%s]状态可编辑", DeclareStatusFlag.PRE_DECLARE.getTitle(), DeclareStatusFlag.DECLARE_NO_PASS.getTitle()));

    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 编辑
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, LoadReportVo vo) throws BizException {
        deleteLoadReportById(id);
        saveVo(vo);
    }

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 根据报文编号获取实体
     **/
    @Override
    public BRailwayLoadReportHead getByMessageId(String messageId) {
        return getOne(new LambdaQueryWrapper<BRailwayLoadReportHead>().eq(BRailwayLoadReportHead::getMessageId, messageId));
    }

    /**
     * @Author majunzi
     * @Date 2019/9/16
     * @Description 下拉选择
     **/
    @Override
    public List<BRailwayLoadReportHead> select() {
        return list(new LambdaQueryWrapper<BRailwayLoadReportHead>().eq(BRailwayLoadReportHead::getAuditStatus, DeclareStatusFlag.DECLARE_PASS.getValue()).orderByDesc(BRailwayLoadReportHead::getAuditTime));
    }

    /**
     * 装车记录单申报
     *
     * @param id
     * @return
     */
    @Override
    public JsonResult declare(String id) {
        BRailwayLoadReportHead loadReportHead = this.baseMapper.selectById(id);
        if (null != loadReportHead) {
            List<BRailwayLoadConta> loadContaList = loadContaMapper.selectList(Wrappers.<BRailwayLoadConta>lambdaQuery().eq(BRailwayLoadConta::getRailwayLoadReportHeadId, loadReportHead.getId()));
            ReqLoadMessageList loadMessageList = ReqLoadMessageList.builder()
                    .dischargePlace(loadReportHead.getDischargePlace())
                    .build();

            List<ReqContaInfo> contaInfoList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(loadContaList)) {
                // 循环遍历铁路进出口申请标体内容
                loadContaList.forEach(loadConta -> {
                    List<SealID> sealIDs = new ArrayList<>();
                    // 获取得到封志号
                    String sealNo = loadConta.getSealNo();
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

                    // 处理运抵编号集合
                    List<ReqArriveInfo> arriveInfoList = new ArrayList<>();
                    List<BRailwayLoadList> bRailwayLoadLists = loadListMapper.selectList(Wrappers.<BRailwayLoadList>lambdaQuery().eq(BRailwayLoadList::getRailwayLoadReportContaId, loadConta.getId()));
                    if (CollectionUtils.isNotEmpty(bRailwayLoadLists)) {
                        bRailwayLoadLists.forEach(load -> {
                            ReqArriveInfo reqArriveInfo = ReqArriveInfo.builder()
                                    .arriveNo(load.getArriveNo())
                                    .build();

                            arriveInfoList.add(reqArriveInfo);
                        });
                    }

                    // 装车记录单集装箱信息
                    ReqContaInfo reqContaInfo = ReqContaInfo.builder()
                            .carriageNo(loadConta.getCarriageNo())
                            .billNo(loadConta.getBillNo())
                            .contaNo(loadConta.getContaNo())
                            .contaType(loadConta.getContaType())
                            .sealIDList(sealIDs)
                            .notes(loadConta.getNotes())
                            .arriveInfoList(arriveInfoList)
                            .build();

                    contaInfoList.add(reqContaInfo);
                });

                loadMessageList.setContaInfoList(contaInfoList);
            }

            // 监管场所经营人
            String operatorCode = "";
            // 组织结构代码
            String stationCode = "";
            // 查询获取得到数据库场站信息
            CStations cStations = cStationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getEnable, Status.ENABLED.getValue()));
            if (null != cStations) {
                // 监管场所经营人
                operatorCode = cStations.getOperatorCode();
                // 组织结构代码
                stationCode = cStations.getCode();
            }

            MessageHead messageHead = MessageHead.builder()
                    .messageId(loadReportHead.getMessageId())
                    .functionCode(FunctionCodeFlag.FUNCTION_CODE_2.getValue())
                    .messageType(loadReportHead.getMessageType())
                    .auditTime(DateUtils.getFullTimeStamp())
                    // 监管场所经营人
                    .sender(operatorCode)
                    // 海关代码
                    .receiver(loadReportHead.getCustomsCode())
                    .version(Constants.MESSAGE_VERSION)
                    .build();

            ReqLoadMessage reqLoadMessage = ReqLoadMessage.builder()
                    .messageHead(messageHead)
                    .messageList(loadMessageList)
                    .build();

            String xmlStr = XstreamUtil.xmlAppendHead(XmlUtil.serializeToStr(reqLoadMessage, CharsetUtil.UTF_8));
            log.info("文件xml格式：{}", xmlStr);

            // 保存的xml文件名称
            String xmlFileName = Constants.XML_PREFIX
                    + XmlTypeConstant.WLJK_TLA
                    + Constants.XML_BODY
                    + Constants.UNDER_LINE
                    + (loadReportHead.getCustomsCode() + stationCode)
                    + Constants.UNDER_LINE
                    + DateUtils.getFullTimeStamp()
                    + Constants.XML_SUFFIX;

            boolean createFile = FileUtils.writeContent(MESSAGE_FILE_PATH, xmlFileName, xmlStr);
            log.info("文件写入成功：{}", createFile);

            // 根据id申请报文表头id来更新申报时间和审核状态字段
            if (createFile) {
                BRailwayLoadReportHead head = BRailwayLoadReportHead.builder()
                        .id(id)// 申请报文表头id
                        .auditTime(System.currentTimeMillis())// 申报时间
                        .auditStatus(DeclareStatusFlag.PRO_DECLARE.getValue())// 申报状态
                        .build();

                // 更新表头的状态为申报海关
                this.baseMapper.updateById(head);
            } else {
                log.error("文件写入失败：{}", createFile);
                return JsonResult.error("9998", "装车记录单申报，文件写入失败");
            }

            return JsonResult.data(createFile);
        }

        return JsonResult.error("9998", "装车记录单申报失败");
    }

    /**
     * 处理装车记录单申请、作废的回执报文文件类型
     *
     * @param loadMessage
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult xmlReturnLoadHandle(LoadMessage loadMessage) {
        if (null != loadMessage) {
            // 获取得到回执报文表头对象
            MessageHead messageHead = loadMessage.getMessageHead();
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
            if (null != messageHead) {
                functinoCode = messageHead.getFunctionCode();
                // 回执报文编号
                messageId = messageHead.getMessageId();
                messageType = messageHead.getMessageType();
                sendId = messageHead.getSender();
            }

            LoadMessageList loadMessageList = loadMessage.getMessageList();
            if (null != loadMessageList) {
                // 生成报文表头的id
                String headUUID = UniqueIdUtil.getUUID();
                // 获取得到回执的表体信息
                ResponseInfo responseInfo = loadMessageList.getResponseInfo();
                List<RspBillInfo> rspBillInfoList = loadMessageList.getRspBillInfoList();
                // TODO 保存海关回执的表体信息
                baseService.saveResponseBodyXml(rspBillInfoList, functinoCode, headUUID);

                if (null != responseInfo) {
                    // 01 审核通过 03 退单
                    chkFlag = responseInfo.getChkFlag();
                    declMsgId = responseInfo.getMsgId();
                    notes = responseInfo.getNotes();
                    String auditStatus = Constants.FIX_NUM_01.equals(chkFlag) ? DeclareStatusFlag.DECLARE_PASS.getValue() : DeclareStatusFlag.DECLARE_NO_PASS.getValue();
                    // 2新增，3删除
                    if (Constants.FIX_NUM_2.equals(functinoCode)) {
                        BRailwayLoadReportHead head = BRailwayLoadReportHead.builder()
                                // 申报状态：01待申报、02申报海关、03审核通过、04审核不通过
                                .auditStatus(auditStatus)
                                .build();

                        // 根据消息id更新申报状态
                        this.baseMapper.update(head, Wrappers.<BRailwayLoadReportHead>lambdaQuery().eq(BRailwayLoadReportHead::getMessageId, declMsgId));

                        BRailwayLoadReportHead reportHead = this.baseMapper.selectOne(Wrappers.<BRailwayLoadReportHead>lambdaQuery().eq(BRailwayLoadReportHead::getMessageId, declMsgId));
                        if (null != reportHead) {
                            // 理货报告运单的审核状态（01待作废/02退单/03理货审核通过/04作废中/05作废通过）
                            String billAuditStatus = Constants.FIX_NUM_01.equals(chkFlag) ? WayBillAuditStatus.WayBillAudit_03.getValue() : WayBillAuditStatus.WayBillAudit_04.getValue();
                            BRailwayLoadConta billInfo = BRailwayLoadConta.builder()
                                    //（01待作废/02退单/03理货审核通过/04作废中/05作废通过）
                                    .auditStatus(billAuditStatus)
                                    .build();

                            // 根据父id更新集装箱的申报状态
                            loadContaMapper.update(billInfo, Wrappers.<BRailwayLoadConta>lambdaQuery().eq(BRailwayLoadConta::getRailwayLoadReportHeadId, reportHead.getId()));
                        }
                    } else {
                        BRailwayLoadDelReportHead reportHead = BRailwayLoadDelReportHead.builder()
                                // 申报状态：01待申报、02申报海关、03审核通过、04审核不通过
                                .auditStatus(auditStatus)
                                .build();

                        loadDelReportHeadMapper.update(reportHead, Wrappers.<BRailwayLoadDelReportHead>lambdaQuery().eq(BRailwayLoadDelReportHead::getMessageId, declMsgId));

                        // 更新理货作废报文表体的审核状态
                        String delStatus = Constants.FIX_NUM_01.equals(chkFlag) ? WayBillAuditStatus.WayBillAudit_08.getValue() : WayBillAuditStatus.WayBillAudit_06.getValue();
                        BRailwayLoadDelConta delBillInfo = BRailwayLoadDelConta.builder()
                                .auditStatus(delStatus)
                                .build();

                        BRailwayLoadDelReportHead delReportHead = loadDelReportHeadMapper.selectOne(Wrappers.<BRailwayLoadDelReportHead>lambdaQuery().eq(BRailwayLoadDelReportHead::getMessageId, declMsgId));
                        if (null != delReportHead) {
                            loadDelContaMapper.update(delBillInfo,
                                    Wrappers.<BRailwayLoadDelConta>lambdaQuery()
                                            .eq(BRailwayLoadDelConta::getRailwayLoadDelReportHeadId, delReportHead.getId()));
                        }

                        // 如果是作废通过
                        int billCount = 0;
                        String id = "";
                        if (StringUtils.isNotEmpty(declMsgId) && null != delReportHead) {
                            BRailwayLoadReportHead head = this.baseMapper.selectOne(Wrappers.<BRailwayLoadReportHead>lambdaQuery().eq(BRailwayLoadReportHead::getMessageId, delReportHead.getMessageAddId()));
                            if (null != head) {
                                id = head.getId();
                                billCount = loadContaMapper.selectCount(Wrappers.<BRailwayLoadConta>lambdaQuery().eq(BRailwayLoadConta::getRailwayLoadReportHeadId, id));
                            }
                        }
                        if (Constants.FIX_NUM_01.equals(chkFlag)) {
                            if (StringUtils.isNotEmpty(id)) {
                                if (rspBillInfoList.size() == billCount) {
                                    // 如果理货报告申请的所有运单号全都作废了，那么将之前的理货报告申请里面的运单号全都删除
                                    this.baseMapper.deleteById(id);
                                    // TODO 删除整个表体集装箱信息
                                    loadContaMapper.delete(Wrappers.<BRailwayLoadConta>lambdaQuery().eq(BRailwayLoadConta::getRailwayLoadReportHeadId, id));
                                    // TODO 删除整个表体运抵单信息
                                    loadListMapper.delete(Wrappers.<BRailwayLoadList>lambdaQuery().eq(BRailwayLoadList::getRailwayLoadReportHeadId, id));
                                } else {
                                    // 部分删除申请表表体数据
                                    rspBillInfoList.forEach(rspBillInfo -> {
                                        String billNo = rspBillInfo.getBillNo();
                                        BRailwayLoadConta loadConta = loadContaMapper.selectOne(Wrappers.<BRailwayLoadConta>lambdaQuery()
                                                .eq(BRailwayLoadConta::getBillNo, billNo));

                                        // TODO 根据提运单号来删除表体集装箱数据
                                        loadContaMapper.delete(Wrappers.<BRailwayLoadConta>lambdaQuery()
                                                .eq(BRailwayLoadConta::getBillNo, billNo));

                                        // TODO 根据表体集装箱id删除运抵单信息
                                        if (null != loadConta) {
                                            loadListMapper.delete(Wrappers.<BRailwayLoadList>lambdaQuery().eq(BRailwayLoadList::getRailwayLoadReportContaId, loadConta.getId()));
                                        }
                                    });
                                }
                            }
                        } else {
                            // 如果是作废退单
                            BRailwayLoadReportHead head = BRailwayLoadReportHead.builder()
                                    .auditStatus(WayBillAuditStatus.WayBillAudit_03.getValue())
                                    .build();

                            this.baseMapper.update(head, Wrappers.<BRailwayLoadReportHead>lambdaQuery().eq(BRailwayLoadReportHead::getMessageId, declMsgId));

                            BRailwayLoadConta loadConta = BRailwayLoadConta.builder()
                                    .auditStatus(WayBillAuditStatus.WayBillAudit_03.getValue())
                                    .build();

                            loadContaMapper.update(loadConta, Wrappers.<BRailwayLoadConta>lambdaQuery().eq(BRailwayLoadConta::getRailwayLoadReportHeadId, id));
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
        } else {
            // TODO
            return JsonResult.error("接收到海关回执信息异常");
        }
    }
}