package cn.samples.depot.web.cz.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.samples.depot.common.utils.ThreadUUIDContext;

/**
 * Description:
 *
 * @className: ThreadUUIDConvert
 * @Author: zhangpeng
 * @Date 2019/7/16 14:24
 * @Version 1.0
 **/
public class ThreadUUIDConvert extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        return ThreadUUIDContext.get();
    }
}