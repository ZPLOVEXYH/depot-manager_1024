package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * TODO 未确定跟海关对应的code，铅封类型（00：进场站通道、10：出场站通道）
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SealTypeFlag implements TitleValue<String> {
    SEAL_TYPE_00("M", "机械封志"),
    SEAL_TYPE_01("E", "电子封志"),
    SEAL_TYPE_02("T", "不能施封的特种重箱");

    public static final Map<String, SealTypeFlag> map = EnumUtils.createValuedEnumMap(SealTypeFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    SealTypeFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static SealTypeFlag findByValue(@JsonProperty("value") String value) {
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
