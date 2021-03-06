package cn.samples.depot.web.bean.railwayarrive;

import cn.samples.depot.web.entity.BExRailwayConta;
import cn.samples.depot.web.entity.BExRailwayList;
import cn.samples.depot.web.entity.BExRailwayReportHead;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 运抵申报-新增编辑
 * @Date 2019-08-21 17:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArriveReport4UpdateVo {

    BExRailwayReportHead head;

    List<ArriveVo> arriveList;

    public static ArriveReport4UpdateVo getEmpty() {
        return ArriveReport4UpdateVo.builder()
                .head(BExRailwayReportHead.builder().build())
                .arriveList(Lists.newArrayList(ArriveVo.builder().arrive(BExRailwayList.builder().build()).contaList(Lists.newArrayList(BExRailwayConta.builder().build())).build()))
                .build();
    }

}
