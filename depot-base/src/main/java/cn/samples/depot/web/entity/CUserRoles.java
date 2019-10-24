/**
 * @filename:CUserRoles 2019年08月20日
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
 * @Description: 用户角色关系表
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@TableName("c_user_roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CUserRoles extends Model<CUserRoles> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 用户名
     */
    @ApiModelProperty(name = "userName", value = "用户名")
    @TableField(value = "user_name")
    private String userName;
    /**
     * 角色编码
     */
    @ApiModelProperty(name = "roleCode", value = "角色编码")
    @TableField(value = "role_code")
    private String roleCode;
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
