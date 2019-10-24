package cn.samples.depot.web.cz.service.event;

import cn.samples.depot.web.entity.CStationAreaPositions;
import cn.samples.depot.web.service.event.CommonEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * @author majunzi
 * @Description 堆存占用历史纪录
 * @time 2019-08-06 10:06
 */
@Setter
@Getter
public class PositionsConstaHistoryEvent extends CommonEvent<CStationAreaPositions> {


    public PositionsConstaHistoryEvent(CStationAreaPositions source, EventType type) {
        super(source, type);
    }

}
