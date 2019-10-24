package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * TODO 施加封志人类型
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EnstaveMentFlag implements TitleValue<String> {
    ENSTAVEMENT_AA("AA", "拼箱人"),
    ENSTAVEMENT_AB("AB", "未知"),
    ENSTAVEMENT_AC("AC", "检疫"),
    ENSTAVEMENT_CA("CA", "承运人"),
    ENSTAVEMENT_CU("CU", "海关"),
    ENSTAVEMENT_SH("SH", "发货人"),
    ENSTAVEMENT_TO("TO", "码头");

    public static final Map<String, EnstaveMentFlag> map = EnumUtils.createValuedEnumMap(EnstaveMentFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    EnstaveMentFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static EnstaveMentFlag findByValue(@JsonProperty("value") String value) {
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
