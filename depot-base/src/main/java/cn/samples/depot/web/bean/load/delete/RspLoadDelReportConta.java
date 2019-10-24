package cn.samples.depot.web.bean.load.delete;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RspLoadDelReportConta implements Serializable {

    @ApiModelProperty(name = "id", value = "id")
    private String id;
    /**
     * 表头ID
     */
    @ApiModelProperty(name = "railwayLoadReportHeadId", value = "表头ID")
    private String railwayLoadReportHeadId;
    /**
     * 提单号
     */
    @ApiModelProperty(name = "billNo", value = "提单号")
    private String billNo;
    /**
     * 箱号
     */
    @ApiModelProperty(name = "contaNo", value = "箱号")
    private String contaNo;
    /**
     * 箱型
     */
    @ApiModelProperty(name = "contaType", value = "箱型")
    private String contaType;
    /**
     * 封志号
     */
    @ApiModelProperty(name = "sealNo", value = "封志号")
    private String sealNo;
    /**
     * 封志数量
     */
    @ApiModelProperty(name = "sealNum", value = "封志数量")
    private Integer sealNum;
    /**
     * 车皮号
     */
    @ApiModelProperty(name = "carriageNo", value = "车皮号")
    private String carriageNo;
    /**
     * 备注
     */
    @ApiModelProperty(name = "notes", value = "备注")
    private String notes;
    /**
     * 审核状态
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态")
    private String auditStatus;
    /**
     * 审核时间
     */
    @ApiModelProperty(name = "auditTime", value = "审核时间")
    private Long auditTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Long createTime;
}
