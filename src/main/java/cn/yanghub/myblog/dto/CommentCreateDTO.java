package cn.yanghub.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @className: CommentCreateDTO
 * @author: yang
 * @date: 2026/4/9 下午5:44
 * @Version: 1.0
 * @description:
 */
@Data
public class CommentCreateDTO {
    @NotNull(message = "文章ID不能为空")
    private Long postId;

    private Long parentId;
    private Long userId;
    private String visitorName;
    private String visitorEmail;
    private String visitorWebsite;

    @NotBlank(message = "评论内容不能为空")
    private String content;
    private String visitorIp;
    private String userAgent;
}