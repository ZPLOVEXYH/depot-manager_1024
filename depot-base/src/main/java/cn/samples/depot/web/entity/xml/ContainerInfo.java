package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 回执信息
 */
@Data
@Root(name = "CONTAINER_INFO")
public class ContainerInfo implements Serializable {

    // 车皮号
    @Element(name = "CARRIAGE_NO", required = false)
    private String carriageNo;

    // 集装箱号
    @Element(name = "CONTA_NO", required = false)
    private String contaNo;

    // 集装箱号(集装箱放行方式)
    @Element(name = "REL_MODE", required = false)
    private String relMode;

    // 集装箱号(集装箱放行方式中文名称)
    private String relModeName;

    // 集装箱放行时间，格式为：yyyy-MM-dd hh:mm:ss(24h)
    @Element(name = "REL_TIME", required = false)
    private String relTime;

    // 备注
    @Element(name = "NOTES", required = false)
    private String notes;

    // 创建时间
    private Long createTime;
}
