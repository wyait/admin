package vip.wyait.admin.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 统一配置参数获取类
 */
@Component
public class ParamProperties {
	/**
	 * 	redis主机ip
	 */
	@Value("${spring.redis.host}")
	private String redisHost;
	/**
	 * redis超时时间
	 */
	@Value("${spring.redis.timeout}")
	private int redisTimeOut;
	/**
	 * redis过期时间
	 */
	@Value("${spring.redis.defaultExpiration}")
	private int defaultExpiration;

	/**
	 * shiro整合redis
	 * 缓存、session管理前缀
	 * @return
	 */
	@Value("${shiro.redis.cache.prefix}")
	private String shiroRedisCachePrefix;
	@Value("${shiro.redis.session.prefix}")
	private String shiroRedisSessionPrefix;
	@Value("${remember.me.expire}")
	private int rememberMeExpire;
	/**
	 * redis密码
	 */
	@Value("${spring.redis.password}")
	private String redisPassword;
	/**
	  * @Description: 日志签名
	  * @Author:   wyait
	  * @Date:     2019/3/19 17:38
	  * @Param:
	  * @return:
	  */
	@Value("${custom.param.log.sign}")
	private String paramLogSign;

	@Value("${user.cookie.name}")
	private String userCookieName;

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public int getRedisTimeOut() {
		return redisTimeOut;
	}

	public void setRedisTimeOut(int redisTimeOut) {
		this.redisTimeOut = redisTimeOut;
	}

	public int getDefaultExpiration() {
		return defaultExpiration;
	}

	public void setDefaultExpiration(int defaultExpiration) {
		this.defaultExpiration = defaultExpiration;
	}

	public String getShiroRedisCachePrefix() {
		return shiroRedisCachePrefix;
	}

	public void setShiroRedisCachePrefix(String shiroRedisCachePrefix) {
		this.shiroRedisCachePrefix = shiroRedisCachePrefix;
	}

	public String getShiroRedisSessionPrefix() {
		return shiroRedisSessionPrefix;
	}

	public void setShiroRedisSessionPrefix(String shiroRedisSessionPrefix) {
		this.shiroRedisSessionPrefix = shiroRedisSessionPrefix;
	}

	public int getRememberMeExpire() {
		return rememberMeExpire;
	}

	public void setRememberMeExpire(int rememberMeExpire) {
		this.rememberMeExpire = rememberMeExpire;
	}

	public String getRedisPassword() {
		return redisPassword;
	}

	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}

	public String getParamLogSign() {
		return paramLogSign;
	}

	public void setParamLogSign(String paramLogSign) {
		this.paramLogSign = paramLogSign;
	}

	public String getUserCookieName() {
		return userCookieName;
	}

	public void setUserCookieName(String userCookieName) {
		this.userCookieName = userCookieName;
	}


	@Override
	public String toString() {
		return "ParamProperties{" +
				"redisHost='" + redisHost + '\'' +
				", redisTimeOut=" + redisTimeOut +
				", defaultExpiration=" + defaultExpiration +
				", shiroRedisCachePrefix='" + shiroRedisCachePrefix + '\'' +
				", shiroRedisSessionPrefix='" + shiroRedisSessionPrefix + '\'' +
				", rememberMeExpire=" + rememberMeExpire +
				", redisPassword='" + redisPassword + '\'' +
				", paramLogSign='" + paramLogSign + '\'' +
				", userCookieName='" + userCookieName + '\'' +
				'}';
	}
}