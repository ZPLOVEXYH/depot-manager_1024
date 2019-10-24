package cn.samples.depot.web.bean.load.add;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class RspLoadReport implements Serializable {

    /**
     * 返回的集装箱信息集合
     */
    List<RspLoadReportConta> contaList;
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
     * 审核状态（01待申报、02申报海关、03审核通过、04审核不通过）
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态（01待申报、02申报海关、03审核通过、04审核不通过）")
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
