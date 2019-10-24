package cn.samples.depot.web.bean.stations.position;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author majunzi
 * @Date 2019/8/13
 * @Description 堆存查询
 **/
@Data
public class AreaPositionsQuery {

    /**
     * 集装箱编号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    private String contaNo;
    /**
     * 所属场区
     */
    @ApiModelProperty(name = "stationAreaCode", value = "堆存区,GET /station/CStationAreas/select")
    private String stationAreaCode;

    /**
     * 堆区编码
     */
    @ApiModelProperty(name = "code", value = "堆位,GET /station/CStationAreaPositions/select")
    private String code;


}
