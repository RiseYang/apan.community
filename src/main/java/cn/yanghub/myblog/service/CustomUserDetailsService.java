package cn.yanghub.myblog.service;

import cn.hutool.core.util.StrUtil;
import cn.yanghub.myblog.entity.User;
import cn.yanghub.myblog.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        User user = userMapper.selectByUsernameOrEmail(username, username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new DisabledException("用户已被禁用");
        }

        // 构建权限列表 - 使用role字段，不是nickname！
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 添加角色权限 (ROLE_前缀)
        String role = String.valueOf(user.getRole());
        if (StrUtil.isNotBlank(role)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
        } else {
            // 默认角色
            authorities.add(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                authorities
        );
    }
}
