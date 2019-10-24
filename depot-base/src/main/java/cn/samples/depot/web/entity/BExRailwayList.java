/**
 * @filename:BExRailwayList 2019年08月21日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.samples.depot.common.model.CRUDView;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 铁路运抵申报报文表体运抵单信息
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月21日
 * @Version: V1.0
 */
@TableName("b_ex_railway_list")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BExRailwayList extends Model<BExRailwayList> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    @JsonView(View.SELECT.class)
    private String id;
    /**
     * 表头ID
     */
    @ApiModelProperty(name = "exRailwayReportHeadId", value = "表头ID")
    @TableField(value = "ex_railway_report_head_id")
    private String exRailwayReportHeadId;
    /**
     * 运抵编号
     */
    @ApiModelProperty(name = "arriveNo", value = "运抵编号")
    @TableField(value = "arrive_no")
    @JsonView(View.SELECT.class)
    private String arriveNo;
    /**
     * 件数
     */
    @ApiModelProperty(name = "packNo", value = "件数")
    @TableField(value = "pack_no")
    private Integer packNo;
    /**
     * 重量
     */
    @ApiModelProperty(name = "grossWt", value = "重量")
    @TableField(value = "gross_wt")
    private Double grossWt;
    /**
     * 备注
     */
    @ApiModelProperty(name = "notes", value = "备注")
    @TableField(value = "notes")
    private String notes;
    /**
     * 审核状态
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态")
    @TableField(value = "audit_status")
    private String auditStatus;
    /**
     * 审核时间
     */
    @ApiModelProperty(name = "auditTime", value = "审核时间")
    @TableField(value = "audit_time")
    private Long auditTime;
    /**
     * 发货企业名称
     */
    @ApiModelProperty(name = "enterprisesName", value = "发货企业名称")
    @TableField(value = "enterprises_name")
    private String enterprisesName;
    /**
     * 发货企业ID
     */
    @ApiModelProperty(name = "enterprisesId", value = "发货企业ID")
    @TableField(value = "enterprises_id")
    private String enterprisesId;
    /**
     * 发运计划编号
     */
    @ApiModelProperty(name = "shipmentPlanNo", value = "发运计划编号")
    @TableField(value = "shipment_plan_no")
    private String shipmentPlanNo;
    /**
     * 发运时间
     */
    @ApiModelProperty(name = "shipmentTime", value = "发运时间")
    @TableField(value = "shipment_time")
    private Long shipmentTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }


}
