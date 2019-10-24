package cn.samples.depot.web.entity.xml.load.rsp;

import cn.samples.depot.web.entity.xml.MessageHead;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 接收海关的装车记录单申请和作废的回执报文
 */
@Root(name = "MESSAGE")
@Data
public class LoadMessage implements Serializable {

    /**
     * 接收海关的装车记录单申请和作废的回执报文表头
     */
    @Element(name = "MESSAGE_HEAD", required = false)
    MessageHead messageHead;

    /**
     * 接收海关的装车记录单申请和作废的回执报文表体
     */
    @Element(name = "MESSAGE_LIST", required = false)
    LoadMessageList messageList;
}