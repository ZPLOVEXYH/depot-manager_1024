package cn.samples.depot.web.service.event;

import org.springframework.context.ApplicationEvent;

public interface EventEmitter {
    void emit(ApplicationEvent event);

    void emit(ClusterEvent clusterEvent);
}
