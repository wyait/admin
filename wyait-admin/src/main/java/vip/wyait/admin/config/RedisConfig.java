package vip.wyait.admin.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @项目名称：wyait-admin
 * @类名称：RedisConfig
 * @类描述：redis配置类
 * @创建人：wyait
 * @version：1.0.0
 */
@Configuration
@EnableCaching//(缓存支持)
// //继承CachingConfigurerSupport，重写CacheManager方法。
public class RedisConfig  extends CachingConfigurerSupport {

	/**
	 * 注入 RedisConnectionFactory
	 */
	@Autowired
	private LettuceConnectionFactory lettuceConnectionFactory;
	
	@SuppressWarnings("unused")
	private Duration timeToLive = Duration.ofSeconds(60);

	/**
	 * 实例化 RedisTemplate 对象
	 *
	 * @return RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		initDomainRedisTemplate(redisTemplate);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/**
	 * 设置数据存入 redis 的序列化方式
	 * </br>redisTemplate 序列化默认使用的jdkSerializeable, 存储二进制字节码, 所以自定义序列化类
	 *
	 * @param redisTemplate
	 */
	private void initDomainRedisTemplate(
			RedisTemplate<String, Object> redisTemplate) {
		//key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
		//所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
		//或者JdkSerializationRedisSerializer序列化方式;

		// 使用Jackson2JsonRedisSerialize 替换默认序列化
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		//// string结构的数据，设置value的序列化规则和 key的序列化规则
		//StringRedisSerializer解决key中午乱码问题。//Long类型不可以会出现异常信息;
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		//value乱码问题：Jackson2JsonRedisSerializer
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

		//设置Hash结构的key和value的序列化方式
		//redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
		//redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
	}

	/**
	 * 指定key的生成策略
	 * @return KeyGenerator
	 */
	@Bean
    public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override public Object generate(Object target, Method method,
					Object... params) {
				StringBuilder sb = new StringBuilder();
				String[] value = new String[1];
				// sb.append(target.getClass().getName());
				// sb.append(":" + method.getName());
				Cacheable cacheable = method.getAnnotation(Cacheable.class);
				if (cacheable != null) {
					value = cacheable.value();
				}
				CachePut cachePut = method.getAnnotation(CachePut.class);
				if (cachePut != null) {
					value = cachePut.value();
				}
				CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
				if (cacheEvict != null) {
					value = cacheEvict.value();
				}
				sb.append(value[0]);
				//获取参数值
				for (Object obj : params) {
					sb.append(":" + obj.toString());
				}
				return sb.toString();
			}
		};
	}

	// 缓存管理器 使用Lettuce，和jedis有很大不同
	/*@Bean
	public RedisCacheManager redisCacheManager() {
		//关键点，spring cache的注解使用的序列化都从这来，没有这个配置的话使用的jdk自己的序列化，实际上不影响使用，只是打印出来不适合人眼识别
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))//key序列化方式
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))//value序列化方式
				.disableCachingNullValues();
				//.entryTtl(timeToLive);//缓存过期时间

		RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(lettuceConnectionFactory)
				.cacheDefaults(config)
				.transactionAware();

		return builder.build();
	}

	private RedisSerializer<String> keySerializer() {
		return new StringRedisSerializer();
	}

	private RedisSerializer<Object> valueSerializer() {
		// 设置序列化
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		return jackson2JsonRedisSerializer;

		//或者使用GenericJackson2JsonRedisSerializer
		//return new GenericJackson2JsonRedisSerializer();
	}*/

}
