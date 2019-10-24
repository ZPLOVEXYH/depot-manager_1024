/**
 * @filename:BShipmentAuditLog 2019年7月24日
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
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;


/**
 * @Description: 审核日志表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@TableName("b_shipment_audit_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BShipmentAuditLog extends Model<BShipmentAuditLog> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

    }

    /**
     * 审核日志id
     */
    @ApiModelProperty(name = "id", value = "审核日志id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 发运计划id
     */
    @ApiModelProperty(name = "shipmentPlanId", value = "发运计划id")
    @TableField(value = "shipment_plan_id")
    private String shipmentPlanId;


    /**
     * 审核人
     */
    @ApiModelProperty(name = "auditor", value = "审核人")
    @TableField(value = "auditor")
    private String auditor;


    /**
     * 审核说明
     */
    @ApiModelProperty(name = "auditRemark", value = "审核说明")
    @TableField(value = "audit_remark")
    private String auditRemark;


    /**
     * 审核时间
     */
    @ApiModelProperty(name = "auditTime", value = "审核时间")
    @TableField(value = "audit_time")
    @CreatedDate
    private Long auditTime = System.currentTimeMillis();


    /**
     * 审核状态（00：待提交、10：待审核、11：审核驳回、12：审核通过、99：作废）
     */
    @ApiModelProperty(name = "auditResult", value = "审核状态（00：待提交、01：发送失败、02：发送成功、10：待审核、11：审核驳回、12：审核通过、99：作废）")
    @TableField(value = "audit_result")
    private String auditResult;


}
