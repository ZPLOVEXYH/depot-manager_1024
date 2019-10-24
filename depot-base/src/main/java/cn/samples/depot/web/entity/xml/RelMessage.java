package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 接收海关的放行指令
 */
@Root(name = "MESSAGE")
@Data
public class RelMessage implements Serializable {

    /**
     * 接收海关的放行指令的xml报文表头
     */
    @Element(name = "MESSAGE_HEAD", required = false)
    MessageHead messageHead;

    /**
     * 接收海关的放行指令的xml报文表体
     */
    @Element(name = "MESSAGE_LIST", required = false)
    RelMessageList messageList;
}