package cn.yanghub.myblog.mapper;

import cn.yanghub.myblog.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @className: SysUserMapper
 * @author: yang
 * @date: 2025/2/15 16:54
 * @Version: 1.0
 * @description:
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户账号查询用户信息
     * @param username
     * @return
     */
    public SysUser getUserByName(@Param("username") String username);

    /**
     *  根据部门Id查询用户信息
     * @param page
     * @param departId
     * @param username 用户登录账户
     * @return
     */
    IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

    /**
     *  根据用户Ids,查询用户所属部门名称信息
     * @param userIds
     * @return
     */
 //   List<SysUserDepVo> getDepNamesByUserIds(@Param("userIds")List<String> userIds);

    /**
     *  根据部门Ids,查询部门下用户信息
     * @param page
     * @param departIds
     * @param username 用户登录账户
     * @return
     */
    IPage<SysUser> getUserByDepIds(Page page, @Param("departIds") List<String> departIds, @Param("username") String username);

    /**
     * 根据角色Id查询用户信息
     * @param page
     * @param roleId 角色id
     * @param username 用户登录账户
     * @return
     */
    IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);


    @Select("<script>" +
            "SELECT s.*, d.dep_id AS dept_id\n" +
            "FROM sys_user AS s\n" +
            "LEFT JOIN sys_user_depart AS d ON s.id = d.user_id\n" +
            "WHERE s.del_flag = 0\n" +
            "   AND s.id IN (\n" +
            "      SELECT user_id FROM sys_user_role WHERE role_id = #{roleId}\n" +
            "   )" +
            "\t<if test=\"username!=null and username!=''\">\n" +
            "\tand username = #{username}\n" +
            "\t</if>"+"</script>"
    )
  //  List<SysUserNew> getUserRoleList(@Param("roleId") String roleId, @Param("username") String username);

    /**
     * 根据用户名设置部门ID
     * @param username
     * @param orgCode
     */
    void updateUserDepart(@Param("username") String username,@Param("orgCode") String orgCode);

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    public SysUser getUserByPhone(@Param("phone") String phone);


    /**
     * 根据邮箱查询用户信息
     * @param email
     * @return
     */
    public SysUser getUserByEmail(@Param("email")String email);

    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     *
     * @param page 分页对象, xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param orgCode
     * @param userParams 用户查询条件，可为空
     * @return
     */
 //   List<SysUserSysDepartModel> getUserByOrgCode(IPage page, @Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);


    /**
     * 查询 getUserByOrgCode 的Total
     *
     * @param orgCode
     * @param userParams 用户查询条件，可为空
     * @return
     */
    Integer getUserByOrgCodeTotal(@Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);

    /**
     * 批量删除角色与用户关系
     * @Author scott
     * @Date 2019/12/13 16:10
     * @param roleIdArray
     */
    void deleteBathRoleUserRelation(@Param("roleIdArray") String[] roleIdArray);

    /**
     * 批量删除角色与权限关系
     * @Author scott
     * @Date 2019/12/13 16:10
     * @param roleIdArray
     */
    void deleteBathRolePermissionRelation(@Param("roleIdArray") String[] roleIdArray);

    /**
     * 查询被逻辑删除的用户
     * @param wrapper
     * @return List<SysUser>
     */
    List<SysUser> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);

    /**
     * 还原被逻辑删除的用户
     * @param userIds 用户id
     * @param entity
     * @return int
     */
    int revertLogicDeleted(@Param("userIds") List<String> userIds, @Param("entity") SysUser entity);

    /**
     * 彻底删除被逻辑删除的用户
     * @param userIds 多个用户id
     * @return int
     */
    int deleteLogicDeleted(@Param("userIds") List<String> userIds);

    /**
     * 更新空字符串为null【此写法有sql注入风险，禁止随便用】
     * @param fieldName
     * @return int
     */
    @Deprecated
    int updateNullByEmptyString(@Param("fieldName") String fieldName);

    /**
     *  根据部门Ids,查询部门下用户信息
     * @param departIds
     * @param username 用户账户名称
     * @return
     */
    List<SysUser> queryByDepIds(@Param("departIds")List<String> departIds,@Param("username") String username);

    @Select("SELECT\n" +
            "\tsu.* \n" +
            "FROM\n" +
            "\tsys_user_role as sur\n" +
            "LEFT JOIN sys_role as sr on sr.id=sur.role_id\n" +
            "LEFT JOIN sys_user as su on su.id = sur.user_id\n" +
            "where sr.role_code = 'xsscysdw' and su.id IS NOT NULL")
    List<SysUser> getRoleUserList();

    @Select("select u.id,u.realname,u.post,u.depart_ids,count(v.id) as documentCount from sys_user u left join db_book_view v on u.username=v.create_by ${ew.customSqlSegment}")
    Page<SysUser> pageByRck(Page<SysUser> page, @Param(Constants.WRAPPER) QueryWrapper<SysUser> wrapper);

    Page<SysUser> getUsersClaim(Page<SysUser> page, @Param("departName") String departName, @Param("username") String username);
}