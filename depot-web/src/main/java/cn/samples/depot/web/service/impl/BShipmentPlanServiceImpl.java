/**
 * @filename:BShipmentPlanServiceImpl 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.AuditStatus;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.UniqueIdUtil;
import cn.samples.depot.web.bean.BShipmentPlanDTO;
import cn.samples.depot.web.bean.ImportExcelPlanFailed;
import cn.samples.depot.web.dto.shipment.BShipmentContainerMsg;
import cn.samples.depot.web.dto.shipment.BShipmentGoodsDetailMsg;
import cn.samples.depot.web.dto.shipment.BShipmentPlanMsg;
import cn.samples.depot.web.dto.shipment.ImportExcelDto;
import cn.samples.depot.web.entity.BShipmentContainer;
import cn.samples.depot.web.entity.BShipmentGoodsDetail;
import cn.samples.depot.web.entity.BShipmentPlan;
import cn.samples.depot.web.entity.CStations;
import cn.samples.depot.web.handler.MyVerifyHandler;
import cn.samples.depot.web.handler.RabbitMessageSender;
import cn.samples.depot.web.mapper.BShipmentContainerMapper;
import cn.samples.depot.web.mapper.BShipmentGoodsDetailMapper;
import cn.samples.depot.web.mapper.BShipmentPlanMapper;
import cn.samples.depot.web.mapper.CStationsMapper;
import cn.samples.depot.web.service.BShipmentPlanService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 发运计划表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Slf4j
@Service
public class BShipmentPlanServiceImpl extends ServiceImpl<BShipmentPlanMapper, BShipmentPlan> implements BShipmentPlanService {
    @Autowired
    private BShipmentPlanMapper planMapper;
    @Autowired
    private BShipmentGoodsDetailMapper goodsDetailMapper;
    @Autowired
    private BShipmentContainerMapper containerMapper;
    @Autowired
    private RabbitMessageSender rabbitMessageSender;
    @Autowired
    CStationsMapper cStationsMapper;

    @Override
    public JsonResult senderForApply(String... ids) {
        // 根据id集合获取得到发送计划集合
        List<BShipmentPlan> planList = planMapper.selectBatchIds(Arrays.asList(ids));
        planList.forEach(x -> {
            BShipmentPlanMsg planMsg = new BShipmentPlanMsg();
            BeanUtils.copyProperties(x, planMsg);

            Wrapper<BShipmentContainer> wrapper_container = Wrappers.<BShipmentContainer>lambdaQuery().eq(BShipmentContainer::getShipmentPlanId, x.getId());
            // 根据发运计划获取得到集装箱信息集合
            List<BShipmentContainer> containerList = containerMapper.selectList(wrapper_container);
            List<BShipmentContainerMsg> containerMsgs = new ArrayList<>();
            containerList.forEach(y -> {
                BShipmentContainerMsg containerMsg = new BShipmentContainerMsg();
                BeanUtils.copyProperties(y, containerMsg);

                containerMsgs.add(containerMsg);

                Wrapper<BShipmentGoodsDetail> wrapper_goods = Wrappers.<BShipmentGoodsDetail>lambdaQuery().eq(BShipmentGoodsDetail::getContainerId, y.getId());
                // 根据集装箱id获取得到商品的信息集合
                List<BShipmentGoodsDetail> goodList = goodsDetailMapper.selectList(wrapper_goods);
                List<BShipmentGoodsDetailMsg> goodMsgs = new ArrayList<>();
                goodList.forEach(z -> {
                    BShipmentGoodsDetailMsg goodsMsg = new BShipmentGoodsDetailMsg();
                    BeanUtils.copyProperties(z, goodsMsg);

                    goodMsgs.add(goodsMsg);
                });
                // 设置商品信息集合
                containerMsg.setGoodsList(goodMsgs);
                log.info("通过mq提交的商品明细信息集合为：{}", goodMsgs.toString());
            });
            log.info("通过mq提交的集装箱信息集合为：{}", containerMsgs.toString());
            // 设置一个发运计划的集装箱信息集合
            planMsg.setContainerList(containerMsgs);
            log.info("通过mq从企业端发送给场站端的消息内容为：{}", planMsg.toString());

            try {
                rabbitMessageSender.sendMessage(planMsg);
            } catch (Exception e) {
                new BizException("rabbitmq发送消息异常");
            }
        });

        return JsonResult.success();
    }

    /**
     * 新增发运计划
     */
    @Override
    @Transactional
    public JsonResult saveBShipmentPlan(BShipmentPlanDTO dto) {
        String id = UUID.randomUUID().toString().replace("-", "");
        dto.getShipmentPlan().setId(id);
        dto.getShipmentPlan().setAuditStatus(AuditStatus.PRE_SUBMIT.getValue());
        dto.getShipmentPlan().setCreateTime(System.currentTimeMillis());
        planMapper.insert(dto.getShipmentPlan());
        dto.getContainerList().forEach(container -> {
            String container_id = UUID.randomUUID().toString().replace("-", "");
            container.setCreateTime(System.currentTimeMillis());
            container.setId(container_id);
            container.setShipmentPlanId(id);
            containerMapper.insert(container);
            dto.getGoodsList().forEach(good -> {
                if (good.getContainerNo().equals(container.getContainerNo())) {
                    good.setContainerId(container_id);
                    good.setShipmentPlanId(id);
                    good.setId(UUID.randomUUID().toString().replace("-", ""));
                    good.setCreateTime(System.currentTimeMillis());
                    goodsDetailMapper.insert(good);
                }
            });
        });
        return JsonResult.success();
    }

    /**
     * 删除发运计划
     */
    @Override
    @Transactional
    public JsonResult deleteBShipmentPlan(String id) {
        planMapper.deleteById(id);
        Wrapper<BShipmentContainer> wrapper_container = Wrappers.<BShipmentContainer>lambdaQuery().eq(BShipmentContainer::getShipmentPlanId, id);
        containerMapper.delete(wrapper_container);
        Wrapper<BShipmentGoodsDetail> wrapper_goods = Wrappers.<BShipmentGoodsDetail>lambdaQuery().eq(BShipmentGoodsDetail::getShipmentPlanId, id);
        goodsDetailMapper.delete(wrapper_goods);
        return JsonResult.success();
    }

    /**
     * 更新发运计划
     *
     * @param id
     * @param bShipmentPlanDTO
     * @return
     */
    @Transactional
    @Override
    public JsonResult updateShipmentPlan(String id, BShipmentPlanDTO bShipmentPlanDTO) {
        BShipmentPlan bShipmentPlan = bShipmentPlanDTO.getShipmentPlan();
        planMapper.updateById(bShipmentPlan);

        List<BShipmentContainer> bShipmentContainers = bShipmentPlanDTO.getContainerList();
        Wrapper<BShipmentContainer> wrapper_container = Wrappers.<BShipmentContainer>lambdaQuery().eq(BShipmentContainer::getShipmentPlanId, id);
        containerMapper.delete(wrapper_container);

        bShipmentContainers.forEach(x -> {
            if (StringUtils.isEmpty(x.getId())) {
                // 自动生成uuid
                String uuid = UUID.randomUUID().toString().replace("-", "");
                x.setId(uuid);
            }

            containerMapper.insert(x);
        });

        List<BShipmentGoodsDetail> bShipmentGoodsDetails = bShipmentPlanDTO.getGoodsList();
        Wrapper<BShipmentGoodsDetail> wrapper_goods = Wrappers.<BShipmentGoodsDetail>lambdaQuery().eq(BShipmentGoodsDetail::getShipmentPlanId, id);
        goodsDetailMapper.delete(wrapper_goods);

        bShipmentGoodsDetails.forEach(x -> {
            if (StringUtils.isEmpty(x.getId())) {
                // 自动生成uuid
                String uuid = UUID.randomUUID().toString().replace("-", "");
                x.setId(uuid);
            }

            goodsDetailMapper.insert(x);
        });

        return JsonResult.success();
    }

