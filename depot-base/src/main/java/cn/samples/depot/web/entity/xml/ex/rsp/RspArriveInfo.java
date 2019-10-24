package cn.samples.depot.web.entity.xml.ex.rsp;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Data
@Root(name = "RESPONSE_RAILWAY_BILLINFO")
public class RspArriveInfo implements Serializable {

    // 运抵编号
    @Element(name = "ARRIVE_NO", required = false)
    private String arriveNo;

    // 审核状态（01审核通过，03退单）
    @Element(name = "CHK_FLAG", required = false)
    private String chkFlag;

    // 备注
    @Element(name = "NOTES", required = false)
    private String notes;
}
