package vip.wyait.admin.shiro;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.wyait.admin.dao.UserMapper;
import vip.wyait.admin.pojo.Permission;
import vip.wyait.admin.pojo.Role;
import vip.wyait.admin.pojo.User;
import vip.wyait.admin.service.AuthService;
import vip.wyait.admin.service.UserServiceImpl;
import vip.wyait.admin.utils.ParamProperties;
import vip.wyait.admin.utils.RedisUtil;

import java.util.List;

/**
 * @项目名称：wyait-admin
 * @类描述：shiroRealm
 * @创建人：wyait
 * @version：V1.0
 */
@Service
public class ShiroRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AuthService authService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private ParamProperties paramProperties;

	/**
	 * 授予角色和权限，使用redis缓存
	 * @param principalCollection
	 * @return
	 */
	//@Cacheable(value = "catCache", key = "#root.caches[0].name + ':' + #id")，只能用于public方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principalCollection) {
		//授权
		logger.debug("授予角色和权限");
		// 添加权限 和 角色信息
		SimpleAuthorizationInfo authorizationInfo = null;
		// 获取当前登陆用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		Integer userId = user.getId();
		//1 缓存中是否存在
		String redisCachePrefix=paramProperties.getShiroRedisCachePrefix();
		authorizationInfo= (SimpleAuthorizationInfo) redisUtil.get(redisCachePrefix+userId);
		logger.debug("---- redis缓存中userId:{}获取到的权限----authorizationInfo:{}",userId,authorizationInfo);
		if(authorizationInfo==null){
			logger.debug("---- userId:{}没有缓存 ----",userId);
			authorizationInfo=new SimpleAuthorizationInfo();
			//如果缓存没有的话
			List<Role> roles = this.authService.getRoleByUser(userId);
				if (null != roles && roles.size() > 0) {
					for (Role role : roles) {
						authorizationInfo.addRole(role.getCode());
						// 角色对应的权限数据
						List<Permission> perms = this.authService.findPermsByRoleId(role
								.getId());
						if (null != perms && perms.size() > 0) {
							// 授权角色下所有权限
							for (Permission perm : perms) {
								authorizationInfo.addStringPermission(perm
										.getCode());
							}
						}
					}
				}
			//2 放入缓存
			redisUtil.set(redisCachePrefix+userId,authorizationInfo,paramProperties.getDefaultExpiration());
		}
		logger.info("---- user:{}获取到以下权限 ----",user);
		logger.info(authorizationInfo.getStringPermissions().toString());
		logger.info("---------------- Shiro 权限获取成功 ----------------------");
		return authorizationInfo;
	}

	/**
	 * 登录认证
	 * @param authenticationToken
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authenticationToken)
			throws AuthenticationException {
		//TODO
		//UsernamePasswordToken用于存放提交的登录信息
		UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
		logger.info("用户登录认证：验证当前Subject时获取到token为：" + ReflectionToStringBuilder
				.toString(token, ToStringStyle.MULTI_LINE_STYLE));
		String mobile = token.getUsername();
		// 调用数据层
		User user = userMapper.findUserByMobile(mobile);

		logger.debug("用户登录认证！用户信息user：" + user);
		if (user == null) {
			// 用户不存在
			return null;
		} else {
			// 密码存在
			// 第一个参数 ，登陆后，需要在session保存数据
			// 第二个参数，查询到密码(加密规则要和自定义的HashedCredentialsMatcher中的HashAlgorithmName散列算法一致)
			// 第三个参数 ，realm名字
			return new SimpleAuthenticationInfo(user, user.getPassword(),
					getName());
//			return new SimpleAuthenticationInfo(user, "123456",
//					getName());
		}
	}

}
