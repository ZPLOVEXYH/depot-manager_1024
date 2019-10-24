package cn.samples.depot.web.bean;

import cn.samples.depot.web.entity.CVehicle;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author majunzi
 * @Description
 * @time 2019-10-15 10:42
 */
@Data
public class CVehicleQuery {

    /**
     * 车牌号
     */
    @ApiModelProperty(name = "vehicleNumber", value = "车牌号")
    @JsonView(CVehicle.View.Table.class)
    private String vehicleNumber;

    /**
     * 企业编码
     */
    @ApiModelProperty(name = "enterpriseCode", value = "企业编码")
    @JsonView(CVehicle.View.Table.class)
    private String enterpriseCode;


    /**
     * 企业名称
     */
    @ApiModelProperty(name = "enterpriceName", value = "企业名称")
    @JsonView(CVehicle.View.Table.class)
    private String enterpriceName;

}
