/**
 * @filename:PContaModel 2019年7月18日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.samples.depot.common.model.CRUDView;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 箱型表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月18日
 * @Version: V1.0
 */
@TableName("p_conta_model")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PContaModel extends Model<PContaModel> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     * 类型值
     */
    @ApiModelProperty(name = "code", value = "类型值")
    @TableField(value = "code")
    @JsonView(View.SELECT.class)
    private String code;


    /**
     * 类型名称
     */
    @ApiModelProperty(name = "name", value = "类型名称")
    @TableField(value = "name")
    @JsonView(View.SELECT.class)
    private String name;


    /**
     * 是否启用(0 未启用 1 启用)
     */
    @ApiModelProperty(name = "enable", value = "是否启用(0 未启用 1 启用)")
    @TableField(value = "enable")
    private Integer enable;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @CreatedDate
    @TableField(value = "create_time")
    private Long createTime = System.currentTimeMillis();


}
