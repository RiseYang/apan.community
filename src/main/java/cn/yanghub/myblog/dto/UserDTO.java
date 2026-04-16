package cn.yanghub.myblog.dto;

/**
 * @className: UserDTO
 * @author: yang
 * @date: 2026/4/9 下午5:59
 * @Version: 1.0
 * @description:
 */
import lombok.Data;

@Data
public class UserDTO {
    private String nickname;
    private String avatarUrl;
    private String bio;
    private String website;
}
