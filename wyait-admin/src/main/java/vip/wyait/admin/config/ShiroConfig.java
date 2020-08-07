package vip.wyait.admin.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.wyait.admin.dao.PermissionMapper;
import vip.wyait.admin.pojo.Permission;
import vip.wyait.admin.shiro.ShiroRealm;
import vip.wyait.admin.utils.ParamProperties;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @项目名称：wyait-admin
 * @包名：com.wyait.admin.config
 * @类描述：
 * @创建人：wyait
 * @version：V1.0
 */
@Configuration
//@EnableTransactionManagement
public class ShiroConfig {
	private static final Logger logger = LoggerFactory
			.getLogger(ShiroConfig.class);

	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private ParamProperties paramProperties;
	@Autowired
	private ShiroRealm shiroRealm;


	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件过滤器
	 *	</br>1,配置shiro安全管理器接口securityManage;
	 *	</br>2,shiro 连接约束配置filterChainDefinitions;
	 */
	@Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
			org.apache.shiro.mgt.SecurityManager securityManager) {
		//shiroFilterFactoryBean对象
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		logger.debug("-----------------Shiro拦截器工厂类注入开始");
		// 配置shiro安全管理器 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		//添加kickout认证
		HashMap<String, Filter> hashMap=new HashMap<String, Filter>();
		//hashMap.put("kickout",kickoutSessionFilter());
		shiroFilterFactoryBean.setFilters(hashMap);

		// 指定要求登录时的链接
		shiroFilterFactoryBean.setLoginUrl("/toLogin");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/home");
		// 未授权时跳转的界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");

		// filterChainDefinitions拦截器=map必须用：LinkedHashMap，因为它必须保证有序
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置退出过滤器,具体的退出代码Shiro已经实现
		filterChainDefinitionMap.put("/logout", "logout");
		//配置记住我或认证通过可以访问的地址
		filterChainDefinitionMap.put("/user/userList", "user");
		filterChainDefinitionMap.put("/", "user");
//		filterChainDefinitionMap.put("/?a=123", "anon");
//
//		// 配置不会被拦截的链接 从上向下顺序判断
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/css/*", "anon");
		filterChainDefinitionMap.put("/js/*", "anon");
		filterChainDefinitionMap.put("/js/*/*", "anon");
		filterChainDefinitionMap.put("/js/*/*/*", "anon");
		filterChainDefinitionMap.put("/images/*/**", "anon");
		filterChainDefinitionMap.put("/layui/*", "anon");
		filterChainDefinitionMap.put("/layui/*/**", "anon");
		filterChainDefinitionMap.put("/treegrid/*", "anon");
		filterChainDefinitionMap.put("/treegrid/*/*", "anon");
		filterChainDefinitionMap.put("/fragments/*", "anon");
		filterChainDefinitionMap.put("/layout", "anon");

		filterChainDefinitionMap.put("/user/sendMsg", "anon");
		filterChainDefinitionMap.put("/user/login", "anon");
		/**
		 * 拿到权限表中的所有认证code和page
		 */
		//需要把要授权的URL  全部装到filterChain中去过滤
		//从数据库获取
		List<Permission> permissionList = permissionMapper.findAll();
		for (Permission perm : permissionList) {
			String permissions = "perms[" + perm.getCode() + "]";
			logger.debug("permissions:"+permissions);
			filterChainDefinitionMap.put(perm.getPage(), permissions);
		}

		filterChainDefinitionMap.put("/user/delUser", "authc,perms[usermanage]");
//		//add操作，该用户必须有【addOperation】权限
////		filterChainDefinitionMap.put("/add", "perms[addOperation]");
//
//		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】-->
//		filterChainDefinitionMap.put("/**", "kickout,authc");
		filterChainDefinitionMap.put("/**", "authc");
		filterChainDefinitionMap.put("/*/*", "authc");
		filterChainDefinitionMap.put("/*/*/*", "authc");
		filterChainDefinitionMap.put("/*/*/*/**", "authc");

		shiroFilterFactoryBean
				.setFilterChainDefinitionMap(filterChainDefinitionMap);
		logger.debug("-----------------Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
	}

	/**
	 * shiro安全管理器设置realm认证和ehcache缓存管理
	 * @return
	 */
	@Bean
    public org.apache.shiro.mgt.SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(shiroRealm);
		// //注入redis缓存管理器;【手动实现】由于redis默认key生成策略，会导致所有用户的权限缓存都一样（都是第一个用户的权限缓存）
		//securityManager.setCacheManager(redisCacheManager());
		// //注入session管理器;
		securityManager.setSessionManager(sessionManager());
		//注入Cookie记住我管理器，无法同步设置redis中的用户session数据有效时间。如果直接保存session中，未测试
		//securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * 配置shiro redisManager
	 * </br>不配置的话，默认redisManager访问的是127.0.0.1:6379的redis
	 *
	 * @return
	 */
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(paramProperties.getRedisHost());
		redisManager.setPort(6379);
		// 配置过期时间 //
		redisManager.setExpire(paramProperties.getDefaultExpiration());
		redisManager.setTimeout(paramProperties.getRedisTimeOut());
		redisManager.setPassword(paramProperties.getRedisPassword());
		return redisManager;
	}

	/**
	 * cacheManager 缓存 redis实现
	 * 使用的是shiro-redis开源插件
	 *
	 * @return
	 */
	public RedisCacheManager redisCacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		//更改redis用于缓存的前缀
		redisCacheManager.setKeyPrefix(paramProperties.getShiroRedisCachePrefix());
		redisCacheManager.setRedisManager(redisManager());
		return redisCacheManager;
	}

