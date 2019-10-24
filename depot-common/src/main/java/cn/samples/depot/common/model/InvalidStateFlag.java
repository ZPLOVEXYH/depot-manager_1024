package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 作废状态类型
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum InvalidStateFlag implements TitleValue<String> {
    InvalidState_01("01", "待作废"),
    InvalidState_02("02", "作废中"),
    InvalidState_03("03", "作废通过"),
    InvalidState_04("04", "作废退单");

    public static final Map<String, InvalidStateFlag> map = EnumUtils.createValuedEnumMap(InvalidStateFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    InvalidStateFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static InvalidStateFlag findByValue(@JsonProperty("value") String value) {
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
