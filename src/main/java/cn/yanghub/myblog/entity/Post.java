package cn.yanghub.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @className: Post
 * @author: yang
 * @date: 2026/4/9 下午4:38
 * @Version: 1.0
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("posts")
public class Post {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long authorId;
    private Long categoryId;
    private String title;
    private String slug;
    private String excerpt;
    private String content;

    @TableField("content_type")
    private ContentType contentType;

    private String coverImage;
    private String coverThumb;

    @TableField("status")
    private PostStatus status;

    @TableField("visibility")
    private Visibility visibility;

    private String password;
    private LocalDateTime scheduledAt;
    private LocalDateTime publishedAt;

    private Integer isTop;
    private Integer isFeatured;
    private Integer isOriginal;
    private String sourceName;
    private String sourceUrl;

    private Integer allowComment;
    private Integer allowLike;
    private Integer allowShare;

    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    private Integer wordCount;
    private Integer readingTime;

    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private String canonicalUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @TableField("deleted_at")
    private LocalDateTime deletedAt;

    public enum ContentType {
        markdown, html, rich_text, block
    }

    public enum PostStatus {
        draft, published, archived, scheduled, private_status
    }

    public enum Visibility {
        public_visibility, password, private_visibility, paid
    }
}
