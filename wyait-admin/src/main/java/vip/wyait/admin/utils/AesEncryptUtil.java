package vip.wyait.admin.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;

/**
 * 
 * @项目名称：common-tools
 * @类名称：AesEncryptUtil
 * @类描述：AES前后端交互加解密工具类
 * @创建人：wyait
 * @version：
 */
public class AesEncryptUtil {

	// 使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同！
	private static String KEY = "5_wyait.com_www.";
	private static String IV = "abcdefghigk12345";
	/**编码格式；默认null为GBK*/
	public static final String CHARSET = "UTF-8";

	/**
	 * AES加密方法【模拟前端加密字符串（Base64）】
	 * @param data 要加密的数据
	 * @param key 加密key
	 * @param iv 加密iv
	 * @return 加密的结果
	 * @throws Exception
	 */
	public static String encryptAes(String data, String key, String iv)
			throws Exception {
		try {
			byte[] keyBytes = key.getBytes(CHARSET);
			// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//
			// "算法/模式/补码方式"
			// 根据给定的字节数组构造一个密钥 ：keyBytes字节数组的长度必须=16
			SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
			// 返回实现指定转换的 Cipher 对象
			// 初始化向量参数
			IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
			// 算法/模式/补码方式
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// 用密钥初始化此 Cipher
			cipher.init(Cipher.ENCRYPT_MODE, sks, ivSpec);
			// 加密 res--> byte[]-->16进制字符串
			byte[] resBytes = CHARSET == null ? data.getBytes() : data
					.getBytes(CHARSET);
			return new Base64().encodeToString(cipher.doFinal(resBytes));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密方法【解密前端加密字符串（Base64）】
	 * @param data 要解密的数据
	 * @param key  解密key
	 * @param iv 解密iv
	 * @return 解密的结果
	 * @throws Exception
	 */
	public static String decryptAes(String data, String key, String iv)
			throws Exception {
		try {
			//byte[] res = Base64.decodeBase64(data);
			//使用base64中的UrlEncode和UrlDecode,这个对象可以在编码时就将该字符串中的特殊字符（对于url而言=，+等）转成可使用的，从而在请求过程中不会改变编码的字符串
			byte[] res = Base64.decodeBase64(URLDecoder.decode(data,CHARSET));
			byte[] keyBytes = key.getBytes(CHARSET);
			// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//
			// "算法/模式/补码方式"
			// 根据给定的字节数组构造一个密钥 ：keyBytes字节数组的长度必须=16
			SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
			// 返回实现指定转换的 Cipher 对象
			// 初始化向量参数
			IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));
			// 算法/模式/补码方式
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// 用密钥初始化此 Cipher
			cipher.init(Cipher.DECRYPT_MODE, sks, ivSpec);
			//返回字符串
			return new String(cipher.doFinal(res),CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 使用默认的key和iv加密
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data) throws Exception {
		return encryptAes(data, KEY, IV);
	}

	/**
	 * 使用默认的key和iv解密
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String desEncrypt(String data) throws Exception {
		return decryptAes(data, KEY, IV);
	}

	/**
	* 测试
	*/
	public static void main(String args[]) throws Exception {

		String test = "654321";

		String data = null;
		String key = "aadmin.wyait.com";
		String iv = "abcdef1234567890";

		data = encryptAes(test, key, iv);

		System.out.println(data);
		System.out.println(decryptAes(data, key, iv));
		System.out.println("测试:"+decryptAes("mnMHqBuA2ct0cdTJZ+bFYmsv3pe4b0OnQ/bEbc+gZUs=", "aadmin.wyait.com", "abcdef1234567890"));
	}

}