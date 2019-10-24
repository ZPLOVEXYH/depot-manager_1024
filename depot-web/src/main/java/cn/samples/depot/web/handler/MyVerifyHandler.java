package cn.samples.depot.web.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.SpringContextUtils;
import cn.samples.depot.web.dto.shipment.ImportExcelDto;
import cn.samples.depot.web.entity.PContaModel;
import cn.samples.depot.web.service.PContaModelService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的excel导入校验器
 */
@Slf4j
@Component
public class MyVerifyHandler implements IExcelVerifyHandler<ImportExcelDto> {

    private PContaModelService service;

    // 提运单号和出运时间集合
    public Map<String, String> dataMap = new HashMap<>();

    // 集装箱号和集装箱尺寸集合
    public Map<String, String> contaMap = new HashMap<>();

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportExcelDto excelDto) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult();
        // 校验导入excel中的列提运单号是否为空
        String deliveryNo = excelDto.getbShipmentPlan().getDeliveryNo();
        if (StringUtils.isEmpty(deliveryNo)) {
            result.setMsg("该提运单号不能为空");
            result.setSuccess(false);
            return result;
        }
        // 校验导入excel中的列出运时间是否为空
        String shipmentDate = excelDto.getbShipmentPlan().getShipmentDate();
        if (null == shipmentDate) {
            result.setMsg("该出运时间不能为空");
            result.setSuccess(false);
            return result;
        }
        // 校验导入excel中的箱号是否为空
        String containerNo = excelDto.getbShipmentContainer().getContainerNo();
        if (StringUtils.isEmpty(containerNo)) {
            result.setMsg("该箱号不能为空");
            result.setSuccess(false);
            return result;
        }
        // 校验导入excel中的列集装箱尺寸是否为空
        String contaModelCode = excelDto.getbShipmentContainer().getContaModelCode();
        if (StringUtils.isEmpty(contaModelCode)) {
            result.setMsg("该集装箱尺寸不能为空");
            result.setSuccess(false);
            return result;
        }
        // 校验导入excel中的列品名是否为空
        if (StringUtils.isEmpty(excelDto.getbShipmentGoodsDetail().getGoodsName())) {
            result.setMsg("该品名不能为空");
            result.setSuccess(false);
            return result;
        }
        // 校验导入excel中的列件数是否为空
        if (null == excelDto.getbShipmentGoodsDetail().getPieceNum()) {
            result.setMsg("该件数不能为空");
            result.setSuccess(false);
            return result;
        }
        // 校验导入excel中的列货物重量是否为空
        if (null == excelDto.getbShipmentGoodsDetail().getWeight()) {
            result.setMsg("该货物重量不能为空");
            result.setSuccess(false);
            return result;
        }

        // 日期格式校验yyyy-MM-dd
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(shipmentDate);
            long dateLong = date.getTime();
            excelDto.getbShipmentPlan().setShipmentTime(dateLong);
            log.info("日期时间戳为：{}", dateLong);
        } catch (Exception e) {
            result.setMsg("该集装箱代码不存在");
            result.setSuccess(false);
            return result;
        }

        Wrapper<PContaModel> wrapper = Wrappers.<PContaModel>lambdaQuery()
                .eq(PContaModel::getEnable, Status.ENABLED.getValue())
                .eq(PContaModel::getCode, contaModelCode);

        // 校验集装箱尺寸的代码与启动状态下的集装箱编码是否一致，若不一致，则提交“提运单号XXX的集装箱代码不存在“
        service = (PContaModelService) SpringContextUtils.getBean("pContaModelService");
        int count = service.count(wrapper);
        if (0 == count) {
            result.setMsg("该集装箱代码不存在");
            result.setSuccess(false);
            return result;
        }

        // 4. 校验同一个提运单号，出运时间是否一致。若一致，视为一条发运计划单。若不一致，则报错“提运单号XXX的出运时间不一致”
        if (CollectionUtils.isEmpty(dataMap)) {
            dataMap.put(deliveryNo, shipmentDate);
        } else {
            // 如果存在相同key的提运单号，那么再判断出运时间是否一致，如果不一致就报错
            if (dataMap.containsKey(deliveryNo)) {
                // 出运时间
                String shipmentDateMap = dataMap.get(deliveryNo);
                if (!shipmentDate.equals(shipmentDateMap)) {
                    result.setMsg("该提运单号的出运时间不一致");
                    result.setSuccess(false);
                    return result;
                }
            }
        }

        // 5. 校验同一个集装箱号，集装箱尺寸是否一致。
        if (CollectionUtils.isEmpty(contaMap)) {
            contaMap.put(containerNo, contaModelCode);
        } else {
            // 如果存在相同key的提运单号，那么再判断出运时间是否一致，如果不一致就报错
            if (contaMap.containsKey(containerNo)) {
                // 出运时间
                String contaModelCodeMap = contaMap.get(containerNo);
                if (!contaModelCode.equals(contaModelCodeMap)) {
                    result.setMsg("该集装箱号的集装箱尺寸不一致");
                    result.setSuccess(false);
                    return result;
                }
            }
        }

        result.setSuccess(true);
        return result;
    }
}
