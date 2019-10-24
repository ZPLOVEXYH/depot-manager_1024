package cn.samples.depot.web.entity.xml.ex.req;

import cn.samples.depot.web.entity.xml.SealID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * 集装箱信息集合
 */
@Data
@Root(name = "CONTAINER_INFO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqContainerInfo implements Serializable {

    // 集装箱号
    @Element(name = "CONTA_NO")
    private String contaNo;

    // 集装箱箱型
    @Element(name = "CONTA_TYPE")
    private String contaType;

    @ElementList(inline = true, required = false)
    private List<SealID> sealIDList;

    // 运抵编号
    @Element(name = "PART_ARRIVE_NO", required = false)
    private String partArriveNo;
}
