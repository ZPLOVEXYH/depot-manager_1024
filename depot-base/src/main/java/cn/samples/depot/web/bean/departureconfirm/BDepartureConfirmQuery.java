package cn.samples.depot.web.bean.departureconfirm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author majunzi
 * @Description
 * @Date 2019-08-21 15:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BDepartureConfirmQuery {
    /**
     * 集装箱编号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    private String contaNo;

    /**
     * 发车时间
     */
    @ApiModelProperty(name = "startDepartureTime", value = "发车时间-开始")
    private Long startDepartureTime;

    /**
     * 发车时间
     */
    @ApiModelProperty(name = "endDepartureTime", value = "发车时间-结束")
    private Long endDepartureTime;
}
