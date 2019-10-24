package cn.samples.depot.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * Description:
 *
 * @className: AppProfile
 * @Author: zhangpeng
 * @Date 2019/7/16 14:23
 * @Version 1.0
 **/
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@SuppressWarnings("deprecation")
public class AppProfile {
    public static final String DEV = "dev";
    public static final String TEST = "test";
    public static final String PRE = "pre";
    public static final String PROD = "prod";

    @Autowired
    private Environment environment;

    public boolean isDev() {
        return environment.acceptsProfiles(DEV);
    }

    public boolean isTest() {
        return environment.acceptsProfiles(TEST);
    }

    public boolean isPre() {
        return environment.acceptsProfiles(PRE);
    }

    public boolean isProd() {
        return environment.acceptsProfiles(PROD);
    }

}
