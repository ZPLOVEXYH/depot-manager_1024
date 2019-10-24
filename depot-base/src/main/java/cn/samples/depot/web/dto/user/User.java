package cn.samples.depot.web.dto.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @ApiModelProperty(name = "id", value = "id")
    private String id;
    /**
     * 用户登录名
     */
    @ApiModelProperty(name = "userName", value = "用户登录名")
    private String userName;
    /**
     * 真实姓名
     */
    @ApiModelProperty(name = "realName", value = "真实姓名")
    private String realName;
    /**
     * 企业编码
     */
    @ApiModelProperty(name = "enterpriseCode", value = "企业编码")
    private String enterpriseCode;
    /**
     * 联系手机号
     */
    @ApiModelProperty(name = "contactPhone", value = "联系手机号")
    private String contactPhone;
    /**
     * 是否启用
     */
    @ApiModelProperty(name = "enable", value = "是否启用")
    private Integer enable;

    /**
     * 角色编码
     **/
    @ApiModelProperty(name = "roleCode", value = "角色编码")
    private String roleCode;

    /**
     * 角色名称
     **/
    @ApiModelProperty(name = "roleName", value = "角色名称")
    private String roleName;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Long createTime;
}
