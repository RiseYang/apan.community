package cn.yanghub.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @className: UserLoginDTO
 * @author: yang
 * @date: 2026/4/9 下午5:27
 * @Version: 1.0
 * @description:
 */
@Data
public class UserLoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}