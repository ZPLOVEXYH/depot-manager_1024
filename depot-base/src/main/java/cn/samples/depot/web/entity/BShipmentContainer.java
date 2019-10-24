/**
 * @filename:BShipmentContainer 2019年7月24日
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
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;


/**
 * @Description: 集装箱信息表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@TableName("b_shipment_container")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ExcelTarget("bShipmentContainer")
public class BShipmentContainer extends Model<BShipmentContainer> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     * 集装箱信息id
     */
    @ApiModelProperty(name = "id", value = "集装箱信息id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "containerNo", value = "集装箱号")
    @TableField(value = "container_no")
    @Excel(name = "箱号")
    private String containerNo;


    /**
     * 发运计划id
     */
    @ApiModelProperty(name = "shipmentPlanId", value = "发运计划id")
    @TableField(value = "shipment_plan_id")
    private String shipmentPlanId;


    /**
     * 铅封号
     */
    @ApiModelProperty(name = "sealNo", value = "铅封号")
    @TableField(value = "seal_no")
    @Excel(name = "铅封号")
    private String sealNo;


    /**
     * 集装箱尺寸名称
     */
    @ApiModelProperty(name = "contaModelName", value = "集装箱尺寸名称")
    @TableField(value = "conta_model_name")
    private String contaModelName;

    /**
     * 集装箱尺寸编号
     */
    @Excel(name = "集装箱尺寸")
    @TableField(exist = false) // 忽视实体类和数据库之间的映射字段
    private String contaModelCode;


    /**
     * 件数
     */
    @ApiModelProperty(name = "pieceNum", value = "件数")
    @TableField(value = "piece_num")
    private Integer pieceNum;


    /**
     * 重量（KG)
     */
    @ApiModelProperty(name = "weight", value = "重量（KG)")
    @TableField(value = "weight")
    private Double weight;

    /**
     * 状态，默认为空，发运计划审核通过之后 置为 待落箱
     **/
    @ApiModelProperty(name = "status", value = "状态（00待落箱，10已落箱，20已发车）,对象：ShipmentPlanStatus")
    @TableField(value = "status")
    private String status;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 提运单号
     */
    @TableField(exist = false)
    private String deliveryNo;
}
