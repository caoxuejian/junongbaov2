package com.nxt.nxtapp.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.widget.Toast;

/**
 * @author Wang Tingting 轻量级数据存储工具类 2014-5-9下午1:44:27
 */
public class Util {
	public static final String SP_NAME = "config";
	private static SharedPreferences sp;
	// 位置
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String ADDRESS = "address";
	// 用户信息
	public static final String UID = "uid";
	public static final String PHONE = "phone";
	public static final String PWD = "password";
	public static final String VERSION = "VERSION";
	public static final String ALERTID = "ALERTID";
	public static final String TOKEN = "tokens";
	public static final String DEVICEID = "imei";
	public static final String USERNAME = "name";
	public static final String APPNAME = "APPNAME";
	public static final String USERPIC = "userpic";
	private static Util util;
	public static final String NICKNAME = "nickname";
	public static final String GENDER = "gender";
	public static final String area = "area";
	public static final String work = "hy";
	// 数据库版本
	public static final String DB_VER = "db_ver";
	// 注销标志
	public static final String LOGOUTFLAG = "logoutFlag";

	public Util(Context ctx) {
		sp = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	public static Util getUtil(Context ctx) {
		if (util == null)
			util = new Util(ctx);
		return util;
	}

	/**
	 * 显示toast信息
	 */
	public static void showMsg(Context context, String msg) {
		try {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存到配置文件
	 */
	public void saveToSp(String tag, String value) {
		Editor edit = sp.edit();
		edit.putString(tag, value);
		edit.commit();
	}

	public void saveToSp(String tag, int value) {
		Editor edit = sp.edit();
		edit.putInt(tag, value);
		edit.commit();
	}
	
	public void saveBooleanToSp(String tag, Boolean value) {
		Editor edit = sp.edit();
		edit.putBoolean(tag, value);
		edit.commit();
	}

	/**
	 * 从配置文件中读取
	 */
	public String getFromSp(String tag, String defValue) {
		return sp.getString(tag, defValue);
	}

	public int getFromSp(String tag, int defValue) {
		return sp.getInt(tag, defValue);
	}

	/**
	 * 单键值存储移除
	 */
	public void removeFromSp(String tag) {
		Editor editor = sp.edit();
		editor.remove(tag);
		editor.commit();
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

	/**
	 * 获取验证码
	 * */
	public static byte[] getData(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int index = -1;
		byte[] buffer = new byte[128];
		try {
			while ((index = is.read(buffer)) != -1) {
				baos.write(buffer, 0, index);
			}
			baos.flush();
			byte[] data = baos.toByteArray();
			// writeFile(data,null);
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
