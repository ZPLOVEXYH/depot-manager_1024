package cn.samples.depot.web.cz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.samples.depot.common.constant.Constants;
import cn.samples.depot.common.constant.XmlTypeConstant;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.*;
import cn.samples.depot.common.utils.*;
import cn.samples.depot.web.bean.railwayarrive.ArriveQuery;
import cn.samples.depot.web.bean.railwayarrive.ArriveReportDel4AddVo;
import cn.samples.depot.web.bean.railwayarrive.ArriveReportDel4DetailVo;
import cn.samples.depot.web.bean.railwayarrive.ArriveReportDel4UpdateVo;
import cn.samples.depot.web.cz.mapper.*;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.entity.xml.MessageHead;
import cn.samples.depot.web.entity.xml.ex.req.ReqArriveInfo;
import cn.samples.depot.web.entity.xml.ex.req.ReqExMessage;
import cn.samples.depot.web.entity.xml.ex.req.ReqExMessageList;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author majunzi
 * @Date 2019/8/26
 * @Description 海关业务-运抵作废
 **/
@Service
@Slf4j
public class BExRailwayDelReportHeadServiceImpl extends ServiceImpl<BExRailwayDelReportHeadMapper, BExRailwayDelReportHead> implements BExRailwayDelReportHeadService {

    @Autowired
    BExRailwayListMapper listMapper;
    @Value("${message.file.path:1.0}")
    private String MESSAGE_FILE_PATH;

    @Autowired
    CStationsService stationsService;
    @Autowired
    BExRailwayDelListService arriveDelService;
    @Autowired
    BExRailwayDelContaService contaDelService;
    @Autowired
    BExRailwayReportHeadService headService;
    @Autowired
    PCustomsCodeMapper customsCodeMapper;
    @Autowired
    CDischargesMapper cDischargesMapper;
    @Autowired
    BExRailwayReportHeadMapper reportHeadMapper;

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 列表查询 todo majunzi
     **/
    @Override
    public IPage<BExRailwayDelReportHead> page(ArriveQuery query, Integer pageNum, Integer pageSize) {
        Page<BExRailwayDelReportHead> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BExRailwayDelReportHead> wrapper = new LambdaQueryWrapper<>();

        Set<String> ids = new HashSet<>();
        int i = 0; //计数器
        if (StringUtils.isNotEmpty(query.getArriveNo())) {
            i++;
            ids.addAll(arriveDelService.listHeadIdsByPartArriveNo(query.getArriveNo()));

        }

        if (StringUtils.isNotEmpty(query.getContaNo())) {
            i++;
            if (i == 1) {//仅集装箱号
                ids.addAll(contaDelService.listHeadIdsByPartContaNo(query.getContaNo()));
            } else if (i == 2) {//运抵单编号+集装箱号
                ids.retainAll(contaDelService.listHeadIdsByPartContaNo(query.getContaNo()));
            }
        }
        if (i > 0 && CollectionUtils.isEmpty(ids)) return null;

        //海关代码
        wrapper.eq((StringUtils.isNotEmpty(query.getCustomsCode())), BExRailwayDelReportHead::getCustomsCode, query.getCustomsCode())
                //卸货地代码
                .eq(StringUtils.isNotEmpty(query.getDischargePlace()), BExRailwayDelReportHead::getDischargePlace, query.getDischargePlace())
                //运抵报文编号
                .like(StringUtils.isNotEmpty(query.getMessageId()), BExRailwayDelReportHead::getMessageId, query.getMessageId())
                //审核状态
                .eq(StringUtils.isNotEmpty(query.getAuditStatus()), BExRailwayDelReportHead::getAuditStatus, query.getAuditStatus())
                //运抵时间
                .ge((null != query.getStartArriveTime() && query.getStartArriveTime() > 0), BExRailwayDelReportHead::getArriveTime, query.getStartArriveTime())
                .le((null != query.getEndArriveTime() && query.getEndArriveTime() > 0), BExRailwayDelReportHead::getArriveTime, query.getEndArriveTime())
                //创建时间
                .ge((null != query.getStartCreateTime() && query.getStartCreateTime() > 0), BExRailwayDelReportHead::getCreateTime, query.getStartCreateTime())
                .le((null != query.getEndCreateTime() && query.getEndCreateTime() > 0), BExRailwayDelReportHead::getCreateTime, query.getEndCreateTime())
                .in((!CollectionUtils.isEmpty(ids)), BExRailwayDelReportHead::getId, ids)
                .orderByDesc(BExRailwayDelReportHead::getCreateTime);


        IPage<BExRailwayDelReportHead> reportHeadIPage = super.page(page, wrapper);
        if (null != reportHeadIPage) {
            List<BExRailwayDelReportHead> delReportHeadList = reportHeadIPage.getRecords();
            if (null != delReportHeadList) {
                delReportHeadList.forEach(delReportHead -> {
                    // 海关代码
                    String customsCode = delReportHead.getCustomsCode();
                    // 卸货地代码
                    String dischargePlace = delReportHead.getDischargePlace();
                    if (null != customsCode) {
                        // 根据code获取得到name
                        PCustomsCode pCustomsCode = customsCodeMapper.selectOne(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, customsCode).eq(PCustomsCode::getEnable, Status.ENABLED.getValue()));
                        if (null != pCustomsCode) {
                            // 获取得到海关中文名称
                            String customsName = pCustomsCode.getName();
                            delReportHead.setCustomsName(customsName);
                        }
                    }
                    if (!StringUtils.isEmpty(dischargePlace)) {
                        // 根据卸货地代码查询得到已启用的卸货地中文名称
                        CDischarges cDischarges = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, dischargePlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
                        if (null != cDischarges) {
                            // 获取得到卸货地中文名称
                            String dischargesName = cDischarges.getName();
                            delReportHead.setDischargePlaceName(dischargesName);
                        }
                    }
                });
            }
        }

