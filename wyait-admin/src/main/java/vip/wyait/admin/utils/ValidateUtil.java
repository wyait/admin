package vip.wyait.admin.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 
 * @项目名称：common-tools
 * @类名称：ValidateUtil
 * @类描述：数据校验工具类
 * @创建人：wyait
 * @version：
 */
public class ValidateUtil {
	/**
	 * 数字、大小写字母 、下划线任意一种及以上，6-20位密码校验
	 */
	/**
	 * 
	 * @描述：【简单】密码校验：数字、大小写字母 、下划线任意一种及一种以上，6-20位密码校验
	 * @创建人：王炎
	 * @创建时间：2017年1月5日15:19:17
	 * @param str
	 * @return
	 */
	private static final Pattern SIMPLE_PASSWORD = Pattern
			.compile("^[0-9_a-zA-Z]{6,20}$");
	public static boolean isSimplePassword(String str) {
		return StringUtils.isNotBlank(str) && SIMPLE_PASSWORD.matcher(str).matches();
	}

	// 原本：Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	private static final Pattern P_MOBILEPHONE = Pattern.compile("^1\\d{10}$");

	/**
	 * 
	 * @描述：【简单】校验是否为11位，且1开头手机号
	 * @创建人：王炎
	 * @创建时间：2016年12月21日 下午1:39:01
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		return StringUtils.isNotBlank(str) && P_MOBILEPHONE.matcher(str).matches();
	}

	// 6位数验证码
	private static final Pattern CODE = Pattern.compile("[0-9]{6}$");

	/**
	 * 
	 * @描述：校验是否为6位数字验证码
	 * @创建人：王炎
	 * @创建时间：2016年12月21日 下午1:39:59
	 * @param str
	 * @return
	 */
	public static boolean isCode(String str) {
		return StringUtils.isNotBlank(str) && CODE.matcher(str).matches();
	}

	// 图片验证码 4位随机字母和数字
	private static final Pattern PICCODE = Pattern.compile("\\w{4}$");

	/**
	 * 
	 * @描述：校验是否为4位随机正整数和字母
	 * @创建人：王炎
	 * @创建时间：2016年12月21日 下午1:40:15
	 * @param str
	 * @return
	 */
	public static boolean isPicCode(String str) {
		return StringUtils.isNotBlank(str) && PICCODE.matcher(str).matches();
	}

	/**年份支持1000-3999 支持横线**/
	private static final Pattern DATE=Pattern
			.compile("[1-3]\\d{3}(-)?((0[1-9]|1[0-2]))?(-)?(0[1-9]|1[0-9]|2[0-9]|3[0-1])?");
	/**
	 * 
	 * @描述：时间格式校验（年份支持1000-3999）支持2018、2018-05、2018-05-05格式校验
	 * @创建人：wyait
	 * @创建时间：2018年8月28日 上午10:12:39
	 * @param str
	 * @return
	 */
	public static boolean isDate(String str){
		return StringUtils.isNotBlank(str) && DATE.matcher(str).matches();
	}

}
