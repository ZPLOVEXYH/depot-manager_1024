package cn.samples.depot.web.bean.stations.position;

import cn.samples.depot.web.entity.CStationAreaPositions;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StationAreaPositionsQuery {


    /**
     * 所属场区
     */
    @JsonView(CStationAreaPositions.View.Table.class)
    @ApiModelProperty(name = "stationAreaCode", value = "堆区")
    private String stationAreaCode;

    /**
     * 备注
     */
    @JsonView(CStationAreaPositions.View.Table.class)
    @ApiModelProperty(name = "code", value = "箱位")
    private String code;

}
