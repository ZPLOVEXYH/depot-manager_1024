package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 发运计划状态去（集装箱状态）（00：待落箱；已落箱；已发车）
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ShipmentPlanStatus implements TitleValue<String> {
    PRE_DROP("00", "待落箱"),
    DROPPED("10", "已落箱"),
    LOADED("20", "已发车");

    public interface View {
    }

    private static final Map<String, ShipmentPlanStatus> map = EnumUtils.createValuedEnumMap(ShipmentPlanStatus.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    String value;

    ShipmentPlanStatus(String value, String title) {
        this.title = title;
        this.value = value;
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

    @JsonCreator
    public static ShipmentPlanStatus findByValue(@JsonProperty("value") String value) {
        return map.get(value);
    }

    public static String[] beforeCurrent(ShipmentPlanStatus status) {
        String[] statuses = null;
        switch (status) {
            case DROPPED:
                statuses = new String[]{ShipmentPlanStatus.PRE_DROP.getValue()};
                break;
            case LOADED:
                statuses = new String[]{ShipmentPlanStatus.PRE_DROP.getValue(), ShipmentPlanStatus.DROPPED.getValue()};
                break;
            default:
                break;
        }
        return statuses;
    }

}
