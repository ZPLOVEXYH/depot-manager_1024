package cn.samples.depot.web.bean.analysis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlanTrendQuery {

    @ApiModelProperty(name = "startDate", value = "开始时间（时间戳，例如：2019-09，为1567267200000）")
    private Long startDate;

    @ApiModelProperty(name = "endDate", value = "结束时间（时间戳，例如：2019-09，为1567267200000）")
    private Long endDate;
}
