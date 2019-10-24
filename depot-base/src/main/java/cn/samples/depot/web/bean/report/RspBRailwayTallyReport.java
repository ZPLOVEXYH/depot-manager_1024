package cn.samples.depot.web.bean.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 理货报告多表查询分页列表
 */
@Data
public class RspBRailwayTallyReport {
    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    /**
     * 报文编码
     */
    @ApiModelProperty(name = "messageId", value = "报文编码")
    private String messageId;
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
    private String iEFlag;
    /**
     * 海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "海关代码")
    private String customsCode;
    /**
     * 海关代码中文
     */
    @ApiModelProperty(name = "customsName", value = "海关代码中文")
    private String customsName;
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
     * 卸货地代码中文名称
     */
    @ApiModelProperty(name = "unloadingPlaceName", value = "卸货地代码中文名称")
    private String unloadingPlaceName;
    /**
     * 装货地代码中文名称
     */
    @ApiModelProperty(name = "loadingPlaceName", value = "装货地代码中文名称")
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
     * 审核状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)")
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
