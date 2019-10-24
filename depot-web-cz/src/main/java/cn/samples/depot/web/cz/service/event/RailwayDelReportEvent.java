package cn.samples.depot.web.cz.service.event;

import cn.samples.depot.common.model.InvalidStateFlag;
import cn.samples.depot.web.service.event.CommonEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author majunzi
 * @Description 运抵作废 事件 传 运抵单编号
 * @time 2019-08-06 10:06
 */
@Setter
@Getter
public class RailwayDelReportEvent extends CommonEvent<List<String>> {

    InvalidStateFlag invalidStateFlag;

    public RailwayDelReportEvent(List<String> arriveNo, InvalidStateFlag invalidStateFlag) {
        super(arriveNo);
        this.invalidStateFlag = invalidStateFlag;
    }

    public boolean isPass() {
        return invalidStateFlag == InvalidStateFlag.InvalidState_03;
    }

    public boolean isFail() {
        return invalidStateFlag == InvalidStateFlag.InvalidState_04;
    }

    public boolean isDeclare() {
        return invalidStateFlag == InvalidStateFlag.InvalidState_02;
    }
}
