package vip.wyait.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.wyait.admin.entity.UserRoleDTO;
import vip.wyait.admin.entity.UserRolesVO;
import vip.wyait.admin.entity.UserSearchDTO;
import vip.wyait.admin.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	/**
	 * 分页查询用户数据
	 * @return
	 */
	List<UserRoleDTO> getUsers(@Param("userSearch") UserSearchDTO userSearch);

	/**
	 * 删除用户
	 * @param id
	 * @param isDel
	 * @return
	 */
	int setDelUser(@Param("id") Integer id, @Param("isDel") Integer isDel,
                   @Param("insertUid") Integer insertUid);

	/**
	 * 设置用户是否离职
	 * @param id
	 * @param isJob
	 * @return
	 */
	int setJobUser(@Param("id") Integer id, @Param("isJob") Integer isJob,
                   @Param("insertUid") Integer insertUid);

	/**
	 * 查询用户及对应的角色
	 * @param id
	 * @return
	 */
	UserRolesVO getUserAndRoles(Integer id);

	/**
	 * 根据用户名和密码查找用户
	 * @param username
	 * @param password
	 * @return
	 */
	User findUser(@Param("username") String username,
                  @Param("password") String password);

	/**
	 *	根据手机号获取用户数据
	 * @param mobile
	 * @return
	 */
	User findUserByMobile(String mobile);

	/**
	 * 根据用户名获取用户数据
	 * @param username
	 * @return
	 */
	User findUserByName(String username);

	/**
	 * 修改用户密码
	 * @param id
	 * @param password
	 * @return
	 */
	int updatePwd(@Param("id") Integer id, @Param("password") String password);

	/**
	 * 是否锁定用户
	 * @param id
	 * @param isLock
	 * @return
	 */
	int setUserLockNum(@Param("id") Integer id, @Param("isLock") int isLock);
	
	/**
	 * 
	 * @描述：根据权限id获取拥有改权限的用户id列表
	 * @创建人：wyait
	 * @param id
	 * @return
	 */
	List<Integer> findUserByPermId(int id);
	
	/**
	 * 
	 * @描述：更新用户version+1
	 * @创建人：wyait
	 * @param uid
	 * @return
	 */
	int updateUserVersion(Integer uid);
	/**
	 * 
	 * @描述： 根据角色id获取拥有改角色的用户id列表
	 * @创建人：wyait
	 * @param roleId
	 * @return
	 */
	List<Integer> findUserByRoleId(int roleId);
}