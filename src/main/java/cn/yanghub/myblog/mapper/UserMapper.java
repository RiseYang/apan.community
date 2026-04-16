package cn.yanghub.myblog.mapper;

import cn.yanghub.myblog.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM users WHERE username = #{username} AND deleted_at=0")
    User selectByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email} AND deleted_at IS NULL")
    User selectByEmail(String email);

    @Select("SELECT * FROM users WHERE deleted_at = 0 AND (username = #{username} OR email = #{email})")
    User selectByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
}

