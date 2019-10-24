/**
 * @filename:BRelReportHeadServiceImpl 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.samples.depot.common.model.*;
import cn.samples.depot.common.utils.DateUtils;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.common.utils.UniqueIdUtil;
import cn.samples.depot.web.bean.report.BRelReportHeadQuery;
import cn.samples.depot.web.bean.report.BRelReportHeadRsp;
import cn.samples.depot.web.bean.report.BRelReportResponse;
import cn.samples.depot.web.cz.mapper.*;
import cn.samples.depot.web.cz.service.BRelReportHeadService;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.entity.xml.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 放行指令表头——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Service
public class BRelReportHeadServiceImpl extends ServiceImpl<BRelReportHeadMapper, BRelReportHead> implements BRelReportHeadService {

    // 放行指令表体提单信息
    @Autowired
    BRelBillInfoMapper billInfoMapper;

    // 放行指令表体运输工具信息
    @Autowired
    BRelShipInfoMapper shipInfoMapper;

    // 放行指令表体集装箱信息
    @Autowired
    BRelContaInfoMapper contaInfoMapper;

    // 放行指令表体单证信息
    @Autowired
    BRelFormInfoMapper formInfoMapper;

    @Autowired
    BRelReportHeadMapper bRelReportHeadMapper;

    @Autowired
    CStationsMapper cStationsMapper;

    @Autowired
    CDischargesMapper cDischargesMapper;

    @Autowired
    PCustomsCodeMapper pCustomsCodeMapper;

    @Autowired
    PRelTypeMapper pRelTypeMapper;

    /**
     * 海关放行执行分页列表
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<BRelReportHeadRsp> selectRelReportListPage(BRelReportHeadQuery query, Integer pageNum, Integer pageSize) {
        // 当前页，总条数 构造 page 对象
        Page<BRelReportHeadRsp> page = new Page<>(pageNum, pageSize);
        List<BRelReportHeadRsp> reportHeadRspList = this.baseMapper.selectRelReportListPage(page, query);
        if (CollectionUtil.isNotEmpty(reportHeadRspList)) {
            reportHeadRspList.forEach(report -> {
                String customsCode = report.getCustomsCode();
                if (StringUtil.isNotEmpty(customsCode)) {
                    PCustomsCode pCustomsCode = pCustomsCodeMapper.selectOne(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, customsCode).eq(PCustomsCode::getEnable, Status.ENABLED.getValue()));
                    if (null != pCustomsCode) {
                        String customsName = pCustomsCode.getName();
                        // 根据海关代码获取得到海关名称
                        report.setCustomsName(customsName);
                    }
                }

                String stationCode = report.getStationCode();
                if (StringUtil.isNotEmpty(stationCode)) {
                    CStations cStations = cStationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getOperatorCode, stationCode).eq(CStations::getEnable, Status.ENABLED.getValue()));
                    if (null != cStations) {
                        String stationsName = cStations.getName();
                        // 根据场站表获取得到场站名称
                        report.setStationName(stationsName);
                    }
                }
            });

        }

        return page.setRecords(reportHeadRspList);
    }

    /**
     * 处理海关响应的放行指令报文回执
     *
     * @param relMessage
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult xmlReturnRelHandle(RelMessage relMessage) {
        if (null != relMessage) {
            // 放行指令表头信息
            MessageHead messageHead = relMessage.getMessageHead();
            // 获取得到放行指令表体信息
            RelMessageList relMessageList = relMessage.getMessageList();
            // 生成放行指令表头的uuid
            String headUUID = UniqueIdUtil.getUUID();
            if (null != messageHead) {
                // 消息创建时间
                String auditTime = messageHead.getAuditTime();
                Long auditDateTime;
                if (StringUtils.isEmpty(auditTime)) {
                    // 如果为空就设置为当前时间
                    auditDateTime = System.currentTimeMillis();
                } else {
                    // 日期类型转换入库，字符串类型转成Long类型
                    auditDateTime = DateUtil.parse(messageHead.getAuditTime()).getTime();
                }
                // 获取得到进出口标识
                String ieFlag = "";
                ShipInfo si = relMessageList.getShipInfo();
                if (null != relMessageList) {
                    // 获取得到进出口标识
                    ieFlag = relMessageList.getIeFlag();
                }
                if (null != si && !StringUtils.isEmpty(ieFlag)) {
                    ieFlag = si.getIeFlag();
                }

                // 放行指令表头信息
                BRelReportHead reportHead = BRelReportHead.builder()
                        .id(headUUID)
                        .messageId(messageHead.getMessageId())
                        .messageType(messageHead.getMessageType())
                        .customsCode(messageHead.getSender())
                        .stationCode(messageHead.getReceiver())
                        .sendId(messageHead.getSender())
                        .sendTime(auditDateTime)
                        .iEFlag(ieFlag)
                        .createTime(System.currentTimeMillis()).build();


                // 插入放行指令表头信息
                this.baseMapper.insert(reportHead);
            }

            if (null != relMessageList) {
                // 放行指令表体提单信息
                List<BillInfo> billInfoList = relMessageList.getBillInfos();
                if (!CollectionUtils.isEmpty(billInfoList)) {
                    // 循环遍历插入bill提单信息
                    billInfoList.forEach(bill -> {
                        // 生成放行指令表体提单的uuid
                        String billUUID = UniqueIdUtil.getUUID();
                        // 放行指令表体提单信息实体bean
                        BRelBillInfo bRelBillInfo = BRelBillInfo.builder()
                                .id(billUUID)
                                .relReportHeadId(headUUID)
                                .billNo(bill.getBillNo())
                                .carriageNo(bill.getCarriageNo())
                                .h2000ArriveNo(bill.getH2000ArriveNo())
                                .relType(bill.getRelMode())
                                // todo
                                .relTime(null != bill.getRelTime() ? DateUtils.getToLong(bill.getRelTime()) : null)
                                .packNo(bill.getPackNo())
                                .grossWt(bill.getGrossWt())
                                .dischargePlace(bill.getDischargePlace())
                                .createTime(System.currentTimeMillis())
                                .build();

                        // 插入放行指令表体提单信息
                        billInfoMapper.insert(bRelBillInfo);

                        // 放行指令表体单证信息集合
                        List<FormInfo> formInfoList = bill.getFormInfos();
                        if (!CollectionUtils.isEmpty(formInfoList)) {
                            // 循环遍历放行指令表体单证信息集合
                            formInfoList.forEach(form -> {
                                // 放行指令表体单证信息bean
                                BRelFormInfo bRelFormInfo = BRelFormInfo.builder()
                                        .id(UniqueIdUtil.getUUID())
                                        .relReportHeadId(headUUID)
                                        .billNo(bill.getBillNo())
                                        .formId(form.getFormNo())
                                        .formType(form.getFormType())
                                        .createTime(System.currentTimeMillis())
                                        .build();

                                // 插入放行指令表体单证信息
                                formInfoMapper.insert(bRelFormInfo);
                            });
                        }
                    });
                }

                // 获取得到放行指令表体集装箱信息
                List<ContainerInfo> containerInfoList = relMessageList.getContainerInfos();
                if (!CollectionUtils.isEmpty(containerInfoList)) {
                    // 放行指令表体集装箱信息循环遍历
                    containerInfoList.forEach(containerInfo -> {
                        // 放行时间
                        String relTime = containerInfo.getRelTime();
                        Long relDateTime;
                        if (StringUtils.isEmpty(relTime)) {
                            // 如果为空就设置为当前时间
                            relDateTime = System.currentTimeMillis();
                        } else {
                            // 日期类型转换入库，字符串类型转成Long类型
                            relDateTime = DateUtil.parse(containerInfo.getRelTime()).getTime();
                        }
                        // 放行指令表体集装箱信息bean
                        BRelContaInfo bRelContaInfo = BRelContaInfo.builder()
                                .id(UniqueIdUtil.getUUID())
                                .relHeadId(headUUID)
                                .carriageNo(containerInfo.getCarriageNo())
                                .contaNo(containerInfo.getContaNo())
                                .relType(containerInfo.getRelMode())
                                .relTime(relDateTime)
                                .createTime(System.currentTimeMillis())
                                .build();

                        // 插入放行指令表体集装箱信息
                        contaInfoMapper.insert(bRelContaInfo);
                    });
                }

                // 获取得到放行指令表体运输工具信息
                ShipInfo shipInfo = relMessageList.getShipInfo();
                if (null != shipInfo) {
                    // 放行指令表体运输工具信息bean
                    BRelShipInfo bRelShipInfo = BRelShipInfo.builder()
                            .id(UniqueIdUtil.getUUID())
                            .relReportHeadId(headUUID)
                            .shipId(shipInfo.getShipId())
                            .iEFlag(shipInfo.getIeFlag())
                            .shipNameEn(shipInfo.getShipNameEn())
                            .voyageNo(shipInfo.getVoyageNo())
                            .lineFlag(shipInfo.getLineFlag())
                            .createTime(System.currentTimeMillis())
                            .build();

                    // 插入放行指令表体运输工具信息
                    shipInfoMapper.insert(bRelShipInfo);
                }
            }

            return JsonResult.success();
        }

        return JsonResult.error("解析得到的放行指令回执报文为空");
    }

    /**
     * 根据放行指令id查询得到放行指令的信息
     *
     * @param id
     * @return
     */
    @Override
    public JsonResult queryRelReportDetail(String id) {
        // 根据放心指令id获取得到放行指令表头信息
        BRelReportHead relReportHead = this.baseMapper.selectById(id);
        // 根据放行指令表头id查询得到交通工具信息
        BRelShipInfo bRelShipInfo = shipInfoMapper.selectOne(Wrappers.<BRelShipInfo>lambdaQuery().eq(BRelShipInfo::getRelReportHeadId, id));
        // 根据放行指令表头id查询得到放行指令表体提单信息
        List<BRelBillInfo> bRelBillInfoList = billInfoMapper.selectList(Wrappers.<BRelBillInfo>lambdaQuery().eq(BRelBillInfo::getRelReportHeadId, id));
        // 根据放行指令表头id查询得到放行指令表体集装箱信息
        List<BRelContaInfo> bRelContaInfoList = contaInfoMapper.selectList(Wrappers.<BRelContaInfo>lambdaQuery().eq(BRelContaInfo::getRelHeadId, id));
        // 根据放行指令表头id查询得到放行指令表体单证信息
        List<BRelFormInfo> bRelFormInfoList = formInfoMapper.selectList(Wrappers.<BRelFormInfo>lambdaQuery().eq(BRelFormInfo::getRelReportHeadId, id));
        if (null != relReportHead) {
            // 场站编号
            String stationCode = relReportHead.getStationCode();
            // 场站名称
            String stationName = "";
            // 根据场站编号查询场站表得到场站名称
            if (!StringUtils.isEmpty(stationCode)) {
//                CStations cStations = cStationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getCode, stationCode).eq(CStations::getEnable, Status.ENABLED.getValue()));
                CStations cStations = cStationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getOperatorCode, stationCode).eq(CStations::getEnable, Status.ENABLED.getValue()));
                if (null != cStations) {
                    stationName = cStations.getName();
                }
            }

            // 构建返回放行指令明细对象
            BRelReportResponse builderRsp = BRelReportResponse.builder()
                    .id(relReportHead.getId())
                    .messageId(relReportHead.getMessageId())
                    .customsCode(relReportHead.getCustomsCode())
                    .messageType(relReportHead.getMessageType())
                    .stationCode(stationCode)
                    .stationName(stationName)
                    .relTime(relReportHead.getSendTime())
                    .createTime(relReportHead.getCreateTime()).build();

            // 如果运输工具不为空
            if (null != bRelShipInfo) {
                ShipInfo shipInfo = new ShipInfo();
                BeanUtils.copyProperties(bRelShipInfo, shipInfo);
                shipInfo.setIeFlag(bRelShipInfo.getIEFlag());

                builderRsp.setShipInfo(shipInfo);
            }

            // 放行指令表体提单信息不为空
            if (!CollectionUtils.isEmpty(bRelBillInfoList)) {
                List<BillInfo> billInfoList = new ArrayList<>();
                // 循环遍历提单信息集合
                bRelBillInfoList.forEach(billInfo -> {
                    BillInfo bill = new BillInfo();
                    BeanUtils.copyProperties(billInfo, bill);
                    bill.setRelTime(null != billInfo.getRelTime() ? String.valueOf(billInfo.getRelTime()) : "");
                    bill.setCreateTime(billInfo.getCreateTime());
                    String relType = billInfo.getRelType();
                    bill.setRelMode(relType);
                    if (!StringUtils.isEmpty(relType)) {
                        // TODO 根据提单放行方式获取得到提单放行方式中文名称
                        PRelType pRelType = pRelTypeMapper.selectOne(Wrappers.<PRelType>lambdaQuery().eq(PRelType::getCode, relType).eq(PRelType::getEnable, Status.ENABLED.getValue()));
                        if (null != pRelType) {
                            bill.setRelModeName(pRelType.getName());
                        }
                    }

                    // 获取得到卸货地代码
                    String dischargePlace = bill.getDischargePlace();
                    if (!StringUtils.isEmpty(dischargePlace)) {
                        // 根据卸货地代码查询得到已启用的卸货地中文名称
                        CDischarges cDischarges = cDischargesMapper.selectOne(Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getCode, dischargePlace).eq(CDischarges::getEnable, Status.ENABLED.getValue()));
                        if (null != cDischarges) {
                            // 获取得到卸货地中文名称
                            String dischargesName = cDischarges.getName();
                            bill.setDischargePlaceName(dischargesName);
                        }
                    }
                    billInfoList.add(bill);
                });
                builderRsp.setBillInfoList(billInfoList);
            }

            // 放行指令表体集装箱信息不为空
            if (!CollectionUtils.isEmpty(bRelContaInfoList)) {
                List<ContainerInfo> containerInfoList = new ArrayList<>();
                // 循环遍历集装箱信息集合
                bRelContaInfoList.forEach(contaInfo -> {
                    ContainerInfo conta = new ContainerInfo();
                    BeanUtils.copyProperties(contaInfo, conta);

                    conta.setCreateTime(contaInfo.getCreateTime());
                    conta.setRelTime(null != contaInfo.getRelTime() ? String.valueOf(contaInfo.getRelTime()) : "");
                    String relType = contaInfo.getRelType();
                    conta.setRelMode(relType);
                    if (!StringUtils.isEmpty(relType)) {
                        // TODO 根据提单放行方式获取得到提单放行方式中文名称
                        PRelType pRelType = pRelTypeMapper.selectOne(Wrappers.<PRelType>lambdaQuery().eq(PRelType::getCode, relType).eq(PRelType::getEnable, Status.ENABLED.getValue()));
                        if (null != pRelType) {
                            conta.setRelModeName(pRelType.getName());
                        }
                    }

                    containerInfoList.add(conta);

                });
                builderRsp.setContainerInfoList(containerInfoList);
            }

            // 放行指令表体单证信息不为空
            if (!CollectionUtils.isEmpty(bRelFormInfoList)) {
                List<FormInfo> formInfoList = new ArrayList<>();
                // 循环遍历单证信息集合
                bRelFormInfoList.forEach(formInfo -> {
                    FormInfo form = new FormInfo();
                    form.setBillNo(formInfo.getBillNo());
                    form.setFormNo(formInfo.getFormId());
                    form.setFormType(formInfo.getFormType());
                    form.setCreateTime(formInfo.getCreateTime());
                    formInfoList.add(form);

                });
                builderRsp.setFormInfoList(formInfoList);
            }

            return JsonResult.data(Params.param("detail", builderRsp)
                    // 放行指令，报文类型
                    .set("relMessageTypeFlag", RelMessageTypeFlag.values())
                    // 集装箱放行方式
                    .set("relModeFlag", RelModeFlag.values())
                    // 单证类型
                    .set("formTypeFlag", FormTypeFlag.values())
                    // 提单放行方式
                    .set("relTypeFlag", RelTypeFlag.values())
                    // 航线标记
                    .set("lineFlag", LineFlag.values())
                    // 进出口标记
                    .set("ieFlag", IEFlag.values()));
        }

        return JsonResult.data(null);
    }
}