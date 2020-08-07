package vip.wyait.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vip.wyait.admin.utils.ParamProperties;

import javax.servlet.http.HttpServletRequest;

/**
 * @创建人：wyait
 * @version：V1.0
 */
@Controller
@RequestMapping("/")
public class IndexController {
	private static final Logger logger = LoggerFactory
			.getLogger(IndexController.class);
	@Autowired
	private ParamProperties paramProperties;

	@RequestMapping("/home")
	public String toHome(HttpServletRequest request) {
		logger.debug("===111-------------home------------");
		//shiro-Session的优先级高于使用servlet的session
		//logger.debug("Session过期时间:{}",request.getSession().getMaxInactiveInterval());
//		logger.debug("shiro-Session过期时间:{}", SecurityUtils.getSubject().getSession().getTimeout());
		return "home";
	}

	@RequestMapping("/{page}")
	public String toPage(
			@PathVariable("page") String page) {
		logger.debug("-------------toindex------------" + page);
		return page;
	}

}