        return reportHeadIPage;

    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 查看详情
     **/
    @Override
    public ArriveReportDel4DetailVo detail(String id) {
        ArriveReportDel4DetailVo vo = new ArriveReportDel4DetailVo();
        BExRailwayDelReportHead delReportHead = getById(id);
        if (null != delReportHead) {
            // 海关代码
            String customsCode = delReportHead.getCustomsCode();
            // 卸货地代码
            String dischargePlace = delReportHead.getDischargePlace();
            if (null != customsCode) {
                // 根据code获取得到name
                PCustomsCode pCustomsCode = customsCodeMapper.selectOne(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, customsCode).eq(PCustomsCode::getEnable, Status.ENABLED.getValue()));
                if (null != pCustomsCode) {
                    // 获取得到海关中文名称
                    String customsName = pCustomsCode.getName();
                    delReportHead.setCustomsName(customsName);
                }
            }
            if (!StringUtils.isEmpty(dischargePlace)) {
                // 根据卸货地代码查询得到已启用的卸货地中文名称
                CDischarges cDischarges = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, dischargePlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
                if (null != cDischarges) {
                    // 获取得到卸货地中文名称
                    String dischargesName = cDischarges.getName();
                    delReportHead.setDischargePlaceName(dischargesName);
                }
            }
        }
        vo.setHead(delReportHead);
        vo.setArriveList(arriveDelService.list(arriveDelService.getHeadIdWrapper(id)));
        vo.setContaList(contaDelService.list(contaDelService.getHeadIdWrapper(id)));
        return vo;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 新增保存
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult save(ArriveReportDel4AddVo vo) throws BizException {
        checkEmpty(vo);
        BExRailwayDelReportHead head = buildDelHeadByMessageAddId(vo.getHead().getMessageAddId());
        // 扭转运抵作废保存接口消息类型，如果消息类型是铁路出口报告申报，那么扭转类型为铁路出口报告作废
        head.setMessageType(MessageTypeFlag.WLJK_ERRD.getValue());
        super.save(head);
        arriveDelService.add(head.getId(), vo.getArriveList());

        return JsonResult.data(head.getId());
    }

    private BExRailwayDelReportHead buildDelHeadByMessageAddId(String messageAddId) throws BizException {
        BExRailwayReportHead head = headService.getOne(new LambdaQueryWrapper<BExRailwayReportHead>().eq(BExRailwayReportHead::getMessageId, messageAddId));
        if (null == head) throw new BizException(String.format("运抵报文编号[%s]不存在", messageAddId));
        return BExRailwayDelReportHead.builder()
                .id(UniqueIdUtil.getUUID())
                .messageId(UniqueIdUtil.getMsgId(head.getCustomsCode(), stationsService.getCode()))
                .messageAddId(messageAddId)
                .messageType(MessageTypeFlag.WLJK_ERRD.getValue())
                .customsCode(head.getCustomsCode())
                .dischargePlace(head.getDischargePlace())
                .arriveTime(head.getArriveTime())
                .auditStatus(DeclareStatusFlag.PRE_DECLARE.getValue())
                .messageMode(head.getMessageMode())
                .createTime(System.currentTimeMillis())
                .build();

    }


    private void checkEmpty(ArriveReportDel4AddVo vo) throws BizException {
        if (StringUtils.isEmpty(vo.getHead().getMessageAddId())) throw new BizException("运抵报文编号不能未空");
        if (CollectionUtils.isEmpty(vo.getArriveList())) throw new BizException("运抵单不能为空");
    }


    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 编辑详情
     **/
    @Override
    public ArriveReportDel4UpdateVo detail4Update(String id) throws BizException {
        ArriveReportDel4UpdateVo vo = new ArriveReportDel4UpdateVo();
        vo.setHead(checkUpdate(id));
        vo.setArriveList(arriveDelService.list(arriveDelService.getHeadIdWrapper(id)));
        return vo;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 删除, 删除 表头，运抵单，集装箱
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) throws BizException {
        checkUpdate(id);
        super.removeById(id);
        contaDelService.remove(contaDelService.getHeadIdWrapper(id));
        arriveDelService.remove(arriveDelService.getHeadIdWrapper(id));
    }

