package cn.samples.depot.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @className: Params
 * @Author: zhangpeng
 * @Date 2019/7/16 15:04
 * @Version 1.0
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public final class Params extends HashMap<String, Object> {

    public static String model(Class<?> clazz) {
        String name = clazz.getSimpleName();
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        if (name.endsWith("y")) {
            return name.substring(0, name.length() - 1) + "ies";
        } else if (name.endsWith("sh") || name.endsWith("ch") || name.endsWith("s") || name.endsWith("x")) {
            return name + "es";
        } else {
            return name;
        }
    }

    private static final long serialVersionUID = -965928700490085751L;

    private Params(Map<String, Object> m) {
        super(m);
    }

    private Params() {
    }


    public static Params param(String key, Object value) {
        if (value instanceof IPage) {
            value = PageView.wrap((IPage) value);
        }
        return new Params().set(key, value);
    }

    public Params set(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Params set(Object value) {
        return this.set(Params.model(value.getClass()), value);
    }

    public Params set(Class<? extends Enum<?>> clazz) {
        return this.set(Params.model(clazz), clazz);
    }

    public Params set(String key, Class<? extends Enum<?>> clazz) {
        return this.set(key, Arrays.asList(clazz.getEnumConstants()));
    }

    public <E extends Enum<E>> Params set(Class<E> clazz, Predicate<E> predicate) {
        return this.set(Params.model(clazz), clazz, predicate);
    }

    public <E extends Enum<E>> Params set(String key, Class<E> clazz, Predicate<E> predicate) {
        return this.set(key, Arrays.stream(clazz.getEnumConstants()).filter(predicate).collect(Collectors.toList()));
    }

    public Params setAll(Map<String, Object> others) {
        if (!CollectionUtils.isEmpty(others)) {
            super.putAll(others);
        }
        return this;
    }

    public Params set(String key, IPage<?> page) {
        return set(key, (Object) PageView.wrap(page));
    }

    public Params putIf(String key, Object value) {
        if (value != null) {
            super.put(key, value);
        }
        return this;
    }

    public Params putIf(Object value) {
        return this.putIf(Params.model(value.getClass()), value);
    }

    public Params putIf(Class<? extends Enum<?>> clazz) {
        return this.putIf(Params.model(clazz), clazz);
    }

    public Params putIf(String key, Class<? extends Enum<?>> clazz) {
        return this.putIf(key, Arrays.asList(clazz.getEnumConstants()));
    }

    public <E extends Enum<E>> Params putIf(Class<E> clazz, Predicate<E> predicate) {
        return this.putIf(Params.model(clazz), clazz, predicate);
    }

    public <E extends Enum<E>> Params putIf(String key, Class<E> clazz, Predicate<E> predicate) {
        return this.putIf(key, Arrays.stream(clazz.getEnumConstants()).filter(predicate).collect(Collectors.toList()));
    }

    public Params putIf(String key, IPage<?> page) {
        return putIf(key, (Object) PageView.wrap(page));
    }

    public Params putIfNotEmpty(String key, Object value) {
        if (StringUtils.isNotEmpty(value == null ? null : value.toString())) {
            super.put(key, value);
        }
        return this;
    }

    public Params putIfNotBlank(String key, Object value) {
        if (StringUtils.isNotBlank(value == null ? null : value.toString())) {
            super.put(key, value);
        }
        return this;
    }

    public Params removes(String... keys) {
        if (keys != null) {
            for (String key : keys) {
                remove(key);
            }
        }
        return this;
    }

    public static Params param(Map<String, Object> map) {
        return new Params(map);
    }

    public Params putIfNotEmpty(String key, IPage<?> page) {
        return putIfNotEmpty(key, (Object) PageView.wrap(page));
    }

    public static Params param(Object value) {
        return Params.param(Params.model(value.getClass()), value);
    }

    public static <E extends Enum<E>> Params param(Class<E> clazz) {
        return Params.param(Params.model(clazz), clazz);
    }

    public static <E extends Enum<E>> Params param(String key, Class<E> clazz) {
        return Params.param(key, Arrays.asList(clazz.getEnumConstants()));
    }

    public static <E extends Enum<E>> Params param(Class<E> clazz, Predicate<E> predicate) {
        return Params.param(Params.model(clazz), clazz, predicate);
    }

    public static <E extends Enum<E>> Params param(String key, Class<E> clazz, Predicate<E> predicate) {
        return Params.param(key, Arrays.stream(clazz.getEnumConstants()).filter(predicate).collect(Collectors.toList()));
    }

    public static Params param(Object... pairs) {
        Params param = new Params();
        if (pairs != null) {
            int len = pairs.length;
            if (len % 2 != 0) {
                throw new IllegalArgumentException("Pair size not even");
            }
            for (int i = 0; i < len; i += 2) {
                Object key = pairs[i];
                if (key instanceof String) {
                    param.set((String) key, pairs[i + 1]);
                } else {
                    throw new IllegalArgumentException("Key not be string");
                }
            }
        }
        return param;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int def) {
        return containsKey(key) ? ((Number) get(key)).intValue() : def;
    }

    public long getLong(String key, long def) {
        return containsKey(key) ? ((Number) get(key)).longValue() : def;
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }
}
