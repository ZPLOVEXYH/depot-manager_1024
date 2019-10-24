package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 进出口：I进口，E出口
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IEFlag implements TitleValue<String> {
    IMPORT("I", "进口"),
    EXPORT("E", "出口");

    private static final Map<String, IEFlag> map = EnumUtils.createValuedEnumMap(IEFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    IEFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static IEFlag findByValue(@JsonProperty("value") String value) {
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
