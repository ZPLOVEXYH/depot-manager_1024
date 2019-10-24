package cn.samples.depot.common.config.netty;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Description:
 *
 * @className: ChannelRepository
 * @Author: zhangpeng
 * @Date 2019/7/16 14:22
 * @Version 1.0
 **/
@Component
public class ChannelRepository {
    private ConcurrentMap<String, Channel> channelCache = new ConcurrentHashMap<>();

    public ChannelRepository put(String key, Channel value) {
        channelCache.put(key, value);
        return this;
    }

    public Channel get(String key) {
        return channelCache.get(key);
    }

    public void remove(String key) {
        this.channelCache.remove(key);
    }

    public int size() {
        return this.channelCache.size();
    }
}
