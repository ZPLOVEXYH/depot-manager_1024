package cn.samples.depot.web.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdatePasswordDTO {
    @ApiModelProperty(name = "username", value = "用户名")
    private String username;

    @ApiModelProperty(name = "passwordOld", value = "旧密码")
    private String passwordOld;

    @Length(min = 6, max = 20, message = "密码长度6-20位字符")
    @ApiModelProperty(name = "password", value = "用户新密码")
    private String password;
}
