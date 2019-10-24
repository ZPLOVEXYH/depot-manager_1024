/**
 * @filename:BShipmentPlan 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.dto.shipment;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.BShipmentContainer;
import cn.samples.depot.web.entity.BShipmentGoodsDetail;
import cn.samples.depot.web.entity.BShipmentPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 发运计划保存bean
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BShipmentPlanMsg implements Serializable {

    /**
     * 发运计划id
     */
    @ApiModelProperty(name = "id", value = "发运计划id")
    private String id;


    /**
     * 发货企业
     */
    @ApiModelProperty(name = "enterprisesId", value = "发货企业")
    private String enterprisesId;


    /**
     * 企业名称
     */
    @ApiModelProperty(name = "enterprisesName", value = "企业名称")
    private String enterprisesName;


    /**
     * 发运计划编号
     */
    @ApiModelProperty(name = "shipmentPlanNo", value = "发运计划编号")
    private String shipmentPlanNo;


    /**
     * 提运单号
     */
    @ApiModelProperty(name = "deliveryNo", value = "提运单号")
    private String deliveryNo;


    /**
     * 发货场站id
     */
    @ApiModelProperty(name = "stationsCode", value = "发货场站编码")
    private String stationsCode;


    /**
     * 发货场站名称
     */
    @ApiModelProperty(name = "stationsName", value = "发货场站名称")
    private String stationsName;


    /**
     * 箱型箱量
     */
    @ApiModelProperty(name = "containerNum", value = "箱型箱量")
    private String containerNum;


    /**
     * 总件数
     */
    @ApiModelProperty(name = "pieceTotal", value = "总件数")
    private Integer pieceTotal;


    /**
     * 总重量（KG)
     */
    @ApiModelProperty(name = "weightTotal", value = "总重量（KG)")
    private Double weightTotal;


    /**
     * 审核状态（00：待提交、10：待审核、11：审核驳回、12：审核通过、99：作废）
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态（00：待提交、10：待审核、11：审核驳回、12：审核通过、99：作废）")
    private String auditStatus;


    /**
     * 出运时间
     */
    @ApiModelProperty(name = "shipmentTime", value = "出运时间")
    private Long shipmentTime;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


    /**
     * 创建人
     */
    @ApiModelProperty(name = "createId", value = "创建人")
    private String createId;


    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 集装箱集合
     */
    @ApiModelProperty(name = "containerList", value = "集装箱集合")
    private List<BShipmentContainerMsg> containerList;

    public BShipmentPlan buildBShipmentPlan() {
        return BShipmentPlan.builder()
                .id(this.id)
                .enterprisesId(this.enterprisesId)
                .enterprisesName(this.enterprisesName)
                .shipmentPlanNo(this.shipmentPlanNo)
                .deliveryNo(this.deliveryNo)
                .stationsCode(this.stationsCode)
                .stationsName(this.stationsName)
                .containerNum(this.containerNum)
                .pieceTotal(this.pieceTotal)
                .weightTotal(this.weightTotal)
                .auditStatus(this.auditStatus)
                .shipmentTime(this.shipmentTime)
                .createTime(this.createTime)
                .createId(this.createId)
                .remark(this.remark)
                .build();
    }

    public List<BShipmentContainer> buildBShipmentContainers() throws BizException {
        if (CollectionUtils.isEmpty(this.containerList)) throw new BizException("集装箱信息为空");
        List<BShipmentContainer> containers = new ArrayList<>();
        for (BShipmentContainerMsg msg : this.containerList) {
            containers.add(BShipmentContainer.builder()
                    .id(msg.getId())
                    .containerNo(msg.getContainerNo())
                    .shipmentPlanId(msg.getShipmentPlanId())
                    .sealNo(msg.getSealNo())
                    .contaModelName(msg.getContaModelName())
                    .pieceNum(msg.getPieceNum())
                    .weight(msg.getWeight())
                    .createTime(msg.getCreateTime())
                    .remark(msg.getRemark())
                    .build());
        }
        return containers;
    }

    public List<BShipmentGoodsDetail> buildBShipmentGoodsDetail() throws BizException {
        List<BShipmentGoodsDetail> goodsDetails = new ArrayList<>();
        for (BShipmentContainerMsg containerMsg : this.containerList) {
            List<BShipmentGoodsDetailMsg> detailMsgs = containerMsg.getGoodsList();
            if (CollectionUtils.isEmpty(detailMsgs))
                throw new BizException("集装箱【" + containerMsg.getContainerNo() + "】的商品信息为空");
            for (BShipmentGoodsDetailMsg msg : detailMsgs) {
                goodsDetails.add(BShipmentGoodsDetail
                        .builder()
                        .id(msg.getId())
                        .containerId(containerMsg.getId())
                        .shipmentPlanId(msg.getShipmentPlanId())
                        .containerNo(containerMsg.getContainerNo())
                        .goodsName(msg.getGoodsName())
                        .pieceNum(msg.getPieceNum())
                        .weight(msg.getWeight())
                        .createTime(msg.getCreateTime())
                        .remark(msg.getRemark())
                        .build());
            }
        }
        return goodsDetails;
    }
}
