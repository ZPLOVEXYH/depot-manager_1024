package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 回执信息
 */
@Data
@Root(name = "SHIPINFO")
public class ShipInfo implements Serializable {

    // 海关编号
    @Element(name = "CUSTOMS_CODE", required = false)
    private String customsCode;

    // 运输工具代码
    @Element(name = "SHIP_ID", required = false)
    private String shipId;

    // 运输工具名称
    @Element(name = "SHIP_NAME_EN", required = false)
    private String shipNameEn;

    // 航线班次
    @Element(name = "VOYAGE_NO", required = false)
    private String voyageNo;

    // 进出口标记
    @Element(name = "I_E_FLAG", required = false)
    private String ieFlag;

    // 航线标记
    @Element(name = "LINE_FLAG", required = false)
    private String lineFlag;

}
