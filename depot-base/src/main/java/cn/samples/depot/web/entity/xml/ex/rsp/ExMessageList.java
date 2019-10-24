package cn.samples.depot.web.entity.xml.ex.rsp;

import cn.samples.depot.web.entity.xml.ResponseInfo;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "MESSAGE_LIST")
@Data
public class ExMessageList implements Serializable {

    @Element(name = "RESPONSE_INFO", required = false)
    private ResponseInfo responseInfo;

    @ElementList(entry = "RESPONSE_ARRIVEINFO", inline = true, required = false)
    private List<RspArriveInfo> rspArriveInfoList;
}
