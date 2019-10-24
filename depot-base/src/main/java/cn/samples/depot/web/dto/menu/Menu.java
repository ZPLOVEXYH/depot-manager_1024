package cn.samples.depot.web.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Menu {

    @ApiModelProperty(name = "id", value = "ID")
    private String id;
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
    /**
     * 菜单地址
     */
    @ApiModelProperty(name = "url", value = "菜单地址")
    private String url;
    /**
     * 菜单图标
     */
    @ApiModelProperty(name = "icon", value = "菜单图标")
    private String icon;
    /**
     * 菜单描述
     */
    @ApiModelProperty(name = "remark", value = "菜单描述")
    private String remark;
    /**
     * 是否启用
     */
    @ApiModelProperty(name = "enable", value = "是否启用")
    private Integer enable;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Long createTime;

    @ApiModelProperty(name = "subMenu", value = "子菜单")
    private List<Menu> subMenu;
}