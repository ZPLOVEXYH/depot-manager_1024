package cn.samples.depot.web.entity.xml.load.req;

import cn.samples.depot.web.entity.xml.SealID;
import cn.samples.depot.web.entity.xml.ex.req.ReqArriveInfo;
import lombok.Builder;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * 仅集装箱货有该节点
 * 非集装箱货无此节点
 */
@Root(name = "CONTA_INFO")
@Data
@Builder
public class ReqContaInfo {

    /**
     * 运单号
     */
    @Element(name = "CARRIAGE_NO", required = false)
    private String carriageNo;
    /**
     * 运单号
     */
    @Element(name = "BILL_NO", required = false)
    private String billNo;
    /**
     * 集装箱箱号
     */
    @Element(name = "CONTA_NO", required = false)
    private String contaNo;
    /**
     * 集装箱箱型
     */
    @Element(name = "CONTA_TYPE", required = false)
    private String contaType;
    /**
     * 封志号码,类型和施加封志人
     */
    @ElementList(inline = true, required = false)
    private List<SealID> sealIDList;
    /**
     * 备注
     */
    @Element(name = "NOTES", required = false)
    private String notes;
    /**
     * 运抵信息集合
     */
    @ElementList(inline = true, required = false)
    private List<ReqArriveInfo> arriveInfoList;
}
