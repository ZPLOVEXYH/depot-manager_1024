package cn.samples.depot.web.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Description: 登录请求实体类
 *
 * @className: LoginRequest
 * @Author: ChenJie
 * @Date 2019/8/27
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {
    @ApiModelProperty(name = "username", value = "用户名")
    private String username;
    @ApiModelProperty(name = "password", value = "密码")
    private String password;
}
