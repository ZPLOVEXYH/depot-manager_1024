package com.sample;

import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.XmlUtil;
import cn.samples.depot.web.DepotWebCzApplication;
import cn.samples.depot.web.bean.report.BRailwayTallyReportQuery;
import cn.samples.depot.web.bean.report.RspBRailwayTallyReport;
import cn.samples.depot.web.cz.handler.TimeScanCustomsRspFiles;
import cn.samples.depot.web.cz.service.BRailwayTallyDelReportHeadService;
import cn.samples.depot.web.cz.service.BRailwayTallyReportHeadService;
import cn.samples.depot.web.cz.service.BRelReportHeadService;
import cn.samples.depot.web.entity.xml.RelMessage;
import cn.samples.depot.web.entity.xml.RspMessage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;

/**
 * 单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DepotWebCzApplication.class)
@Slf4j
public class ReportTest {

    @Autowired
    private BRailwayTallyReportHeadService service;

    @Autowired
    private BRelReportHeadService relReportHeadService;

    @Autowired
    private BRailwayTallyReportHeadService reportHeadService;

    @Autowired
    TimeScanCustomsRspFiles timeScanCustomsRspFiles;

    @Autowired
    BRailwayTallyDelReportHeadService delReportHeadService;

    /**
     * 测试海关进出口申报列表多表关联分页
     */
    @Test
    public void contextLoads() {
        BRailwayTallyReportQuery query = new BRailwayTallyReportQuery();
        query.setIEFlag("I");
        Page<RspBRailwayTallyReport> pages = service.selectReportListPage(query, 1, 2);
        log.info("pages:{}", pages);
    }

    /**
     * 测试接收到海关的放行指令报文保存入库
     */
    @Test
    public void testSaveRel() {
        File[] files = cn.hutool.core.io.FileUtil.ls("C:\\Users\\zp&xyh\\Desktop\\");
        Arrays.stream(files).forEach(file -> {
            String fileName = file.getName();
            System.out.println("文件夹下的文件名为：" + fileName);
            if (fileName.equals("放行指令2.xml")) {
                RelMessage relMsg = XmlUtil.deserialize(RelMessage.class, file, false);
                System.out.println(relMsg);

                JsonResult jsonResult = relReportHeadService.xmlReturnRelHandle(relMsg);
                System.out.println(jsonResult.getCode());
            }
        });
    }

    /**
     * 测试接收到海关的理货报告报文保存入库
     */
    @Test
    public void testSaveReport() {
        File[] files = cn.hutool.core.io.FileUtil.ls("C:\\Users\\zp&xyh\\Desktop\\");
        Arrays.stream(files).forEach(file -> {
            String fileName = file.getName();
            System.out.println("文件夹下的文件名为：" + fileName);
            if (fileName.equals("铁路进出口回执报文.xml")) {
                RspMessage rspMessage = XmlUtil.deserialize(RspMessage.class, file, false);
                System.out.println(rspMessage);

                JsonResult jsonResult = reportHeadService.xmlReturnReceiptHandle(rspMessage);
                System.out.println(jsonResult.getMessage());
            }
        });
    }

    /**
     * 测试根据放行指令id查询得到放行指令的信息
     */
    @Test
    public void testQueryRelReportDetail() {
        JsonResult jsonResult = relReportHeadService.queryRelReportDetail("6fffcb8c066744729b3a867c0e0c9eaa");
        log.info("返回的报文code:{}，message:{}，data:{}", jsonResult.getCode(), jsonResult.getMessage(), jsonResult.getData());
    }

    /**
     * 测试定时扫描海关回执的文件
     */
    @Test
    public void testTimeScanRspFile() {
        JsonResult jsonResult = timeScanCustomsRspFiles.timeScanRspFile("D://xml/");
        log.info("返回的报文code:{}，message:{}，data:{}", jsonResult.getCode(), jsonResult.getMessage(), jsonResult.getData());
    }

    /**
     * 测试删除理货报告
     */
    @Test
    public void testDeleteReport() {
        JsonResult jsonResult = reportHeadService.removeReportById("111");
        log.info("code:{}, message:{}, data:{}", jsonResult.getCode(), jsonResult.getMessage(), jsonResult.getData());
    }

    /**
     * 测试删除理货作废报告
     */
    @Test
    public void testDeleteDelReport() {
        JsonResult jsonResult = delReportHeadService.removeDelReportById("1");
        log.info("code:{}, message:{}, data:{}", jsonResult.getCode(), jsonResult.getMessage(), jsonResult.getData());
    }

    /**
     * 测试根据理货报告id查询得到审核通过的数据
     */
    @Test
    public void testQueryByMsgId() {
//        BRailwayTallyReportHeadSave reportHeadSave = reportHeadService.queryByMsgId("1");
//        log.info("reportHeadSave:{}", reportHeadSave.toString());
    }
}
