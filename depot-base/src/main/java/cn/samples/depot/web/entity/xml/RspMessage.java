package cn.samples.depot.web.entity.xml;

import lombok.Data;
import lombok.ToString;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 海关响应的xml报文
 */
@Root(name = "MESSAGE")
@Data
@ToString
public class RspMessage implements Serializable {

    /**
     * 发送给海关的xml报文表头
     */
    @Element(name = "MESSAGE_HEAD", required = false)
    MessageHead messageHead;

    /**
     * 发送给海关的xml报文表体
     */
    @Element(name = "MESSAGE_LIST", required = false)
    RspMessageList messageList;
}
