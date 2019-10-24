package cn.samples.depot.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.SerializationException;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Description: 不使用sdr自带的json序列化工具，一切操作基于string
 *
 * @className: JsonRedisSeriaziler
 * @Author: zhangpeng
 * @Date 2019/7/16 14:42
 * @Version 1.0
 **/
@Component
public class JsonRedisSeriaziler {

    public static final String EMPTY_JSON = "{}";

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    protected ObjectMapper objectMapper = new ObjectMapper();

    public JsonRedisSeriaziler() {
    }

    /**
     * java-object as json-string
     *
     * @param object
     * @return
     */
    public String seriazileAsString(Object object) {
        if (object == null) {
            return EMPTY_JSON;
        }

        String result = null;
        try {
            result = this.objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }

        return result;
    }

    /**
     * json-string to java-object
     *
     * @param str
     * @return
     */
    public <T> T deserializeAsObject(String str, Class<T> clazz) {
        if (str == null || clazz == null) {
            return null;
        }

        T result = null;
        try {
            result = this.objectMapper.readValue(str, clazz);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * json to list
     *
     * @param str
     * @param clazz
     * @return
     */
    public <T> List<T> deserializeAsList(String str, Class<T> clazz) {
        if (str == null || clazz == null) {
            return null;
        }

        TypeFactory tf = TypeFactory.defaultInstance();
        // 指定容器结构和类型（这里是ArrayList和clazz）
        List<T> list = null;
        try {
            list = objectMapper.readValue(str, tf.constructCollectionType(List.class, clazz));
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
        return list;
    }
}