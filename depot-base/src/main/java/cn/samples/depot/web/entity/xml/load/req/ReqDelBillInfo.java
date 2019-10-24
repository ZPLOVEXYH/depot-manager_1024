package cn.samples.depot.web.entity.xml.load.req;

import lombok.Builder;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Data
@Root(name = "BILL_INFO")
@Builder
public class ReqDelBillInfo implements Serializable {

    // 车皮号
    @Element(name = "CARRIAGE_NO", required = false)
    private String carriageNo;

    // 运单号
    @Element(name = "BILL_NO", required = false)
    private String billNo;

    // 备注
    @Element(name = "NOTES", required = false)
    private String notes;
}
