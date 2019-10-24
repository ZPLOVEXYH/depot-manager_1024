package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * 回执信息
 */
@Data
@Root(name = "BILL_INFO")
public class BillInfo implements Serializable {

    // 提运单号
    @Element(name = "BILL_NO", required = false)
    private String billNo;

    // 车皮号
    @Element(name = "CARRIAGE_NO", required = false)
    private String carriageNo;

    // H2000运抵编号
    @Element(name = "H2000ARRIVE_NO")
    private String h2000ArriveNo;

    // 放行方式
    @Element(name = "REL_MODE", required = false)
    private String relMode;

    // 放行方式中文名称
    private String relModeName;

    // 放行时间
    @Element(name = "REL_TIME", required = false)
    private String relTime;

    // 件数
    @Element(name = "PACK_NO", required = false)
    private String packNo = "0";

    // 毛重（KG）
    @Element(name = "GROSS_WT", required = false)
    private String grossWt = "0";

    // 出口：运抵卸货地代码，进口：理货卸货地代码
    @Element(name = "DISCHARGE_PLACE", required = false)
    private String dischargePlace;

    // 运抵理货卸货地中文名称
    private String dischargePlaceName;

    // 创建时间
    private Long createTime;

    // 备注
    @Element(name = "NOTES", required = false)
    private String notes;

    // 备注
    @ElementList(inline = true)
    private List<FormInfo> formInfos;
}
