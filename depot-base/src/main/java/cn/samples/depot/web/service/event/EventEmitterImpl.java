package cn.samples.depot.web.service.event;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public class EventEmitterImpl implements EventEmitter, ApplicationContextAware, InitializingBean, MessageListener {
    private ApplicationContext applicationContext;

    @Override
    public void emit(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    @Override
    public void emit(ClusterEvent clusterEvent) {
        //todo 仅在开发期间用，后面接MQ
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void onMessage(Message message) {

    }
}
