/**
 * @filename:BRailwayLoadDelReportHeadServiceImpl 2019年08月20日
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
import cn.samples.depot.web.bean.load.LoadDelAddVo;
import cn.samples.depot.web.bean.load.LoadDelUpdateVo;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.cz.mapper.*;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.entity.xml.MessageHead;
import cn.samples.depot.web.entity.xml.load.req.ReqDelBillInfo;
import cn.samples.depot.web.entity.xml.load.req.ReqDelLoadMessage;
import cn.samples.depot.web.entity.xml.load.req.ReqDelLoadMessageList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 装车记录单作废报文表头——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
@Slf4j
public class BRailwayLoadDelReportHeadServiceImpl extends ServiceImpl<BRailwayLoadDelReportHeadMapper, BRailwayLoadDelReportHead> implements BRailwayLoadDelReportHeadService {

    /**
     * 装车记录单作废报文表体集装箱信息
     */
    @Autowired
    private BRailwayLoadDelContaMapper loadDelContaMapper;

    /**
     * 装车记录单作废报文表体运抵单信息
     */
    @Autowired
    private BRailwayLoadDelListMapper loadDelListMapper;

    /**
     * 场站信息
     */
    @Autowired
    CStationsMapper cStationsMapper;
    /**
     * 装车记录单申请报文表体运抵单信息
     */
    @Autowired
    private BRailwayLoadContaMapper loadContaMapper;
    /**
     * 发送海关报文的文件放置路径
     */
    @Value("${message.file.path:1.0}")
    private String MESSAGE_FILE_PATH;
    @Autowired
    private BRailwayLoadDelContaService delContaService;
    @Autowired
    private BRailwayLoadDelListService delListService;
    @Autowired
    private BRailwayLoadReportHeadService headService;
    @Autowired
    CStationsService stationsService;
    @Autowired
    PCustomsCodeMapper customsCodeMapper;
    @Autowired
    CDischargesMapper cDischargesMapper;
    @Autowired
    BRailwayLoadReportHeadMapper reportHeadMapper;

    /**
     * 单个删除装车记录单作废报文
     *
     * @param id
     * @return
     */
    @Override
    public JsonResult deleteLoadDelReportById(String id) {
        // 根据id查询得到理货作废信息
        BRailwayLoadDelReportHead head = this.baseMapper.selectById(id);
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
                log.info("删除装车记录单作废报文表头：{}", deleteHead);
                if (deleteHead > 0) {
                    int deleteLoadConta = loadDelContaMapper.delete(Wrappers.<BRailwayLoadDelConta>lambdaQuery().eq(BRailwayLoadDelConta::getRailwayLoadDelReportHeadId, id));
                    log.info("删除装车记录单作废报文表体集装箱信息：{}", deleteLoadConta);

                    int deleteList = loadDelListMapper.delete(Wrappers.<BRailwayLoadDelList>lambdaQuery().eq(BRailwayLoadDelList::getRailwayLoadDelReportHeadId, id));
                    log.info("删除装车记录单作废报文表体：{}", deleteList);
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
     * 查看指定装车记录单作废报文数据
     *
     * @param id
     * @return
     */
    @Override
    public LoadDelUpdateVo queryLoadDelReportById(String id) {
        return LoadDelUpdateVo.builder()
                .head(this.baseMapper.selectById(id))
                .contaList(loadDelContaMapper.selectList(Wrappers.<BRailwayLoadDelConta>lambdaQuery()
                        .eq(BRailwayLoadDelConta::getRailwayLoadDelReportHeadId, id)))
                .build();
    }

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 分页查询
     **/
    @Override
    public IPage<BRailwayLoadDelReportHead> page(LoadQuery query, Integer pageNum, Integer pageSize) {
        Set<String> ids = new HashSet<>();
        int i = 0; //计数器
        //运抵单
        if (StringUtils.isNotEmpty(query.getArriveNo())) {
            i++;
            ids.addAll(delListService.listHeadIdsByPartArriveNo(query.getArriveNo()));
        }

        //集装箱
        if (StringUtils.isNotEmpty(query.getContaNo()) || StringUtils.isNotEmpty(query.getBillNo())) {
            i++;
            if (i == 1) {
                ids.addAll(delContaService.listHeadIdsByLoadQuery(query));
            } else if (i == 2) {
                ids.retainAll(delContaService.listHeadIdsByLoadQuery(query));
            }
        }
        if (i > 0 && CollectionUtils.isEmpty(ids)) return null;
        //表头
        Page<BRailwayLoadDelReportHead> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BRailwayLoadDelReportHead> wrapper = new LambdaQueryWrapper<>();
        //装卸地代码
        wrapper.eq(StringUtils.isNotEmpty(query.getDischargePlace()), BRailwayLoadDelReportHead::getDischargePlace, query.getDischargePlace())
                //报文编号
                .like(StringUtils.isNotEmpty(query.getMessageId()), BRailwayLoadDelReportHead::getMessageId, query.getMessageId())
                //创建时间
                .ge((null != query.getStartCreateTime() && query.getStartCreateTime() > 0), BRailwayLoadDelReportHead::getCreateTime, query.getStartCreateTime())
                .le((null != query.getEndCreateTime() && query.getEndCreateTime() > 0), BRailwayLoadDelReportHead::getCreateTime, query.getEndCreateTime())
                .in((!CollectionUtils.isEmpty(ids)), BRailwayLoadDelReportHead::getId, ids)
                .orderByDesc(BRailwayLoadDelReportHead::getCreateTime);

        IPage<BRailwayLoadDelReportHead> loadDelReportHeadIPage = super.page(page, wrapper);
        if (null != loadDelReportHeadIPage) {
            List<BRailwayLoadDelReportHead> reportHeadList = loadDelReportHeadIPage.getRecords();
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

        return loadDelReportHeadIPage;
    }

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult saveVo(LoadDelAddVo vo) throws BizException {
        BRailwayLoadDelReportHead head = buildDelHeadByMessageAddId(vo.getHead().getMessageAddId());
        // 扭转运抵作废保存接口消息类型，如果消息类型是铁路出口报告申报，那么扭转类型为铁路出口报告作废
        head.setMessageType(MessageTypeFlag.WLJK_TLD.getValue());
        save(head);
        List<BRailwayLoadConta> contaList = vo.getContaList();
        delContaService.add(head.getId(), vo.getContaList());

        return JsonResult.data(head.getId());
    }

    private BRailwayLoadDelReportHead buildDelHeadByMessageAddId(String messageAddId) throws BizException {
        BRailwayLoadReportHead head = headService.getByMessageId(messageAddId);
        if (null == head) throw new BizException(String.format("运抵报文编号[%s]不存在", messageAddId));
        return BRailwayLoadDelReportHead.builder()
                .id(UniqueIdUtil.getUUID())
                .messageId(UniqueIdUtil.getMsgId(head.getCustomsCode(), stationsService.getCode()))
                .messageAddId(messageAddId)
                .messageType(MessageTypeFlag.WLJK_TLD.getValue())
                .customsCode(head.getCustomsCode())
                .dischargePlace(head.getDischargePlace())
                .auditStatus(DeclareStatusFlag.PRE_DECLARE.getValue())
                .createTime(System.currentTimeMillis())
                .build();

    }


    @Override
    public BRailwayLoadDelReportHead checkUpdate(String id) throws BizException {
        if (StringUtils.isEmpty(id)) throw new BizException("表头id不能为空");
        BRailwayLoadDelReportHead head = getById(id);
        if (null == head) throw new BizException("找不到对应装车作废记录");
        if (DeclareStatusFlag.PRE_DECLARE.getValue().equals(head.getAuditStatus()) || DeclareStatusFlag.DECLARE_NO_PASS.getValue().equals(head.getAuditStatus()))
            return head;
        throw new BizException(String.format("仅[%s,%s]状态可操作", DeclareStatusFlag.PRE_DECLARE.getTitle(), DeclareStatusFlag.DECLARE_NO_PASS.getTitle()));

    }

    /**
     * 装车记录单作废
     *
     * @param id
     * @return
     */
    @Override
    public JsonResult declare(String id) {
        // 装车记录单作废表头信息
        BRailwayLoadDelReportHead loadDelReportHead = this.baseMapper.selectById(id);
        if (null != loadDelReportHead) {
            List<ReqDelBillInfo> delBillInfoList = new ArrayList<>();
            List<BRailwayLoadDelConta> loadDelContaList = loadDelContaMapper.selectList(Wrappers.<BRailwayLoadDelConta>lambdaQuery().eq(BRailwayLoadDelConta::getRailwayLoadDelReportHeadId, loadDelReportHead.getId()));
            if (CollectionUtils.isNotEmpty(loadDelContaList)) {
                loadDelContaList.forEach(loadDelConta -> {
                    ReqDelBillInfo delBillInfo = ReqDelBillInfo.builder()
                            .carriageNo(loadDelConta.getCarriageNo())
                            .billNo(loadDelConta.getBillNo())
                            .notes(loadDelConta.getNotes())
                            .build();

                    delBillInfoList.add(delBillInfo);
                });
            }

            // 发送海关的装车记录单报废表体信息
            ReqDelLoadMessageList delLoadMessageList = ReqDelLoadMessageList.builder()
                    .dischargePlace(loadDelReportHead.getDischargePlace())
                    .delBillInfoList(delBillInfoList)
                    .build();

            // 获取得到当前用户信息
            String currentStations = "";
            // 查询获取得到数据库场站信息
            CStations cStations = cStationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getEnable, Status.ENABLED.getValue()));
            if (null != cStations) {
                // 当前场站code
                currentStations = cStations.getCode();
            }

            // 发送海关的装车记录单报废表头信息
            MessageHead delMessageHead = MessageHead.builder()
                    .messageId(loadDelReportHead.getMessageId())
                    .functionCode(FunctionCodeFlag.FUNCTION_CODE_3.getValue())
                    .messageType(loadDelReportHead.getMessageType())
                    .auditTime(DateUtils.getFullTimeStamp())
                    // 监管场所经营人
                    .sender(loadDelReportHead.getCustomsCode() + currentStations)
                    // 海关代码
                    .receiver(loadDelReportHead.getCustomsCode())
                    .version(Constants.MESSAGE_VERSION)
                    .build();

            // 发送海关的装车记录单报废报文
            ReqDelLoadMessage delLoadMessage = ReqDelLoadMessage.builder()
                    .messageHead(delMessageHead)
                    .delLoadMessageList(delLoadMessageList)
                    .build();

            String xmlStr = XstreamUtil.xmlAppendHead(XmlUtil.serializeToStr(delLoadMessage, CharsetUtil.UTF_8));
            log.info("文件xml格式：{}", xmlStr);

            // 保存的xml文件名称
            String xmlFileName = Constants.XML_PREFIX
                    + XmlTypeConstant.WLJK_TLD
                    + Constants.XML_BODY
                    + Constants.UNDER_LINE
                    + currentStations
                    + Constants.UNDER_LINE
                    + DateUtils.getFullTimeStamp()
                    + Constants.XML_SUFFIX;

            boolean createFile = FileUtils.writeContent(MESSAGE_FILE_PATH, xmlFileName, xmlStr);
            // 根据id申请报文表头id来更新申报时间和审核状态字段
            if (createFile) {
                log.info("文件写入成功：{}", createFile);
                BRailwayLoadDelReportHead head = BRailwayLoadDelReportHead.builder()
                        .id(id)// 申请报文表头id
                        .auditTime(System.currentTimeMillis())// 申报时间
                        .auditStatus(DeclareStatusFlag.PRO_DECLARE.getValue())// 更新理货作废的状态为作废中
                        .build();

                // 更新表头的状态为申报海关
                this.baseMapper.updateById(head);

                BRailwayLoadDelConta loadDelConta = BRailwayLoadDelConta.builder()
                        .railwayLoadDelReportHeadId(id)
                        .auditStatus(WayBillAuditStatus.WayBillAudit_07.getValue())// 更新装车记录单下的集装箱的状态为作废中
                        .build();

                loadDelContaMapper.update(loadDelConta, Wrappers.<BRailwayLoadDelConta>lambdaQuery().eq(BRailwayLoadDelConta::getRailwayLoadDelReportHeadId, id));

                BRailwayLoadReportHead reportHead = reportHeadMapper.selectOne(Wrappers.<BRailwayLoadReportHead>lambdaQuery().eq(BRailwayLoadReportHead::getMessageId, loadDelReportHead.getMessageAddId()));
                if (null != reportHead) {
                    loadDelContaList.forEach(delConta -> {
                        // TODO 更新理货申请表中的运单状态为作废中
                        BRailwayLoadConta loadConta = BRailwayLoadConta.builder()
                                .auditStatus(WayBillAuditStatus.WayBillAudit_07.getValue())
                                .build();

                        // 根据提单号来更新申请表中的状态为作废中
                        loadContaMapper.update(loadConta, Wrappers.<BRailwayLoadConta>lambdaQuery()
                                .eq(BRailwayLoadConta::getRailwayLoadReportHeadId, reportHead.getId())
                                .eq(BRailwayLoadConta::getBillNo, delConta.getBillNo()));
                    });
                }
            } else {
                log.error("文件写入失败：{}", createFile);
                return JsonResult.error("9998", "装车记录单报废，文件写入失败");
            }

            return JsonResult.data(createFile);
        }

        return JsonResult.error("9998", "装车记录单报废失败");
    }
}