/**
 * @filename:BShipmentGoodsDetail 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.dto.shipment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;

/**
 * @Description: 商品明细表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Data
public class BShipmentGoodsDetailMsg implements Serializable {

    /**
     * 商品明细id
     */
    @ApiModelProperty(name = "id", value = "商品明细id")
    private String id;


    /**
     * 集装箱id
     */
    @ApiModelProperty(name = "shipmentId", value = "集装箱id")
    private String shipmentId;


    /**
     * 发运计划id
     */
    @ApiModelProperty(name = "shipmentPlanId", value = "发运计划id")
    private String shipmentPlanId;


    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "shipmentNo", value = "集装箱号")
    private String shipmentNo;


    /**
     * 商品品名
     */
    @ApiModelProperty(name = "goodsName", value = "商品品名")
    private String goodsName;


    /**
     * 件数
     */
    @ApiModelProperty(name = "pieceNum", value = "件数")
    private Integer pieceNum;


    /**
     * 重量（KG)
     */
    @ApiModelProperty(name = "weight", value = "重量（KG)")
    private Double weight;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;


}
