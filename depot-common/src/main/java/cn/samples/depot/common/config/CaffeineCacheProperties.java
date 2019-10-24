package cn.samples.depot.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * Caffeine本地缓存自定义配置
 *
 * @author kancy
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("spring.cache.caffeine")
@ConditionalOnProperty(prefix = "spring.cache.caffeine", name = "enabled", havingValue = "true")
public class CaffeineCacheProperties {
    private List<String> cacheName;
    private Duration expireAfterWrite;
    private Duration expireAfterAccess;
    private Duration refreshAfterWrite;
    private Long maximumSize = Long.valueOf(-1);

    private List<Config> configs;

    @Getter
    @Setter
    public static class Config {
        private List<String> cacheName;
        /**
         * 指定在创建条目后经过固定的持续时间或最近更换其值时，应自动从缓存中删除每个条目
         */
        Duration expireAfterWrite;
        /**
         * 指定每个条目应该从缓存中一次固定期限已经进入的创作后经过被自动删除，最近更换其价值，或其最后一次阅读
         */
        Duration expireAfterAccess;
        /**
         * 指定在创建条目后经过固定的持续时间或最近更换其值时，活动条目有资格进行自动刷新
         */
        private Duration refreshAfterWrite;
        /**
         * 缓存的最大数量
         */
        Long maximumSize;
    }
}