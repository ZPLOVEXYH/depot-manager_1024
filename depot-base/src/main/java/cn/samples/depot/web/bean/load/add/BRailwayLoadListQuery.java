package cn.samples.depot.web.bean.load.add;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BRailwayLoadListQuery {

    /**
     * 集装箱表ID
     */
    @ApiModelProperty(name = "railwayLoadReportContaId", value = "集装箱表ID")
    private String railwayLoadReportContaId;
}
