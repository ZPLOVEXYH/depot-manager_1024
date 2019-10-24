/**
 * @filename:BShipmentGoodsDetail 2019年7月24日
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
 * @Description: 商品明细表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@TableName("b_shipment_goods_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ExcelTarget("bShipmentGoodsDetail")
public class BShipmentGoodsDetail extends Model<BShipmentGoodsDetail> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     * 商品明细id
     */
    @ApiModelProperty(name = "id", value = "商品明细id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 集装箱id
     */
    @ApiModelProperty(name = "containerId", value = "集装箱id")
    @TableField(value = "container_id")
    private String containerId;


    /**
     * 发运计划id
     */
    @ApiModelProperty(name = "shipmentPlanId", value = "发运计划id")
    @TableField(value = "shipment_plan_id")
    private String shipmentPlanId;


    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "containerNo", value = "集装箱号")
    @TableField(value = "container_no")
    private String containerNo;


    /**
     * 商品品名
     */
    @ApiModelProperty(name = "goodsName", value = "商品品名")
    @TableField(value = "goods_name")
    @Excel(name = "品名")
    private String goodsName;


    /**
     * 件数
     */
    @ApiModelProperty(name = "pieceNum", value = "件数")
    @TableField(value = "piece_num")
    @Excel(name = "件数")
    private Integer pieceNum;


    /**
     * 重量（KG)
     */
    @ApiModelProperty(name = "weight", value = "重量（KG)")
    @TableField(value = "weight")
    @Excel(name = "货物重量/KG")
    private Double weight;


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

    /**
     * 集装箱尺寸
     */
    @TableField(exist = false)
    private String contaModelCode;
}