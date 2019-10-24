package cn.samples.depot.common.config;

import cn.samples.depot.common.config.netty.NettyProperties;
import cn.samples.depot.common.config.netty.client.NettyClientFilter;
import cn.samples.depot.common.config.netty.handler.SimpleChatChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * Description: netty配置文件
 *
 * @className: NettyConfiguration
 * @Author: zhangpeng
 * @Date 2019/7/16 14:06
 * @Version 1.0
 **/
@Configuration
@EnableConfigurationProperties(NettyProperties.class)
public class NettyConfiguration {

    @Autowired
    NettyProperties nettyProperties;

    @Autowired
    NettyClientFilter nettyClientFilter;

    @Autowired
    private SimpleChatChannelInitializer somethingChannelInitializer;

    /**
     * netty socket服务端配置
     *
     * @return
     */
    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(somethingChannelInitializer);
        b.option(ChannelOption.SO_BACKLOG, nettyProperties.getBacklog());
        return b;
    }

    /**
     * netty scoket客户端配置
     *
     * @return
     */
    @Bean(name = "clientBootstrap")
    public Bootstrap clientBootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(nettyClientFilter);

        return bootstrap;
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(nettyProperties.getBossCount());
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(nettyProperties.getWorkerCount());
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress tcpSocketAddress() {
        return new InetSocketAddress(nettyProperties.getTcpPort());
    }

    @Bean(name = "clientSocketAddress")
    public InetSocketAddress clientSocketAddress() {
        return new InetSocketAddress(nettyProperties.getHostName(), nettyProperties.getTcpPort());
    }
}
