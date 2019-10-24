/**
 * @filename:BExRailwayShipmentPlanLog 2019年08月22日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.samples.depot.common.model.CRUDView;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Description: 运抵报告发运计划记录表
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月22日
 * @Version: V1.0
 */
@TableName("b_ex_railway_shipment_plan_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BExRailwayShipmentPlanLog extends Model<BExRailwayShipmentPlanLog> {

    /**
     * 发运计划编号
     */
    @ApiModelProperty(name = "shipmentPlanNo", value = "发运计划编号")
    @TableField(value = "shipment_plan_no")
    private String shipmentPlanNo;

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }


}
