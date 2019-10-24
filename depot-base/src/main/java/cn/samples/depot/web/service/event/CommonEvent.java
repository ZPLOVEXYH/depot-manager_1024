package cn.samples.depot.web.service.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
@SuppressWarnings("unchecked")
public abstract class CommonEvent<T> extends ApplicationEvent {
    private static final long serialVersionUID = 3336399408618856934L;
    private EventType type;

    public CommonEvent(T source) {
        super(source);
    }

    public CommonEvent(T source, EventType type) {
        super(source);
        this.type = type;
    }


    @Override
    public T getSource() {
        return (T) super.getSource();
    }

    public enum EventType {
        CREATE, SAVE, VIEW, UPDATE, DELETE
    }

    public boolean isSave() {
        return type == EventType.SAVE;
    }

    public boolean isUpdate() {
        return type == EventType.UPDATE;
    }

    public boolean isDelete() {
        return type == EventType.DELETE;
    }
}
