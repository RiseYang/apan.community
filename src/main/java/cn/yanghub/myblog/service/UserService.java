package cn.yanghub.myblog.service;

import cn.yanghub.myblog.dto.UserDTO;
import cn.yanghub.myblog.dto.UserLoginDTO;
import cn.yanghub.myblog.dto.UserRegisterDTO;
import cn.yanghub.myblog.entity.User;
import cn.yanghub.myblog.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface UserService extends IService<User> {
    UserVO register(UserRegisterDTO registerDTO);
    Map<String, String> login(UserLoginDTO loginDTO);
    UserVO getCurrentUser();
    UserVO updateProfile(UserDTO userDTO);
}

