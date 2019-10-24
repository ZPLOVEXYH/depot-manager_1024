/**
 * @filename:BRailwayTallyReportHeadService 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.report.BRailwayTallyReportHeadSave;
import cn.samples.depot.web.bean.report.BRailwayTallyReportQuery;
import cn.samples.depot.web.bean.report.RspBRailwayTallyReport;
import cn.samples.depot.web.bean.report.TallyReportVo;
import cn.samples.depot.web.entity.BRailwayTallyReportHead;
import cn.samples.depot.web.entity.xml.RspMessage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 铁路进口理货申请报文表头——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
public interface BRailwayTallyReportHeadService extends IService<BRailwayTallyReportHead> {

    Page<RspBRailwayTallyReport> selectReportListPage(BRailwayTallyReportQuery query, int pageNum, int pageSize);

    /**
     * 铁路进出口理货申报
     *
     * @param id
     * @return
     */
    JsonResult declare(String id);

    /**
     * 处理海关响应的报文回执
     *
     * @param rspMessage
     * @return
     */
    JsonResult xmlReturnReceiptHandle(RspMessage rspMessage);

    JsonResult saveReportInfo(BRailwayTallyReportHeadSave headSave);

    BRailwayTallyReportHeadSave queryByMsgId(String declMessageId);

    /**
     * 单个删除铁路进口理货申请报文表头
     *
     * @param id
     * @return
     */
    JsonResult removeReportById(String id);

    BRailwayTallyReportHeadSave queryDetailById(String id);

    BRailwayTallyReportHead getByMessageId(String messageId);

    void update(String id, TallyReportVo vo) throws BizException;

    void deleteTallyReportById(String id) throws BizException;

    void saveVo(TallyReportVo vo) throws BizException;
}