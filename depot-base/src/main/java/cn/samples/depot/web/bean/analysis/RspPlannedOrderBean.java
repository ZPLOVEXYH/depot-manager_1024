package cn.samples.depot.web.bean.analysis;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RspPlannedOrderBean {
    private String name;

    private long value;
}
