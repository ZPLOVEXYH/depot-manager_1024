package cn.samples.depot.web.entity.xml.ex.req;

import cn.samples.depot.web.entity.xml.MessageHead;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 接收海关的运抵申请和作废的回执报文
 */
@Root(name = "MESSAGE")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqExMessage implements Serializable {

    /**
     * 接收海关的运抵申请和作废的回执报文表头
     */
    @Element(name = "MESSAGE_HEAD", required = false)
    MessageHead messageHead;

    /**
     * 接收海关的运抵申请和作废的回执报文表体
     */
    @Element(name = "MESSAGE_LIST", required = false)
    ReqExMessageList messageList;
}