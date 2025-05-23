package cn.yanghub.myblog.controller;

import cn.hutool.core.util.RandomUtil;
import cn.yanghub.myblog.config.JeecgBaseConfig;
import cn.yanghub.myblog.entity.SysLoginModel;
import cn.yanghub.myblog.entity.SysUser;
import cn.yanghub.myblog.service.ISysUserService;
import cn.yanghub.myblog.utils.RandImageUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.Md5Util;
import org.jeecg.common.util.PasswordUtil;

import org.jeecg.common.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @className: LoginController
 * @author: yang
 * @date: 2025/2/3 13:03
 * @Version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/sys")
@Slf4j
@Api(tags = "登录接口")
public class LoginController {


    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JeecgBaseConfig jeecgBaseConfig;

    private final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    @ApiOperation("登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) {

        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();

        //校验验证码
        String captcha = sysLoginModel.getCaptcha();
        if (captcha == null) {
            result.error500("验证码无效");
            return result;
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        // 加入密钥作为混淆，避免简单的拼接，被外部利用，用户自定义该密钥即可
        String origin = lowerCaseCaptcha + sysLoginModel.getCheckKey() + jeecgBaseConfig.getSignatureSecret();
        String realKey = Md5Util.md5Encode(origin, "utf-8");
        Object checkCode = redisUtil.get(realKey);
        //当进入登录页时，有一定几率出现验证码错误 #1714
        if (checkCode == null || !checkCode.toString().equals(lowerCaseCaptcha)) {
            log.warn("验证码错误，key= {} , Ui checkCode= {}, Redis checkCode = {}", sysLoginModel.getCheckKey(), lowerCaseCaptcha, checkCode);
            result.error500("验证码错误");
            // 改成特殊的code 便于前端判断
            result.setCode(HttpStatus.PRECONDITION_FAILED.value());
            return result;
        }

        //1. 校验用户是否有效
        // 登录代码验证用户是否注销bug，if条件永远为false
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        //登录代码验证用户是否注销bug，if条件永远为false
        result = sysUserService.checkUserIsEffective(sysUser);
        if(!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

        //用户登录信息
 //       userInfo(sysUser, result);
        //登录成功，删除redis中的验证码
        redisUtil.del(realKey);
        //登录成功，删除redis中的验证码
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
 //       baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null,loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        return result;
    }

    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
 /*   private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
        String username = sysUser.getUsername();
        String syspassword = sysUser.getPassword();
        // 获取用户部门信息
        JSONObject obj = new JSONObject(new LinkedHashMap<>());

        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        obj.put("token", token);

        // update-begin--Author:sunjianlei Date:20210802 for：获取用户租户信息
        String tenantIds = sysUser.getRelTenantIds();
        if (oConvertUtils.isNotEmpty(tenantIds)) {
            List<Integer> tenantIdList = new ArrayList<>();
            for(String id: tenantIds.split(SymbolConstant.COMMA)){
                tenantIdList.add(Integer.valueOf(id));
            }
            // 该方法仅查询有效的租户，如果返回0个就说明所有的租户均无效。
            List<SysTenant> tenantList = sysTenantService.queryEffectiveTenant(tenantIdList);
            if (tenantList.size() == 0) {
                result.error500("与该用户关联的租户均已被冻结，无法登录！");
                return result;
            } else {
                obj.put("tenantList", tenantList);
            }
        }
        // update-end--Author:sunjianlei Date:20210802 for：获取用户租户信息
        //广西项目判断角色使用
        sysUser.setRoles(commonAPI.queryUserRoles(sysUser.getUsername()));
        obj.put("userInfo", sysUser);

        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
        obj.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            obj.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            obj.put("multi_depart", 1);
        } else {
            //查询当前是否有登录部门
            // update-begin--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            SysUser sysUserById = sysUserService.getById(sysUser.getId());
            if(oConvertUtils.isEmpty(sysUserById.getOrgCode())){
                sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            }
            // update-end--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            obj.put("multi_depart", 2);
        }
        //obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        result.setResult(obj);
        //result.success("登录成功");
        return result;
    }

  */

    /**
     * 后台生成图形验证码 ：有效
     * @param response
     * @param key
     */
    @ApiOperation("获取验证码")
    @GetMapping(value = "/randomImage/{key}")
    public Result<String> randomImage(HttpServletResponse response, @PathVariable("key") String key){
        Result<String> res = new Result<String>();
        try {
            //生成验证码
            String code = RandomUtil.randomString(BASE_CHECK_CODES,4);
            //存到redis中
            String lowerCaseCode = code.toLowerCase();

            //update-begin-author:taoyan date:2022-9-13 for: VUEN-2245 【漏洞】发现新漏洞待处理20220906
            // 加入密钥作为混淆，避免简单的拼接，被外部利用，用户自定义该密钥即可
            String origin = lowerCaseCode+key+jeecgBaseConfig.getSignatureSecret();
            String realKey = Md5Util.md5Encode(origin, "utf-8");
            //update-end-author:taoyan date:2022-9-13 for: VUEN-2245 【漏洞】发现新漏洞待处理20220906

            redisUtil.set(realKey, lowerCaseCode, 60);
            log.info("获取验证码，Redis key = {}，checkCode = {}", realKey, code);
            //返回前端
            String base64 = RandImageUtil.generate(code);
            res.setSuccess(true);
            res.setResult(base64);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.error500("获取验证码失败,请检查redis配置!");
            return res;
        }
        return res;
    }

}