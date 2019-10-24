package cn.samples.depot.web.bean.analysis;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class RspPlannedOrderDto {
    /**
     * 有效发运计划单
     */
    private List<Map<String, Object>> planValid;

    /**
     * 发运计划单总量
     */
    private List<Map<String, Object>> planTotal;
}
