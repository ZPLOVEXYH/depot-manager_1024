package cn.samples.depot.web.bean.load;

import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadDelReportHead;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 装车作废-新增
 * @Date 2019-08-21 17:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoadDelAddVo {

    BRailwayLoadDelReportHead head;

    List<BRailwayLoadConta> contaList;

    public static LoadDelAddVo getEmpty() {
        return LoadDelAddVo.builder()
                .head(BRailwayLoadDelReportHead.builder().build())
                .contaList(Lists.newArrayList(BRailwayLoadConta.builder().build()))
                .build();
    }
}
