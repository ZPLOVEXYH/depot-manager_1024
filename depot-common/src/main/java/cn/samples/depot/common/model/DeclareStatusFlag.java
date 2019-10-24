package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 申报状态：01待申报、02申报海关、03审核通过、04审核不通过
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeclareStatusFlag implements TitleValue<String> {
    PRE_DECLARE("01", "待申报"),
    PRO_DECLARE("02", "申报海关"),
    DECLARE_PASS("03", "审核通过"),
    DECLARE_NO_PASS("04", "审核不通过"),
    Invalid("07", "作废中");

    private static final Map<String, DeclareStatusFlag> map = EnumUtils.createValuedEnumMap(DeclareStatusFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    DeclareStatusFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static DeclareStatusFlag findByValue(@JsonProperty("value") String value) {
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

    public static List<DeclareStatusFlag> select() {
        return Arrays.asList(PRE_DECLARE, PRO_DECLARE, DECLARE_PASS, DECLARE_NO_PASS);
    }

    public interface View {
    }

}
