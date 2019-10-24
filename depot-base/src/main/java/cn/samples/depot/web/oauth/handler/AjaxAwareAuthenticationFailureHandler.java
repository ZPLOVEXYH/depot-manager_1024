package cn.samples.depot.web.oauth.handler;

import cn.samples.depot.common.utils.ErrorCode;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.oauth.exceptions.JwtExpiredTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: ajax请求认证失败的处理器
 *
 * @className: AjaxAwareAuthenticationFailureHandler
 * @Author: zhangpeng
 * @Date 2019/7/16 13:54
 * @Version 1.0
 **/
@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;

    @Autowired
    public AjaxAwareAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (e instanceof BadCredentialsException) {
            mapper.writeValue(response.getWriter(), JsonResult.error(ErrorCode.USER_FAILED.getErrorCode(), "Invalid username or password"));
        }
        if (e instanceof JwtExpiredTokenException) {
            mapper.writeValue(response.getWriter(), JsonResult.error(ErrorCode.JWT_TOKEN_EXPIRED.getErrorCode(), "Token has expired"));
            return;
        }
        if (e instanceof UsernameNotFoundException) {
            mapper.writeValue(response.getWriter(), JsonResult.error(ErrorCode.USER_NOT_FIND.getErrorCode(), "该用户已被禁用"));
            return;
        }
        if (e instanceof AuthenticationServiceException) {
            mapper.writeValue(response.getWriter(), JsonResult.error(ErrorCode.AUTHENTICATION_FAILED.getErrorCode(), e.getMessage()));
            return;
        }

        mapper.writeValue(response.getWriter(), JsonResult.error(ErrorCode.AUTHENTICATION_FAILED.getErrorCode(), "Authentication failed"));

    }
}
