package cn.samples.depot.common.utils;

/**
 * Description: 实现日志追踪上下文
 *
 * @className: LogTraceContext
 * @Author: zhangpeng
 * @Date 2019/7/16 14:42
 * @Version 1.0
 **/
public class LogTraceContext {

    /**
     * 初始化
     */
    public static void init() {
        ThreadUUIDContext.remove();
        IncrementIdContext.remove();
    }
}
