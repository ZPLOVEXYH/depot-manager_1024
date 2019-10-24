package cn.samples.depot.web.oauth.jwt;

import cn.samples.depot.common.config.JwtSettings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Description: JwtToken工厂类
 *
 * @className: JwtTokenFactory
 * @Author: ChenJie
 * @Date 2019/8/29
 * @Version 1.0
 **/
@Component
public class JwtTokenFactory {
    @Autowired
    private JwtSettings settings;

    /**
     * 基于当前上下文生成 Token
     *
     * @param userContext
     * @return
     */
    public String createAccessJwtToken(UserContext userContext) {
        if (userContext.getUser() == null)
            throw new IllegalArgumentException("Cannot create JWT Token without userInfo");

        if (userContext.getRole() == null)
            throw new IllegalArgumentException("User doesn't have any roles");

        Claims claims = Jwts.claims().setSubject(userContext.getUser().getUserName());

        LocalDateTime currentTime = LocalDateTime.now();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return token;
    }

//    public String createRefreshToken(UserContext userContext) {
//        if (userContext.getUser() == null)
//            throw new IllegalArgumentException("Cannot create JWT Token without userInfo");
//
//        if (userContext.getRole() == null)
//            throw new IllegalArgumentException("User doesn't have any roles");
//        LocalDateTime currentTime = LocalDateTime.now();
//        Claims claims = Jwts.claims().setSubject(userContext.getUser().getUserName());
//
//        String token = Jwts.builder()
//                .setClaims(claims)
//                .setIssuer(settings.getTokenIssuer())
//                .setId(UUID.randomUUID().toString())
//                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
//                .setExpiration(Date.from(currentTime
//                        .plusMinutes(settings.getRefreshTokenExpTime())
//                        .atZone(ZoneId.systemDefault()).toInstant()))
//                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
//                .compact();
//        return token;
//    }
}
