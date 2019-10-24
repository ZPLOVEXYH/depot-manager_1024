package cn.samples.depot.web.bean.railwayarrive;

import cn.samples.depot.web.entity.BExRailwayDelReportHead;
import cn.samples.depot.web.entity.BExRailwayList;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 运抵作废-新增
 * @Date 2019-08-21 17:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArriveReportDel4AddVo {

    BExRailwayDelReportHead head;

    List<BExRailwayList> arriveList;

    public static ArriveReportDel4AddVo getEmpty() {
        return ArriveReportDel4AddVo.builder()
                .head(BExRailwayDelReportHead.builder().build())
                .arriveList(Lists.newArrayList(BExRailwayList.builder().build()))
                .build();
    }
}
