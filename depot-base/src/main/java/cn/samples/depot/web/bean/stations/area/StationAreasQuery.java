package cn.samples.depot.web.bean.stations.area;

import cn.samples.depot.common.model.CRUDView;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StationAreasQuery {

    /**
     * 类型名称
     */
    @JsonView(CRUDView.Table.class)
    @ApiModelProperty(name = "code", value = "类型编码")
    private String code;

    /**
     * 创建时间
     */
    @JsonView(CRUDView.Table.class)
    @ApiModelProperty(name = "startCreateTime", value = "起始创建时间")
    private Long startCreateTime;

    /**
     * 创建时间
     */
    @JsonView(CRUDView.Table.class)
    @ApiModelProperty(name = "endCreateTime", value = "结束创建时间")
    private Long endCreateTime;

}
