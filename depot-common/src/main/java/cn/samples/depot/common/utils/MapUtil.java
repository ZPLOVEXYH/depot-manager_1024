package cn.samples.depot.common.utils;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yhllo on 2017/8/16.
 */
public class MapUtil {

    /**
     * 根据传入参数构建map
     *
     * @param keyValues 参数个数必须为偶数，奇数位参数为key，偶数位参数为value
     * @return 返回构建的map，注：参数个数为0时返回空的map；为奇数时，返回null
     */
    public static Map<String, Object> buildMap(Object... keyValues) {
        Assert.isTrue(null == keyValues || keyValues.length % 2 == 0, "参数个数必须为偶数个");

        Map<String, Object> params = new HashMap<String, Object>();
        if (null != keyValues) {
            for (int i = 0; i < keyValues.length; i += 2) {
                params.put(String.valueOf(keyValues[i]), keyValues[i + 1]);
            }
        }

        return params;
    }
}
