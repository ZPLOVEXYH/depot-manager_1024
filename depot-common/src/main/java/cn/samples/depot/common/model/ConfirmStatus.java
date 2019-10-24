package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 确认类型（00：卡口确认、10：人工确认、99：空值）
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ConfirmStatus implements TitleValue<String> {
    CARD_CONFIRM("00", "卡口确认"),
    PEOPLE_CONFIRM("10", "人工确认"),
    NULL_VALUE("99", "");

    public interface View {
    }

    public static final Map<String, ConfirmStatus> map = EnumUtils.createValuedEnumMap(ConfirmStatus.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    String value;

    ConfirmStatus(String value, String title) {
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
    public static ConfirmStatus findByValue(@JsonProperty("value") String value) {
        return map.get(value);
    }

}
