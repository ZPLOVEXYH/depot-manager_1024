package cn.samples.depot.web.entity.xml;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 装车记录新增报文表头
 */
@Root(name = "MESSAGE_HEAD")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageHead implements Serializable {

    /**
     * 报文编号
     */
    @Element(name = "MESSAGE_ID", required = false)
    private String messageId;

    /**
     * 报文编号
     */
    @Element(name = "FUNCTION_CODE", required = false)
    private String functionCode;

    /**
     * 消息类型
     */
    @Element(name = "MESSAGE_TYPE", required = false)
    private String messageType;

    /**
     * 消息创建时间
     */
    @Element(name = "MESSAGE_CREATE_TIME", required = false)
    private String auditTime;

    /**
     * 发送者
     */
    @Element(name = "SENDER", required = false)
    private String sender;

    /**
     * 接收者
     */
    @Element(name = "RECEIVER", required = false)
    private String receiver;

    /**
     * 版本号
     */
    @Element(name = "VERSION", required = false)
    private String version = "1.0";
}