//    private static String UPLOADED_FOLDER = "D://temp//";

    /**
     * @Description: 1.校验上传的文件类型及其对应的数据
     * 2.将通过（1）的数据转换为实体对象集合
     * 3.将对象集合传入cacheAndPublish()中
     * 4.封装本次数据校验结果并返回
     * @Param: [file]
     * @Retrun: JsonResult
     */
    @Override
    public void uploadExcel(String stationsCode, MultipartFile file, HttpServletResponse response) {
        // 截取原始文件名里的类型名(最后一个“.”后的数据)
        String fileName = file.getOriginalFilename();
//        byte[] bytes = file.getBytes();
//        Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//        Files.write(path, bytes);

        // 获取上传的文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        // 判断是否属于Excel表格的类型，不属于则直接返回“上传文件类型异常”(CodeMsg.FilE_ERROR)
//        if (!".xls".equals(suffix) && !".xlsx".equals(suffix)) {
//            return JsonResult.error("上传文件类型异常，仅支持xls和xlsx");
//        }
        ImportParams importParams = new ImportParams();
        // 对上传的数据进行处理，详情信息请看-com.ydc.excel_to_db.handler.ExcelModelHandler;
//        IExcelDataHandler<ExcelModel> handler = new ExcelModelHandler();
//        handler.setNeedHandlerFields(new String[] {"姓名"});
//        importParams.setDataHanlder(handler);
        // 是否需要验证
        importParams.setNeedVerify(true);
        importParams.setVerifyHandler(new MyVerifyHandler());

        try {
            ExcelImportResult<ImportExcelDto> result = ExcelImportUtil.importExcelMore(file.getInputStream(), ImportExcelDto.class, importParams);
            log.info("excel导入成功的数据位：{}", result.getList().toString());
            // 当结果中通过校验的数据(result.getList())为空时
            // 直接返回“上传Excel表格格式有误<br>或者<br> 上传Excel数据为空”(CodeMsg.Excel_FORMAT_ERROR)
            Workbook workbook = result.getFailWorkbook();
            List<ImportExcelDto> failList = result.getFailList();
            List<ImportExcelPlanFailed> faileds = ImportExcelPlanFailed.members2ImportExcelPlanFaileds(failList);
//            faileds.forEach(x -> {
//                String errorMsg = x.getErrorMsg();
//                System.out.println("字段的错误信息为：" + errorMsg);
//            });
//            if (result.getList().size() == 0 || result.getList().get(0) == null) {
//                return JsonResult.error("上传Excel表格格式有误或者上传Excel数据为空");
//            }
            // 导入成功的条数
            int succSize = result.getList().size();
            // 导入失败的条数
            int failSize = result.getFailList().size();
            // 如果没有失败的数据，则进行存库操作
            if (0 == failSize) {
                saveDB(stationsCode, result);
            } else {
                ExportParams exportParams = new ExportParams();
                Workbook failWrokBook = ExcelExportUtil.exportExcel(exportParams, ImportExcelPlanFailed.class, faileds);
                // 告诉浏览器用什么软件可以打开此文件
                response.setHeader("content-Type", "application/vnd.ms-excel");
                // 下载文件的默认名称
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("发运计划申请表", "UTF-8") + ".xls");
                //编码
                response.setCharacterEncoding("UTF-8");
                failWrokBook.write(response.getOutputStream());
            }

            // 将excel中的数据保存到数据库中
//            String msg = "在Excel数据格式校验环节中，共获得有效数据" + (succSize + failSize) + "条</br>其中," + succSize
//                    + "条数据通过格式校验," + failSize + "条数据未通过格式校验 </br> 是否需要查看完整数据同步结果信息？";
//            String msg = "在Excel数据格式校验环节中，共获得有效数据" + (succSize + failSize) + "条</br>其中," + succSize
//                    + "条数据通过格式校验," + failSize + "条数据未通过格式校验。";
//            return new JsonResult(msg);
        } catch (IOException e) {
            new BizException(e.getMessage());
        } catch (Exception e) {
            new BizException(e.getMessage());
        }

        // 当以上过程中抛出异常时，返回"服务端异常，请您联系管理员查看服务器日志"(CodeMsg.SERVER_ERROR)
