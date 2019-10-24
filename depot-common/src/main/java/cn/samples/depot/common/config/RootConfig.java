package cn.samples.depot.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Description: 系统超级管理员 配置类 (用于特殊情况处理检测系统问题)
 *
 * @className: RootConfig
 * @Author: ChenJie
 * @Date 2019/8/30
 * @Version 1.0
 **/
@Configuration
@Data
@ConfigurationProperties("sample.root")
public class RootConfig {
    /**
     * 是否禁用超管
     */
    private boolean disabled;
    /**
     * 超管密码(配置文件会覆盖系统初始管理员密码)
     */
    private String password;
}
