package com.nxt.nxtapp.common;

import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.nxt.nxtapp.common.base64.Base64;

/**
 * Java版3DES加密解密，适用于PHP版3DES加密解密(PHP语言开发的MCRYPT_3DES算法、MCRYPT_MODE_ECB模式、
 * PKCS7填充方式)
 * 
 * @author G007N
 */
public class AESSafe {
	private static SecretKey secretKey = null;// key对象
	private static Cipher cipher = null; // 私鈅加密对象Cipher

	/* 密钥为16的倍数 */
	public static String keyString = null;// 密钥
	private static Logger log = Logger.getAnonymousLogger();

	// 使用时候先设置key的值，再执行first
	public static void first() {
		try {
			secretKey = new SecretKeySpec(keyString.getBytes(), "AES");// 获得密钥
			/* 获得一个私鈅加密类Cipher，DESede是算法，ECB是加密模式，PKCS5Padding是填充方式 */
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		} catch (Exception e) {
			LogUtil.LogI("AESSafe", "异常：" + e.getMessage());
		}
	}

	/**
	 * 加密
	 * 
	 * @param message
	 * @return
	 */
	public static String desEncrypt(String message) {
		String result = ""; // DES加密字符串
		String newResult = "";// 去掉换行符后的加密字符串
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey); // 设置工作模式为加密模式，给出密钥
			byte[] resultBytes = cipher.doFinal(message.getBytes("UTF-8")); // 正式执行加密操作

			result = new String(Base64.encode(resultBytes));// 进行BASE64编码
			newResult = filter(result); // 去掉加密串中的换行符
		} catch (Exception e) {
			LogUtil.LogI("AESSafe", "异常：" + e.getMessage());
		}
		return newResult;
	}

	/**
	 * 解密
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String desDecrypt(String message) throws Exception {
		String result = "";
		try {

			byte[] messageBytes = Base64.decode(message.getBytes()); // 进行BASE64编码
			cipher.init(Cipher.DECRYPT_MODE, secretKey); // 设置工作模式为解密模式，给出密钥
			byte[] resultBytes = cipher.doFinal(messageBytes);// 正式执行解密操作
			result = new String(resultBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 去掉加密字符串换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String filter(String str) {
		String output = "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int asc = str.charAt(i);
			if (asc != 10 && asc != 13) {
				sb.append(str.subSequence(i, i + 1));
			}
		}
		output = new String(sb);
		return output;
	}
}