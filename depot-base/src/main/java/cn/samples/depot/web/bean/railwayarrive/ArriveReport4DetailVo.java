package cn.samples.depot.web.bean.railwayarrive;

import cn.samples.depot.web.entity.BExRailwayConta;
import cn.samples.depot.web.entity.BExRailwayList;
import cn.samples.depot.web.entity.BExRailwayReportHead;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 运抵申报-查看详情
 * @Date 2019-08-21 17:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArriveReport4DetailVo {
    BExRailwayReportHead head;
    List<BExRailwayList> arriveList;
    List<BExRailwayConta> contaList;
}
