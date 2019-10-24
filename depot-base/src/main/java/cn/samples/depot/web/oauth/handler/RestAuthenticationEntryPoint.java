package cn.samples.depot.web.oauth.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 未处理异常处理入口
 *
 * @className: RestAuthenticationEntryPoint
 * @Author: zhangpeng
 * @Date 2019/7/16
 * @Version 1.0
 **/
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
    }
}
