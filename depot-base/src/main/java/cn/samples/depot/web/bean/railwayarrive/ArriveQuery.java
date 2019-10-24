package cn.samples.depot.web.bean.railwayarrive;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author majunzi
 * @Date 2019/8/21
 * @Description 运抵报告  查询
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArriveQuery {
    /**
     * 海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "海关代码，传code")
    private String customsCode;

    /**
     * 卸货地代码
     */
    @ApiModelProperty(name = "dischargePlace", value = "卸货地代码，传code")
    private String dischargePlace;

    /**
     * 报文编号
     */
    @ApiModelProperty(name = "messageId", value = "运抵报文编号")
    private String messageId;

    /**
     * 运抵编号
     */
    @ApiModelProperty(name = "arriveNo", value = "运抵编号")
    @TableField(value = "arrive_no")
    private String arriveNo;

    /**
     * 箱号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    private String contaNo;

    /**
     * 审核状态（01待申报、02申报海关、03审核通过、04审核不通过）
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态 ,传value")
    private String auditStatus;

    /**
     * 运抵时间
     */
    @ApiModelProperty(name = "startArriveTime", value = "运抵时间-开始")
    private Long startArriveTime;

    /**
     * 运抵时间
     */
    @ApiModelProperty(name = "endArriveTime", value = "运抵时间-结束")
    private Long endArriveTime;

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
