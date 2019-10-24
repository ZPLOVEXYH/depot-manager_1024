package cn.samples.depot.web.entity.xml;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "RAILWAY_BILLINFO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RailWayBillInfos {
    @Element(name = "BILL_NO", required = false)
    private String billNo;

    @Element(name = "CARRIAGE_NO", required = false)
    private String carriageNo;

    @Element(name = "PACK_NO", required = false)
    private String packNo;

    @Element(name = "GROSS_WT", required = false)
    private String grossWt;

    @Element(name = "CONTA_NO", required = false)
    private String contaNo;

    @Element(name = "CONTA_TYPE", required = false)
    private String contaType;

    @ElementList(inline = true, required = false)
    private List<SealID> sealIDs;

    @Element(name = "NOTES", required = false)
    private String notes;
}
