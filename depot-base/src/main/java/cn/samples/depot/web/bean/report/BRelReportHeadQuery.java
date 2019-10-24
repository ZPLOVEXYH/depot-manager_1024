package cn.samples.depot.web.bean.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 海关放行指令列表分页查询请求bean
 */
@Data
public class BRelReportHeadQuery implements Serializable {

    /**
     * 海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "海关代码")
    private String customsCode;
    /**
     * 报文类型(放行WLJK_REL,装车WLJK_TLA)
     */
    @ApiModelProperty(name = "messageType", value = "报文类型(放行WLJK_REL,装车WLJK_TLA)")
    private String messageType;
    /**
     * 进出口标记（I 进口,E 出口）
     */
    @ApiModelProperty(name = "ieFlag", value = "进出口标记（I 进口,E 出口）")
    @JsonProperty(value = "ieflag")
    private String ieFlag;
    /**
     * 运输工具名称
     */
    @ApiModelProperty(name = "shipNameEn", value = "运输工具名称")
    private String shipNameEn;
    /**
     * 提运单号
     */
    @ApiModelProperty(name = "billNo", value = "提运单号")
    private String billNo;
    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    private String contaNo;
    /**
     * 开始创建时间
     */
    @ApiModelProperty(name = "startCreateTime", value = "开始创建时间")
    private Long startCreateTime;
    /**
     * 结束创建时间
     */
    @ApiModelProperty(name = "endCreateTime", value = "结束创建时间")
    private Long endCreateTime;

    public String getContaNo() {
        if (null != contaNo) {
            return "%" + contaNo + "%";
        }
        return contaNo;
    }

    public String getBillNo() {
        if (null != billNo) {
            return "%" + billNo + "%";
        }
        return billNo;
    }

    public String getShipNameEn() {
        if (null != shipNameEn) {
            return "%" + shipNameEn + "%";
        }
        return shipNameEn;
    }
}
