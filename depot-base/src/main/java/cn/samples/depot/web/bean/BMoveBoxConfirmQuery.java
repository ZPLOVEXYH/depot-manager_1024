package cn.samples.depot.web.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author majunzi
 * @Description 移箱确认
 * @Date 2019-08-13 11:15
 */
@Data
public class BMoveBoxConfirmQuery {
    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "containerNo", value = "集装箱号")
    private String containerNo;

    /**
     * 移箱时间  from
     */
    @ApiModelProperty(name = "startOpTime", value = "操作时间")
    private Long startOpTime;

    /**
     * 移箱时间 to
     */
    @ApiModelProperty(name = "endOpTime", value = "操作时间")
    private Long endOpTime;

}
