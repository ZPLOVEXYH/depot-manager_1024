package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "MESSAGE_LIST")
@Data
public class RelMessageList {

    /**
     * 关别代码
     */
    @Element(name = "CUSTOMS_CODE", required = false)
    private String customsCode;

    /**
     * I：进口,E：出口,O：未知/无
     */
    @Element(name = "I_E_FLAG", required = false)
    private String ieFlag;

    /**
     * 集装箱信息集合
     */
    @ElementList(inline = true)
    private List<ContainerInfo> containerInfos;

    /**
     * 运单信息集合
     */
    @ElementList(inline = true)
    private List<BillInfo> billInfos;

    // 运输工具
//    @ElementList(inline = true)
//    private List<ShipInfo> billInfos;

    /**
     * 运输工具
     */
    @Element(name = "SHIPINFO", required = false)
    private ShipInfo shipInfo;
}
