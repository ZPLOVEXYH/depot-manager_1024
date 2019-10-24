package cn.samples.depot.web.entity.xml.load.req;

import cn.samples.depot.web.entity.xml.MessageHead;
import lombok.Builder;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 装车记录新增报文
 */
@Root(name = "MESSAGE")
@Data
@Builder
public class ReqLoadMessage implements Serializable {

    /**
     * 装车记录新增报文表头
     */
    @Element(name = "MESSAGE_HEAD", required = false)
    MessageHead messageHead;

    /**
     * 装车记录表体
     */
    @Element(name = "MESSAGE_LIST", required = false)
    ReqLoadMessageList messageList;
}