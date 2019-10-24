package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 单证类型
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FormTypeFlag implements TitleValue<String> {
    FROM_TYPE_01("01", "报关单"),
    FROM_TYPE_02("02", "转关单"),
    FROM_TYPE_27("27", "铁路进口理货报告新增"),
    FROM_TYPE_28("28", "铁路进口理货报告删除"),
    FROM_TYPE_29("29", "铁路出口运抵报告新增"),
    FROM_TYPE_30("30", "铁路出口运抵报告删除"),
    FROM_TYPE_32("32", "铁路出口理货报告删除"),
    FROM_TYPE_36("36", "铁路离场申请"),
    FROM_TYPE_37("37", "铁路离场删除");

    private static final Map<String, FormTypeFlag> map = EnumUtils.createValuedEnumMap(FormTypeFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    FormTypeFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static FormTypeFlag findByValue(@JsonProperty("value") String value) {
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
