package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 报文类型代码
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MessageTypeFlag implements TitleValue<String> {
    WLJK_IRTA("WLJK_IRTA", "铁路进口理货报告申请"),
    WLJK_IRTD("WLJK_IRTD", "铁路进口理货报告删除"),
    WLJK_IRTR("WLJK_IRTR", "铁路进口理货报告回执"),

    WLJK_ERTA("WLJK_ERTA", "铁路出口理货报告申请"),
    WLJK_ERTD("WLJK_ERTD", "铁路出口理货报告删除"),
    WLJK_ERTR("WLJK_ERTR", "铁路出口理货报告回执"),

    WLJK_TREL("WLJK_TREL", "铁路放行指令"),
    WLJK_TLN("WLJK_TLN", "铁路装车通知"),

    WLJK_ERRA("WLJK_ERRA", "铁路运抵申报"),
    WLJK_ERRD("WLJK_ERRD", "铁路运抵作废"),
    WLJK_ERRR("WLJK_ERRR", "铁路运抵回执"),

    WLJK_TLA("WLJK_TLA", "铁路装车记录申请"),
    WLJK_TLD("WLJK_TLD", "铁路装车记录作废"),
    WLJK_TLR("WLJK_TLR", "铁路装车回执");

    private static final Map<String, MessageTypeFlag> map = EnumUtils.createValuedEnumMap(MessageTypeFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    MessageTypeFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static MessageTypeFlag findByValue(@JsonProperty("value") String value) {
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
