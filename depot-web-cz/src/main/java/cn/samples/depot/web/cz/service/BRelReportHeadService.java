/**
 * @filename:BRelReportHeadService 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.report.BRelReportHeadQuery;
import cn.samples.depot.web.bean.report.BRelReportHeadRsp;
import cn.samples.depot.web.entity.BRelReportHead;
import cn.samples.depot.web.entity.xml.RelMessage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 放行指令表头——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
public interface BRelReportHeadService extends IService<BRelReportHead> {

    Page<BRelReportHeadRsp> selectRelReportListPage(BRelReportHeadQuery query, Integer pageNum, Integer pageSize);

    JsonResult xmlReturnRelHandle(RelMessage relMessage);

    /**
     * 查看指定放行指令表头数据
     *
     * @param id
     * @return
     */
    JsonResult queryRelReportDetail(String id);
}