package cn.yanghub.myblog.service.impl;

import cn.yanghub.myblog.entity.SysUser;
import cn.yanghub.myblog.mapper.SysUserMapper;
import cn.yanghub.myblog.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @className: SysUserServiceImpl
 * @author: yang
 * @date: 2025/2/15 19:38
 * @Version: 1.0
 * @description:
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {


    @Autowired
    private SysUserMapper userMapper;
//    @Autowired
//    private SysPermissionMapper sysPermissionMapper;
//    @Autowired
//    private SysUserRoleMapper sysUserRoleMapper;
//    @Autowired
//    private SysUserDepartMapper sysUserDepartMapper;
//    @Autowired
//    private SysDepartMapper sysDepartMapper;
//    @Autowired
//    private SysRoleMapper sysRoleMapper;
//    @Autowired
//    private SysDepartRoleUserMapper departRoleUserMapper;
//    @Autowired
//    private SysDepartRoleMapper sysDepartRoleMapper;
//    @Autowired
//    private SysThirdAccountMapper sysThirdAccountMapper;
//    @Autowired
//    ThirdAppWechatEnterpriseServiceImpl wechatEnterpriseService;
//    @Autowired
//    ThirdAppDingtalkServiceImpl dingtalkService;
//    @Autowired
//    SysRoleIndexMapper sysRoleIndexMapper;

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword) {
        SysUser user = userMapper.getUserByName(username);
        String passwordEncode = PasswordUtil.encrypt(username, oldpassword, user.getSalt());
        if (!user.getPassword().equals(passwordEncode)) {
            return Result.error("旧密码输入错误!");
        }
        if (oConvertUtils.isEmpty(newpassword)) {
            return Result.error("新密码不允许为空!");
        }
        if (!newpassword.equals(confirmpassword)) {
            return Result.error("两次输入密码不一致!");
        }
        String password = PasswordUtil.encrypt(username, newpassword, user.getSalt());
        this.userMapper.update(new SysUser().setPassword(password), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
        return Result.ok("密码重置成功!");
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> changePassword(SysUser sysUser) {
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String password = sysUser.getPassword();
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
        sysUser.setPassword(passwordEncode);
        this.userMapper.updateById(sysUser);
        return Result.ok("密码修改成功!");
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(String userId) {
        //1.删除用户
        this.removeById(userId);
        return false;
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchUsers(String userIds) {
        //1.删除用户
        this.removeByIds(Arrays.asList(userIds.split(",")));
        return false;
    }

    @Override
    public SysUser getUserByName(String username) {
        return userMapper.getUserByName(username);
    }

    @Override
    public void addUserWithRole(SysUser user, String roles) {

    }

    @Override
    public void editUserWithRole(SysUser user, String roles) {

    }

    @Override
    public List<String> getRole(String username) {
        return List.of();
    }

    @Override
    public SysUserCacheInfo getCacheUser(String username) {
        return null;
    }


    /**
     * 根据部门Id查询
     * @param page
     * @param departId 部门id
     * @param username 用户账户名称
     * @return
     */
    @Override
    public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username) {
        return userMapper.getUserByDepId(page, departId,username);
    }

    @Override
    public IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username) {
        return userMapper.getUserByDepIds(page, departIds,username);
    }

    @Override
    public Map<String, String> getDepNamesByUserIds(List<String> userIds) {
        return Map.of();
    }


    /**
     * 根据角色Id查询
     * @param page
     * @param roleId 角色id
     * @param username 用户账户名称
     * @return
     */
    @Override
    public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
        return userMapper.getUserByRoleId(page,roleId,username);
    }

    @Override
    public Set<String> getUserRolesSet(String username) {
        return Set.of();
    }

    @Override
    public Set<String> getUserPermissionsSet(String username) {
        return Set.of();
    }


    @Override
    @CacheEvict(value= {CacheConstant.SYS_USERS_CACHE}, key="#username")
    public void updateUserDepart(String username,String orgCode) {
        baseMapper.updateUserDepart(username, orgCode);
    }


    @Override
    public SysUser getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }


    @Override
    public SysUser getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public void addUserWithDepart(SysUser user, String selectedParts) {

    }

    @Override
    public void editUserWithDepart(SysUser user, String departs) {

    }


    /**
     * 校验用户是否有效
     * @param sysUser
     * @return
     */
    @Override
    public Result<?> checkUserIsEffective(SysUser sysUser) {
        Result<?> result = new Result<Object>();
        //情况1：根据用户信息查询，该用户不存在
        if (sysUser == null) {
            result.error500("该用户不存在，请注册");
 //           baseCommonService.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        }
        //情况2：根据用户信息查询，该用户已注销
        //update-begin---author:王帅   Date:20200601  for：if条件永远为falsebug------------
        if (CommonConstant.DEL_FLAG_1.equals(sysUser.getDelFlag())) {
            //update-end---author:王帅   Date:20200601  for：if条件永远为falsebug------------
//            baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已注销");
            return result;
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
 //           baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已冻结");
            return result;
        }
        return result;
    }

    @Override
    public List<SysUser> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_1);
        return userMapper.selectLogicDeleted(wrapper);
    }



}