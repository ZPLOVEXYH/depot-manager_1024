/**
 * @filename:BRailwayTallyReportHead 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.bean.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 铁路进口理货申请报文表头
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BRailwayTallyReportHeadSave implements Serializable {

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
     * 进出口标记（I 进口,E 出口）
     */
    @ApiModelProperty(name = "iEFlag", value = "进出口标记（I 进口,E 出口）")
    @JsonProperty(value = "ieflag")
    private String iEFlag;
    /**
     * 海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "海关代码")
    @NotBlank(message = "海关代码不能为空")
    private String customsCode;
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
    @CreatedDate
    private Long createTime = System.currentTimeMillis();

    @ApiModelProperty(name = "billInfoSaves", value = "集装箱运单信息集合")
    private List<BRailwayTallyBillInfoSave> billInfoSaveList = new ArrayList<>();

}
