package cn.samples.depot.web.cz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.samples.depot.common.constant.Constants;
import cn.samples.depot.common.constant.XmlTypeConstant;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.*;
import cn.samples.depot.common.utils.*;
import cn.samples.depot.web.bean.railwayarrive.ArriveQuery;
import cn.samples.depot.web.bean.railwayarrive.ArriveReport4DetailVo;
import cn.samples.depot.web.bean.railwayarrive.ArriveReport4UpdateVo;
import cn.samples.depot.web.bean.railwayarrive.ArriveVo;
import cn.samples.depot.web.cz.mapper.*;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.entity.xml.MessageHead;
import cn.samples.depot.web.entity.xml.ResponseInfo;
import cn.samples.depot.web.entity.xml.SealID;
import cn.samples.depot.web.entity.xml.ex.req.ReqArriveInfo;
import cn.samples.depot.web.entity.xml.ex.req.ReqContainerInfo;
import cn.samples.depot.web.entity.xml.ex.req.ReqExMessage;
import cn.samples.depot.web.entity.xml.ex.req.ReqExMessageList;
import cn.samples.depot.web.entity.xml.ex.rsp.ExMessage;
import cn.samples.depot.web.entity.xml.ex.rsp.ExMessageList;
import cn.samples.depot.web.entity.xml.ex.rsp.RspArriveInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author majunzi
 * @Date 2019/8/22
 * @Description 铁路运抵申请报文表头
 **/
@Service
@Slf4j
public class BExRailwayReportHeadServiceImpl extends ServiceImpl<BExRailwayReportHeadMapper, BExRailwayReportHead> implements BExRailwayReportHeadService {


    @Value("${message.file.path:1.0}")
    private String MESSAGE_FILE_PATH;


    @Autowired
    BExRailwayDelReportHeadMapper delReportHeadMapper;

    @Autowired
    BExRailwayDelListMapper delListMapper;

    @Autowired
    BExRailwayListMapper listMapper;


    /**
     * 基础实现类
     */
    @Autowired
    BaseService baseService;


    /**
     * 运抵申报表体集装箱信息
     */
    @Autowired
    BExRailwayContaService contaService;
    @Autowired
    BExRailwayListService arriveService;
    @Autowired
    CStationsService stationsService;
    @Autowired
    PCustomsCodeMapper customsCodeMapper;
    @Autowired
    CDischargesMapper cDischargesMapper;

