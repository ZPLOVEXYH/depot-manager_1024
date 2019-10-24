package cn.samples.depot.web.bean.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BRailwayTallyDelReportQuery {

    @ApiModelProperty(name = "iEFlag", value = "进出口标记（I 进口,E 出口）")
    @JsonProperty(value = "ieflag")
    private String iEFlag;

    @ApiModelProperty(name = "auditStatus", value = "审核状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)")
    private String auditStatus;

    @ApiModelProperty(name = "customsCode", value = "海关")
    private String customsCode;

    @ApiModelProperty(name = "unloadingPlace", value = "装货地代码")
    private String unloadingPlace;

    @ApiModelProperty(name = "loadingPlace", value = "卸货地代码")
    private String loadingPlace;

    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    private String contaNo;

    @ApiModelProperty(name = "billNo", value = "提单号")
    private String billNo;

    @ApiModelProperty(name = "carriageNo", value = "车皮号")
    private String carriageNo;

    @ApiModelProperty(name = "startCreateTime", value = "开始入库时间")
    private Long startCreateTime;

    @ApiModelProperty(name = "endCreateTime", value = "结束入库时间")
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

    public String getCarriageNo() {
        if (null != carriageNo) {
            return "%" + carriageNo + "%";
        }
        return carriageNo;
    }
}