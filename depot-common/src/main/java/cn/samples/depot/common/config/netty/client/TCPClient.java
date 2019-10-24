package cn.samples.depot.common.config.netty.client;

import io.netty.bootstrap.Bootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Description:
 *
 * @className: TCPClient
 * @Author: zhangpeng
 * @Date 2019/7/16 14:05
 * @Version 1.0
 **/
@Component
public class TCPClient {

    @Autowired
    Bootstrap clientBootstrap;

    @Autowired
    InetSocketAddress clientSocketAddress;

    /**
     * netty socket客户端
     *
     * @throws InterruptedException
     */
    public void connect() throws InterruptedException {
        clientBootstrap
                .connect(clientSocketAddress.getAddress(), clientSocketAddress.getPort())
                .sync()
                .channel();
    }
}

