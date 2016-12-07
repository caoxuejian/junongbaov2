package com.nxt.nxtapp.database;

import java.io.File;

import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.Util;

import android.content.ContentValues;
import android.content.Context;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * 数据库公共类，提供基本数据库操作(使用SQLCipher库加密) 继承本类进行重写
 * 
 * @author 李张弛
 * 
 */

public abstract class DBSafe extends SQLiteOpenHelper {
	private static final String TAG = "DBSafe";
	// 创建单例模式的db // 执行open()打开数据库时，保存返回的数据库对象
	public static SQLiteDatabase db = null;
	// 本地Context对象
	private Context mContext = null;
	// 加密的数据库密码
	private String mMima;
	// 查询游标对象
	private Cursor cursor;
	// 数据库名
	private String dbName = null;
	private Util util;

	public DBSafe(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		util = new Util(context);
		mMima = util.getFromSp(Util.PWD, null);
		if (mMima == null) {
			mMima = "nxt!@#$%^";
		}
		this.mContext = context;
		this.dbName = name;
	}

	/**
	 * 打开数据库
	 */
	public SQLiteDatabase open() {
		String logoutflag = util.getFromSp(Util.LOGOUTFLAG, "");
		if ("logout".equals(logoutflag)) {
			util.saveToSp(Util.LOGOUTFLAG, "");
			openDB();
		} else {
			if (db == null) {
				openDB();
			}
		}
		return db;
	}

	public void openDB() {
		// 加载libs/armeabi中的so
		SQLiteDatabase.loadLibs(mContext);
		// 获取到SQLiteOpenHelper
		LogUtil.LogI(TAG, "密码1 = " + mMima);
		try {
			db = getWritableDatabase(mMima);
		} catch (Exception e) {
			// 继续异常处理，如果匹配默认密码都不行就删除！
			try {
				db = getWritableDatabase("nxt!@#$%^");
				LogUtil.LogI(TAG, "匹配默认密码成功！");
			} catch (Exception e1) {
				LogUtil.LogI(TAG, "数据库已存在，且密码不正确，执行删除操作，然后重新创建新的");
				// 创建前先删除旧的数据库
				File dbFile = mContext.getDatabasePath(dbName);
				dbFile.delete();
				db = getWritableDatabase(mMima);
			}
		}
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	/**
	 * 插入数据
	 * 
	 * @param tableName
	 *            表名
	 * @param nullColumn
	 *            null
	 * @param contentValues
	 *            名值对
	 * @return 新插入数据的ID，错误返回-1
	 * @throws Exception
	 */
	public long insert(String tableName, String nullColumn,
			ContentValues contentValues) throws Exception {
		try {
			return db.insert(tableName, nullColumn, contentValues);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 通过主键ID删除数据
	 * 
	 * @param tableName
	 *            表名
	 * @param key
	 *            主键名
	 * @param id
	 *            主键值
	 * @return 受影响的记录数
	 * @throws Exception
	 */
	public long delete(String tableName, String key, int id) throws Exception {
		try {
			return db.delete(tableName, key + " = " + id, null);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查找表的所有数据
	 * 
	 * @param database
	 * 
	 * @param tableName
	 *            表名
	 * @param columns
	 *            如果返回所有列，则填null
	 * @return Cursor
	 * @throws Exception
	 */
	public Cursor findAll(String tableName, String[] columns) {
		LogUtil.LogI(TAG, "db =  " + db.getPageSize());

		cursor = db.query(tableName, columns, null, null, null, null, null);
		cursor.moveToFirst();
		LogUtil.LogI(TAG, "cursor.getCount =  " + cursor.getCount());
		return cursor;
	}

	/**
	 * 根据主键查找数据
	 * 
	 * @param tableName
	 *            表名
	 * @param key
	 *            主键名
	 * @param id
	 *            主键值
	 * @param columns
	 *            如果返回所有列，则填null
	 * @return Cursor游标
	 * @throws Exception
	 */
	public Cursor findById(String tableName, String key, int id,
			String[] columns) throws Exception {
		try {
			return db.query(tableName, columns, key + " = " + id, null, null,
					null, null);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据条件查询数据
	 * 
	 * @param tableName
	 *            表名
	 * @param names
	 *            查询条件
	 * @param values
	 *            查询条件值
	 * @param columns
	 *            如果返回所有列，则填null
	 * @param orderColumn
	 *            排序的列
	 * @param limit
	 *            限制返回数
	 * @return Cursor游标
	 * @throws Exception
	 */
	public Cursor find(String tableName, String[] names, String[] values,
			String[] columns, String orderColumn, String limit)
			throws Exception {
		try {
			StringBuffer selection = new StringBuffer();
			for (int i = 0; i < names.length; i++) {
				selection.append(names[i]);
				selection.append(" = ?");
				if (i != names.length - 1) {
					selection.append(",");
				}
			}
			cursor = db.query(true, tableName, columns, selection.toString(),
					values, null, null, orderColumn, limit);
			cursor.moveToFirst();
			return cursor;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param tableName
	 *            表名
	 * @param names
	 *            查询条件
	 * @param values
	 *            查询条件值
	 * @param args
	 *            更新列-值对
	 * @return true或false
	 * @throws Exception
	 */
	public boolean udpate(String tableName, String[] names, String[] values,
			ContentValues args) throws Exception {
		try {
			StringBuffer selection = new StringBuffer();
			for (int i = 0; i < names.length; i++) {
				selection.append(names[i]);
				selection.append(" = ?");
				if (i != names.length - 1) {
					selection.append(",");
				}
			}
			return db.update(tableName, args, selection.toString(), values) > 0;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 执行sql语句，包括创建表、删除、插入
	 * 
	 * @param sql
	 */
	public void executeSql(String sql) {
		db.execSQL(sql);
	}

}
