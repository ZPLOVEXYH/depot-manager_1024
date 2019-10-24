package cn.samples.depot.web.bean.departureconfirm;

import cn.samples.depot.common.model.TitleValue;
import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * 发货确认状态（ 01已装车）
 * <p>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DepartureStatus implements TitleValue<String> {
    LOADED("01", "已装车");

    public interface View {
    }

    private static final Map<String, DepartureStatus> map = EnumUtils.createValuedEnumMap(DepartureStatus.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    String value;

    DepartureStatus(String value, String title) {
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
    public static DepartureStatus findByValue(@JsonProperty("value") String value) {
        return map.get(value);
    }

}
