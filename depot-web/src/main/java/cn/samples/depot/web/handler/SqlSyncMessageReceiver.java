package cn.samples.depot.web.handler;

import cn.samples.depot.common.constant.MqQueueConstant;
import cn.samples.depot.web.mapper.PAreaCodeMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

/**
 * sql同步，消息队列
 */
@Slf4j
@Component
public class SqlSyncMessageReceiver {

    @Autowired
    PAreaCodeMapper syncSqlMapper;

    /**
     * 配置监听的哪一个队列，同时在没有queue和exchange的情况下会去创建并建立绑定关系
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqQueueConstant.SQL_SYNC_QUEUE, durable = "true"),
            exchange = @Exchange(name = MqQueueConstant.SQL_SYNC_EXCHANGE, durable = "true", type = "direct"),
            key = MqQueueConstant.SQL_SYNC_ROUTING
    )
    )
    /**
     * 如果有消息过来，在消费的时候调用这个方法
     * * Delivery Tag 用来标识信道中投递的消息。RabbitMQ 推送消息给 Consumer 时，会附带一个 Delivery Tag，
     *          * 以便 Consumer 可以在消息确认时告诉 RabbitMQ 到底是哪条消息被确认了。
     *          * RabbitMQ 保证在每个信道中，每条消息的 Delivery Tag 从 1 开始递增。
     */
    @RabbitHandler
    @Transactional
    public void onMessage(@Payload String msg, @Headers Map<String, Object> headers, Channel channel) {
        //消费者操作
        log.info("---------收到场站端发送过来的sql执行脚本的消息，开始消费---------");
        log.info("收到场站端发送过来的sql内容为：{}", msg);

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        Boolean publishConfirm = (Boolean) headers.get(AmqpHeaders.PUBLISH_CONFIRM);
        // 如果是重复投递的消息，redelivered 为 true
        Boolean redelivered = (Boolean) headers.get(AmqpHeaders.REDELIVERED);
        log.info("publishConfirm:{}, redelivered:{}", publishConfirm, redelivered);

        /**
         *  multiple 取值为 false 时，表示通知 RabbitMQ 当前消息被确认
         *  如果为 true，则额外将比第一个参数指定的 delivery tag 小的消息一并确认
         */
        try {
            // ACK,确认一条消息已经被消费
            log.info("开始执行sql脚本：{}", msg);
            syncSqlMapper.executorSql(msg);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.info("执行sql脚本失败{}", msg);
            log.error("consume confirm error!", e);
            //这一步千万不要忘记，不会会导致消息未确认，消息到达连接的qos之后便不能再接收新消息
            //一般重试肯定的有次数，这里简单的根据是否已经重发过来来决定重发。第二个参数表示是否重新分发
            try {
                channel.basicReject(deliveryTag, !redelivered);
            } catch (IOException e1) {
                log.error("rabbitmq消息再次消费失败", e);
                e1.printStackTrace();
            }
            //这个方法我知道的是比上面多一个批量确认的参数
            // channel.basicNack(deliveryTag, false,!redelivered);
        }
    }
}
