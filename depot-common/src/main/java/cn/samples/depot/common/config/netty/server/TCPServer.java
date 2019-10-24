package cn.samples.depot.common.config.netty.server;

import cn.samples.depot.common.config.netty.TCPServerStartFailedException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * Description:
 *
 * @className: TCPServer
 * @Author: zhangpeng
 * @Date 2019/7/16 14:06
 * @Version 1.0
 **/
@Slf4j
@Component
public class TCPServer {

    @Autowired
    ServerBootstrap serverBootstrap;

    @Autowired
    InetSocketAddress tcpSocketAddress;

    private Channel serverChannel;

    public void start() {
        try {
            ChannelFuture serverChannelFuture = serverBootstrap.bind(tcpSocketAddress).sync();
            log.info("Server is started : port {}", tcpSocketAddress.getPort());
            serverChannel = serverChannelFuture.channel().closeFuture().sync().channel();
        } catch (InterruptedException e) {
            throw new TCPServerStartFailedException(e);
        }
    }

    @PreDestroy
    public void stop() {
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel.parent().close();
        }
    }
}
