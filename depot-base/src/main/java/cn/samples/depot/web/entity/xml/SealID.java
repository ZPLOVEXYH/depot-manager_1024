package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.io.Serializable;

@Root(name = "SEALID")
@Data
public class SealID implements Serializable {

    @Attribute(name = "AgencyCode", required = false)
    private String agencyCode;

    @Text(required = false)
    private String text;
}
