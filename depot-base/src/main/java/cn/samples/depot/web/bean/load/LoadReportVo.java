package cn.samples.depot.web.bean.load;

import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadList;
import cn.samples.depot.web.entity.BRailwayLoadReportHead;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 装车记录-新增编辑
 * @Date 2019-08-21 17:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoadReportVo {

    BRailwayLoadReportHead head;

    List<ContaVo> contaVoList;

    public static LoadReportVo getEmpty() {
        return LoadReportVo.builder()
                .head(BRailwayLoadReportHead.builder().build())
                .contaVoList(Lists.newArrayList(ContaVo.builder().conta(BRailwayLoadConta.builder().build()).arrayList(Lists.newArrayList(BRailwayLoadList.builder().build())).build()))
                .build();
    }
}
