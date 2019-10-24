package cn.samples.depot.web.oauth.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Description: 自定义Token失效异常(便于做个性化处理)
 *
 * @className: JwtExpiredTokenException
 * @Author: ChenJie
 * @Date 2019/8/29
 * @Version 1.0
 **/
public class JwtExpiredTokenException extends AuthenticationException {
    private static final long serialVersionUID = -5959543783324224864L;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(String msg, Throwable t) {
        super(msg, t);
    }
}
