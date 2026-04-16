package cn.yanghub.myblog.vo;

import cn.yanghub.myblog.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @className: UserVO
 * @author: yang
 * @date: 2026/4/9 下午6:09
 * @Version: 1.0
 * @description:
 */
/**
 * 用户信息视图对象
 * 用于前端展示的用户信息，不包含敏感字段
 */
@Data
public class UserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 个人网站
     */
    private String website;

    /**
     * 用户角色
     */
    private User.UserRole role;

    /**
     * 账号状态：0-禁用, 1-正常, 2-未激活
     */
    private Integer status;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 注册时间
     */
    private LocalDateTime createdAt;
}