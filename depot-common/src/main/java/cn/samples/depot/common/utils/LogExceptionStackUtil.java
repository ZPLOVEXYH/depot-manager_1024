package cn.samples.depot.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Description: 将控制台打印出来的错误堆栈信息输出到logback
 *
 * @className: LogExceptionStackUtil
 * @Author: zhangpeng
 * @Date 2019/7/16 14:42
 * @Version 1.0
 **/
public class LogExceptionStackUtil {

    /**
     * 功能说明:在日志文件中，打印异常堆栈
     *
     * @param e
     * @return
     */
    public static String logExceptionStack(Throwable e) {
        StringWriter errorsWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(errorsWriter));
        return errorsWriter.toString();
    }
}
