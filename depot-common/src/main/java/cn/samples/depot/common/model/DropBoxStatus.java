package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 状态(00 待计划 01 待落箱 02 已落箱)
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DropBoxStatus implements TitleValue<String> {
    PRE_PLAN("00", "待计划"),
    PRE_DROP("01", "待落箱"),
    DROPPED("02", "已落箱");

    public interface View {
    }

    private static final Map<String, DropBoxStatus> map = EnumUtils.createValuedEnumMap(DropBoxStatus.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    String value;

    DropBoxStatus(String value, String title) {
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
    public static DropBoxStatus findByValue(@JsonProperty("value") String value) {
        return map.get(value);
    }

}
