/**
 * @filename:CMenus 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.samples.depot.common.model.CRUDView;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 菜单表
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@TableName("c_menus")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CMenus extends Model<CMenus> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 菜单编码
     */
    @ApiModelProperty(name = "code", value = "菜单编码")
    @TableField(value = "code")
    private String code;
    /**
     * 菜单名称
     */
    @ApiModelProperty(name = "name", value = "菜单名称")
    @TableField(value = "name")
    private String name;
    /**
     * 父级菜单编码
     */
    @ApiModelProperty(name = "parentCode", value = "父级菜单编码")
    @TableField(value = "parent_code")
    private String parentCode;
    /**
     * 菜单级别
     */
    @ApiModelProperty(name = "level", value = "菜单级别")
    @TableField(value = "level")
    private Integer level;
    /**
     * 排序号
     */
    @ApiModelProperty(name = "sort", value = "排序号")
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 菜单地址
     */
    @ApiModelProperty(name = "url", value = "菜单地址")
    @TableField(value = "url")
    private String url;
    /**
     * 菜单图标
     */
    @ApiModelProperty(name = "icon", value = "菜单图标")
    @TableField(value = "icon")
    private String icon;
    /**
     * 菜单描述
     */
    @ApiModelProperty(name = "remark", value = "菜单描述")
    @TableField(value = "remark")
    private String remark;
    /**
     * 是否启用
     */
    @ApiModelProperty(name = "enable", value = "是否启用")
    @TableField(value = "enable")
    private Integer enable;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }


}
