package cn.samples.depot.web.bean.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 海关放行指令列表分页查询请求bean
 */
@Data
public class BRelReportHeadRsp implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键")
    private String id;

    /**
     * 放行报文编号
     */
    @ApiModelProperty(name = "messageId", value = "放行报文编号")
    private String messageId;
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
     * 报文类型(放行WLJK_REL,装车WLJK_TLA)
     */
    @ApiModelProperty(name = "messageType", value = "报文类型(放行WLJK_REL,装车WLJK_TLA)")
    private String messageType;
    /**
     * 进出口标记（I 进口,E 出口）
     */
    @ApiModelProperty(name = "ieFlag", value = "进出口标记（I 进口,E 出口）")
    private String ieFlag;
    /**
     * 场站编码
     */
    @ApiModelProperty(name = "stationCode", value = "场站编码")
    private String stationCode;
    /**
     * 场站名称
     */
    @ApiModelProperty(name = "stationName", value = "场站名称")
    private String stationName;
    /**
     * 提运单号
     */
    @ApiModelProperty(name = "billNo", value = "提运单号")
    private String billNo;
    /**
     * H2000运抵编号
     */
    @ApiModelProperty(name = "h2000ArriveNo", value = "H2000运抵编号")
    private String h2000ArriveNo;
    /**
     * 提单放行方式
     */
    @ApiModelProperty(name = "relType", value = "提单放行方式")
    private String relType;
    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    private String contaNo;
    /**
     * 集装箱放行方式
     */
    @ApiModelProperty(name = "contaRelType", value = "集装箱放行方式")
    private String contaRelType;
    /**
     * 运输工具名称
     */
    @ApiModelProperty(name = "shipNameEn", value = "运输工具名称")
    private String shipNameEn;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Long createTime;
    /**
     * 放行时间
     */
    @ApiModelProperty(name = "relTime", value = "放行时间")
    private Long relTime;
}
