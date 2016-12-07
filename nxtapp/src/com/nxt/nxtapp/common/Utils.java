package com.nxt.nxtapp.common;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 常用方法工具类
 * 
 * @author 王情情
 * @version V1.0
 * 
 */
public class Utils {
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	/**
	 * 判断网络是否可用
	 * 
	 * @param Activity
	 * @return boolean
	 */
	public static boolean detect(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public static int getNetworkType(Activity act) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) act
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param String
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input)) return true;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否由数字组成
	 * 
	 * @param String
	 * @return boolean
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 返回当前运行程序的包名
	 */
	public static String getPackageName(Context ctx) {
		String packageNames = null;
		try {
			PackageInfo info = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			packageNames = info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageNames;
	}
	
	/**
	 * 返回当前运行程序的版本号
	 */
	public static int getPackageVersion(Context ctx) {
		int packageNames = 0;
		try {
			PackageInfo info = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			packageNames = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageNames;
	}
}
