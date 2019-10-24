/**
 * @filename:CUsers 2019年08月20日
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
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 用户信息表
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@TableName("c_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class CUsers extends Model<CUsers> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 用户登录名
     */
    @ApiModelProperty(name = "userName", value = "用户登录名")
    @TableField(value = "user_name")
    @JsonView(View.ME.class)
    private String userName;
    /**
     * 真实姓名
     */
    @ApiModelProperty(name = "realName", value = "真实姓名")
    @TableField(value = "real_name")
    @JsonView(View.ME.class)
    private String realName;
    /**
     * 企业编码
     */
    @ApiModelProperty(name = "enterpriseCode", value = "企业编码")
    @TableField(value = "enterprise_code")
    @JsonView(View.ME.class)
    private String enterpriseCode;
    /**
     * 企业中文名称
     */
    @ApiModelProperty(name = "enterpriseName", value = "企业中文名称")
    @TableField(exist = false)
    @JsonView(View.ME.class)
    private String enterpriseName;
    /**
     * 登录密码
     */
    @ApiModelProperty(name = "password", value = "登录密码")
    @Length(min = 6, max = 20, message = "登录密码长度6-20位字符")
    @TableField(value = "password")
    private String password;
    /**
     * 联系手机号
     */
    @ApiModelProperty(name = "contactPhone", value = "联系手机号")
    @TableField(value = "contact_phone")
    private String contactPhone;

    /**
     * 角色编码
     */
    @ApiModelProperty(name = "roleCode", value = "角色编码")
    @TableField(value = "role_code")
    private String roleCode;

    /**
     * 角色名称
     */
    @ApiModelProperty(name = "roleName", value = "角色名称")
    @TableField(value = "role_name")
    private String roleName;
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

        interface ME extends View { //展示当前登录用户信息
        }
    }


}
