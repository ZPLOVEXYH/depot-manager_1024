package cn.samples.depot.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 缓存配置
 *
 * @author ChenJie
 */
@Configuration
@EnableCaching(order = 1)
public class CachingConfig {

    @Autowired
    CaffeineCacheProperties caffeineCacheProperties;

    /**
     * 创建基于Caffeine的Cache Manager
     *
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "spring.cache.caffeine", name = "enabled", havingValue = "true")
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Map<String, CaffeineCache> cacheMap = new HashMap<>();

        // 设置全局配置的本地缓存
        List<String> globalCacheNames = caffeineCacheProperties.getCacheName();
        if (globalCacheNames != null && !globalCacheNames.isEmpty()) {
            addCacheObject(cacheMap, globalCacheNames, caffeineCacheProperties.getExpireAfterWrite(),
                    caffeineCacheProperties.getExpireAfterAccess(), caffeineCacheProperties.getRefreshAfterWrite(), caffeineCacheProperties.getMaximumSize());
        }

        // 设置自定义属性缓存, 可以覆盖全局缓存
        List<CaffeineCacheProperties.Config> configs = caffeineCacheProperties.getConfigs();
        if (configs != null && !configs.isEmpty()) {
            for (CaffeineCacheProperties.Config config : configs) {
                List<String> cacheNames = config.getCacheName();
                if (cacheNames == null || cacheNames.isEmpty()) {
                    continue;
                }
                Duration expireAfterWrite = Optional.ofNullable(config.getExpireAfterWrite()).orElse(caffeineCacheProperties.getExpireAfterWrite());
                Duration expireAfterAccess = Optional.ofNullable(config.getExpireAfterAccess()).orElse(caffeineCacheProperties.getExpireAfterAccess());
                Duration refreshAfterWrite = Optional.ofNullable(config.getRefreshAfterWrite()).orElse(caffeineCacheProperties.getRefreshAfterWrite());
                Long maximumSize = Optional.ofNullable(config.getMaximumSize()).orElse(caffeineCacheProperties.getMaximumSize());
                addCacheObject(cacheMap, cacheNames, expireAfterWrite, expireAfterAccess, refreshAfterWrite, maximumSize);
            }
        }
        // 加入到缓存管理器进行管理
        cacheManager.setCaches(cacheMap.values());
        return cacheManager;
    }

    private void addCacheObject(Map<String, CaffeineCache> cacheMap, List<String> cacheNames, Duration expireAfterWrite, Duration expireAfterAccess, Duration refreshAfterWrite, Long maximumSize) {
        for (String cacheName : cacheNames) {
            Caffeine<Object, Object> recordStats = Caffeine.newBuilder().recordStats().maximumSize(maximumSize);
            if (expireAfterAccess != null) recordStats.expireAfterAccess(expireAfterAccess);
            if (expireAfterWrite != null) recordStats.expireAfterWrite(expireAfterWrite);
            if (refreshAfterWrite != null) recordStats.refreshAfterWrite(refreshAfterWrite);
            Cache<Object, Object> cache = recordStats.build();
            CaffeineCache caffeineCache = new CaffeineCache(cacheName, cache);

            // 覆盖添加
            cacheMap.put(cacheName, caffeineCache);
        }
    }
}