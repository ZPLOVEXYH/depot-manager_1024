package cn.samples.depot.web;

//import cn.samples.depot.common.config.msmq.MSMQListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

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
@EnableConfigurationProperties
public class DepotWebApplication implements CommandLineRunner {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(DepotWebApplication.class, args);
//        String[] strs = applicationContext.getBeanDefinitionNames();
//        for (String s : strs) {
//            log.info("spring容器的bean：{}", s);
//        }
    }

//    @Autowired
//    TCPClient tcpClient;
//
//    @Autowired
//    TCPServer tcpServer;
//
//    // private MSMQListener msmqListener;
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

    @Override
    public void run(String... args) throws Exception {
        log.info("run...");

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
//                    tcpServer.start();
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
    }
}
