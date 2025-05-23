package cn.yanghub.myblog.controller;

import cn.hutool.core.util.RandomUtil;
import cn.yanghub.myblog.entity.SysUser;
import cn.yanghub.myblog.service.ISysUserService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @className: SysUserController
 * @author: yang
 * @date: 2025/2/15 16:15
 * @Version: 1.0
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 用户注册接口
     *
     * @param jsonObject
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<JSONObject> userRegister(@RequestBody JSONObject jsonObject, SysUser user) {
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = jsonObject.getString("phone");
        String smscode = jsonObject.getString("smscode");

        //update-begin-author:taoyan date:2022-9-13 for: VUEN-2245 【漏洞】发现新漏洞待处理20220906
        String redisKey = CommonConstant.PHONE_REDIS_KEY_PRE + phone;
        Object code = redisUtil.get(redisKey);
        //update-end-author:taoyan date:2022-9-13 for: VUEN-2245 【漏洞】发现新漏洞待处理20220906

        String username = jsonObject.getString("username");
        //未设置用户名，则用手机号作为用户名
        if (oConvertUtils.isEmpty(username)) {
            username = phone;
        }
        //未设置密码，则随机生成一个密码
        String password = jsonObject.getString("password");
        if (oConvertUtils.isEmpty(password)) {
            password = RandomUtil.randomString(8);
        }
        String email = jsonObject.getString("email");
        SysUser sysUser1 = sysUserService.getUserByName(username);
        if (sysUser1 != null) {
            result.setMessage("用户名已注册");
            result.setSuccess(false);
            return result;
        }
        SysUser sysUser2 = sysUserService.getUserByPhone(phone);
        if (sysUser2 != null) {
            result.setMessage("该手机号已注册");
            result.setSuccess(false);
            return result;
        }

        if (oConvertUtils.isNotEmpty(email)) {
            SysUser sysUser3 = sysUserService.getUserByEmail(email);
            if (sysUser3 != null) {
                result.setMessage("邮箱已被注册");
                result.setSuccess(false);
                return result;
            }
        }
        if (null == code) {
            result.setMessage("手机验证码失效，请重新获取");
            result.setSuccess(false);
            return result;
        }
        if (!smscode.equals(code.toString())) {
            result.setMessage("手机验证码错误");
            result.setSuccess(false);
            return result;
        }

        try {
            user.setCreateTime(new Date());// 设置创建时间
            String salt = oConvertUtils.randomGen(8);
            String passwordEncode = PasswordUtil.encrypt(username, password, salt);
            user.setSalt(salt);
            user.setUsername(username);
            user.setRealname(username);
            user.setPassword(passwordEncode);
            user.setEmail(email);
            user.setPhone(phone);
            user.setStatus(CommonConstant.USER_UNFREEZE);
            user.setDelFlag(CommonConstant.DEL_FLAG_0);
            user.setActivitiSync(CommonConstant.ACT_SYNC_0);
            sysUserService.addUserWithRole(user, "ee8626f80f7c2619917b6236f3a7f02b");//默认临时角色 test
            result.success("注册成功");
        } catch (Exception e) {
            result.error500("注册失败");
        }
        return result;
    }

}