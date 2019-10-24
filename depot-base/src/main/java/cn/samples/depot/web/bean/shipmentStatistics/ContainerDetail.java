package cn.samples.depot.web.bean.shipmentStatistics;

import cn.samples.depot.web.entity.BContainerBillInfo;
import cn.samples.depot.web.entity.BContainerHistory;
import cn.samples.depot.web.entity.BContainerInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author majunzi
 * @Description 发运计划查询-箱货详情
 * @time 2019-09-20 17:28
 */
@Data
@Builder
public class ContainerDetail {
    //基本箱信息
    private BContainerInfo head;
    //单、货信息
    private List<BContainerBillInfo> billInfo;
    //集装箱历史记录
    private List<BContainerHistory> histories;
}
