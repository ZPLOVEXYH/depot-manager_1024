package cn.samples.depot.web.bean.analysis;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RspPlannedTrendBean {
    /**
     * X轴的年月展示
     */
    private String date;

    /**
     * 发运订单总数
     */
    private Long orderTotal;

    /**
     * 箱量
     */
    private Long containerNum;
}
