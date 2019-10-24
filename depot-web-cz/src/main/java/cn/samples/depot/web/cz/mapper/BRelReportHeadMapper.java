/**
 * @filename:BRelReportHeadMapper 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.mapper;

import cn.samples.depot.web.bean.report.BRelReportHeadQuery;
import cn.samples.depot.web.bean.report.BRelReportHeadRsp;
import cn.samples.depot.web.entity.BRelReportHead;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 放行指令表头——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Mapper
public interface BRelReportHeadMapper extends BaseMapper<BRelReportHead> {

    List<BRelReportHeadRsp> selectRelReportListPage(Page<BRelReportHeadRsp> page, @Param(value = "query") BRelReportHeadQuery query);
}