    @Override
    public void deleteByListId(String listId) throws BizException {
        BExRailwayDelList list = arriveDelService.getOne(new LambdaQueryWrapper<BExRailwayDelList>().eq(BExRailwayDelList::getId, listId));
        if (null == list) throw new BizException("找不到该运抵单");
        if (arriveDelService.list(arriveDelService.getHeadIdWrapper(list.getExRailwayReportDelHeadId())).size() <= 1)
            throw new BizException("最后一条运抵单了，无法删除");
        arriveDelService.removeById(listId);
        contaDelService.remove(contaDelService.getListIdWrapper(listId));

    }

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 编辑/删除：针对待申报和退单的数据
     **/
    @Override
    public BExRailwayDelReportHead checkUpdate(String id) throws BizException {
        if (StringUtils.isEmpty(id)) throw new BizException("表头id不能为空");
        BExRailwayDelReportHead head = getById(id);
        if (DeclareStatusFlag.PRE_DECLARE.getValue().equals(head.getAuditStatus()) ||
                DeclareStatusFlag.DECLARE_NO_PASS.getValue().equals(head.getAuditStatus())) return head;
        throw new BizException(String.format("仅[%s,%s]可编辑", DeclareStatusFlag.PRE_DECLARE.getTitle(), DeclareStatusFlag.DECLARE_NO_PASS.getTitle()));
    }

    /**
     * 运抵报告作废
     *
     * @param id
     * @return
     */
    @Override
    public JsonResult declare(String id) {
        BExRailwayDelReportHead reportHead = this.baseMapper.selectById(id);
        if (null != reportHead) {
            // 根据表头id查询得到运抵编号信息
            List<BExRailwayDelList> exRailwayLists = arriveDelService.list(arriveDelService.getHeadIdWrapper(reportHead.getId()));
            // 运抵编号信息集合
            List<ReqArriveInfo> reqArriveInfoList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(exRailwayLists)) {
                exRailwayLists.forEach(ex -> {
                    ReqArriveInfo reqArriveInfo = ReqArriveInfo.builder()
                            .arriveNo(ex.getArriveNo())
                            .build();

                    reqArriveInfoList.add(reqArriveInfo);
                });
            }

            // 获取得到海关代码
            String customsCode = reportHead.getCustomsCode();

            // TODO
            ReqExMessageList reqExMessageList = ReqExMessageList.builder()
                    // 海关代码
                    .declPort(customsCode)
                    .dischargePlace(reportHead.getDischargePlace())
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
                    .functionCode(FunctionCodeFlag.FUNCTION_CODE_3.getValue())
                    .messageType(reportHead.getMessageType())
                    .auditTime(DateUtils.getFullTimeStamp())
                    // 消息类型为WLJK_ERRA或WLJK_ERRD时：企业在物流系统备案的代码，备案规则为：4 位备案关区代码+9 位企业组织机构代码，其中不得有“-”等字符。
                    // 消息类型为WLJK_TGR时：发送方为主管海关的4位关区号
                    .sender(sender)// TODO 发送者
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
                    + XmlTypeConstant.WLJK_ERRD
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

                BExRailwayDelReportHead head = BExRailwayDelReportHead.builder()
                        .id(id)// 申请报文表头id
                        .auditTime(System.currentTimeMillis())// 申报时间
                        .auditStatus(DeclareStatusFlag.PRO_DECLARE.getValue())// 申报状态
                        .build();

                this.baseMapper.updateById(head);

                // TODO
                BExRailwayReportHead bExRailwayReportHead = reportHeadMapper.selectOne(Wrappers.<BExRailwayReportHead>lambdaQuery().eq(BExRailwayReportHead::getMessageId, reportHead.getMessageAddId()));
                if (null != bExRailwayReportHead) {
                    exRailwayLists.forEach(delConta -> {
                        // TODO 更新理货运抵表中的运单状态为作废中
                        BExRailwayList billInfo = BExRailwayList.builder()
                                .auditStatus(WayBillAuditStatus.WayBillAudit_07.getValue())
                                .build();

                        // 根据提单号来更新申请表中的状态为作废中
                        listMapper.update(billInfo, Wrappers.<BExRailwayList>lambdaQuery()
                                .eq(BExRailwayList::getExRailwayReportHeadId, reportHead.getId())
                                .eq(BExRailwayList::getArriveNo, delConta.getArriveNo()));
                    });
                }
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