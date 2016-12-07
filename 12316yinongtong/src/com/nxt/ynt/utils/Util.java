package com.nxt.ynt.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.List;

import com.nxt.ynt.entity.AppConfigData;
import com.nxt.ynt.page.IntentActivityUtil;
import com.nxt.ynt.page.ReadRaw;

import junit.framework.Assert;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;


public class Util {
	public static final String SP_NAME = "config";
	private static SharedPreferences sp;
	public static final String IMEI = "imei";
	// 位置
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String ADDRESS = "address";
	// 判断是否第一次安装判断
	public static final String FIRST_INSTALL = "first";
	// 用户信息
	public static final String UID = "uid";
	public static final String UNAME = "name";
	public static final String PWD = "password";
	public static final String PHONE = "phone";
	public static final String area = "area";
	public static final String work = "hy";
	public static final String workid = "hyid";
	public static final String NICK = "nickname";
	public static final String UPIC = "userpic";
	public static final String SEX = "gender";
	public static final String TOKEN = "tokens";
	public static final String status = "status"; // 激活状态
	public static final String HOMEBUTTON = "HOMEBUTTON";
	public static final String ICON = "icon";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String URL = "url";
	public static final String BUTTONS = "buttons";
	public static final String JYTOKEN = "token";
	public static final String PARAS = "paras";
	// 提示信息
	public static final String ALERTID = "ALERTID";
	// 在线获取联系人标记
	public static final String CONTRACTFLAG = "CONTRACTFLAG";
	// 应用信息
	public static final String CHECK = "check";
	public static final String DOWNLOAD = "download";
	public static final String MAINACTIVITY = "mainactivity";
	public static final String APPNAME = "APPNAME";
	public static final String MENUMSG = "MENUMSG";
	public static final String COLUMNID = "COLUMNID1";// 信息通栏目定制id
	public static final String NOCUSTOM = "NOCUSTOM1";// 信息通栏目未定制
	public static final String CUSTOM = "CUSTOM1";// 信息通栏目定制
	public static final String XXTAREA = "XXTAREA";//信息通联播地区列表
	public static final String TEXTSIZE = "textsize";//webview加载页面字体大小
	public static final String ColumnVer="ColumnVer";//信息通栏目版本

	// 是否上传过全部好友关系的标记
	public static final String ISUPFRIENDS = "isUploadFriends";
	// 是否是开机自启标记
	public static final String ISAUTOSTART = "isAutoStart";
	// openfire密码
	public static final String XMPPPWD = "nxtl4wbnL6rnxt";
	// 数据库版本
	public static final String DB_VER = "db_ver";
	// 是否更新公众号
	public static final String ISUPDATE_GZH = "isupdate_gzh";
	// 是否开启声音提醒
	public static final String ISSOUND = "isSound";
	public static final String YBCASEID = "ybcaseid";
	// 是否停留在聊天界面上
	public static final String ISINMYCHAT = "isInMychat";
	private static final String TAG = "SDK_Sample.Util";
	// 是否打印log
	public static final String IFSYSOLOG = "ifSysoLog";
	// 注销标志
	public static final String LOGOUTFLAG = "logoutFlag";
	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	//是否订阅
	public static final String YN_PAY = "yn_pay";
	//是否登录
	public static final String ISINSTALL="isinstall";

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static byte[] bmpToByteArray1(final Bitmap bmp,
			final boolean needRecycle) {
		int i;
		int j;
		if (bmp.getHeight() > bmp.getWidth()) {
			i = bmp.getWidth();
			j = bmp.getWidth();
		} else {
			i = bmp.getHeight();
			j = bmp.getHeight();
		}
		Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
		Canvas localCanvas = new Canvas(localBitmap);
		while (true) {
			localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i,
					j), null);
			if (needRecycle)
				bmp.recycle();
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					localByteArrayOutputStream);
			localBitmap.recycle();
			byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
			try {
				localByteArrayOutputStream.close();
				return arrayOfByte;
			} catch (Exception e) {
			}
			i = bmp.getHeight();
			j = bmp.getHeight();
		}
	}

	public static byte[] getHtmlByteArray(final String url) {
		URL htmlUrl = null;
		InputStream inStream = null;
		try {
			htmlUrl = new URL(url);
			URLConnection connection = htmlUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = inputStreamToByte(inStream);
		return data;
	}

	public static byte[] inputStreamToByte(InputStream is) {
		try {
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readFromFile(String fileName, int offset, int len) {
		if (fileName == null) {
			return null;
		}
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		if (len == -1) {
			len = (int) file.length();
		}
		if (offset < 0) {
			return null;
		}
		if (len <= 0) {
			return null;
		}
		if (offset + len > (int) file.length()) {
			return null;
		}
		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len]; // 创建合适文件大小的数组
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public static Bitmap extractThumbNail(final String path, final int height,
			final int width, final boolean crop) {
		Assert.assertTrue(path != null && !path.equals("") && height > 0
				&& width > 0);
		BitmapFactory.Options options = new BitmapFactory.Options();
		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY)
					: (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}
			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}
			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				return null;
			}
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth,
					newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}
			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm,
						(bm.getWidth() - width) >> 1,
						(bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}
				bm.recycle();
				bm = cropped;
			}
			return bm;
		} catch (final OutOfMemoryError e) {
			options = null;
		}
		return null;
	}

	public Util(Context ctx) {
		sp = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
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
	
	public Boolean getBooleanFromSp(String tag, Boolean defValue) {
		return sp.getBoolean(tag, defValue);
	}

	public static void toMainActivity(Context context) {
		AppConfigData appConfigData = ReadRaw.getAppConfigData(context);
		// 主activity名称
		String mainactivity = appConfigData.getMainactivity();
		// 版本更新验证接口
		String updateurl = com.nxt.nxtapp.setting.GetHost.getHost()
				+ "/andriod/" + appConfigData.getUpdateurl();
		// 程序名称
		String appname = appConfigData.getAppname();
		// 版本更新下载接口
		String versionplist = com.nxt.nxtapp.setting.GetHost.getHost()
				+ "/andriod/" + appConfigData.getVersionplist();
		IntentActivityUtil.toActivity(context, sp.getString(MAINACTIVITY, ""),
				sp.getString(APPNAME, ""), sp.getString(CHECK, ""),
				sp.getString(DOWNLOAD, ""), sp.getString(HOMEBUTTON, ""));
		// IntentActivityUtil.toActivity(context, mainactivity, appname,
		// updateurl, versionplist);
		((Activity) context).finish();
	}

	/**
	 * 判断程序是在前台还是后台运行
	 */
	public static boolean isBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 返回当前时间
	 */
	public static String getDate() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));
		StringBuffer sbBuffer = new StringBuffer();
		if (mins.length() < 2) {
			sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
					+ "0" + mins);
		} else {
			sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
					+ mins);
		}
		return sbBuffer.toString();
	}

	public static int dip2px(Context paramContext, float paramFloat) {
		return (int) (0.5F + paramFloat
				* paramContext.getResources().getDisplayMetrics().density);
	}

}