/**
 * @filename:CEnterprises 2019年7月19日
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

import javax.validation.constraints.NotBlank;

/**
 * @Description: 企业信息表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@TableName("c_enterprises")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CEnterprises extends Model<CEnterprises> {

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
     * 企业编码
     */
    @NotBlank(message = "企业编码不能为空")
    @ApiModelProperty(name = "code", value = "企业编码")
    @TableField(value = "code")
    @JsonView(View.SELECT.class)
    private String code;


    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    @ApiModelProperty(name = "name", value = "企业名称")
    @TableField(value = "name")
    @JsonView(View.SELECT.class)
    private String name;


    /**
     * 企业类型
     */
    @NotBlank(message = "企业类型不能为空")
    @ApiModelProperty(name = "enterpriseTypeCode", value = "企业类型")
    @TableField(value = "enterprise_type_code")
    private String enterpriseTypeCode;


    /**
     * 组织机构代码
     */
    @NotBlank(message = "组织机构代码不能为空")
    @ApiModelProperty(name = "orgCode", value = "组织机构代码")
    @TableField(value = "org_code")
    private String orgCode;


    /**
     * 统一社会信用码
     */
    @NotBlank(message = "统一社会信用码不能为空")
    @ApiModelProperty(name = "creditCode", value = "统一社会信用码")
    @TableField(value = "credit_code")
    private String creditCode;


    /**
     * 联系人
     */
    @NotBlank(message = "联系人不能为空")
    @ApiModelProperty(name = "contact", value = "联系人")
    @TableField(value = "contact")
    private String contact;


    /**
     * 联系号码
     */
    @NotBlank(message = "联系号码不能为空")
    @ApiModelProperty(name = "contactPhone", value = "联系号码")
    @TableField(value = "contact_phone")
    private String contactPhone;


    /**
     * 企业地址
     */
    @ApiModelProperty(name = "address", value = "企业地址")
    @TableField(value = "address")
    private String address;


    /**
     * 审核状态
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态")
    @TableField(value = "audit_status")
    private String auditStatus;


    /**
     * 审核时间
     */
    @ApiModelProperty(name = "auditTime", value = "审核时间")
    @TableField(value = "audit_time")
    private Long auditTime;


    /**
     * 审核人
     */
    @ApiModelProperty(name = "auditUser", value = "审核人")
    @TableField(value = "audit_user")
    private String auditUser;


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


}
