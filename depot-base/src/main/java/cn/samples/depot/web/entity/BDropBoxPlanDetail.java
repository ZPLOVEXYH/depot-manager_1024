/**
 * @filename:BDropBoxPlanDetail 2019年08月01日
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
 * @Description: 落箱计划明细表(具体发运信息)
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@TableName("b_drop_box_plan_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BDropBoxPlanDetail extends Model<BDropBoxPlanDetail> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     * 审核日志id
     */
    @ApiModelProperty(name = "id", value = "审核日志id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 落箱计划ID
     */
    @ApiModelProperty(name = "dropBoxPlanId", value = "落箱计划ID")
    @TableField(value = "drop_box_plan_id")
    private String dropBoxPlanId;


    /**
     * 发货企业ID
     */
    @ApiModelProperty(name = "enterprisesId", value = "发货企业ID")
    @TableField(value = "enterprises_id")
    private String enterprisesId;


    /**
     * 发货企业名称
     */
    @ApiModelProperty(name = "enterprisesName", value = "发货企业名称")
    @TableField(value = "enterprises_name")
    private String enterprisesName;

    /**
     * 发运计划id
     */
    @ApiModelProperty(name = "shipmentPlanId", value = "发运计划id")
    @TableField(value = "shipment_plan_id")
    private String shipmentPlanId;

    /**
     * 发运计划编号
     */
    @ApiModelProperty(name = "shipmentPlanNo", value = "发运计划编号")
    @TableField(value = "shipment_plan_no")
    private String shipmentPlanNo;


    /**
     * 总件数
     */
    @ApiModelProperty(name = "pieceTotal", value = "总件数")
    @TableField(value = "piece_total")
    private Integer pieceTotal;


    /**
     * 总重量
     */
    @ApiModelProperty(name = "weightTotal", value = "总重量")
    @TableField(value = "weight_total")
    private Double weightTotal;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
