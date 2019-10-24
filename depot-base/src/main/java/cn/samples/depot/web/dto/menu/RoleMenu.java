package cn.samples.depot.web.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleMenu {
    /**
     * 菜单编码
     */
    @ApiModelProperty(name = "code", value = "菜单编码")
    private String code;
    /**
     * 菜单名称
     */
    @ApiModelProperty(name = "name", value = "菜单名称")
    private String name;
    /**
     * 父级菜单编码
     */
    @ApiModelProperty(name = "parentCode", value = "父级菜单编码")
    private String parentCode;
    /**
     * 菜单级别
     */
    @ApiModelProperty(name = "level", value = "菜单级别")
    private Integer level;
    /**
     * 排序号
     */
    @ApiModelProperty(name = "sort", value = "排序号")
    private Integer sort;

    @ApiModelProperty(name = "select", value = "是否选中 0未选中 1选中")
    private Integer select;

    @ApiModelProperty(name = "subMenu", value = "子菜单")
    private List<RoleMenu> subMenu;
}
