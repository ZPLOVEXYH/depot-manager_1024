package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * @Author majunzi
 * @Date 2019/9/20
 * @Description 集装箱作业类型：落箱计划，到货，落箱，移箱，发车
 **/
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ContaOptionType implements TitleValue<String> {
    DROPBOX_PLAN("10", "落箱计划", ShipmentPlanStatus.PRE_DROP),
    GOODS_ARR("20", "到货", ShipmentPlanStatus.PRE_DROP),
    DROPBOX("30", "落箱", ShipmentPlanStatus.DROPPED),
    MOVE_BOX("40", "移箱", ShipmentPlanStatus.DROPPED),
    DEPARTURE("50", "发车", ShipmentPlanStatus.LOADED);

    public interface View {
    }

    private static final Map<String, ContaOptionType> map = EnumUtils.createValuedEnumMap(ContaOptionType.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    String value;

    @JsonView(View.class)
    ShipmentPlanStatus status;

    ContaOptionType(String value, String title, ShipmentPlanStatus status) {
        this.title = title;
        this.value = value;
        this.status = status;
    }

    @JsonView(View.class)
    public String getQuery() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getValue() {
        return value;
    }

    public ShipmentPlanStatus getStatus() {
        return status;
    }

    @JsonCreator
    public static ContaOptionType findByValue(@JsonProperty("value") String value) {
        return map.get(value);
    }

}
