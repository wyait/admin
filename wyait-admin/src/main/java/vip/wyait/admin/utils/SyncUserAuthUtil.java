package vip.wyait.admin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.wyait.admin.dao.UserMapper;

import java.util.List;


/**
 * 
 * @项目名称：wyait-admin
 * @类名称：SyncUserAuthUtil
 * @类描述：同步用户权限工具类
 * @创建人：wyait
 * @创建时间：2020年8月5日11:11:43
 * @version：
 */
@Component
public class SyncUserAuthUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SyncUserAuthUtil.class);
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ParamProperties paramProperties;

	/**
	 * 
	 * @描述：角色变更，更改用户改角色的用户version
	 * @创建人：wyait
	 * @param：roleId 角色id
	 * @创建时间：2018年11月29日10:04:35
	 */
	public void syncUserRole(int roleId) {
		// 1 根据角色id获取拥有改角色的用户id列表
		List<Integer> uids = this.userMapper.findUserByRoleId(roleId);
		LOGGER.debug("==根据角色id获取拥有改角色的用户id列表uids:{}", uids);
		// 2 遍历用户信息，更新version
		for (Integer uid : uids) {
			// 更新用户version+1
			this.userMapper.updateUserVersion(uid);
			// 删除用户redis权限缓存
			if (redisUtil.get(paramProperties.getShiroRedisCachePrefix() + uid) != null) {
				redisUtil.delete(paramProperties.getShiroRedisCachePrefix()
						+ uid);
			}
		}
	}

	/**
	 * 
	 * @描述：权限变更，更改用户改权限的用户version
	 * @创建人：wyait
	 * @param：id 权限id
	 * @创建时间：2018年11月29日10:04:35
	 */
	public void syncUserPermission(int id) {
		// 1 根据权限id获取拥有改权限的用户id列表
		List<Integer> uids = this.userMapper.findUserByPermId(id);
		LOGGER.debug("==根据权限id获取拥有改权限的用户id列表uids:{}", uids);
		// 2 遍历用户信息，更新version
		for (Integer uid : uids) {
			// 更新用户version+1
			this.userMapper.updateUserVersion(uid);
			// 删除用户redis权限缓存
			if (redisUtil.get(paramProperties.getShiroRedisCachePrefix() + uid) != null) {
				redisUtil.delete(paramProperties.getShiroRedisCachePrefix()
						+ uid);
			}
		}
	}
}
