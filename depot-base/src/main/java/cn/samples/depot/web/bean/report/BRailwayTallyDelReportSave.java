package cn.samples.depot.web.bean.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BRailwayTallyDelReportSave implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    /**
     * 报文编号
     */
    @ApiModelProperty(name = "messageId", value = "报文编号")
    private String messageId;
    /**
     * 申报编号
     */
    @ApiModelProperty(name = "messageIdAdd", value = "申报编号")
    private String messageIdAdd;
    /**
     * 报文类型
     */
    @ApiModelProperty(name = "messageType", value = "报文类型")
    private String messageType;
    /**
     * 海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "海关代码")
    private String customsCode;
    /**
     * 卸货地代码
     */
    @ApiModelProperty(name = "dischargePlace", value = "卸货地代码")
    private String dischargePlace;
    /**
     * 卸货地代码
     */
    @ApiModelProperty(name = "unloadingPlace", value = "卸货地代码")
    private String unloadingPlace;
    /**
     * 装货地代码
     */
    @ApiModelProperty(name = "loadingPlace", value = "装货地代码")
    private String loadingPlace;
    /**
     * 装卸开始时间
     */
    @ApiModelProperty(name = "actualDatetime", value = "装卸开始时间")
    private Long actualDatetime;
    /**
     * 装卸结束时间
     */
    @ApiModelProperty(name = "completedDatetime", value = "装卸结束时间")
    private Long completedDatetime;
    /**
     * 审核状态
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态")
    private String auditStatus;
    /**
     * 申报时间
     */
    @ApiModelProperty(name = "auditTime", value = "申报时间")
    private Long auditTime;
    /**
     * 进出口标记（I 进口,E 出口）
     */
    @ApiModelProperty(name = "iEFlag", value = "进出口标记（I 进口,E 出口）")
    @JsonProperty(value = "ieflag")
    private String iEFlag;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Long createTime = System.currentTimeMillis();

    @ApiModelProperty(name = "delBillInfoSaves", value = "集装箱运单信息集合")
    private List<BRailwayTallyDelBillInfoSave> delBillInfoSaveList;
}
