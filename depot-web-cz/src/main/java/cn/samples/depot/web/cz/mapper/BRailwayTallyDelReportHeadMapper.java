/**
 * @filename:BRailwayTallyDelReportHeadMapper 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.mapper;

import cn.samples.depot.web.bean.report.BRailwayTallyDelReportQuery;
import cn.samples.depot.web.bean.report.RspBRailwayTallyDelReport;
import cn.samples.depot.web.entity.BRailwayTallyDelReportHead;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 铁路进口理货作废报文表头——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Mapper
public interface BRailwayTallyDelReportHeadMapper extends BaseMapper<BRailwayTallyDelReportHead> {

    List<RspBRailwayTallyDelReport> selectDelReportListPage(Page<RspBRailwayTallyDelReport> page, @Param("query") BRailwayTallyDelReportQuery query);
}
