package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 抬杆类型（00：自动抬杆、10：人工抬杆，99：不抬杆）
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RelTypeFlag implements TitleValue<String> {
    AUTO_REL("00", "自动抬杆"),
    PEOPLE_REL("10", "人工抬杆"),
    NO_REL("99", "不抬杆");

    public interface View {
    }

    public static final Map<String, RelTypeFlag> map = EnumUtils.createValuedEnumMap(RelTypeFlag.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    String value;

    RelTypeFlag(String value, String title) {
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
    public static RelTypeFlag findByValue(@JsonProperty("value") String value) {
        return map.get(value);
    }

}
