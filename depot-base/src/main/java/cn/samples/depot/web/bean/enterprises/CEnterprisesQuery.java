package cn.samples.depot.web.bean.enterprises;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CEnterprisesQuery implements Serializable {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "id")
    private String id;


    /**
     * 企业编码
     */
    @ApiModelProperty(name = "code", value = "企业编码")
    private String code;


    /**
     * 企业名称
     */
    @ApiModelProperty(name = "name", value = "企业名称")
    private String name;


    /**
     * 企业类型
     */
    @ApiModelProperty(name = "enterpriseTypeCode", value = "企业类型")
    private String enterpriseTypeCode;


    /**
     * 组织机构代码
     */
    @ApiModelProperty(name = "orgCode", value = "组织机构代码")
    private String orgCode;


    /**
     * 统一社会信用码
     */
    @ApiModelProperty(name = "creditCode", value = "统一社会信用码")
    private String creditCode;


    /**
     * 联系人
     */
    @ApiModelProperty(name = "contact", value = "联系人")
    private String contact;


    /**
     * 联系号码
     */
    @ApiModelProperty(name = "contactPhone", value = "联系号码")
    private String contactPhone;


    /**
     * 企业地址
     */
    @ApiModelProperty(name = "address", value = "企业地址")
    private String address;


    /**
     * 审核状态
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态")
    private String auditStatus;


    /**
     * 审核时间
     */
    @ApiModelProperty(name = "auditTime", value = "审核时间")
    private Long auditTime;


    /**
     * 审核人
     */
    @ApiModelProperty(name = "auditUser", value = "审核人")
    private String auditUser;


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
}
