package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 放行指令报文类型代码
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RelMessageTypeFlag implements TitleValue<String> {
    WLJK_TREL("WLJK_TREL", "铁路放行指令"),
    WLJK_TLN("WLJK_TLN", "铁路装车通知");

    private static final Map<String, RelMessageTypeFlag> map = EnumUtils.createValuedEnumMap(RelMessageTypeFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    RelMessageTypeFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static RelMessageTypeFlag findByValue(@JsonProperty("value") String value) {
        return map.get(value);
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

    public interface View {
    }

}
