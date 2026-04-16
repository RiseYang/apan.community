package cn.yanghub.myblog.vo;

/**
 * @className: PostVO
 * @author: yang
 * @date: 2026/4/9 下午5:32
 * @Version: 1.0
 * @description:
 */
import cn.yanghub.myblog.entity.Post;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostVO {
    private Long id;
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private Post.ContentType contentType;
    private String coverImage;
    private String coverThumb;
    private Post.PostStatus status;
    private Post.Visibility visibility;
    private LocalDateTime publishedAt;
    private Integer isTop;
    private Integer isFeatured;
    private Integer isOriginal;
    private String sourceName;
    private String sourceUrl;
    private Integer allowComment;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer wordCount;
    private Integer readingTime;
    private String metaTitle;
    private String metaDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 作者信息
    private Long authorId;
    private String authorName;
    private String authorAvatar;

    // 分类信息
    private Long categoryId;
    private String categoryName;
    private String categorySlug;
    private String categoryColor;

    // 标签信息
    private List<String> tagNames;
    private List<String> tagSlugs;
}