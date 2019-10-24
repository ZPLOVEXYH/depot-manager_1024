package cn.samples.depot.web.entity.xml.ex.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Root(name = "ARRIVE_INFO")
public class ReqArriveInfo implements Serializable {

    // 运抵编号
    @Element(name = "ARRIVE_NO", required = false)
    private String arriveNo;

    // 件数
    @Element(name = "PACK_NO", required = false)
    private String packNo;

    // 毛重(公斤)
    @Element(name = "GROSS_WT", required = false)
    private String grossWt;

    // 备注
    @Element(name = "NOTES", required = false)
    private String notes;
}
