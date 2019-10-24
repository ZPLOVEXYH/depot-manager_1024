package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * // 集装箱号(集装箱放行方式)
 * //01未放行
 * //02转关放行
 * //03跨关区一体化放行
 * //04关区内一体化放行
 * //05属地申报口岸验放放行
 * //06报关放行
 * //07待人工放行
 * //08人工放行
 * //09新舱单非贸放行
 * //10人工部分放行
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RelModeFlag implements TitleValue<String> {
    REL_MODE_01("01", "未放行"),
    REL_MODE_02("02", "转关放行"),
    REL_MODE_03("03", "跨关区一体化放行"),
    REL_MODE_04("04", "关区内一体化放行"),
    REL_MODE_05("05", "属地申报口岸验放放行"),
    REL_MODE_06("06", "报关放行"),
    REL_MODE_07("07", "待人工放行"),
    REL_MODE_08("08", "人工放行"),
    REL_MODE_09("09", "新舱单非贸放行"),
    REL_MODE_10("10", "人工部分放行");

    private static final Map<String, RelModeFlag> map = EnumUtils.createValuedEnumMap(RelModeFlag.class);
    @JsonView(View.class)
    String title;
    @JsonView(View.class)
    String value;

    RelModeFlag(String value, String title) {
        this.title = title;
        this.value = value;
    }

    @JsonCreator
    public static RelModeFlag findByValue(@JsonProperty("value") String value) {
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
