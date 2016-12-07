package com.nxt.nxtapp.database;

import com.nxt.nxtapp.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtilSafe {
	public final static int DATABASE_VERSION = 12;// 任何人不得修改这个变量，修改之前联系赵佳丽
	public final static String DATABASE_NAME = "xnb.db";
	public static final String TAG = "DBUtilSafe";
	private Context context;
	private String className;
	private Object bean;
	private static BaseDBHelperSafe helperSafe;
	private static BaseDBHelperNormal helperNormal;
	private static net.sqlcipher.database.SQLiteDatabase dbSafe;
	private static SQLiteDatabase dbNormal;
	private String pd;

	public DBUtilSafe(Context context, String className, Object bean) {
		this.context = context;
		this.className = className;
		this.bean = bean;
		this.pd = context.getResources().getString(R.string.ifDatabaseSee);

		Util util=  new Util(context);
		
		if (pd.equals("no")) {
			util.saveToSp("sql_pd", "no");
			helperSafe = new BaseDBHelperSafe(context, DATABASE_NAME, DATABASE_VERSION, this);
			dbSafe = helperSafe.open();
		} else {
			util.saveToSp("sql_pd", "yes");
			helperNormal = BaseDBHelperNormal.getInstance(context, DATABASE_NAME,
					DATABASE_VERSION, this);
			dbNormal = helperNormal.getWritableDatabase();
		}

	}

	// 执行
	public void execSQL(String sql) {
		if (pd.equals("no")) {
			dbSafe.execSQL(sql);
		} else {
			dbNormal.execSQL(sql);
		}
	}

	// 查询 rawQuery
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		if (pd.equals("no")) {
			return dbSafe.rawQuery(sql, selectionArgs);
		} else {
			LogUtil.LogI(TAG, "rawQuery====不加密");
			return dbNormal.rawQuery(sql, selectionArgs);

		}

	}

	// 查询 query
	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		if (pd.equals("no")) {

			return dbSafe.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);

		} else {

			return dbNormal.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);

		}
	}

	// 增
	public long insert(String arg0, String arg1, ContentValues arg2) {
		if (pd.equals("no")) {

			return dbSafe.insert(arg0, arg1, arg2);

		} else {

			return dbNormal.insert(arg0, arg1, arg2);

		}
	}

	// 删
	public int delete(String arg0, String arg1, String[] arg2) {
		if (pd.equals("no")) {

			return dbSafe.delete(arg0, arg1, arg2);

		} else {

			return dbNormal.delete(arg0, arg1, arg2);
		}
	}

	// 更新
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		if (pd.equals("no")) {

			return dbSafe.update(table, values, whereClause, whereArgs);

		} else {

			return dbNormal.update(table, values, whereClause, whereArgs);
		}
	}

	// close
	public void close() {
		if (pd.equals("no")) {

			dbSafe.close();

		} else {

			dbNormal.close();
		}
	}

	public void onUpgrade(int oldVersion, int newVersion) {
		// if (className.equals("XNBDUtil")) {
		// ((com.nxt.ynt.database.XNBDUtil) bean).onUpgrade(oldVersion,
		// newVersion);
		// }
	}
}