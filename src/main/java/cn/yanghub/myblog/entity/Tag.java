package cn.yanghub.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @className: Tag
 * @author: yang
 * @date: 2026/4/9 下午4:38
 * @Version: 1.0
 * @description:
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tags")
public class Tag {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String slug;
    private String color;
    private String description;
    private Integer postCount;
    private Integer isFeatured;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}