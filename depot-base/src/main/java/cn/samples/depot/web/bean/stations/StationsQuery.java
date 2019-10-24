package cn.samples.depot.web.bean.stations;

import cn.samples.depot.web.bean.BaseQuery;
import cn.samples.depot.web.entity.CStations;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StationsQuery extends BaseQuery {

    /**
     * 场站类型
     */
    @JsonView(CStations.View.Table.class)
    @ApiModelProperty(name = "stationTypeCode", value = "场站类型")
    private String stationTypeCode;

    /**
     * 经营人代码
     */
    @JsonView(CStations.View.Table.class)
    @ApiModelProperty(name = "operatorCode", value = "经营人代码")
    private String operatorCode;

    /**
     * 经营人名称
     */
    @JsonView(CStations.View.Table.class)
    @ApiModelProperty(name = "operatorName", value = "经营人名称")
    private String operatorName;

    /**
     * 联系人
     */
    @JsonView(CStations.View.Table.class)
    @ApiModelProperty(name = "contact", value = "联系人")
    private String contact;

    /**
     * 联系方式
     */
    @JsonView(CStations.View.Table.class)
    @ApiModelProperty(name = "contactPhone", value = "联系方式")
    private String contactPhone;

    /**
     * 联系地址
     */
    @JsonView(CStations.View.Table.class)
    @ApiModelProperty(name = "address", value = "联系地址")
    private String address;

}
