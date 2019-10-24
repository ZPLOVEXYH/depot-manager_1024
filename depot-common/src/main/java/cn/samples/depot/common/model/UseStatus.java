package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 堆位启用状态(0未占用 1已占用 2预占用)
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UseStatus implements TitleValue<Integer> {
    NOT_USED(0, "可用"), PRE_USED(1, "锁定"), USED(2, "占用");

    public interface View {
    }

    private static final Map<Integer, UseStatus> map = EnumUtils.createValuedEnumMap(UseStatus.class);

    @JsonView(UseStatus.View.class)
    String title;

    @JsonView(UseStatus.View.class)
    int value;

    UseStatus(int value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonView(UseStatus.View.class)
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
    public static UseStatus findByValue(@JsonProperty("value") int value) {
        return map.get(value);
    }
}
