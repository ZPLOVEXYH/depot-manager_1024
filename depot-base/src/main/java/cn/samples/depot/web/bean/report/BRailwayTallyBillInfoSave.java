package cn.samples.depot.web.bean.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BRailwayTallyBillInfoSave implements Serializable {
    /**
     *
     */
    @ApiModelProperty(name = "id", value = "id")
    private String id;
    /**
     * 表头ID
     */
    @ApiModelProperty(name = "railwayTallyReportHeadId", value = "表头ID")
    private String railwayTallyReportHeadId;
    /**
     * 运单号
     */
    @ApiModelProperty(name = "billNo", value = "运单号")
    @NotBlank(message = "运单号不能为空")
    private String billNo;
    /**
     * 车皮号
     */
    @ApiModelProperty(name = "carriageNo", value = "车皮号")
    @NotBlank(message = "车皮号不能为空")
    private String carriageNo;
    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    @NotBlank(message = "集装箱号不能为空")
    private String contaNo;
    /**
     * 箱型
     */
    @ApiModelProperty(name = "contaType", value = "箱型")
    private String contaType;
    /**
     * 审核状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)")
    private String auditStatus;
    /**
     * 封志号
     */
    @ApiModelProperty(name = "sealNo", value = "封志号")
    private String sealNo;
    /**
     * 件数
     */
    @ApiModelProperty(name = "packNo", value = "件数")
    private Integer packNo;
    /**
     * 重量
     */
    @ApiModelProperty(name = "grossWt", value = "重量")
    private Double grossWt;
    /**
     * 备注
     */
    @ApiModelProperty(name = "notes", value = "备注")
    private String notes;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();

}
