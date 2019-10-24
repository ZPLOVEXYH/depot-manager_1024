package cn.samples.depot.web.cz.service.event;

import cn.samples.depot.common.model.UseStatus;
import cn.samples.depot.web.entity.CStationAreaPositions;
import cn.samples.depot.web.service.event.CommonEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * @author majunzi
 * @Description
 * @time 2019-08-06 10:06
 */
@Setter
@Getter
public class AreaPositionEvent extends CommonEvent<CStationAreaPositions> {

    private UseStatus useStatus;
    private String areaCode;
    private String positonCode;

    public AreaPositionEvent(String areaCode, String positonCode, UseStatus useStatus) {
        super(new CStationAreaPositions());
        this.useStatus = useStatus;
        this.areaCode = areaCode;
        this.positonCode = positonCode;
    }

    public boolean isUsed() {
        return useStatus == UseStatus.USED;
    }

    public boolean isPreUsed() {
        return useStatus == UseStatus.PRE_USED;
    }

}
