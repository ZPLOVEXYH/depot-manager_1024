package cn.samples.depot.web.bean.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 铁路进口理货作废报文表头
 */
@Data
public class RspBRailwayTallyDelReport {
    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    /**
     * 理货作废编号
     */
    @ApiModelProperty(name = "messageId", value = "理货作废编号")
    private String messageId;
    /**
     * 对应理货报文编号
     */
    @ApiModelProperty(name = "messageIdAdd", value = "对应理货报文编号")
    private String messageIdAdd;
    /**
     * 报文类型
     */
    @ApiModelProperty(name = "messageType", value = "报文类型")
    private String messageType;
    /**
     * 报文回执类型
     */
    @ApiModelProperty(name = "rspMessageType", value = "报文回执类型")
    private String rspMessageType;
    /**
     * 进出口标记（I 进口,E 出口）
     */
    @ApiModelProperty(name = "iEFlag", value = "进出口标记（I 进口,E 出口）")
    @JsonProperty(value = "ieflag")
    private String iEFlag;
    /**
     * 海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "海关代码")
    private String customsCode;
    /**
     * 海关名称
     */
    @ApiModelProperty(name = "customsName", value = "海关名称")
    private String customsName;
    /**
     * 卸货地代码
     */
    @ApiModelProperty(name = "unloadingPlace", value = "卸货地代码")
    private String unloadingPlace;
    /**
     * 卸货地中文名称
     */
    @ApiModelProperty(name = "unloadingPlaceName", value = "卸货地中文名称")
    private String unloadingPlaceName;
    /**
     * 装货地代码
     */
    @ApiModelProperty(name = "loadingPlace", value = "装货地代码")
    private String loadingPlace;
    /**
     * 装货地中文名称
     */
    @ApiModelProperty(name = "loadingPlaceName", value = "装货地中文名称")
    private String loadingPlaceName;
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
     * 申报状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)
     */
    @ApiModelProperty(name = "auditStatus", value = "申报状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)")
    private String auditStatus;
    /**
     * 申报时间
     */
    @ApiModelProperty(name = "auditTime", value = "申报时间")
    private Long auditTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Long createTime;
}
