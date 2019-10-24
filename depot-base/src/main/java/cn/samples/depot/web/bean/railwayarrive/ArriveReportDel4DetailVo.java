package cn.samples.depot.web.bean.railwayarrive;

import cn.samples.depot.web.entity.BExRailwayDelConta;
import cn.samples.depot.web.entity.BExRailwayDelList;
import cn.samples.depot.web.entity.BExRailwayDelReportHead;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 运抵作废-查看详情
 * @Date 2019-08-21 17:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArriveReportDel4DetailVo {
    BExRailwayDelReportHead head;
    List<BExRailwayDelList> arriveList;
    List<BExRailwayDelConta> contaList;
}
