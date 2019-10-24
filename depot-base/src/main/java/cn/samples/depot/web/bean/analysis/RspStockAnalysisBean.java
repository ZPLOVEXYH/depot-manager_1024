package cn.samples.depot.web.bean.analysis;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RspStockAnalysisBean {
    private String name;

    private long value;
}
