package cn.samples.depot.web.entity.xml.ex.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "MESSAGE_LIST")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqExMessageList implements Serializable {

    // 海关代码
    @Element(name = "DECL_PORT")
    private String declPort;

    // 卸货地代码
    @Element(name = "DISCHARGE_PLACE")
    private String dischargePlace;

    // 运抵卸货地时间格式：YYYY-MM-DD HH:mm:ss
    @Element(name = "ARRIVE_TIME", required = false)
    private String arriveTime;

    // 运抵编号集合
    @ElementList(entry = "ARRIVE_INFO", inline = true, required = false)
    private List<ReqArriveInfo> rspArriveInfoList;

    // 集装箱信息集合
    @ElementList(entry = "CONTAINER_INFO", inline = true, required = false)
    private List<ReqContainerInfo> reqContainerInfoList;
}
