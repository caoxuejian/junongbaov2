package com.nxt.ynt.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.nxt.jnb.R;
import com.nxt.ynt.entity.DiQu;

/**
 * 天气数据库操作类
 * 
 * @author 王超
 */
public class DBUtils {
	private static final String TAG = "DBUtils";
	private Handler handler;
	// public static final String DATABASE_PATH =
	// "/data/data/com.nxt.ynt/databases/ynt/";
	// public static final String SP_PATH =
	// "/data/data/com.nxt.ynt/shared_prefs/";
	// private static final String DATABASE_PATH = Environment
	// .getExternalStorageDirectory().getAbsolutePath() + "/nqt/";

	// private static final int DATABASE_VERSION = 0;

	private String database_path, sp_path, versionCode;
	private static final String NQT_NAME = "nqt.sqlite";
	private static final String WORK_NAME = "work.sqlite";

	// private static String outFileName = DATABASE_PATH + "/" + DATABASE_NAME;

	private static SQLiteDatabase database;

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public DBUtils(Context context) {
		database_path = "/data/data/"
				+ com.nxt.nxtapp.common.Util.getPackageName(context)
				+ "/databases/weather/";
		sp_path = "/data/data/"
				+ com.nxt.nxtapp.common.Util.getPackageName(context)
				+ "/shared_prefs/";
		try {
			// 获取程序版本号
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = String.valueOf(pi.versionCode);

			File dir = new File(database_path + versionCode);
			File sp_dir = new File(sp_path);
			// 如果/sdcard/nqt目录不存在，创建这个目录
			if (!dir.exists())
				dir.mkdirs();
			if (!sp_dir.exists()) {
				sp_dir.mkdirs();
			}
			// 如果在/sdcard/nqt目录中不存在nqtweather.db文件，则从res/raw目录中复制这个文件到
			// SD卡的目录（/sdcard/nqt）
			/*
			 * if (!(new File(DATABASE_PATH + versionCode + "/" +
			 * DATABASE_NAME)) .exists()) { // 获得封装nqtweather.db文件的InputStream对象
			 * InputStream is = context.getResources().openRawResource(
			 * R.raw.nqt); FileOutputStream fos = new
			 * FileOutputStream(DATABASE_PATH + versionCode + "/" +
			 * DATABASE_NAME); byte[] buffer = new byte[2048]; int count = -1;
			 * // 开始复制nqtweather.db文件 while ((count = is.read(buffer)) != -1) {
			 * fos.write(buffer, 0, count); } fos.flush(); fos.close();
			 * is.close(); }
			 */
			copyDB(NQT_NAME, context, R.raw.nqt);

			for (int i = 0; i < 8; i++) {
				if (!(new File(sp_path + i + ".xml")).exists()) {
					AssetManager am = context.getAssets();
					InputStream is = am.open(i + ".xml");
					FileOutputStream fos = new FileOutputStream(sp_path + i
							+ ".xml");
					byte[] buffer = new byte[2048];
					int count = -1;
					// 开始复制配置文档
					while ((count = is.read(buffer)) != -1) {
						fos.write(buffer, 0, count);
					}
					fos.flush();
					fos.close();
					is.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询天气情况的方法
	 */
	public Map<String, String> queryWeather(String zkind, String zwid) {
		Map<String, String> map = new HashMap<String, String>();
		String ztitle = null;
		String zdetail = null;
		String zsuoxie = null;
		String zname = null;
		database = SQLiteDatabase.openOrCreateDatabase(database_path
				+ versionCode + "/" + NQT_NAME, null);
		String sql = "select * from ZWEATHER where ZKIND='" + zkind + "'"
				+ " AND ZWID='" + zwid + "'";
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				ztitle = cursor.getString(cursor.getColumnIndex("ZTITLE"));
				zdetail = cursor.getString(cursor.getColumnIndex("ZDETAIL"));
				zsuoxie = cursor.getString(cursor.getColumnIndex("ZSUOXIE"));
				zname = cursor.getString(cursor.getColumnIndex("ZNAME"));
			}
		}
		map.put("ztitle", ztitle);
		map.put("zdetail", zdetail);
		map.put("zsuoxie", zsuoxie);
		map.put("zname", zname);
		cursor.close();
		database.close();
		return map;
	}

	/**
	 * 初始化省份
	 */
	public void initProvince() {
		new Thread() {
			public void run() {

				List<DiQu> list = new ArrayList<DiQu>();
				DiQu diqu;
				database = SQLiteDatabase.openOrCreateDatabase(database_path
						+ versionCode + "/" + NQT_NAME, null);
				String sql = "SELECT ZAREAID,ZAREANAME FROM ZWEATHERAREA WHERE ZPID='0' ORDER BY py";
				Cursor cursor = database.rawQuery(sql, null);
				while (cursor.moveToNext()) {
//					LogUtil.LogI(TAG, cursor.getString(1));
					if(!cursor.getString(1).equals("全国")){
						diqu = new DiQu(cursor.getString(0), cursor.getString(1));
						list.add(diqu);
					}
				}
				Message msg = new Message();
				msg.what = 0;
				msg.obj = list;
				handler.sendMessage(msg);

				cursor.close();
				database.close();

			};
		}.start();
	}

	/**
	 * 初始化城市
	 */
	public void initCities(final String shengId) {
		new Thread() {
			public void run() {
				List<DiQu> list = new ArrayList<DiQu>();
				DiQu diqu;
				database = SQLiteDatabase.openOrCreateDatabase(database_path
						+ versionCode + "/" + NQT_NAME, null);
				String sql = "SELECT ZAREAID,ZAREANAME FROM ZWEATHERAREA WHERE ZPID='"
						+ shengId + "'"+"ORDER BY py";
				Cursor cursor = database.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					diqu = new DiQu(cursor.getString(0), cursor.getString(1));
					list.add(diqu);
				}
				Message msg = new Message();
				msg.what = 1;
				msg.obj = list;
				handler.sendMessage(msg);

				cursor.close();
				database.close();
			};
		}.start();
	}

	/**
	 * 初始化-区
	 */
	public void initDistricts(final String shiId) {
		new Thread() {
			public void run() {
				List<DiQu> list = new ArrayList<DiQu>();
				DiQu diqu;
				database = SQLiteDatabase.openOrCreateDatabase(database_path
						+ versionCode + "/" + NQT_NAME, null);
				String sql = "SELECT ZAREAID,ZAREANAME FROM ZWEATHERAREA WHERE ZPID='"
						+ shiId + "'"+"ORDER BY py";
				Cursor cursor = database.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					diqu = new DiQu(cursor.getString(0), cursor.getString(1));
					list.add(diqu);
				}
				Message msg = new Message();
				msg.what = 2;
				msg.obj = list;
				handler.sendMessage(msg);

				cursor.close();
				database.close();
			};
		}.start();
	}

