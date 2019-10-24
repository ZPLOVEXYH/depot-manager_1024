package cn.samples.depot.web.bean.load;

import cn.samples.depot.web.entity.BRailwayLoadDelConta;
import cn.samples.depot.web.entity.BRailwayLoadDelReportHead;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 装车作废-编辑
 * @Date 2019-08-21 17:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoadDelUpdateVo {

    BRailwayLoadDelReportHead head;

    List<BRailwayLoadDelConta> contaList;

    public static LoadDelUpdateVo getEmpty() {
        return LoadDelUpdateVo.builder()
                .head(BRailwayLoadDelReportHead.builder().build())
                .contaList(Lists.newArrayList(BRailwayLoadDelConta.builder().build()))
                .build();
    }
}
