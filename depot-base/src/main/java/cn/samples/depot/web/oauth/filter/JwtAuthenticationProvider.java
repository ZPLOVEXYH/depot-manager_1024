package cn.samples.depot.web.oauth.filter;

import cn.samples.depot.common.config.JwtSettings;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.oauth.exceptions.JwtExpiredTokenException;
import cn.samples.depot.web.oauth.jwt.JwtAuthenticationToken;
import cn.samples.depot.web.oauth.jwt.RawAccessJwtToken;
import cn.samples.depot.web.oauth.jwt.UserContext;
import cn.samples.depot.web.oauth.jwt.UserSession;
import cn.samples.depot.web.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @className: JwtAuthenticationProvider
 * @Author: zhangpeng
 * @Date 2019/7/16 13:55
 * @Version 1.0
 **/
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private JwtSettings jwtSettings;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSession userSession;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());

        String subject = jwsClaims.getBody().getSubject();

        JsonResult<?> result = userSession.get(subject);//获取缓存
        if (result.getData() == null)
            throw new JwtExpiredTokenException("system session is expired");
        Params params = (Params) (result.getData());
        if (!rawAccessToken.getToken().equals(params.get("token")))
            throw new JwtExpiredTokenException("system session is expired");

        UserContext context = UserContext.create(userService.getUser(subject), userService.getRole(subject));
        return new JwtAuthenticationToken(context, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
