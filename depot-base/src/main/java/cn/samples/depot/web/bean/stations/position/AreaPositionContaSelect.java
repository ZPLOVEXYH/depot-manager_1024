package cn.samples.depot.web.bean.stations.position;

import cn.samples.depot.web.entity.CStationAreaPositions;
import cn.samples.depot.web.entity.CStationAreas;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author majunzi
 * @Description 堆存集装箱 下来
 * @Date 2019-08-13 16:22
 */
@Data
@Builder
public class AreaPositionContaSelect {
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

    /**
     * 堆位编码
     */
    @ApiModelProperty(name = "code", value = "堆位编码")
    private String positioncode;


    /**
     * 堆位编码名称
     */
    @ApiModelProperty(name = "name", value = "堆位名称")
    private String positionName;


    @ApiModelProperty(name = "areaCode", value = "堆区code")
    private String areaCode;
    @ApiModelProperty(name = "areaName", value = "堆区名称")
    private String areaName;


    public static AreaPositionContaSelect build(CStationAreas area, CStationAreaPositions position) {
        if (null == area) area = CStationAreas.builder().build();
        return AreaPositionContaSelect.builder()
                .contaNo(position.getContaNo())
                .contaType(position.getContaType())
                .positioncode(position.getCode())
                .positionName(position.getName())
                .areaCode(area.getCode())
                .areaName(area.getName())
                .build();
    }
}
