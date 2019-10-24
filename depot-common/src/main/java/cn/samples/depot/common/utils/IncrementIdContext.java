package cn.samples.depot.common.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Description:
 *
 * @className: IncrementIdContext
 * @Author: zhangpeng
 * @Date 2019/7/16 14:42
 * @Version 1.0
 **/
public class IncrementIdContext {
    private static final ThreadLocal<AtomicLong> CONTEXT = new ThreadLocal<AtomicLong>();

    public static String get() {
        if (CONTEXT.get() == null) set(new AtomicLong(0));
        return String.valueOf(CONTEXT.get().incrementAndGet());
    }

    public static void set(AtomicLong id) {
        CONTEXT.set(id);
    }

    public static void remove() {
        CONTEXT.remove();
    }

}