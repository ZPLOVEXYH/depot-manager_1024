package cn.samples.depot.web;

import cn.samples.depot.web.cz.handler.TimeScanCustomsRspFiles;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description: springboot main方法
 *
 * @className: DepotWebApplication
 * @Author: zhangpeng
 * @Date 2019/7/16 14:40
 * @Version 1.0
 **/
@Slf4j
@SpringBootApplication(scanBasePackages = {"cn.samples.depot"})
@EnableCaching// 开启缓存，需要显示的指定
@EnableConfigurationProperties
public class DepotWebCzApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DepotWebCzApplication.class, args);
    }

//    @Autowired
//    TCPClient tcpClient;
//
//    @Autowired
//    TCPServer tcpServer;

//    private MSMQListener msmqListener;
//
//    // 是否开启 msmq 同步
//    @Value("${msmq.sync:false}")
//    private boolean msmqSync;
//
//    // msmq 主键地址
//    @Value("${msmq.host}")
//    private String msmqHost;
//
//    // msmq 监听队列
//    @Value("${msmq.queuename}")
//    private String msmqQueue;
//
//    // msmq 取消息超时时间
//    @Value("${msmq.timeout}")
//    private int msmqTimeout;

    @Autowired
    TimeScanCustomsRspFiles timeScanCustomsRspFiles;
    // 海关回执报文延迟首次执行的时间（单位：分钟）
    @Value("${file.scan.initialDelay:0}")
    private int initialDelay;
    // 海关回执报文连续执行之间的时间（单位：分钟）
    @Value("${file.scan.period:1}")
    private int period;
    // 定时扫描海关回执的文件路径
    @Value("${customs.file.path}")
    private String scanFilePath;

    @Override
    public void run(String... args) throws Exception {
        log.info("run...");
        log.info(scanFilePath);
//        // 启动线程请求监听
//        ExecutorService executorService =  new ThreadPoolExecutor(1, 1,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>());
//
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    log.error("启动netty socket服务...");
//                    tcpClient.connect();
//                    log.info("启动端口，netty socket启动成功...");
//                } catch (Exception ex) {
//                    log.error("Start netty socket error, e={}", ex);
//                }
//            }
//        });

//        // 启动线程请求监听
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    log.error("启动netty socket服务...");
//                    tcpClient.connect();
//                    log.info("启动端口，netty socket启动成功...");
//                } catch (Exception ex) {
//                    log.error("Start netty socket error, e={}", ex);
//                }
//            }
//        }, "NettySocketChannelThread").start();


        // 开始 msmq 消息监听
//        if (msmqSync) {
//            msmqListener = new MSMQListener(msmqHost, msmqQueue, msmqTimeout);
//            msmqListener.start(new MSMQMessageHandler(msmqQueue));
//        }


        // 多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，
        // 其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题。
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("file-schedule-scan-pool-%d").daemon(true).build());
//        executorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                log.info("Start Scan Customs Xml Files...");
//                timeScanCustomsRspFiles.timeScanRspFile(scanFilePath);
//                log.info("End Scan Customs Xml Files...");
//            }
//        }, initialDelay, period, TimeUnit.MINUTES);

        executorService.scheduleAtFixedRate(() -> {
            log.info("Start Scan Customs Xml Files...");
            timeScanCustomsRspFiles.timeScanRspFile(scanFilePath);
            log.info("End Scan Customs Xml Files...");
        }, initialDelay, period, TimeUnit.MINUTES);

    }
}
