package cn.samples.depot.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @className: JwtSettings
 * @Author: zhangpeng
 * @Date 2019/7/16 14:05
 * @Version 1.0
 **/
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtSettings {
    /**
     * token的过期时间
     */
    private Integer tokenExpirationTime;

    /**
     * Token issuer.
     */
    private String tokenIssuer;

    /**
     * Key is used to sign {@link JwtToken}.
     */
    private String tokenSigningKey;

    /**
     * {@link JwtToken} can be refreshed during this timeframe.
     */
    private Integer refreshTokenExpTime;
}
