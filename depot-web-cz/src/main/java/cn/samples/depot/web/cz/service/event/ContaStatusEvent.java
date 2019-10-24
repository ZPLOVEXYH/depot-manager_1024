package cn.samples.depot.web.cz.service.event;

import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.web.bean.BShipmentPlanDTO;
import cn.samples.depot.web.entity.*;
import cn.samples.depot.web.service.event.CommonEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author majunzi
 * @Date 2019/9/23
 * @Description 集装箱状态 事件
 * 集装箱状态表（b_container_info）
 * 单货信息表（b_container_bill_info）
 * 集装箱历史记录表（b_container_history）
 * <p>
 * 工作：
 * 事情的起点：发运计划审核通过
 * <p>
 * 一：发运计划 审核通过
 * 1.发运计划：更新状态为 待落箱；更新集装箱状态为待落箱
 * 2.生成集装箱状态记录（b_container_info）：箱号，箱型，铅封号，场站编码，场站名称，状态：默认待落箱，发货企业 ，发运计划编号，进出口（E），
 * 3.生成集装箱单货信息记录（b_container_bill_info）：
 * <p>
 * 二：落箱计划 落箱安排
 * //1.更新发运计划及集装箱状态 待落箱
 * 2.生成集装箱历史信息 （b_container_history）：
 * <p>
 * 三：货到确认
 * //1.更新发运计划及集装箱状态 待落箱
 * 2.生成集装箱历史信息 （b_container_history）：
 * 3.更新集装箱状态记录（b_container_info）：进场时间：货到确认的时间
 * <p>
 * 四：落箱确认 落箱
 * 1.更新发运计划及集装箱状态 已落箱
 * 2.生成集装箱历史信息 （b_container_history）：
 * <p>
 * 五：移箱确认 移箱
 * 1.更新发运计划及集装箱状态 已落箱
 * 2.生成集装箱历史信息 （b_container_history）：
 * <p>
 * 六：发车确认 发车
 * 1.更新发运计划及集装箱状态 已发车
 * 2.生成集装箱历史信息 （b_container_history）：
 * 3.更新集装箱状态记录（b_container_info）：出场时间（发车确认的时间），状态（已发车），运抵放行，理货放行，装车记录放行
 * <p>
 * 进场时间：货到确认的时间
 * 出场时间：发车确认的时间
 * <p>
 * 堆区，堆位：实时查
 * 运抵放行：实时查
 * 理货放行：实时查
 * 装车记录放行：实时查
 * <p>
 * 提货企业：空
 **/
@Slf4j
public class ContaStatusEvent extends CommonEvent<Object> {

    ContaOptionType optionType;

    public ContaStatusEvent(BShipmentPlanDTO shipmentPlanDto) {
        super(shipmentPlanDto);
    }

    public ContaStatusEvent(Object source, ContaOptionType optionType) {
        super(source);
        this.optionType = optionType;
    }

    public boolean isShipmentPass() {
        return optionType == null;
    }

    public boolean isDropBoxPlan() {
        return optionType == ContaOptionType.DROPBOX_PLAN && source instanceof BDropBoxPlan;
    }

    public boolean isGoodsArr() {
        return optionType == ContaOptionType.GOODS_ARR && source instanceof BGoodsArriveConfirm;
    }

    public boolean isDropBox() {
        return optionType == ContaOptionType.DROPBOX && source instanceof BDropBoxConfirm;
    }

    public boolean isMoveBox() {
        return optionType == ContaOptionType.MOVE_BOX && source instanceof BMoveBoxConfirm;
    }

    public boolean isDeparture() {
        return optionType == ContaOptionType.DEPARTURE && source instanceof BDepartureConfirm;
    }

    public ContaOptionType getOptionType() {
        return optionType;
    }
}