//	@Bean
//	public ParamProperties paramProperties(){
//		return new ParamProperties();
//	}


	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis
	 * 使用的是shiro-redis开源插件
	 */
	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		//更改redis用于session管理的前缀
		redisSessionDAO.setKeyPrefix(paramProperties.getShiroRedisSessionPrefix());
		redisSessionDAO.setRedisManager(redisManager());
		//设置session过期时间
		redisSessionDAO.setExpire(paramProperties.getDefaultExpiration());
		return redisSessionDAO;
	}

	/**
	 *
	 * @描述：sessionManager添加session缓存操作DAO
	 * </br>注意：WebSessionManager里的setCacheManager方法会在调用时把sessionDAO里的cacheManager覆盖。
	 * </br>所以如果WebSessionManager有配置cacheManager，需同步改为和sessionDAO里的redisCacheManager。
	 * @创建人：wyait
	 * @创建时间：2018年4月21日 下午8:13:52
	 * @return
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setCacheManager(redisCacheManager());
		//先从缓存中获取session
		sessionManager.setSessionDAO(redisSessionDAO());
		//设置全局session过期时间(毫秒)
		sessionManager.setGlobalSessionTimeout(paramProperties.getDefaultExpiration()*1000L);
		//自定义cookie // true 启用自定义的SessionIdCookie
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionIdCookie(sessionIdCookie());
		return sessionManager;
	}

	/**
	 *
	 * @描述：自定义cookie名称等配置，保证cookie生命周期和session保持一致。
	 *  避免客户端关闭浏览器后，默认的JSESSIONID重新生成，导致JSESSIONID和session匹配不到
	 * @创建人：wyait
	 * @创建时间：2018年5月8日 下午1:26:23
	 * @return
	 */
	@Bean
	public SimpleCookie sessionIdCookie() {
		// DefaultSecurityManager
		SimpleCookie simpleCookie = new SimpleCookie();
		// sessionManager.setCacheManager(ehCacheManager());
		// 如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这样能有效的防止XSS攻击。
		simpleCookie.setHttpOnly(true);
		//切记名称不可用默认的cookie名称：JSESSIONID，否则会被浏览器覆盖有效期设置
		simpleCookie.setName("SHRIOSESSIONID");
		// 和redis中保存session用户数据时间一致
		simpleCookie.setMaxAge(paramProperties.getDefaultExpiration());
		return simpleCookie;
	}

	/**
	 * 身份认证realm; (账号密码校验；权限等)
	 *
	 * @return
	 */
//	@Bean
//	public ShiroRealm shiroRealm() {
//		ShiroRealm shiroRealm = new ShiroRealm();
//		//使用自定义的CredentialsMatcher进行密码校验和输错次数限制
////		shiroRealm.
//		//shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//		return shiroRealm;
//	}


	/**
	 * 设置记住我cookie过期时间
	 * @return
	 */
	/*@Bean
	public SimpleCookie remeberMeCookie(){
		logger.debug("记住我，设置cookie过期时间！");
		//cookie名称;对应前端的checkbox的name = rememberMe
		SimpleCookie scookie=new SimpleCookie("rememberMe");
		scookie.setValue("1");
		//记住我cookie生效时间30天 ,单位秒  [3天]
		scookie.setMaxAge(paramProperties().getRememberMeExpire());

		return scookie;
	}*/

	/**
	 * 配置cookie记住我管理器
	 * @return
	 */
	/*@Bean
	public CookieRememberMeManager rememberMeManager(){
		logger.debug("配置cookie记住我管理器！");
		CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(remeberMeCookie());
		return cookieRememberMeManager;
	}*/

	/**
	 * @描述：ShiroDialect，为了在thymeleaf里使用shiro的标签的bean 
	 * @创建人：wyait
	 * @创建时间：2017年12月21日 下午1:52:59
	 * @return
	 */
	@Bean
    public ShiroDialect shiroDialect(){
		return new ShiroDialect();
    }

	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis
	 * 使用的是shiro-redis开源插件
	 */
	//@Bean
	/*public EnterpriseCacheSessionDAO enterCacheSessionDAO() {
		EnterpriseCacheSessionDAO enterCacheSessionDAO = new EnterpriseCacheSessionDAO();
		//添加缓存管理器
		//enterCacheSessionDAO.setCacheManager(ehCacheManager());
		//添加活跃缓存名称
		enterCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		//指定sessionID生成类
		//enterCacheSessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
		return enterCacheSessionDAO;
	}*/

	/**
	 *
	 * @描述：开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
	 * </br>Enable Shiro Annotations for Spring-configured beans. Only run after the lifecycleBeanProcessor(保证实现了Shiro内部lifecycle函数的bean执行) has run
	 * </br>不使用注解的话，可以注释掉这两个配置
	 * @创建人：wyait
	 * @创建时间：2018年5月21日 下午6:07:56
	 * @return
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}

}
