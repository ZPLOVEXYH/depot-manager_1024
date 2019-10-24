package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 航线标记（1干线、2支线、3内贸/空箱）
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LineFlag implements TitleValue<String> {
    LINE_FLAG_1("1", "干线"),
    LINE_FLAG_2("2", "支线"),
    LINE_FLAG_3("3", "内贸/空箱");

    public static final Map<String, LineFlag> map = EnumUtils.createValuedEnumMap(LineFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    LineFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static LineFlag findByValue(@JsonProperty("value") String value) {
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
