package cn.samples.depot.web.bean.railwayarrive;

import cn.samples.depot.web.entity.BExRailwayDelList;
import cn.samples.depot.web.entity.BExRailwayDelReportHead;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 运抵作废-编辑
 * @Date 2019-08-21 17:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArriveReportDel4UpdateVo {

    BExRailwayDelReportHead head;

    List<BExRailwayDelList> arriveList;

    public static ArriveReportDel4UpdateVo getEmpty() {
        return ArriveReportDel4UpdateVo.builder()
                .head(BExRailwayDelReportHead.builder().build())
                .arriveList(Lists.newArrayList(BExRailwayDelList.builder().build()))
                .build();
    }
}
