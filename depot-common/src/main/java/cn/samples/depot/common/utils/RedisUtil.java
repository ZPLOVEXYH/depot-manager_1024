package cn.samples.depot.common.utils;

import cn.samples.depot.common.model.ValueScore;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Description: 入缓存代理实现类
 *
 * @className: RedisUtil
 * @Author: zhangpeng
 * @Date 2019/7/16 14:42
 * @Version 1.0
 **/
@Component
public class RedisUtil {
    private final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JsonRedisSeriaziler jsonRedisSeriaziler;

    private void handleRedisException(Exception e) {
        if (e instanceof RedisConnectionFailureException) {
            logger.warn("redis连接失败，请检查redis配置");
        } else {
            logger.warn("redis操作失败，{}", e.getMessage());
        }
    }


    public void saveToCache(String cacheKey, String value) {
        ValueOperations<String, String> valueOper = stringRedisTemplate.opsForValue();
        try {
            valueOper.set(cacheKey, value);
        } catch (Exception e) {
            handleRedisException(e);
        }
    }


    public long saveToSetCache(String cacheKey, String value) {
        SetOperations<String, String> setOper = stringRedisTemplate.opsForSet();
        try {
            return setOper.add(cacheKey, value);
        } catch (Exception e) {
            handleRedisException(e);
            return 0;
        }
    }

    public long saveToSetCache(String cacheKey, List<String> values) {
        SetOperations<String, String> setOper = stringRedisTemplate.opsForSet();
        try {
            return setOper.add(cacheKey, values.toArray(new String[values.size()]));
        } catch (Exception e) {
            handleRedisException(e);
            return 0;
        }
    }

    public void saveToHashCache(String cacheKey, String hashKey, String value) {
        HashOperations<String, String, String> hashOper = stringRedisTemplate.opsForHash();
        try {
            hashOper.put(cacheKey, hashKey, value);
        } catch (Exception e) {
            handleRedisException(e);
        }
    }

    public void saveToHashCache(String cacheKey, Map<String, String> values) {
        HashOperations<String, String, String> hashOper = stringRedisTemplate.opsForHash();
        try {
            hashOper.putAll(cacheKey, values);
        } catch (Exception e) {
            handleRedisException(e);
        }
    }

    public String getFromCache(String cacheKey) {
        ValueOperations<String, String> valueOper = stringRedisTemplate.opsForValue();
        try {
            return valueOper.get(cacheKey);
        } catch (Exception e) {
            handleRedisException(e);
            return null;
        }
    }

    public boolean hasSetCacheValue(String cacheKey, String value) {
        SetOperations<String, String> setOper = stringRedisTemplate.opsForSet();
        try {
            return setOper.isMember(cacheKey, value);
        } catch (Exception e) {
            handleRedisException(e);
            return false;
        }
    }

    public Set<String> getAllFromSetCache(String cacheKey) {
        SetOperations<String, String> setOper = stringRedisTemplate.opsForSet();
        try {
            return setOper.members(cacheKey);
        } catch (Exception e) {
            handleRedisException(e);
            return null;
        }
    }

    public String getFromHashCache(String cacheKey, String hashKey) {
        HashOperations<String, String, String> hashOper = stringRedisTemplate.opsForHash();
        try {
            return hashOper.get(cacheKey, hashKey);
        } catch (Exception e) {
            handleRedisException(e);
            return null;
        }
    }

    public Map<String, String> getAllFromHashCache(String cacheKey) {
        HashOperations<String, String, String> hashOper = stringRedisTemplate.opsForHash();
        try {
            return hashOper.entries(cacheKey);
        } catch (Exception e) {
            handleRedisException(e);
            return null;
        }
    }

    public void saveToCacheAsJson(String cacheKey, Object value) {
        saveToCache(cacheKey, jsonRedisSeriaziler.seriazileAsString(value));
    }

    public long saveToSetCacheAsJson(String cacheKey, Object value) {
        return saveToSetCache(cacheKey, jsonRedisSeriaziler.seriazileAsString(value));
    }

