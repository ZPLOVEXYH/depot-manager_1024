package cn.samples.depot.web.bean.load.delete;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BRailwayLoadDelListQuery {

    /**
     * 集装箱表ID
     */
    @ApiModelProperty(name = "railwayLoadDelContaId", value = "集装箱表ID")
    private String railwayLoadDelContaId;
}
