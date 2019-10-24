/**
 * @filename:BRailwayTallyReportHeadMapper 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.mapper;

import cn.samples.depot.web.bean.report.BRailwayTallyReportQuery;
import cn.samples.depot.web.bean.report.RspBRailwayTallyReport;
import cn.samples.depot.web.entity.BRailwayTallyReportHead;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 铁路进口理货申请报文表头——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Mapper
public interface BRailwayTallyReportHeadMapper extends BaseMapper<BRailwayTallyReportHead> {

    List<RspBRailwayTallyReport> selectReportListPage(Page<RspBRailwayTallyReport> page, @Param("query") BRailwayTallyReportQuery query);
}
