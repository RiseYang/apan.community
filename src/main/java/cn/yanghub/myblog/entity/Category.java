package cn.yanghub.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @className: Category
 * @author: yang
 * @date: 2026/4/9 下午4:38
 * @Version: 1.0
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("categories")
public class Category {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String slug;
    private String description;
    private Long parentId;
    private Integer level;
    private String path;
    private String icon;
    private String color;
    private Integer sortOrder;
    private Integer isNav;
    private Integer isFeatured;
    private Integer postCount;
    private String metaTitle;
    private String metaDescription;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
