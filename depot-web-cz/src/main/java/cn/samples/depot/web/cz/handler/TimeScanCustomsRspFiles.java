package cn.samples.depot.web.cz.handler;

import cn.hutool.core.io.FileUtil;
import cn.samples.depot.common.constant.XmlTypeConstant;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.XmlUtil;
import cn.samples.depot.web.cz.service.BExRailwayReportHeadService;
import cn.samples.depot.web.cz.service.BRailwayLoadReportHeadService;
import cn.samples.depot.web.cz.service.BRailwayTallyReportHeadService;
import cn.samples.depot.web.cz.service.BRelReportHeadService;
import cn.samples.depot.web.entity.xml.RelMessage;
import cn.samples.depot.web.entity.xml.RspMessage;
import cn.samples.depot.web.entity.xml.ex.rsp.ExMessage;
import cn.samples.depot.web.entity.xml.load.rsp.LoadMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

@Component
@Slf4j
public class TimeScanCustomsRspFiles {

    // 理货报告申请
    @Autowired
    BRailwayTallyReportHeadService reportHeadService;

    // 放行通知
    @Autowired
    BRelReportHeadService bRelReportHeadService;

    // 运抵单申请
    @Autowired
    BExRailwayReportHeadService exRailwayReportHeadService;

    // 装车记录单
    @Autowired
    BRailwayLoadReportHeadService loadReportHeadService;

    @Value("${move.file.path}")
    private String MOVE_FILE_PATH;

    /**
     * 定时扫描海关回执的文件
     *
     * @return
     */
    public JsonResult timeScanRspFile(String scanFilePath) {
        // 扫描指定的文件夹路径
        File[] files = FileUtil.ls(scanFilePath);
        if (files.length != 0) {
            // 循环遍历文件内容
            Arrays.stream(files).forEach(file -> {
                // 获取得到文件名称
                String fileName = file.getName();
                JsonResult jsonResult = new JsonResult();

                // 如果读取得到的是铁路进口理货报告回执，铁路进口理货报告回执||铁路出口理货报告回执
                if (fileName.contains(XmlTypeConstant.WLJK_IRTR) || fileName.contains(XmlTypeConstant.WLJK_ERTR)) {
                    RspMessage rspMessage = XmlUtil.deserialize(RspMessage.class, file, false);
                    jsonResult = reportHeadService.xmlReturnReceiptHandle(rspMessage);
                    log.info("理货报告回执的结果为：code:{}, message:{}, data:{}", jsonResult.getCode(), jsonResult.getMessage(), jsonResult.getData());

                    // 铁路放行指令报告回执，铁路放行指令报告回执||铁路装车通知回执
                } else if (fileName.contains(XmlTypeConstant.WLJK_TREL) || fileName.contains(XmlTypeConstant.WLJK_TLN)) {
                    RelMessage relMessage = XmlUtil.deserialize(RelMessage.class, file, false);
                    jsonResult = bRelReportHeadService.xmlReturnRelHandle(relMessage);
                    log.info("放行指令回执的结果为：code:{}, message:{}, data:{}", jsonResult.getCode(), jsonResult.getMessage(), jsonResult.getData());

                    // 运抵申请、运抵作废的回执报文文件类型
                } else if (fileName.contains(XmlTypeConstant.WLJK_ERRR)) {
                    ExMessage exMessage = XmlUtil.deserialize(ExMessage.class, file, false);
                    // TODO 处理运抵申请和运抵作废的回执报文
                    jsonResult = exRailwayReportHeadService.xmlReturnExHandle(exMessage);
                    log.info("抵申请和运抵作废的回执报文结果为：code:{}, message:{}, data:{}", jsonResult.getCode(), jsonResult.getMessage(), jsonResult.getData());

                    // 装车记录单申请、作废的回执报文文件类型
                } else if (fileName.contains(XmlTypeConstant.WLJK_TLR)) {
                    LoadMessage loadMessage = XmlUtil.deserialize(LoadMessage.class, file, false);
                    // TODO 可参考， 处理装车记录单申请、作废的回执报文文件类型
                    jsonResult = loadReportHeadService.xmlReturnLoadHandle(loadMessage);
                    log.info("装车记录单申请、作废的回执报文结果为：code:{}, message:{}, data:{}", jsonResult.getCode(), jsonResult.getMessage(), jsonResult.getData());
                }

                // 如果文件夹中的报文文件成功入库，那么将消费完的xml文件移到其他文件夹内
                if ("0000".equals(jsonResult.getCode())) {
                    // 文件处理成功完之后移动到另外一个文件夹中
                    FileUtil.move(file, new File(MOVE_FILE_PATH + fileName), true);
                }
            });
        }

        return JsonResult.success();
    }
}
