package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * 发送给海关的xml报文
 */
@Root(name = "MESSAGE")
@Data
public class Message {

    /**
     * 发送给海关的xml报文表头
     */
    @ElementList(inline = true, required = false)
    List<MessageHead> messageHead;

    /**
     * 发送给海关的xml报文表体
     */
    @ElementList(inline = true, required = false)
    List<MessageList> messageList;

}
