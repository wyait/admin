package vip.wyait.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.wyait.admin.interceptor.UserActionInterceptor;


/**
 * 
 * @项目名称：wyait-admin
 * @类名称：MyWebMvcConfig
 * @类描述：自定义静态资源映射路径和静态资源存放路径
 * @创建人：wyait
 * @version：
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

	/**
	 * 添加自定义静态资源映射路径和静态资源存放路径(图片)
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/images/**").addResourceLocations(
				"/images/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		registry.addResourceHandler("/layui/**").addResourceLocations("/layui/");
		registry.addResourceHandler("/treegrid/**").addResourceLocations("/treegrid/");
		// 配置中的file:表示是一个具体的硬盘路径，其他的配置指的是系统环境变量
		/*registry.addResourceHandler("/img/**").addResourceLocations(
				"file:D:/demo-images/");*/
		//super.addResourceHandlers(registry);
	}

	/**
	 * 
	 * @描述：在Spring添加拦截器之前先创建拦截器对象，这样就能在Spring映射这个拦截器前，把拦截器中的依赖注入的对象给初始化完成了。
	 * </br>避免拦截器中注入的对象为null问题。
	 * @创建人：wyait
	 * @return
	 */
//	@Bean
//	public UserActionInterceptor userActionInterceptor(){
//		return new UserActionInterceptor();
//	}
//
//	/**
//	 * 添加拦截器
//	 */
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		// 路径根据后期项目的扩展，进行调整
//		registry.addInterceptor(userActionInterceptor())
//				.addPathPatterns("/user/**", "/auth/**")
//				.excludePathPatterns("/user/sendMsg", "/user/login");
//		//super.addInterceptors(registry);
//	}
	
}
