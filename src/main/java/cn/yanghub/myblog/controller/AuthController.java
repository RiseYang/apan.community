package cn.yanghub.myblog.controller;

import cn.yanghub.myblog.common.Result;
import cn.yanghub.myblog.dto.UserLoginDTO;
import cn.yanghub.myblog.dto.UserRegisterDTO;
import cn.yanghub.myblog.service.UserService;
import cn.yanghub.myblog.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @className: AuthController
 * @author: yang
 * @date: 2026/4/9 下午5:36
 * @Version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        Map<String, String> result = userService.login(loginDTO);
        return Result.success(result);
    }

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        UserVO user = userService.register(registerDTO);
        return Result.success(user);
    }

    @GetMapping("/me")
    public Result<UserVO> getCurrentUser(Authentication authentication) {
        UserVO user = userService.getCurrentUser();
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        // JWT 无状态，客户端清除 token 即可
        return Result.success();
    }
}
