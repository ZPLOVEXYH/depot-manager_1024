package cn.samples.depot.common.config.netty.client;

import cn.samples.depot.common.config.netty.DataDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @className: NettyClientFilter
 * @Author: zhangpeng
 * @Date 2019/7/16 14:05
 * @Version 1.0
 **/
@Component
public class NettyClientFilter extends ChannelInitializer<SocketChannel> {

    @Autowired
    NettyClientHandler nettyClientHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new DataDecoder());
        channelPipeline.addLast(nettyClientHandler);
    }
}
