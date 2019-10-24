package cn.samples.depot.web.cz.handler;

import cn.samples.depot.common.constant.MqQueueConstant;
import cn.samples.depot.web.cz.service.BShipmentPlanService;
import cn.samples.depot.web.dto.shipment.BShipmentPlanMsg;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class BShipmentPlanMsgReceiver {
    @Autowired
    BShipmentPlanService planService;

    /**
     * 配置监听的哪一个队列，同时在没有queue和exchange的情况下会去创建并建立绑定关系
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqQueueConstant.APPLY_QUEUE, durable = "true"),
            exchange = @Exchange(name = MqQueueConstant.APPLY_EXCHANGE, durable = "true", type = "direct"),
            key = MqQueueConstant.APPLY_ROUTING
    )
    )
    @RabbitHandler
    public void onMessage(@Payload BShipmentPlanMsg msg, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        //消费者操作
        log.info("收到发运计划：{}", msg.toString());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        Boolean publishConfirm = (Boolean) headers.get(AmqpHeaders.PUBLISH_CONFIRM);
        // 如果是重复投递的消息，redelivered 为 true
        Boolean redelivered = (Boolean) headers.get(AmqpHeaders.REDELIVERED);

        if (!redelivered) planService.handlePlanMsg(msg);

        log.info("publishConfirm:{}, redelivered:{}", publishConfirm, redelivered);
        // ACK,确认一条消息已经被消费
        channel.basicAck(deliveryTag, false);
    }
}
