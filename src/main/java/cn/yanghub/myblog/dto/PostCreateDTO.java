package cn.yanghub.myblog.dto;

import cn.yanghub.myblog.entity.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
/**
 * @className: PostCreateDTO
 * @author: yang
 * @date: 2026/4/9 下午5:29
 * @Version: 1.0
 * @description:
 */

@Data
public class PostCreateDTO {
    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String slug;
    private String excerpt;
    private String coverImage;
    private Long categoryId;
    private List<Long> tagIds;
    private Post.ContentType contentType;
    private Post.PostStatus status;
    private Post.Visibility visibility;
    private String password;
    private Integer allowComment;
    private Integer allowLike;
}
