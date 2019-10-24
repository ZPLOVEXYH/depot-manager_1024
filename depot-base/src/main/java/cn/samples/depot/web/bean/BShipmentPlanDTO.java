package cn.samples.depot.web.bean;

import cn.samples.depot.web.entity.BShipmentContainer;
import cn.samples.depot.web.entity.BShipmentGoodsDetail;
import cn.samples.depot.web.entity.BShipmentPlan;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
/**
 * 新增发运计划传输对象
 * **/
@Builder
public class BShipmentPlanDTO {
    /**
     * 发运计划对象
     */
    private BShipmentPlan shipmentPlan;
    /**
     * 发运计划商品明细集合
     */
    private List<BShipmentGoodsDetail> goodsList;
    /**
     * 发运计划集装箱信息集合
     */
    private List<BShipmentContainer> containerList;
}
