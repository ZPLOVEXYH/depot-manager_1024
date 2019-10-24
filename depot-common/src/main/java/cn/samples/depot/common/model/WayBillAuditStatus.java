package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 理货报告运单的审核状态（01待作废/02退单/03理货审核通过/04作废中/05作废通过）
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WayBillAuditStatus implements TitleValue<String> {
    WayBillAudit_01("01", "待申报"),
    WayBillAudit_02("02", "申报海关"),
    WayBillAudit_03("03", "审核通过"),
    WayBillAudit_04("04", "审核不通过"),
    WayBillAudit_05("05", "待作废"),
    WayBillAudit_06("06", "退单"),
    WayBillAudit_07("07", "作废中"),
    WayBillAudit_08("08", "作废通过");

    public static final Map<String, WayBillAuditStatus> map = EnumUtils.createValuedEnumMap(WayBillAuditStatus.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    WayBillAuditStatus(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static WayBillAuditStatus findByValue(@JsonProperty("value") String value) {
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
