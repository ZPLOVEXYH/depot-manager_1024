package cn.samples.depot.common.config.netty;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * Description:
 *
 * @className: NettyProperties
 * @Author: zhangpeng
 * @Date 2019/7/16 14:23
 * @Version 1.0
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "netty")
public class NettyProperties {
    @NotNull
    private String hostName;

    @NotNull
    private int tcpPort;

    @NotNull
    private int bossCount;

    @NotNull
    private int workerCount;

    @NotNull
    private boolean keepAlive;

    @NotNull
    private int backlog;
}
