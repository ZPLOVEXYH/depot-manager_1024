/**
 * @filename:BShipmentPlan 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
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
 * @Description: 发运计划表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@TableName("b_shipment_plan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ExcelTarget("bShipmentPlan")
public class BShipmentPlan extends Model<BShipmentPlan> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }

        interface ARRIVESELECT extends View {
            //运抵查询
        }

        interface STATISTICS extends View {
            //发运计划查询
        }
    }

    /**
     * 发运计划id
     */
    @ApiModelProperty(name = "id", value = "发运计划id")
    @TableId(value = "id", type = IdType.UUID)
    @JsonView(View.ARRIVESELECT.class)
    private String id;


    /**
     * 发货企业
     */
    @ApiModelProperty(name = "enterprisesId", value = "发货企业")
    @TableField(value = "enterprises_id")
    @JsonView(View.ARRIVESELECT.class)
    private String enterprisesId;


    /**
     * 企业名称
     */
    @ApiModelProperty(name = "enterprisesName", value = "企业名称")
    @TableField(value = "enterprises_name")
    @JsonView(View.ARRIVESELECT.class)
    private String enterprisesName;


    /**
     * 发运计划编号
     */
    @ApiModelProperty(name = "shipmentPlanNo", value = "发运计划编号")
    @TableField(value = "shipment_plan_no")
    @JsonView(View.ARRIVESELECT.class)
    private String shipmentPlanNo;


    /**
     * 提运单号
     */
    @ApiModelProperty(name = "deliveryNo", value = "提运单号")
    @TableField(value = "delivery_no")
    @Excel(name = "提运单号")
    private String deliveryNo;


    /**
     * 发货场站id
     */
    @ApiModelProperty(name = "stationsCode", value = "发货场站编码")
    @TableField(value = "stations_code")
    private String stationsCode;


    /**
     * 发货场站名称
     */
    @ApiModelProperty(name = "stationsName", value = "发货场站名称")
    @TableField(value = "stations_name")
    private String stationsName;


    /**
     * 箱型箱量
     */
    @ApiModelProperty(name = "containerNum", value = "箱型箱量")
    @TableField(value = "container_num")
    private String containerNum;


    /**
     * 总件数
     */
    @ApiModelProperty(name = "pieceTotal", value = "总件数")
    @TableField(value = "piece_total")
    private Integer pieceTotal;


    /**
     * 总重量（KG)
     */
    @ApiModelProperty(name = "weightTotal", value = "总重量（KG)")
    @TableField(value = "weight_total")
    private Double weightTotal;


    /**
     * 审核状态（00：待提交、01：发送失败、02：发送成功、03:接收失败、10：待审核、11：审核驳回、12：审核通过、99：作废）
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态（00：待提交、01：发送失败、02：发送成功、03:接收失败、10：待审核、11：审核驳回、12：审核通过、99：作废）")
    @TableField(value = "audit_status")
    private String auditStatus;

    /**
     * 状态，默认为空，审核通过之后 置为 待落箱
     **/
    @ApiModelProperty(name = "status", value = "状态（00待落箱，10已落箱，20已发车）,对象：ShipmentPlanStatus")
    @TableField(value = "status")
    private String status;


    /**
     * 出运时间
     */
    @ApiModelProperty(name = "shipmentTime", value = "出运时间")
    @TableField(value = "shipment_time")
    @JsonView(View.ARRIVESELECT.class)
    private Long shipmentTime;

    /**
     * 导入excel的出运时间
     */
    @TableField(exist = false)
    @Excel(name = "出运时间（格式：年-月-日）")
    private String shipmentDate;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


    /**
     * 创建人
     */
    @ApiModelProperty(name = "createId", value = "创建人")
    @TableField(value = "create_id")
    private String createId;


    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    @TableField(value = "remark")
    private String remark;
}
