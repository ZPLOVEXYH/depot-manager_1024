/**
 * @filename:PAreaCode 2019年7月18日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.samples.depot.common.model.CRUDView;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 监管区域表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月18日
 * @Version: V1.0
 */
@TableName("p_area_code")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PAreaCode extends Model<PAreaCode> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     * 海关代码
     */
    @ApiModelProperty(name = "code", value = "海关代码")
    @TableField(value = "code")
    private String code;

    /**
     * 海关代码名称
     */
    @ApiModelProperty(name = "name", value = "海关代码名称")
    @TableField(value = "name")
    private String name;

    /**
     * 所属海关
     */
    @ApiModelProperty(name = "customsCode", value = "所属海关")
    @TableField(value = "customs_code")
    private String customsCode;

    /**
     * 是否启用(0 未启用 1 启用)
     */
    @ApiModelProperty(name = "enable", value = "是否启用(0 未启用 1 启用)")
    @TableField(value = "enable")
    private Integer enable;

    /**
     * 创建时间
     */
    @CreatedDate
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    private Long createTime = System.currentTimeMillis();

}
