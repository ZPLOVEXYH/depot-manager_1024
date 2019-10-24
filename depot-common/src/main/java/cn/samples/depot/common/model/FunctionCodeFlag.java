package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 报文编号
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FunctionCodeFlag implements TitleValue<String> {
    FUNCTION_CODE_2("2", "新增"),
    FUNCTION_CODE_3("3", "删除");

    private static final Map<String, FunctionCodeFlag> map = EnumUtils.createValuedEnumMap(FunctionCodeFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    FunctionCodeFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static FunctionCodeFlag findByValue(@JsonProperty("value") String value) {
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
