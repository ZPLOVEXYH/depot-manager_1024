package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 生成方式(01手动输入02发运计划生成)
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MessageModeFlag implements TitleValue<String> {
    MESSAGE_MODE_01("01", "手动输入"),
    MESSAGE_MODE_02("02", "发运计划生成");

    private static final Map<String, MessageModeFlag> map = EnumUtils.createValuedEnumMap(MessageModeFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    MessageModeFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static MessageModeFlag findByValue(@JsonProperty("value") String value) {
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
