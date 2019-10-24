package cn.samples.depot.web.bean.report;

import cn.samples.depot.web.entity.xml.BillInfo;
import cn.samples.depot.web.entity.xml.ContainerInfo;
import cn.samples.depot.web.entity.xml.FormInfo;
import cn.samples.depot.web.entity.xml.ShipInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 海关放行指令明细页bean
 */
@Data
@Builder
public class BRelReportResponse implements Serializable {

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
     * 报文类型(放行WLJK_REL,装车WLJK_TLA)
     */
    @ApiModelProperty(name = "messageType", value = "报文类型(放行WLJK_REL,装车WLJK_TLA)")
    private String messageType;
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
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Long createTime;
    /**
     * 放行时间
     */
    @ApiModelProperty(name = "relTime", value = "放行时间")
    private Long relTime;
    /**
     * 运输工具
     */
    @ApiModelProperty(name = "shipInfo", value = "放行时间")
    private ShipInfo shipInfo;
    /**
     * 运单信息集合
     */
    @ApiModelProperty(name = "billInfoList", value = "运单信息集合")
    private List<BillInfo> billInfoList;
    /**
     * 集装箱信息集合
     */
    @ApiModelProperty(name = "containerInfoList", value = "集装箱信息集合")
    private List<ContainerInfo> containerInfoList;
    /**
     * 单证信息集合
     */
    @ApiModelProperty(name = "formInfoList", value = "单证信息集合")
    private List<FormInfo> formInfoList;
}
