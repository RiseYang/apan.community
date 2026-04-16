package cn.yanghub.myblog.service.impl;

/**
 * @className: UserServiceImpl
 * @author: yang
 * @date: 2026/4/9 下午5:19
 * @Version: 1.0
 * @description:
 */
import cn.yanghub.myblog.dto.UserDTO;
import cn.yanghub.myblog.dto.UserLoginDTO;
import cn.yanghub.myblog.dto.UserRegisterDTO;
import cn.yanghub.myblog.entity.User;
import cn.yanghub.myblog.mapper.UserMapper;
import cn.yanghub.myblog.service.UserService;
import cn.yanghub.myblog.utils.JwtUtil;
import cn.yanghub.myblog.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserVO register(UserRegisterDTO registerDTO) {
        // 检查用户名
        if (userMapper.selectByUsername(registerDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱
        if (userMapper.selectByEmail(registerDTO.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setRole(User.UserRole.subscriber);
        user.setStatus(1);

        userMapper.insert(user);

        return convertToVO(user);
    }

    @Override
    public Map<String, String> login(UserLoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails);

        // 更新登录信息
        User user = userMapper.selectByUsername(userDetails.getUsername());
        if (user == null) {
            // 这里可以选择抛出一个自定义异常，或者直接返回错误信息
            // 既然已经登录成功了（authentication 通过了），说明用户肯定存在
            // 如果这里为 null，说明可能是逻辑删除导致的查询失败
            throw new RuntimeException("用户不存在或已被删除，无法更新登录信息");
        }
        user.setLastLoginAt(LocalDateTime.now());
        user.setLastLoginIp(getClientIp());
        userMapper.updateById(user);

        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatarUrl());
        result.put("role", user.getRole().name());

        return result;
    }

    @Override
    public UserVO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectByUsername(username);
        return convertToVO(user);
    }

    @Override
    @Transactional
    public UserVO updateProfile(UserDTO userDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectByUsername(username);

        BeanUtils.copyProperties(userDTO, user, "id", "username", "email", "role", "status");
        userMapper.updateById(user);

        return convertToVO(user);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    private String getClientIp() {
        // 简化实现，实际应从Request获取
        return "127.0.0.1";
    }
}