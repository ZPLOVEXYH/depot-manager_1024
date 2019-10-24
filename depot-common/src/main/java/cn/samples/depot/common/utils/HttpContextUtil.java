package cn.samples.depot.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @className: HttpContextUtil
 * @Author: zhangpeng
 * @Date 2019/7/16 14:43
 * @Version 1.0
 **/
public class HttpContextUtil {
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
