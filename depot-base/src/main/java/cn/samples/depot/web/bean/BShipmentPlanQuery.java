package cn.samples.depot.web.bean;

import cn.samples.depot.web.entity.BShipmentPlan;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BShipmentPlanQuery {

    /**
     * 发货企业
     */
    @ApiModelProperty(name = "enterprisesId", value = "发货企业")
    @JsonView({BShipmentPlan.View.ARRIVESELECT.class, BShipmentPlan.View.Table.class, BShipmentPlan.View.STATISTICS.class})
    private String enterprisesId;

    /**
     * 发运计划编号
     */
    @ApiModelProperty(name = "shipmentPlanNo", value = "发运计划编号")
    @JsonView({BShipmentPlan.View.ARRIVESELECT.class, BShipmentPlan.View.Table.class})
    private String shipmentPlanNo;


    /**
     * 审核状态（00：待提交、10：待审核、11：审核驳回、12：审核通过、99：作废）
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态（00：待提交、10：待审核、11：审核驳回、12：审核通过、99：作废）")
    @JsonView({BShipmentPlan.View.Table.class})
    private String auditStatus;

    /**
     * 状态（00待落箱，10已落箱，20已发车）,对象：ShipmentPlanStatus
     */
    @ApiModelProperty(name = "status", value = "状态（00待落箱，10已落箱，20已发车）,对象：ShipmentPlanStatus")
    @JsonView(BShipmentPlan.View.STATISTICS.class)
    private String status;


    /**
     * 开始出运时间
     */
    @ApiModelProperty(name = "startShipmentTime", value = "开始出运时间")
    @JsonView({BShipmentPlan.View.ARRIVESELECT.class, BShipmentPlan.View.Table.class, BShipmentPlan.View.STATISTICS.class})
    private Long startShipmentTime;


    /**
     * 结束出运时间
     */
    @ApiModelProperty(name = "endShipmentTime", value = "结束出运时间")
    @JsonView({BShipmentPlan.View.ARRIVESELECT.class, BShipmentPlan.View.Table.class, BShipmentPlan.View.STATISTICS.class})
    private Long endShipmentTime;


}
