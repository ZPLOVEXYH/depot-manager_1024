package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 货到确认状态（00：未到货、10：已到货）
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GoodsConfirmStatus implements TitleValue<String> {
    NO_GOODS_ARRIVAL("00", "未到货"),
    GOODS_ARRIVAL("10", "已到货");

    public interface View {
    }

    public static final Map<String, GoodsConfirmStatus> map = EnumUtils.createValuedEnumMap(GoodsConfirmStatus.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    String value;

    GoodsConfirmStatus(String value, String title) {
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
    public static GoodsConfirmStatus findByValue(@JsonProperty("value") String value) {
        return map.get(value);
    }

}
