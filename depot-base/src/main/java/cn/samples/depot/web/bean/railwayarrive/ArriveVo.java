package cn.samples.depot.web.bean.railwayarrive;

import cn.samples.depot.web.entity.BExRailwayConta;
import cn.samples.depot.web.entity.BExRailwayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 运抵单
 * @Date 2019-08-21 17:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArriveVo {

    BExRailwayList arrive;

    List<BExRailwayConta> contaList;

}
