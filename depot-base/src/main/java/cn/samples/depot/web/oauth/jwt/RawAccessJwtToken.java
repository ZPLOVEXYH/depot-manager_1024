package cn.samples.depot.web.oauth.jwt;

import cn.samples.depot.web.oauth.exceptions.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * Description: JwtToken转换Claims,校验签名是否有效
 *
 * @className: RawAccessJwtToken
 * @Author: ChenJie
 * @Date 2019/8/29
 * @Version 1.0
 **/
public class RawAccessJwtToken {
    private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

    private String token;

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    /**
     * Parses and validates JWT Token signature.
     *
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     */
    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            logger.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException("Token is expired", expiredEx);
        }
    }

    public String getToken() {
        return token;
    }
}
