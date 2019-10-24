package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 海关响应xml的审核状态：01：接受申报，03：退单
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ChkFlag implements TitleValue<String> {
    AUDIT_PASS("01", "接受申报"),
    CHARGE_BACK("03", "退单");

    private static final Map<String, ChkFlag> map = EnumUtils.createValuedEnumMap(ChkFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    ChkFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static ChkFlag findByValue(@JsonProperty("value") String value) {
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
