package cn.samples.depot.common.utils;

import java.util.UUID;

/**
 * Description: ThreadUUID的上下文
 *
 * @className: ThreadUUIDContext
 * @Author: zhangpeng
 * @Date 2019/7/16 14:41
 * @Version 1.0
 **/
public class ThreadUUIDContext {
    private static final ThreadLocal<String> UUID_CONTEXT = new ThreadLocal<String>();

    public static String get() {
        if (isBlank(UUID_CONTEXT.get())) set(UUID.randomUUID().toString());
        return UUID_CONTEXT.get();
    }

    public static void set(String uuid) {
        UUID_CONTEXT.set(uuid);
    }

    public static void remove() {
        UUID_CONTEXT.remove();
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }
}
