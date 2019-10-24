package cn.samples.depot.web.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.samples.depot.common.utils.IncrementIdContext;

/**
 * Description:
 *
 * @className: IncrementIdConvert
 * @Author: zhangpeng
 * @Date 2019/7/16 14:24
 * @Version 1.0
 **/
public class IncrementIdConvert extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        return IncrementIdContext.get();
    }
}