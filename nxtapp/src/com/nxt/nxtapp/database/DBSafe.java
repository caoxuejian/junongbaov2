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
 * ���ݿ⹫���࣬�ṩ�������ݿ����(ʹ��SQLCipher�����) �̳б��������д
 * 
 * @author ���ų�
 * 
 */

public abstract class DBSafe extends SQLiteOpenHelper {
	private static final String TAG = "DBSafe";
	// ��������ģʽ��db // ִ��open()�����ݿ�ʱ�����淵�ص����ݿ����
	public static SQLiteDatabase db = null;
	// ����Context����
	private Context mContext = null;
	// ���ܵ����ݿ�����
	private String mMima;
	// ��ѯ�α����
	private Cursor cursor;
	// ���ݿ���
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
	 * �����ݿ�
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
		// ����libs/armeabi�е�so
		SQLiteDatabase.loadLibs(mContext);
		// ��ȡ��SQLiteOpenHelper
		LogUtil.LogI(TAG, "����1 = " + mMima);
		try {
			db = getWritableDatabase(mMima);
		} catch (Exception e) {
			// �����쳣�������ƥ��Ĭ�����붼���о�ɾ����
			try {
				db = getWritableDatabase("nxt!@#$%^");
				LogUtil.LogI(TAG, "ƥ��Ĭ������ɹ���");
			} catch (Exception e1) {
				LogUtil.LogI(TAG, "���ݿ��Ѵ��ڣ������벻��ȷ��ִ��ɾ��������Ȼ�����´����µ�");
				// ����ǰ��ɾ���ɵ����ݿ�
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
	 * ��������
	 * 
	 * @param tableName
	 *            ����
	 * @param nullColumn
	 *            null
	 * @param contentValues
	 *            ��ֵ��
	 * @return �²������ݵ�ID�����󷵻�-1
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
	 * ͨ������IDɾ������
	 * 
	 * @param tableName
	 *            ����
	 * @param key
	 *            ������
	 * @param id
	 *            ����ֵ
	 * @return ��Ӱ��ļ�¼��
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
	 * ���ұ����������
	 * 
	 * @param database
	 * 
	 * @param tableName
	 *            ����
	 * @param columns
	 *            ������������У�����null
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
	 * ����������������
	 * 
	 * @param tableName
	 *            ����
	 * @param key
	 *            ������
	 * @param id
	 *            ����ֵ
	 * @param columns
	 *            ������������У�����null
	 * @return Cursor�α�
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
	 * ����������ѯ����
	 * 
	 * @param tableName
	 *            ����
	 * @param names
	 *            ��ѯ����
	 * @param values
	 *            ��ѯ����ֵ
	 * @param columns
	 *            ������������У�����null
	 * @param orderColumn
	 *            �������
	 * @param limit
	 *            ���Ʒ�����
	 * @return Cursor�α�
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
	 *            ����
	 * @param names
	 *            ��ѯ����
	 * @param values
	 *            ��ѯ����ֵ
	 * @param args
	 *            ������-ֵ��
	 * @return true��false
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
	 * ִ��sql��䣬����������ɾ��������
	 * 
	 * @param sql
	 */
	public void executeSql(String sql) {
		db.execSQL(sql);
	}

}
