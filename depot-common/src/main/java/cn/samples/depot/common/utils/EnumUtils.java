package cn.samples.depot.common.utils;

import cn.samples.depot.common.model.Valued;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Slf4j
@SuppressWarnings("rawtypes")
public class EnumUtils extends org.apache.commons.lang3.EnumUtils {
    private static Map<String, Class> keyMap = Maps.newConcurrentMap();

    private EnumUtils() {
    }

    public static <V, E extends Valued<V>> Map<V, E> createValuedEnumMap(Class<E> enumClass) {
        return createValuedEnumMap(enumClass, input -> input.getValue());
    }

    public static <V, E extends Valued<V>> Map<V, E> createValuedEnumMap(Class<E> enumClass, Function<E, V> func) {
        return Collections.unmodifiableMap(Maps.uniqueIndex(Arrays.asList(enumClass.getEnumConstants()), func));
    }

    public static void registerEnum(Class... enumClasses) {
        for (Class enumClass : enumClasses) {
            String key = StringUtils.lowerCase(ClassUtils.getShortName(enumClass));
            if (keyMap.containsKey(key)) {
                log.warn("'" + key + "' already registered with '" + keyMap.get(key).getName() + "', replaced with : " + enumClass.getName());
            }
            keyMap.put(key, enumClass);
        }
    }

    public static Class getEnum(String key) throws ClassNotFoundException {
        Class clz = keyMap.get(StringUtils.lowerCase(key));
        if (clz == null) throw new ClassNotFoundException();
        return clz;
    }
}
