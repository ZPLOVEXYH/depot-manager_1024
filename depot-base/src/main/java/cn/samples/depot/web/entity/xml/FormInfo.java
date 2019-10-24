package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 回执信息
 */
@Data
@Root(name = "FORM_INFO")
public class FormInfo implements Serializable {

    /**
     * 单证号
     */
    @Element(name = "FORM_NO")
    private String formNo;

    /**
     * 单证类型
     */
    @Element(name = "FORM_TYPE")
    private String formType;

    /**
     * 经营人 收发货人(仅报关单)
     */
    @Element(name = "TRADE_NAME", required = false)
    private String tradeName;

    // 创建时间
    private Long createTime;

    /**
     * 提单号
     */
    private String billNo;

}
