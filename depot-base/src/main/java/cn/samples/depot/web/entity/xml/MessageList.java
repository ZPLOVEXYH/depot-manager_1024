package cn.samples.depot.web.entity.xml;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "MESSAGE_LIST")
@Data
public class MessageList {
    @Element(name = "DECL_PORT", required = false)
    private String declPort;

    @Element(name = "UNLOADING_PLACE", required = false)
    private String unloadingPlace;

    @Element(name = "LOADING_PLACE", required = false)
    private String loadingPlace;

    @Element(name = "ACTUALDATETIME", required = false)
    private String actualDateTime;

    @Element(name = "COMPLETEDDATETIME", required = false)
    private String completedDateTime;

    @ElementList(inline = true, required = false)
    private List<RailWayBillInfos> billList;
}
