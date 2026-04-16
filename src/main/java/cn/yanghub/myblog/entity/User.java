package cn.yanghub.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;

import java.time.LocalDateTime;

/**
 * @className: User
 * @author: yang
 * @date: 2026/4/9 下午4:36
 * @Version: 1.0
 * @description:
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String email;
    private String passwordHash;
    private String nickname;
    private String avatarUrl;
    private String bio;
    private String website;

    @TableField("role")
    private UserRole role;

    private Integer status;
    private LocalDateTime emailVerifiedAt;
    private LocalDateTime lastLoginAt;
    private String lastLoginIp;
    private Integer loginFailCount;
    private LocalDateTime lockedUntil;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime createdAt;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime updatedAt;

    @TableField(typeHandler = LocalDateTimeTypeHandler.class)
    private String deletedAt;

    public enum UserRole {
        super_admin, admin, editor, author, subscriber
    }
}
