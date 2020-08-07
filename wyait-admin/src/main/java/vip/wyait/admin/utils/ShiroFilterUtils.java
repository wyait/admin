package vip.wyait.admin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.wyait.admin.entity.ResponseResult;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 
 * @项目名称：wyait-admin
 * @类名称：ShiroFilterUtils
 * @类描述：shiro工具类
 * @创建人：wyait
 * @创建时间：2020年8月5日11:11:30
 * @version：
 */
public class ShiroFilterUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(ShiroFilterUtils.class);
	private final static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 
     * @描述：判断请求是否是ajax
     * @创建人：wyait
     * @创建时间：2018年4月24日 下午5:00:22
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request){
    	String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
    	if("XMLHttpRequest".equalsIgnoreCase(header)){
    		logger.debug("shiro工具类【wyait-manager-->ShiroFilterUtils.isAjax】当前请求,为Ajax请求");
    		return Boolean.TRUE;
    	}
    	logger.debug("shiro工具类【wyait-manager-->ShiroFilterUtils.isAjax】当前请求,非Ajax请求");
    	return Boolean.FALSE;
    }

	/**
	 *
	 * @描述：response输出json
	 * @创建人：wyait
	 * @创建时间：2018年4月24日 下午5:14:22
	 * @param response
	 * @param result
	 */
	public static void out(HttpServletResponse response, ResponseResult result){
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");//设置编码
			response.setContentType("application/json");//设置返回类型
			out = response.getWriter();
			out.println(objectMapper.writeValueAsString(result));//输出
			logger.error("用户在线数量限制【wyait-manage-->ShiroFilterUtils.out】响应json信息成功");
		} catch (Exception e) {
			logger.error("用户在线数量限制【wyait-manage-->ShiroFilterUtils.out】响应json信息出错", e);
		}finally{
			if(null != out){
				out.flush();
				out.close();
			}
		}
	}

}
