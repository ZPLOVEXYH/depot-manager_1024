package cn.samples.depot.common.config;

import cn.samples.depot.common.constant.MqQueueConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: rabbit初始化配置
 *
 * @className: RabbitConfig
 * @Author: zhangpeng
 * @Date 2019/7/16 14:23
 * @Version 1.0
 **/
@Configuration
public class RabbitConfig {

    /**
     * 初始化数据接收队列
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(MqQueueConstant.APPLY_QUEUE);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MqQueueConstant.APPLY_EXCHANGE);
    }


    /**
     * 队列绑定交换机
     *
     * @param queue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding dataBindingExchange(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(MqQueueConstant.APPLY_ROUTING);
    }

    /**
     * 初始化数据接收队列
     *
     * @return
     */
    @Bean
    public Queue sqlSyncQueue() {
        return new Queue(MqQueueConstant.SQL_SYNC_QUEUE);
    }

    @Bean
    public DirectExchange sqlSyncExchange() {
        return new DirectExchange(MqQueueConstant.SQL_SYNC_EXCHANGE);
    }


    /**
     * 队列绑定交换机
     *
     * @param sqlSyncQueue
     * @param sqlSyncExchange
     * @return
     */
    @Bean
    public Binding sqlBindingExchange(Queue sqlSyncQueue, DirectExchange sqlSyncExchange) {
        return BindingBuilder.bind(sqlSyncQueue).to(sqlSyncExchange).with(MqQueueConstant.SQL_SYNC_ROUTING);
    }
}
