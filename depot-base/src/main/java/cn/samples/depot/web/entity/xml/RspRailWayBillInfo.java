package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Data
@Root(name = "RESPONSE_RAILWAY_BILLINFO")
public class RspRailWayBillInfo implements Serializable {

    // 运单号
    @Element(name = "BILL_NO", required = false)
    private String billNo;

    // 审核状态（01审核通过，03退单）
    @Element(name = "CHK_FLAG", required = false)
    private String chkFlag;

    // 备注
    @Element(name = "NOTES", required = false)
    private String notes;
}
