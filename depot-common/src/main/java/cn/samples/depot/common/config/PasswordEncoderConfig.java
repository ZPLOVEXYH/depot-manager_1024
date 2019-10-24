package cn.samples.depot.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Description: 用户登录密码加密方法
 *
 * @className: PasswordEncoderConfig
 * @Author: zhangpeng
 * @Date 2019/7/16 13:58
 * @Version 1.0
 **/
@Configuration
public class PasswordEncoderConfig {

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