	/**
	 * 查询地区的方法
	 */
	public List<String> queryArea(String zkind) {
		List<String> list = new ArrayList<String>();
		String ztitle = null;
		database = SQLiteDatabase.openOrCreateDatabase(database_path
				+ versionCode + "/" + NQT_NAME, null);
		String sql = "select " + zkind + " from ZWEATHERAREA";
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				ztitle = cursor.getString(cursor.getColumnIndex(zkind));
				list.add(ztitle);
			}
		}
		cursor.close();
		database.close();
		return list;
	}

	/**
	 * 查询行业的方法
	 */
	public List<String> queryWork(String zkind) {
		List<String> list = new ArrayList<String>();
		String ztitle = null;
		database = SQLiteDatabase.openOrCreateDatabase(database_path
				+ versionCode + "/" + NQT_NAME, null);
		String sql = "select " + zkind + " from ZPRODUCTKIND";
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				ztitle = cursor.getString(cursor.getColumnIndex(zkind));
				list.add(ztitle);
			}
		}
		cursor.close();
		database.close();
		return list;
	}

	/**
	 * 初始化地区
	 */
	public void initJi(final int ji, final String zid) {

		new Thread() {
			public void run() {

				List<DiQu> list = new ArrayList<DiQu>();
				DiQu diqu;

				database = SQLiteDatabase.openOrCreateDatabase(database_path
						+ versionCode + "/" + NQT_NAME, null);

				String sql = null;
				if (ji == 1) {
					sql = "SELECT ZID,ZKINDNAME FROM ZPRODUCTKIND WHERE ZPARENTID='0'";
				} else {
					sql = "SELECT ZID,ZKINDNAME FROM ZPRODUCTKIND WHERE ZPARENTID='"
							+ zid + "'";
				}
				
				Cursor cursor = database.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					diqu = new DiQu(cursor.getString(0), cursor.getString(1));
					list.add(diqu);
				}
				Message msg = new Message();
				msg.what = 0;
				msg.obj = list;
				handler.sendMessage(msg);

				cursor.close();
				database.close();
			};
		}.start();

	}

	/**
	 * 根据缩写和zwid查各项指数 根据区县和市查weathercity
	 */
	public String getWeatherCity(String zareaname, String zpname) {
		String weathercity = null;
		database = SQLiteDatabase.openOrCreateDatabase(database_path
				+ versionCode + "/" + NQT_NAME, null);
		String sql = "select * from ZWEATHERAREA where ZAREANAME='" + zareaname
				+ "'" + " AND ZPNAME='" + zpname + "'";
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				weathercity = cursor.getString(cursor
						.getColumnIndex("ZWEATHERCITY"));
			}
		}
		cursor.close();
		database.close();
		return weathercity;
	}

	/**
	 * 把查询的结果用字符串返回
	 */
	public String weatherStr(String zkind, String zwid, String column) {
		Map<String, String> map = queryWeather(zkind, zwid);
		if ("null".equals(map.get(column))||map.get(column)==null) {
			map.put(column, "霾");
		}
		return map.get(column);
	}

	/**
	 * 复制数据库
	 */
	public void copyDB(String dbname, Context context, int id) {
		if (!(new File(database_path + versionCode + "/" + dbname)).exists()) {
			try {
				// 获得封装nqtweather.db文件的InputStream对象
				InputStream is = context.getResources().openRawResource(id);
				FileOutputStream fos = new FileOutputStream(database_path
						+ versionCode + "/" + dbname);
				byte[] buffer = new byte[2048];
				int count = -1;
				// 开始复制nqtweather.db文件
				while ((count = is.read(buffer)) != -1) {
					fos.write(buffer, 0, count);
				}
				fos.flush();
				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
