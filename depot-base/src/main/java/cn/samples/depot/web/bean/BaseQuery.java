package cn.samples.depot.web.bean;

import cn.samples.depot.common.model.CRUDView;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseQuery {

    /**
     * 类型名称
     */
    @JsonView(CRUDView.Table.class)
    @ApiModelProperty(name = "code", value = "类型编码")
    private String code;
    /**
     * 类型名称
     */
    @JsonView(CRUDView.Table.class)
    @ApiModelProperty(name = "name", value = "类型名称")
    private String name;

    /**
     * 是否启用(0 未启用 1 启用)
     */
    @JsonView(CRUDView.Table.class)
    @ApiModelProperty(name = "enable", value = "是否启用(0 未启用 1 启用)")
    private Integer enable;

    /**
     * 创建时间
     */
    @JsonView(CRUDView.Table.class)
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private long createTime;

}
