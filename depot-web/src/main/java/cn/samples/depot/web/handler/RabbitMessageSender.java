package cn.samples.depot.web.handler;

import cn.samples.depot.common.constant.MqQueueConstant;
import cn.samples.depot.common.model.AuditStatus;
import cn.samples.depot.web.dto.shipment.BShipmentPlanMsg;
import cn.samples.depot.web.entity.BShipmentPlan;
import cn.samples.depot.web.mapper.BShipmentPlanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rabbitmq消息发送类
 *
 * @author ZhangPeng
 * @date 2019-07-29 13:14:00
 */
@Slf4j
@Component
public class RabbitMessageSender {

    /**
     * 自动注入RabbitTemplate模板类
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BShipmentPlanMapper bShipmentPlanMapper;

    /**
     * 回调函数: confirm确认
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            log.info("回调函数，confirm确认，correlationData: {}", correlationData);
            String messageId = correlationData.getId();

            BShipmentPlan bShipmentPlan = new BShipmentPlan();
            bShipmentPlan.setId(messageId);
            if (ack) {
                // 如果confirm返回成功 则进行更新
                log.info("ack：{}，confirm返回成功!!!", ack);
                // 更新发运计划申请状态为发送成功
                bShipmentPlan.setAuditStatus(AuditStatus.SEND_SUCCESS.getValue());
                // 更新发运计划申请状态为发送成功
                bShipmentPlanMapper.updateById(bShipmentPlan);
            } else {
                // 失败则进行具体的后续操作:重试 或者补偿等手段
                log.info("ack：{},confirm返回失败!!!", ack);
                // 更新发运计划申请状态为发送失败
                bShipmentPlan.setAuditStatus(AuditStatus.SEND_FAIL.getValue());
                // 更新发运计划申请状态为发送失败
                bShipmentPlanMapper.updateById(bShipmentPlan);
            }
        }
    };

    /**
     * 返回消息：return
     */
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int i, String s, String s1, String s2) {
            log.info("消息主体 message : {}", message);
            log.info("消息主体 message : {}" + i);
            log.info("描述：{}", s);
            log.info("消息使用的交换器 exchange : {}", s1);
            log.info("消息使用的路由键 routing : {}", s2);
        }
    };

    /**
     * 发送消息方法调用: 构建自定义对象消息
     *
     * @param msg
     * @throws Exception
     */
    public void sendMessage(BShipmentPlanMsg msg) throws Exception {
        // 通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，
        // 也就是只确认是否正确到达 Exchange 中
        rabbitTemplate.setConfirmCallback(confirmCallback);
        // 通过实现 ReturnCallback 接口，启动消息失败返回，比如路由不到队列时触发回调
        rabbitTemplate.setReturnCallback(returnCallback);
        // 消息唯一ID
        CorrelationData correlationData = new CorrelationData(msg.getId());
        rabbitTemplate.convertAndSend(MqQueueConstant.APPLY_EXCHANGE, MqQueueConstant.APPLY_ROUTING, msg, correlationData);
    }
}
