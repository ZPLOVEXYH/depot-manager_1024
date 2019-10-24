package cn.samples.depot.web.bean.stations.position;

import cn.samples.depot.web.entity.CStationAreaPositions;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author majunzi 集装箱信息（箱型，箱号）
 * @Description
 * @Date 2019-08-21 15:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContaBase {
    /**
     * 集装箱编号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱编号")
    private String contaNo;
    /**
     * 箱型
     */
    @ApiModelProperty(name = "contaType", value = "箱型")
    private String contaType;

    public static ContaBase build(CStationAreaPositions stationAreaPosition) {
        if (null == stationAreaPosition) return null;
        return ContaBase.builder().contaNo(stationAreaPosition.getContaNo()).contaType(stationAreaPosition.getContaType()).build();
    }
}
