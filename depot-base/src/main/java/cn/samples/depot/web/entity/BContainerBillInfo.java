/**
 * @filename:BContainerBillInfo 2019年09月20日
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
 * @Description: 集装箱单货信息表
 * @Author: ZhangPeng
 * @CreateDate: 2019年09月20日
 * @Version: V1.0
 */
@TableName("b_container_bill_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BContainerBillInfo extends Model<BContainerBillInfo> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 箱状态表ID
     */
    @ApiModelProperty(name = "containerId", value = "箱状态表ID")
    @TableField(value = "container_id")
    private String containerId;

    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "containerNo", value = "集装箱号")
    @TableField(value = "container_no")
    private String containerNo;


    /**
     * 发运计划ID
     */
    @ApiModelProperty(name = "shipmentPlanId", value = "发运计划ID")
    @TableField(value = "shipment_plan_id")
    private String shipmentPlanId;


    /**
     * 提运单号
     */
    @ApiModelProperty(name = "billNo", value = "提运单号")
    @TableField(value = "bill_no")
    private String billNo;


    /**
     * 发运计划编号
     */
    @ApiModelProperty(name = "shipmentPlanNo", value = "发运计划编号")
    @TableField(value = "shipment_plan_no")
    private String shipmentPlanNo;


    /**
     * 件数
     */
    @ApiModelProperty(name = "packNo", value = "件数")
    @TableField(value = "pack_no")
    private Integer packNo;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
