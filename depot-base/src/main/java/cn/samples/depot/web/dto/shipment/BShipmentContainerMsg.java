/**
 * @filename:BShipmentContainer 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.dto.shipment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.util.List;


/**
 * @Description: 集装箱信息表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Data
public class BShipmentContainerMsg implements Serializable {

    /**
     * 集装箱信息id
     */
    @ApiModelProperty(name = "id", value = "集装箱信息id")
    private String id;


    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "containerNo", value = "集装箱号")
    private String containerNo;


    /**
     * 发运计划id
     */
    @ApiModelProperty(name = "shipmentPlanId", value = "发运计划id")
    private String shipmentPlanId;


    /**
     * 铅封号
     */
    @ApiModelProperty(name = "sealNo", value = "铅封号")
    private String sealNo;


    /**
     * 集装箱尺寸名称
     */
    @ApiModelProperty(name = "contaModelName", value = "集装箱尺寸名称")
    private String contaModelName;


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

    /**
     * 商品明细集合
     */
    @ApiModelProperty(name = "goodsList", value = "商品明细集合")
    private List<BShipmentGoodsDetailMsg> goodsList;
}
