package cn.samples.depot.web.handler;

import cn.samples.depot.common.constant.MqQueueConstant;
import cn.samples.depot.web.entity.BShipmentAuditLog;
import cn.samples.depot.web.entity.BShipmentPlan;
import cn.samples.depot.web.service.BShipmentAuditLogService;
import cn.samples.depot.web.service.BShipmentPlanService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
 * 消费rabbitmq审核消息
 */
@Slf4j
@Component
public class AuditMessageReceiver {

    @Autowired
    BShipmentAuditLogService logService;

    @Autowired
    BShipmentPlanService planService;

    /**
     * 配置监听的哪一个队列，同时在没有queue和exchange的情况下会去创建并建立绑定关系
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqQueueConstant.AUDIT_QUEUE, durable = "true"),
            exchange = @Exchange(name = MqQueueConstant.AUDIT_EXCHANGE, durable = "true", type = "direct"),
            key = MqQueueConstant.AUDIT_ROUTING
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
    public void onMessage(@Payload BShipmentAuditLog msg, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        //消费者操作
        log.info("---------收到场站端发送过来的审核日志的消息，开始消费---------");
        log.info("订单信息：{}", msg.toString());

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        Boolean publishConfirm = (Boolean) headers.get(AmqpHeaders.PUBLISH_CONFIRM);
        // 如果是重复投递的消息，redelivered 为 true
        Boolean redelivered = (Boolean) headers.get(AmqpHeaders.REDELIVERED);
        log.info("publishConfirm:{}, redelivered:{}", publishConfirm, redelivered);

        if (!redelivered) {
            BShipmentPlan plan = BShipmentPlan.builder().auditStatus(msg.getAuditResult()).build();
            boolean updateFlag = planService.update(plan, Wrappers.<BShipmentPlan>lambdaQuery().eq(BShipmentPlan::getId, msg.getShipmentPlanId()));
            log.info("根据场站端发送的审核消息来更新企业端的发运计划审核状态：{}", updateFlag == true ? "成功" : "失败");

//            return JsonResult.data(service.update(pContaModel, Wrappers.<PContaModel>lambdaQuery().eq(PContaModel::getCode, code)));
            boolean saveFlag = logService.save(msg);
            log.info("场站端发送的审核消息日志保存：{}", saveFlag == true ? "成功" : "失败");
        }

        /**
         *  multiple 取值为 false 时，表示通知 RabbitMQ 当前消息被确认
         *  如果为 true，则额外将比第一个参数指定的 delivery tag 小的消息一并确认
         */
        boolean multiple = false;
        // ACK,确认一条消息已经被消费
        channel.basicAck(deliveryTag, multiple);
    }
}
