package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * 回执信息
 */
@Data
@Root(name = "RESPONSE_INFO")
public class ResponseInfo implements Serializable {

    // 申报报文编号
    @Element(name = "MSGID", required = false)
    private String msgId;

    // 审核状态（01审核通过，03退单）
    @Element(name = "CHK_FLAG", required = false)
    private String chkFlag;

    // 操作时间yyyy-MM-dd hh:mm: ss(24h)
    @Element(name = "PER_TIME", required = false)
    private String perTime;

    // 操作人
    @Element(name = "PER_OP", required = false)
    private String perOp;

    // 备注
    @Element(name = "NOTES", required = false)
    private String notes;
}
