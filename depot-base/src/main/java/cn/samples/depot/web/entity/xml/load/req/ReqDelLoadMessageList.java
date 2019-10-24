package cn.samples.depot.web.entity.xml.load.req;

import lombok.Builder;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * 装车记录表体
 */
@Root(name = "MESSAGE_LIST")
@Data
@Builder
public class ReqDelLoadMessageList implements Serializable {
    /**
     * 装货地代码
     */
    @Element(name = "DISCHARGE_PLACE", required = false)
    private String dischargePlace;

    /**
     * 装车记录表体集装箱信息集合
     */
    @ElementList(inline = true, required = false)
    private List<ReqDelBillInfo> delBillInfoList;
}
