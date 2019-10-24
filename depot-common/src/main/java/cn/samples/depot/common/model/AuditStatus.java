package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 审核状态（00：待提交、10：待审核、11：审核驳回、12：审核通过、99：作废）
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuditStatus implements TitleValue<String> {
    PRE_SUBMIT("00", "待提交"),
    SEND_FAIL("01", "发送失败"),
    SEND_SUCCESS("02", "发送成功"),
    RECEIVE_FAIL("03", "接收失败"),
    PRE_AUDIT("10", "待审核"),
    AUDIT_REJECT("11", "审核驳回"),
    AUDIT_PASS("12", "审核通过"),
    INVALID("99", "作废");

    public interface View {
    }

    private static final Map<String, AuditStatus> map = EnumUtils.createValuedEnumMap(AuditStatus.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    String value;

    AuditStatus(String value, String title) {
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
    public static AuditStatus findByValue(@JsonProperty("value") String value) {
        return map.get(value);
    }

}
