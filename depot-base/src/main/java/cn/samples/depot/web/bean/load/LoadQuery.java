package cn.samples.depot.web.bean.load;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author majunzi
 * @Date 2019/8/29
 * @Description 装车记录  查询
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoadQuery {

    /**
     * 卸货地代码
     */
    @ApiModelProperty(name = "dischargePlace", value = "装货地代码")
    private String dischargePlace;
    /**
     * 报文编号
     */
    @ApiModelProperty(name = "messageId", value = "报文编号")
    private String messageId;
    /**
     * 运抵编号
     */
    @ApiModelProperty(name = "arriveNo", value = "运抵编号")
    private String arriveNo;
    /**
     * 箱号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    private String contaNo;

    /**
     * 提单号
     */
    @ApiModelProperty(name = "billNo", value = "运单号")
    private String billNo;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "startCreateTime", value = "创建时间-开始")
    private Long startCreateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "endCreateTime", value = "创建时间-结束")
    private Long endCreateTime;

}