//        return JsonResult.error("服务端异常，请您联系管理员查看服务器日志");
    }

    @Override
    public BShipmentPlanDTO detail(String id) {
        return BShipmentPlanDTO.builder()
                .shipmentPlan(getById(id))
                .containerList(containerMapper.selectList(new LambdaQueryWrapper<BShipmentContainer>().eq(BShipmentContainer::getShipmentPlanId, id)))
                .goodsList(goodsDetailMapper.selectList(new LambdaQueryWrapper<BShipmentGoodsDetail>().eq(BShipmentGoodsDetail::getShipmentPlanId, id)))
                .build();
    }

    /**
     * @Description: 清空redis中的部分旧数据
     * @Param: []
     * @Retrun: void
     */
    @Transactional
    public void saveDB(String stationsCode, ExcelImportResult<ImportExcelDto> result) {
        // 通过校验的数据
        List<ImportExcelDto> successList = result.getList();
        Set<BShipmentPlan> planSet = new HashSet<>();
        List<BShipmentContainer> containerList = new ArrayList<>();
        List<BShipmentGoodsDetail> goodsDetails = new ArrayList<>();

        // 设置发运计划、集装箱、商品信息是否是同一个发运计划
        successList.forEach(x -> {
            // 发运计划编号
            String deliveryNo = "";
            // 获取集装箱uuid
            String containerUUID = UniqueIdUtil.getUUID();
            // 获取商品明细uuid
            String goodsUUID = UniqueIdUtil.getUUID();
            // 获取发运计划对象信息
            BShipmentPlan bShipmentPlan = x.getbShipmentPlan();
            if (null != bShipmentPlan) {
                // 获取得到提运单号
                deliveryNo = bShipmentPlan.getDeliveryNo();
                bShipmentPlan.setCreateTime(null);
            }
            planSet.add(bShipmentPlan);

            // 获取集装箱对象信息
            BShipmentContainer container = x.getbShipmentContainer();
            String containerNo = "";
            if (null != container) {
                container.setDeliveryNo(deliveryNo);
                containerNo = container.getContainerNo();
                container.setId(containerUUID);
            }
            containerList.add(container);

            // 获取商品明细信息
            BShipmentGoodsDetail goodsDetail = x.getbShipmentGoodsDetail();
            if (null != goodsDetail) {
                goodsDetail.setDeliveryNo(deliveryNo);
                // 集装箱号
                goodsDetail.setContainerNo(containerNo);
                goodsDetail.setContainerId(containerUUID);
                goodsDetail.setId(goodsUUID);
                goodsDetail.setContaModelCode(container.getContaModelCode());
            }
            goodsDetails.add(goodsDetail);
        });

        // 根据发运计划编号来合并相同的发运计划
        Map<String, List<BShipmentPlan>> planMap = planSet.stream().collect(Collectors.groupingBy(BShipmentPlan::getDeliveryNo));
        Map<String, String> uuidMap = new HashMap<>();
        planMap.keySet().stream().forEach(y -> {
            // 获取发运计划uuid
            String planUUID = UniqueIdUtil.getUUID();
            List<BShipmentPlan> planList = planMap.get(y);
            // 发运计划总件数
            int containerPieceTotal = goodsDetails.stream().filter(x -> y.equals(x.getDeliveryNo())).mapToInt(BShipmentGoodsDetail::getPieceNum).sum();
            // 发运计划总重量
            double containerWeight = goodsDetails.stream().filter(x -> y.equals(x.getDeliveryNo())).mapToDouble(BShipmentGoodsDetail::getWeight).sum();
            planList.forEach(z -> {
                // TODO 获取得到场站编号
//                String stationsCode = z.getStationsCode();
                CStations cStations = cStationsMapper.selectOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getCode, stationsCode).eq(CStations::getEnable, Status.ENABLED.getValue()));
                if (null != cStations) {
                    // 发货场站id
                    z.setStationsCode(cStations.getCode());
                    // 发货场站名称
                    z.setStationsName(cStations.getName());
                }
                // 总件数
                z.setPieceTotal(containerPieceTotal);
                // 总重量
                z.setWeightTotal(containerWeight);
                // 发运计划id
                z.setId(planUUID);
                // 审核状态（00：待提交、01：发送失败、02：发送成功、03:接收失败、10：待审核、11：审核驳回、12：审核通过、99：作废）
                z.setAuditStatus(AuditStatus.PRE_SUBMIT.getValue());
                // TODO 发运计划编号
                z.setShipmentPlanNo(UniqueIdUtil.getShipmentPlanNum("000001", 6));
                // TODO 发货企业名称id
                z.setEnterprisesId("测试发货企业id");
                // TODO 发货企业名称
                z.setEnterprisesName("测试发货企业名称");
                // 设置当前时间戳
                z.setCreateTime(System.currentTimeMillis());
                // TODO 箱型箱量
                StringBuffer sb = new StringBuffer();
                // 集装箱总件数
                Map<String, List<BShipmentGoodsDetail>> conMaps = goodsDetails.stream().filter(x -> y.equals(x.getDeliveryNo())).collect(Collectors.groupingBy(BShipmentGoodsDetail::getContaModelCode));
                conMaps.entrySet().stream().forEach(s -> {
                    sb.append(s.getKey()).append("*").append(s.getValue().size()).append("+");
                });

                z.setContainerNum(sb.substring(0, sb.length() - 1).toString());

                // TODO  excel导入插入发运计划表
                planMapper.insert(z);
            });

            uuidMap.put(y, planUUID);
        });

        // 根据发运计划编号来合并相同的发运计划集装箱信息
        Map<String, List<BShipmentContainer>> containerMap = containerList.stream().collect(Collectors.groupingBy(BShipmentContainer::getDeliveryNo));
        containerMap.keySet().stream().forEach(y -> {
            List<BShipmentContainer> containers = containerMap.get(y);
            // 集装箱总件数
            Map<String, List<BShipmentGoodsDetail>> containerMaps = goodsDetails.stream().filter(x -> y.equals(x.getDeliveryNo())).collect(Collectors.groupingBy(BShipmentGoodsDetail::getContainerNo));
            containers.forEach(z -> {
//                // 获取集装箱uuid
//                String containerUUID = UniqueIdUtil.getUUID();
//                z.setId(containerUUID);
                // 发运计划id
                z.setShipmentPlanId(uuidMap.get(y));
                // 集装箱总件数
                z.setPieceNum(containerMaps.get(z.getContainerNo()).stream().mapToInt(BShipmentGoodsDetail::getPieceNum).sum());
                // 集装箱总重量
                z.setWeight(containerMaps.get(z.getContainerNo()).stream().mapToDouble(BShipmentGoodsDetail::getWeight).sum());
                // 集装箱尺寸名称
                z.setContaModelName(z.getContaModelCode());

                // TODO 插入集装箱信息
                containerMapper.insert(z);
            });
        });

        // 根据发运计划编号来合并相同的发运计划商品信息
        Map<String, List<BShipmentGoodsDetail>> goodsMap = goodsDetails.stream().collect(Collectors.groupingBy(BShipmentGoodsDetail::getDeliveryNo));
        goodsMap.keySet().stream().forEach(y -> {
            List<BShipmentGoodsDetail> goods = goodsMap.get(y);
            // 获取集装箱uuid
            String goodsUUID = UniqueIdUtil.getUUID();
            goods.forEach(z -> {
                // 发运计划id
                z.setShipmentPlanId(uuidMap.get(y));
                // 插入到商品详细
                goodsDetailMapper.insert(z);
            });
        });
    }
}