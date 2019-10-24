/**
 * @filename:CDischarges 2019年7月19日
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
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 装卸货地表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@TableName("c_discharges")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CDischarges extends Model<CDischarges> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 装卸货地代码
     */
    @ApiModelProperty(name = "code", value = "装卸货地代码")
    @TableField(value = "code")
    @JsonView(View.SELECT.class)
    private String code;


    /**
     * 装卸货地名称
     */
    @ApiModelProperty(name = "name", value = "装卸货地名称")
    @TableField(value = "name")
    @JsonView(View.SELECT.class)
    private String name;


    /**
     * 备注描述
     */
    @ApiModelProperty(name = "remark", value = "备注描述")
    @TableField(value = "remark")
    private String remark;

    /**
     * 是否启用 (0 未启用 1 启用)
     */
    @ApiModelProperty(name = "enable", value = "是否启用 (0 未启用 1 启用)")
    @TableField(value = "enable")
    private Integer enable;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
