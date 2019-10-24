package cn.samples.depot.web.bean.report;

import cn.samples.depot.web.entity.BRailwayTallyBillInfo;
import cn.samples.depot.web.entity.BRailwayTallyReportHead;
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
public class TallyReportVo {

    BRailwayTallyReportHead head;

    List<BRailwayTallyBillInfo> billInfoList;

    public static TallyReportVo getEmpty() {
        return TallyReportVo.builder()
                .head(BRailwayTallyReportHead.builder().build())
                .billInfoList(Lists.newArrayList(BRailwayTallyBillInfo.builder().build()))
                .build();
    }
}
