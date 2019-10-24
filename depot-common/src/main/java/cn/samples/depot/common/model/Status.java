package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 状态
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status implements TitleValue<Integer> {
    DISABLED(0, "未启用"), ENABLED(1, "启用");

    public interface View {
    }

    private static final Map<Integer, Status> map = EnumUtils.createValuedEnumMap(Status.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    int value;

    Status(int value, String title) {
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
    public Integer getValue() {
        return value;
    }

    @JsonCreator
    public static Status findByValue(@JsonProperty("value") int value) {
        return map.get(value);
    }

}
