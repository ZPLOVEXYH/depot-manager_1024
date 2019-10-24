/**
 * @filename:BRailwayTallyDelReportHeadService 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.report.BRailwayTallyDelReportQuery;
import cn.samples.depot.web.bean.report.BRailwayTallyDelReportSave;
import cn.samples.depot.web.bean.report.RspBRailwayTallyDelReport;
import cn.samples.depot.web.entity.BRailwayTallyDelReportHead;
import cn.samples.depot.web.entity.xml.RspMessage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 铁路进口理货作废报文表头——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
public interface BRailwayTallyDelReportHeadService extends IService<BRailwayTallyDelReportHead> {

    Page<RspBRailwayTallyDelReport> selectDelReportListPage(BRailwayTallyDelReportQuery query, int pageNum, int pageSize);

    /**
     * 铁路进出口理货作废
     *
     * @param id
     * @return
     */
    JsonResult declareDel(String id);

    JsonResult xmlDelReturnReceiptHandle(RspMessage rspMessage);

    /**
     * 铁路进口理货作废报文表头和表体
     *
     * @param id
     * @return
     */
    JsonResult removeDelReportById(String id);

    JsonResult saveDelReportInfo(BRailwayTallyDelReportSave delReportSave);

    BRailwayTallyDelReportSave queryByMsgId(String msgId);

    BRailwayTallyDelReportSave editByMsgId(String msgId);

    BRailwayTallyDelReportHead checkUpdate(String id) throws BizException;
}