    public <T> void saveToHashCacheAsJson(String cacheKey, String hashKey, T value) {
        saveToHashCache(cacheKey, hashKey, jsonRedisSeriaziler.seriazileAsString(value));
    }

    public <T> T getFromCacheByJson(String cacheKey, Class<T> clazz) {
        return jsonRedisSeriaziler.deserializeAsObject(getFromCache(cacheKey), clazz);
    }

    public boolean hasSetCacheValueByJson(String cacheKey, Object value) {
        return hasSetCacheValue(cacheKey, jsonRedisSeriaziler.seriazileAsString(value));
    }

    public <T> T getFromHashCacheByJson(String cacheKey, String hashKey, Class<T> clazz) {
        return jsonRedisSeriaziler.deserializeAsObject(getFromHashCache(cacheKey, hashKey), clazz);
    }

    public <T> List<T> getAllFromHashCacheByJson(String cacheKey, Class<T> clazz) {
        HashOperations<String, String, String> hashOper = stringRedisTemplate.opsForHash();
        List<String> values = null;
        try {
            values = hashOper.values(cacheKey);
        } catch (Exception e) {
            handleRedisException(e);
            return null;
        }

        List<T> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(values)) {
            return list;
        }

        for (String value : values) {
            list.add(jsonRedisSeriaziler.deserializeAsObject(value, clazz));
        }
        return list;
    }

    public <T> void saveToCacheById(Class<T> clazz, String id, T value) {
        saveToCacheById(clazz, id, value, null);
    }

    public <T> void saveToCacheById(Class<T> clazz, String id, T value, Date expireDate) {
        saveToSetCache(getTypeIdsKey(clazz), id);

        String cacheKey = getTypeCacheKey(clazz, id);
        saveToCache(cacheKey, jsonRedisSeriaziler.seriazileAsString(value));
        if (null != expireDate) {
            setExpireAt(cacheKey, expireDate);
        }
    }

    public <T> T getFromCacheById(Class<T> clazz, String id) {
        return getFromCacheByJson(getTypeCacheKey(clazz, id), clazz);
    }

    public <T> List<T> getAllFromCache(Class<T> clazz) {
        return getAllFromHashCacheByJson(getTypeCacheKey(clazz), clazz);
    }

    private <T> String getTypeIdsKey(Class<T> clazz) {
        return "ids:" + clazz.getSimpleName().toUpperCase();
    }

    private <T> String getTypeCacheKey(Class<T> clazz) {
        return "urn:" + clazz.getSimpleName().toLowerCase();
    }

    private <T> String getTypeCacheKey(Class<T> clazz, String id) {
        return getTypeCacheKey(clazz) + ":" + id;
    }

    public void removeFromCache(String cacheKey) {
        try {
            stringRedisTemplate.delete(cacheKey);
        } catch (Exception e) {
            handleRedisException(e);
        }
    }

    public void removeFromCache(Collection<String> cacheKeys) {
        try {
            stringRedisTemplate.delete(cacheKeys);
        } catch (Exception e) {
            handleRedisException(e);
        }
    }

    public long removeFromHashCache(String cacheKey, String hashKey) {
        HashOperations<String, String, String> hashOper = stringRedisTemplate.opsForHash();
        try {
            return hashOper.delete(cacheKey, hashKey);
        } catch (Exception e) {
            handleRedisException(e);
            return 0;
        }
    }

    public long removeFromSetCache(String cacheKey, String value) {
        SetOperations<String, String> setOpt = stringRedisTemplate.opsForSet();
        try {
            return setOpt.remove(cacheKey, value);
        } catch (Exception e) {
            handleRedisException(e);
            return 0;
        }
    }

    public boolean setExpire(String key, long timeout, TimeUnit unit) {
        try {
            return stringRedisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            handleRedisException(e);
            return false;
        }
    }

    public boolean setExpireAt(String key, Date expireDate) {
        try {
            return stringRedisTemplate.expireAt(key, expireDate);
        } catch (Exception e) {
            handleRedisException(e);
            return false;
        }
    }

    public Double zscore(String key, String obj) {
        logger.info("zscore {} {}", key, obj);
        ZSetOperations<String, String> zsetOper = stringRedisTemplate.opsForZSet();
        try {
            return zsetOper.score(key, obj);
        } catch (Exception e) {
            handleRedisException(e);
            return null;
        }
    }

    public List<String> zrange(String key, long start, long end) {
        logger.info("zrange {} {} {}", key, start, end);
        ZSetOperations<String, String> zsetOper = stringRedisTemplate.opsForZSet();
        try {
            return Lists.newArrayList(zsetOper.range(key, start, end));
        } catch (Exception e) {
            handleRedisException(e);
            return null;
        }
    }

    public List<String> zrevRange(String key, long start, long end) {
        logger.info("zrevrange {} {} {}", key, start, end);
        ZSetOperations<String, String> zsetOper = stringRedisTemplate.opsForZSet();
        try {
            return Lists.newArrayList(zsetOper.reverseRange(key, start, end));
        } catch (Exception e) {
            handleRedisException(e);
            return null;
        }
    }

    public List<ValueScore> zrangeWithScores(String key, long start, long end) {
        logger.info("zrange {} {} {} withscores", key, start, end);
        ZSetOperations<String, String> zsetOper = stringRedisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> set = null;
        try {
            set = zsetOper.rangeWithScores(key, start, end);
        } catch (Exception e) {
            handleRedisException(e);
        }
        return getValueScores(set);
    }

    public List<ValueScore> zrangeByScoreWithScores(String key, double min, double max) {
        logger.info("zrangebyscore {} {} {} withscores", key, min, max);
        ZSetOperations<String, String> zsetOper = stringRedisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> set = null;
        try {
            set = zsetOper.reverseRangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            handleRedisException(e);
        }
        return getValueScores(set);
    }

    public List<ValueScore> zrevRangeWithScores(String key, long start, long end) {
        logger.info("zrevrange {} {} {} withscores", key, start, end);
        ZSetOperations<String, String> zsetOper = stringRedisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> set = null;
        try {
            set = zsetOper.reverseRangeWithScores(key, start, end);
        } catch (Exception e) {
            handleRedisException(e);
        }
        return getValueScores(set);
    }

    public List<ValueScore> zrevRangeByScoreWithScores(String key, double min, double max) {
        logger.info("zrevrangebyscore {} {} {} withscores", key, min, max);
        ZSetOperations<String, String> zsetOper = stringRedisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> set = null;
        try {
            set = zsetOper.reverseRangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            handleRedisException(e);
        }
        return getValueScores(set);
    }

    private List<ValueScore> getValueScores(Set<ZSetOperations.TypedTuple<String>> set) {
        if (CollectionUtils.isEmpty(set)) {
            return null;
        }

        List<ValueScore> valueScores = Lists.newArrayList();
        for (ZSetOperations.TypedTuple<String> s : set) {
            valueScores.add(new ValueScore(s.getValue(), s.getScore()));
        }
        return valueScores;
    }

    public Set<String> keys(String pattern) {
        logger.info("keys {}", pattern);
        try {
            return stringRedisTemplate.keys(pattern);
        } catch (Exception e) {
            handleRedisException(e);
            return null;
        }
    }

    public String getAndSet(String key, String value) {
        ValueOperations<String, String> valueOper = stringRedisTemplate.opsForValue();
        try {
            return valueOper.getAndSet(key, value);
        } catch (Exception e) {
            handleRedisException(e);
        }
        return null;
    }

    public Long increment(String key, long increment) {
        ValueOperations<String, String> valueOper = stringRedisTemplate.opsForValue();
        try {
            return valueOper.increment(key, increment);
        } catch (Exception e) {
            handleRedisException(e);
        }
        return null;
    }

    public Long increInHash(String hashKey, String key, long increment) {
        HashOperations<String, String, String> hashOper = stringRedisTemplate.opsForHash();
        try {
            return hashOper.increment(hashKey, key, increment);
        } catch (Exception e) {
            handleRedisException(e);
        }

        return null;
    }
}