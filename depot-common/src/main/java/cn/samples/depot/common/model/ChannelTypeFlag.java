package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通道进出类型（00：进场站通道、10：出场站通道）
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ChannelTypeFlag implements TitleValue<String> {
    IN_CHANNEL("00", "进场站通道"),
    OUT_CHANNEL("10", "出场站通道");

    public interface View {
    }

    public static final Map<String, ChannelTypeFlag> map = EnumUtils.createValuedEnumMap(ChannelTypeFlag.class);

    @JsonView(View.class)
    String title;

    @JsonView(View.class)
    String value;

    ChannelTypeFlag(String value, String title) {
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
    public static ChannelTypeFlag findByValue(@JsonProperty("value") String value) {
        return map.get(value);
    }

    public static ChannelTypeFlag findEnumByKey(String value) {
        Map<String, ChannelTypeFlag> select = Arrays.stream(ChannelTypeFlag.values()).collect(Collectors.toMap(ChannelTypeFlag::getValue, Function.identity()));
        return select.get(value);
    }

    public static void main(String[] args) {
        ChannelTypeFlag channelTypeFlag = findEnumByKey("00");
        System.out.println(channelTypeFlag.getQuery());
        System.out.println(channelTypeFlag.getTitle());
        System.out.println(channelTypeFlag.getValue());
    }

}
