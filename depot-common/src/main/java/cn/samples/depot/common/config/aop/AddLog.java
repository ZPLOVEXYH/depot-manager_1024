package cn.samples.depot.common.config.aop;

import java.lang.annotation.*;

/**
 * Description: 系统日志注解
 *
 * @className: AddLog
 * @Author: zhangpeng
 * @Date 2019/7/16 14:00
 * @Version 1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AddLog {
    String value() default "";

    /**
     * 操作描述
     *
     * @return
     */
    String optInfo() default "";
}
