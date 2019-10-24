package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 海关响应xml的审核状态：01：审核通过，03：退单
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CreateType implements TitleValue<String> {
    MANUAL("01", "手动输入"),
    SHIP_PLAN("02", "发运计划生成");

    private static final Map<String, CreateType> map = EnumUtils.createValuedEnumMap(CreateType.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    CreateType(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static CreateType findByValue(@JsonProperty("value") String value) {
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
