package cn.samples.depot.web.service.event;

import org.springframework.context.ApplicationEvent;

public class ClusterEvent extends ApplicationEvent {
    /**
     *
     */
    private static final long serialVersionUID = -4209217987887742943L;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never
     *               {@code null})
     */
    public ClusterEvent(Object source) {
        super(source);
    }

    public String getTopic() {
        return this.getClass().getName();
    }

    ;
}