    @Override
    public IPage<BExRailwayReportHead> page(ArriveQuery query, Integer pageNum, Integer pageSize) {
        Set<String> ids = new HashSet<>();
        int i = 0; //计数器
        if (StringUtils.isNotEmpty(query.getArriveNo())) {
            i++;
            ids.addAll(arriveService.listHeadIdsByPartArriveNo(query.getArriveNo()));
        }

        if (StringUtils.isNotEmpty(query.getContaNo())) {
            i++;
            if (i == 1) {//仅集装箱号
                ids.addAll(contaService.listHeadIdsByContaNo(query.getContaNo()));
            } else if (i == 2) {//运抵单编号+集装箱号
                ids.retainAll(contaService.listHeadIdsByContaNo(query.getContaNo()));
            }
        }
        if (i > 0 && CollectionUtils.isEmpty(ids)) return null;

        Page<BExRailwayReportHead> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BExRailwayReportHead> wrapper = new LambdaQueryWrapper<>();
        //海关代码
        wrapper.eq((StringUtils.isNotEmpty(query.getCustomsCode())), BExRailwayReportHead::getCustomsCode, query.getCustomsCode())
                //卸货地代码
                .eq(StringUtils.isNotEmpty(query.getDischargePlace()), BExRailwayReportHead::getDischargePlace, query.getDischargePlace())
                //运抵报文编号
                .like(StringUtils.isNotEmpty(query.getMessageId()), BExRailwayReportHead::getMessageId, query.getMessageId())
                //审核状态
                .eq(StringUtils.isNotEmpty(query.getAuditStatus()), BExRailwayReportHead::getAuditStatus, query.getAuditStatus())
                //运抵时间
                .ge((null != query.getStartArriveTime() && query.getStartArriveTime() > 0), BExRailwayReportHead::getArriveTime, query.getStartArriveTime())
                .le((null != query.getEndArriveTime() && query.getEndArriveTime() > 0), BExRailwayReportHead::getArriveTime, query.getEndArriveTime())
                //创建时间
                .ge((null != query.getStartCreateTime() && query.getStartCreateTime() > 0), BExRailwayReportHead::getCreateTime, query.getStartCreateTime())
                .le((null != query.getEndCreateTime() && query.getEndCreateTime() > 0), BExRailwayReportHead::getCreateTime, query.getEndCreateTime())
                .in((!CollectionUtils.isEmpty(ids)), BExRailwayReportHead::getId, ids)
                .orderByDesc(BExRailwayReportHead::getCreateTime);

        IPage<BExRailwayReportHead> reportHeadIPage = super.page(page, wrapper);
        if (null != reportHeadIPage) {
            List<BExRailwayReportHead> reportHeadList = reportHeadIPage.getRecords();
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

        return reportHeadIPage;

    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 查看-详情
     **/
    @Override
    public ArriveReport4DetailVo detail(String id) {
        ArriveReport4DetailVo vo = new ArriveReport4DetailVo();
        BExRailwayReportHead reportHead = getById(id);
        if (null != reportHead) {
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
        }
        vo.setHead(reportHead);
        vo.setArriveList(arriveService.list(new LambdaQueryWrapper<BExRailwayList>().eq(BExRailwayList::getExRailwayReportHeadId, id)));
        vo.setContaList(contaService.list(new LambdaQueryWrapper<BExRailwayConta>().eq(BExRailwayConta::getExRailwayReportHeadId, id)));
        return vo;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 新增-保存
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult save(ArriveReport4UpdateVo vo) throws BizException {
        //表头
        BExRailwayReportHead head = vo.getHead();
        saveBefore(head);
        super.save(head);
        //运抵单
        List<ArriveVo> arriveVoList = vo.getArriveList();
        if (CollectionUtils.isEmpty(arriveVoList)) throw new BizException("运抵单不能为空");
        List<BExRailwayConta> contaList = new ArrayList<>();//集装箱
        for (ArriveVo arriveVo : arriveVoList) {
            BExRailwayList arrive = arriveVo.getArrive();
            arriveService.save(head, arrive);
            //集装箱
            List<BExRailwayConta> contas = arriveVo.getContaList();
            Set<String> contaNos = new HashSet<>();//集装箱号不能重复
            if (CollectionUtils.isEmpty(contas)) throw new BizException("集装箱不能为空");
            for (BExRailwayConta conta : contas) {
                if (!contaNos.add(conta.getContaNo()))
                    throw new BizException(String.format("集装箱号[%s]发生重复", conta.getContaNo()));
                contaService.saveBefore(head, arrive, conta);
                contaList.add(conta);
            }
        }
        contaService.saveBatch(contaList);

        return JsonResult.data(head.getId());
    }

    private void saveBefore(BExRailwayReportHead head) throws BizException {
        checkEmpty(head);
        if (StringUtils.isEmpty(head.getMessageId()))
            head.setMessageId(UniqueIdUtil.getMsgId(head.getCustomsCode(), stationsService.getCode()));
        if (null == head.getCreateTime() || head.getCreateTime() <= 0) head.setCreateTime(System.currentTimeMillis());
        if (StringUtils.isEmpty(head.getAuditStatus())) head.setAuditStatus(DeclareStatusFlag.PRE_DECLARE.getValue());
        if (StringUtils.isEmpty(head.getMessageType())) head.setMessageType(MessageTypeFlag.WLJK_ERRA.getValue());
    }


    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 非空校验
     **/
    private void checkEmpty(BExRailwayReportHead head) throws BizException {
        if (null == head) throw new BizException("海关代码不能为空");
        if (StringUtils.isEmpty(head.getMessageMode())) throw new BizException("生成方式不能为空");
        if (null == CreateType.findByValue(head.getMessageMode())) throw new BizException("生成方式错误");
        if (StringUtils.isEmpty(head.getCustomsCode())) throw new BizException("海关代码不能为空");
        if (StringUtils.isEmpty(head.getDischargePlace())) throw new BizException("卸货地代码不能为空");
        if (null == head.getArriveTime() || head.getArriveTime() <= 0) throw new BizException("运抵卸货时间不能为空");
    }

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 编辑/删除：针对待申报的数据
     **/
    private BExRailwayReportHead checkUpdate(String id) throws BizException {
        BExRailwayReportHead head = getById(id);
        if (!DeclareStatusFlag.PRE_DECLARE.getValue().equals(head.getAuditStatus()))
            throw new BizException(String.format("仅[%s]可编辑", DeclareStatusFlag.PRE_DECLARE.getTitle()));
        return head;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 编辑-详情
     **/
    @Override
    public ArriveReport4UpdateVo detail4Update(String id) throws BizException {
        return ArriveReport4UpdateVo.builder()
                .head(checkUpdate(id))
                .arriveList(getArriveVosByHeadId(id))
                .build();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 根据运抵报文编号 获取vo
     **/
    @Override
    public ArriveReport4UpdateVo getVoByMessageId(String messageId) {
        BExRailwayReportHead head = getOne(new LambdaQueryWrapper<BExRailwayReportHead>().eq(BExRailwayReportHead::getMessageId, messageId));
        return ArriveReport4UpdateVo.builder()
                .head(head)
                .arriveList(getArriveVosByHeadId(head.getId()))
                .build();
    }

    private List<ArriveVo> getArriveVosByHeadId(String headId) {
        return arriveService.list(new LambdaQueryWrapper<BExRailwayList>().eq(BExRailwayList::getExRailwayReportHeadId, headId))
                .stream().map(arrive -> {
                            return ArriveVo.builder()
                                    .arrive(arrive)
                                    .contaList(contaService.list(new LambdaQueryWrapper<BExRailwayConta>().eq(BExRailwayConta::getExRailwayListId, arrive.getId())))
                                    .build();
                        }
                ).collect(Collectors.toList());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 编辑-保存
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, ArriveReport4UpdateVo vo) throws BizException {
        deleteById(id);
        save(vo);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 删除, 依次删除，头，运抵单（发运计划运抵标记），集装箱
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) throws BizException {
        checkUpdate(id);
        super.removeById(id);
        contaService.remove(new LambdaQueryWrapper<BExRailwayConta>().eq(BExRailwayConta::getExRailwayReportHeadId, id));
        arriveService.removeByHeadId(id);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 下拉选择, 审核通过，按审核时间倒序排列
     **/
    @Override
    public List<BExRailwayReportHead> select() {
        return list(new LambdaQueryWrapper<BExRailwayReportHead>().eq(BExRailwayReportHead::getAuditStatus, DeclareStatusFlag.DECLARE_PASS.getValue()).orderByDesc(BExRailwayReportHead::getAuditTime));
    }

    /**
     * 处理运抵申请和运抵作废的回执报文
     *
     * @param exMessage
     * @return
     */
    @Override
    public JsonResult xmlReturnExHandle(ExMessage exMessage) {
        if (null != exMessage) {
            // 获取得到回执报文表头对象
            MessageHead messageHead = exMessage.getMessageHead();
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

            ExMessageList exMessageList = exMessage.getMessageList();
            if (null != exMessageList) {
                // 生成报文表头的id
                String headUUID = UniqueIdUtil.getUUID();
                // 获取得到回执的表体信息
                ResponseInfo responseInfo = exMessageList.getResponseInfo();
                List<RspArriveInfo> rspBillInfoList = exMessageList.getRspArriveInfoList();
                // TODO 保存海关回执的表体信息
                baseService.saveExResponseBodyXml(rspBillInfoList, functinoCode, headUUID);

                if (null != responseInfo) {
                    // 01 审核通过 03 退单
                    chkFlag = responseInfo.getChkFlag();
                    declMsgId = responseInfo.getMsgId();
                    notes = responseInfo.getNotes();
                    String auditStatus = Constants.FIX_NUM_01.equals(chkFlag) ? DeclareStatusFlag.DECLARE_PASS.getValue() : DeclareStatusFlag.DECLARE_NO_PASS.getValue();
                    // 2新增，3删除
                    if (Constants.FIX_NUM_2.equals(functinoCode)) {
                        BExRailwayReportHead head = BExRailwayReportHead.builder()
                                // 申报状态：01待申报、02申报海关、03审核通过、04审核不通过
                                .auditStatus(auditStatus)
                                .build();

                        // 根据消息id更新申报状态
                        this.baseMapper.update(head, Wrappers.<BExRailwayReportHead>lambdaQuery().eq(BExRailwayReportHead::getMessageId, declMsgId));

                        BExRailwayReportHead reportHead = this.baseMapper.selectOne(Wrappers.<BExRailwayReportHead>lambdaQuery().eq(BExRailwayReportHead::getMessageId, declMsgId));
                        if (null != reportHead) {
                            // 理货报告运单的审核状态（01待作废/02退单/03理货审核通过/04作废中/05作废通过）
                            String billAuditStatus = Constants.FIX_NUM_01.equals(chkFlag) ? WayBillAuditStatus.WayBillAudit_03.getValue() : WayBillAuditStatus.WayBillAudit_02.getValue();
                            BExRailwayList billInfo = BExRailwayList.builder()
                                    //（01待作废/02退单/03理货审核通过/04作废中/05作废通过）
                                    .auditStatus(billAuditStatus)
                                    .build();

                            // 根据父id更新集装箱的申报状态
                            arriveService.update(billInfo, Wrappers.<BExRailwayList>lambdaQuery().eq(BExRailwayList::getExRailwayReportHeadId, reportHead.getId()));
                        }
                    } else {
                        BExRailwayDelReportHead reportHead = BExRailwayDelReportHead.builder()
                                // 申报状态：01待申报、02申报海关、03审核通过、04审核不通过
                                .auditStatus(auditStatus)
                                .build();

                        delReportHeadMapper.update(reportHead, Wrappers.<BExRailwayDelReportHead>lambdaQuery().eq(BExRailwayDelReportHead::getMessageId, declMsgId));

                        // 更新理货作废报文表体的审核状态
                        String delStatus = Constants.FIX_NUM_01.equals(chkFlag) ? WayBillAuditStatus.WayBillAudit_08.getValue() : WayBillAuditStatus.WayBillAudit_06.getValue();
                        BExRailwayDelList delBillInfo = BExRailwayDelList.builder()
                                .auditStatus(delStatus)
                                .build();

                        BExRailwayDelReportHead delReportHead = delReportHeadMapper.selectOne(Wrappers.<BExRailwayDelReportHead>lambdaQuery().eq(BExRailwayDelReportHead::getMessageId, declMsgId));
                        if (null != delReportHead) {
                            delListMapper.update(delBillInfo,
                                    Wrappers.<BExRailwayDelList>lambdaQuery()
                                            .eq(BExRailwayDelList::getExRailwayReportDelHeadId, delReportHead.getId()));

                        }

                        // 如果是作废通过
                        int billCount = 0;
                        String id = "";
                        if (StringUtils.isNotEmpty(declMsgId)) {
                            if (null != delReportHead) {
                                BExRailwayReportHead head = this.baseMapper.selectOne(Wrappers.<BExRailwayReportHead>lambdaQuery().eq(BExRailwayReportHead::getMessageId, delReportHead.getMessageAddId()));
                                if (null != head) {
                                    id = head.getId();
                                    billCount = arriveService.count(Wrappers.<BExRailwayList>lambdaQuery().eq(BExRailwayList::getExRailwayReportHeadId, id));
                                }
                            }
                        }

                        if (Constants.FIX_NUM_01.equals(chkFlag)) {
                            if (StringUtils.isNotEmpty(id)) {
                                if (rspBillInfoList.size() == billCount) {
                                    // 如果理货报告申请的所有运单号全都作废了，那么将之前的理货报告申请里面的运单号全都删除
                                    this.baseMapper.deleteById(id);
                                    // TODO 删除整个表体运抵单信息
                                    arriveService.remove(Wrappers.<BExRailwayList>lambdaQuery().eq(BExRailwayList::getExRailwayReportHeadId, id));
                                    // TODO 删除整个表体集装箱信息
                                    contaService.remove(Wrappers.<BExRailwayConta>lambdaQuery().eq(BExRailwayConta::getExRailwayReportHeadId, id));
                                } else {
                                    // 部分删除申请表表体数据
                                    rspBillInfoList.forEach(rspBillInfo -> {
                                        // 获取得到运抵编号
                                        String arriveNo = rspBillInfo.getArriveNo();
                                        BExRailwayList loadConta = arriveService.getOne(Wrappers.<BExRailwayList>lambdaQuery()
                                                .eq(BExRailwayList::getArriveNo, arriveNo));

                                        // TODO 根据提运抵编号来删除表体运抵单信息
                                        arriveService.remove(Wrappers.<BExRailwayList>lambdaQuery()
                                                .eq(BExRailwayList::getArriveNo, arriveNo));

                                        // TODO 根据表体运抵单id删除集装箱信息
                                        if (null != loadConta) {
                                            contaService.remove(Wrappers.<BExRailwayConta>lambdaQuery().eq(BExRailwayConta::getExRailwayReportHeadId, loadConta.getId()));
                                        }
                                    });
                                }
                            }
                        } else {
                            // 如果是作废退单
                            BExRailwayReportHead head = BExRailwayReportHead.builder()
                                    .auditStatus(WayBillAuditStatus.WayBillAudit_03.getValue())
                                    .build();

                            this.baseMapper.update(head, Wrappers.<BExRailwayReportHead>lambdaQuery().eq(BExRailwayReportHead::getMessageId, declMsgId));

                            BExRailwayList bExRailwayList = BExRailwayList.builder()
                                    .auditStatus(WayBillAuditStatus.WayBillAudit_03.getValue())
                                    .build();

                            listMapper.update(bExRailwayList, Wrappers.<BExRailwayList>lambdaQuery().eq(BExRailwayList::getExRailwayReportHeadId, id));
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

    /**
     * 运抵报告申报
     *
     * @param id
     * @return
     */
    @Override
    public JsonResult declare(String id) {
        BExRailwayReportHead reportHead = this.baseMapper.selectById(id);
        if (null != reportHead) {
            // 根据表头id查询得到运抵编号信息
            List<BExRailwayList> exRailwayLists = arriveService.list(Wrappers.<BExRailwayList>lambdaQuery().eq(BExRailwayList::getExRailwayReportHeadId, reportHead.getId()));
            // 运抵编号信息集合
            List<ReqArriveInfo> reqArriveInfoList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(exRailwayLists)) {
                exRailwayLists.forEach(ex -> {
                    ReqArriveInfo reqArriveInfo = ReqArriveInfo.builder()
                            .arriveNo(ex.getArriveNo())
                            .packNo(null != ex.getPackNo() ? String.valueOf(ex.getPackNo()) : "")
                            .grossWt(null != ex.getGrossWt() ? String.valueOf(ex.getGrossWt()) : "")
                            .notes(ex.getNotes())
                            .build();

                    reqArriveInfoList.add(reqArriveInfo);
                });
            }

            // 根据表头id查询得到表体集装箱信息集合
            List<BExRailwayConta> contaList = contaService.list(Wrappers.<BExRailwayConta>lambdaQuery().eq(BExRailwayConta::getExRailwayReportHeadId, reportHead.getId()));
            List<ReqContainerInfo> reqContainerInfoList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(contaList)) {
                contaList.forEach(conta -> {
                    List<SealID> sealIDs = new ArrayList<>();
                    String sealNo = conta.getSealNo();
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

                    // 请求海关的表体集装箱信息
                    ReqContainerInfo reqContainerInfo = ReqContainerInfo.builder()
                            .contaNo(conta.getContaNo())
                            .contaType(conta.getContaType())
                            .partArriveNo(conta.getArriveNo())
                            .sealIDList(sealIDs)
                            .build();

                    reqContainerInfoList.add(reqContainerInfo);
                });
            }

            // 获取得到海关代码
            String customsCode = reportHead.getCustomsCode();

            // TODO
            ReqExMessageList reqExMessageList = ReqExMessageList.builder()
                    // 海关代码
                    .declPort(customsCode)
                    .arriveTime(String.valueOf(null != reportHead.getArriveTime() ? DateUtils.longToString(reportHead.getArriveTime()) : DateUtils.longToString(System.currentTimeMillis())))
                    .dischargePlace(reportHead.getDischargePlace())
                    .reqContainerInfoList(reqContainerInfoList)
                    .rspArriveInfoList(reqArriveInfoList)
                    .build();

            // 获取得到当前用户信息
            String currentStations = "";
            // 查询获取得到数据库场站信息
            CStations cStations = stationsService.getOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getEnable, Status.ENABLED.getValue()));
            if (null != cStations) {
                // 当前场站code
                currentStations = cStations.getCode();
            }

            String sender = reportHead.getCustomsCode() + currentStations;
            // TODO消息表头信息
            MessageHead messageHead = MessageHead.builder()
                    .messageId(reportHead.getMessageId())
                    .functionCode(FunctionCodeFlag.FUNCTION_CODE_2.getValue())
                    .messageType(reportHead.getMessageType())
                    .auditTime(DateUtils.getFullTimeStamp())
                    // 消息类型为WLJK_ERRA或WLJK_ERRD时：企业在物流系统备案的代码，备案规则为：4 位备案关区代码+9 位企业组织机构代码，其中不得有“-”等字符。
                    // 消息类型为WLJK_TGR时：发送方为主管海关的4 位关区号
                    .sender(sender)
                    // 消息类型为WLJK_ERRA或WLJK_ERRD时：接收报文的主管海关的4 位关区代码
                    // 消息类型为WLJK_ERRR时：企业在物流系统备案的代码，备案规则为：4 位备案关区代码+9 位企业组织机构代码，其中不得有“-”等字符。
                    .receiver(reportHead.getCustomsCode())
                    .version(Constants.MESSAGE_VERSION)
                    .build();

            // 消息内容
            ReqExMessage reqExMessage = ReqExMessage.builder()
                    .messageHead(messageHead)
                    .messageList(reqExMessageList)
                    .build();

            String xmlStr = XstreamUtil.xmlAppendHead(XmlUtil.serializeToStr(reqExMessage, CharsetUtil.UTF_8));
            log.info("文件xml格式：{}", xmlStr);

            // 保存的xml文件名称
            String xmlFileName = Constants.XML_PREFIX
                    + XmlTypeConstant.WLJK_ERRA
                    + Constants.XML_BODY
                    + Constants.UNDER_LINE
                    + sender
                    + Constants.UNDER_LINE
                    + DateUtils.getFullTimeStamp()
                    + Constants.XML_SUFFIX;

            boolean createFile = FileUtils.writeContent(MESSAGE_FILE_PATH, xmlFileName, xmlStr);
            // 根据id申请报文表头id来更新申报时间和审核状态字段
            if (createFile) {
                log.info("文件写入成功：{}", createFile);

                BExRailwayReportHead head = BExRailwayReportHead.builder()
                        .id(id)// 申请报文表头id
                        .auditTime(System.currentTimeMillis())// 申报时间
                        .auditStatus(DeclareStatusFlag.PRO_DECLARE.getValue())// 申报状态
                        .build();

                this.baseMapper.updateById(head);
            } else {
                log.error("文件写入失败：{}", createFile);
                return JsonResult.error("文件写入失败");
            }

            return JsonResult.data(createFile);
        } else {
            return JsonResult.error("9998", "运抵报告申报失败");
        }
    }
}