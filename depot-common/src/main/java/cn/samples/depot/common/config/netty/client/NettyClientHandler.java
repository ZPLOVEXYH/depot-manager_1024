package cn.samples.depot.common.config.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Description:
 *
 * @className: NettyClientHandler
 * @Author: zhangpeng
 * @Date 2019/7/16 14:05
 * @Version 1.0
 **/
@Slf4j
@Component
public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) {
        InetSocketAddress insocket = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        // 获取得到客户端的ip地址
        String clientIP = insocket.getAddress().getHostAddress();
        log.info("获取得到的scoket客户端的ip地址为：{}", clientIP);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("netty exception：{}", cause.getMessage());
        ctx.close();
    }
}
