package vip.wyait.admin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送短信验证码 TODO
 */
public class SendMsgServer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SendMsgServer.class);

	/**
	 * 
	 * @描述：(公共)发送短消息
	 * @return  发送成功返回:ok，发送失败返回:no
	 */
	public static String SendMsg(String messageStr, String phoneNum) {
		LOGGER.debug("==發送短信！");
		//TODO
		return "ok";
	}

}
