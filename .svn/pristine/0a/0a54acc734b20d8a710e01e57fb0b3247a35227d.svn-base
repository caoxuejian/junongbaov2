//package com.nxt.ynt.database;
//
//import java.util.List;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import com.nxt.nxtapp.common.LogUtil;
//import com.nxt.nxtapp.json.JsonPaser;
//import com.nxt.ynt.R;
//import com.nxt.ynt.entity.NewsRoot;
//import com.nxt.ynt.entity.XNBmsg;
//import com.nxt.ynt.page.ReadRaw;
//import com.nxt.ynt.utils.Util;
//
//public class DBUtilSafe {
//	private static final String TAG = "DBUtilSafe";
//	private Context context;
//	private String className;
//	private static BaseDBHelperSafe helperSafe;
//	private static BaseDBHelperNormal helperNormal;
//	private static net.sqlcipher.database.SQLiteDatabase dbSafe;
//	private static SQLiteDatabase dbNormal;
//	private String pd;
//	private Util util;
//	private static String TABLE_NAME;
//	public final static String MSGID = "xnbid";
//	public final static String UID = "UID";
//	public final static String XNB_NAME = "name";
//	public final static String LASTMSG = "lastmsg";
//	public final static String LASTTIME = "lasttime";
//	public final static String MSGNUM = "msgnum";
//	public final static String TYPE = "type";
//	public final static String PARAM = "param";
//	public final static String UPIC = "upic";
//	public final static String ID = "id";
//	public final static String PROFILE = "profile";
//	public final static String ver = "ver";// 数据库版本号
//	public final static String isdefault = "isdefault";// 判断是否删除数据的标志
//	private static String CONTRACT_TABLE_NAME;
//	public final static String cid = "cid";// id
//	public final static String name = "name";// 用户名
//	public final static String phone = "phone";// 电话
//	public final static String address = "address";// 地区
//	public final static String nick = "nick";// 昵称
//	public final static String upic = "upic";// 头像url
//	public final static String reserve1 = "reserve1";// 保留字段:行业
//	public final static String reserve2 = "reserve2";// 保留字段:备注
//	public final static String dyflag = "dyflag";// 加薪
//	public final static String sex = "sex";
//	public final static String status = "status";// 状态是否有效
//	private static String PHONE_NAME;// 手机联系人表
//	private static String PNumber = "phone";
//	private static String PName = "name";
//	private static String PState = "state";
//	private static String PLastTime = "lastTime";
//	private static String BANGDING_NAME; // 绑定模块表
//	private static String MoKuaiName = "MoKuaiName";// 模块名
//	private static String token = "token";// 标识
//	private static String paras = "paras";// 绑定参数
//	private static String ZhangHao = "ZhangHao";// 模块名
//	private static String password = "password";// 密码
//	private static String ziduan2 = "ziduan2";// 标识
//	private static String ziduan3 = "ziduan3";// 标识
//
//	public DBUtilSafe(Context context, String dbName, int dbVersion,
//			String className, Object bean) {
//		com.nxt.nxtapp.common.LogUtil.LogE(TAG, "DBUtilSafe");
//		this.context = context;
//		util = new Util(context);
//		this.className = className;
//		this.pd = context.getResources().getString(R.string.ifDatabaseSee);
//		if (pd.equals("no")) {
//			util.saveToSp("sql_pd", "no");
//			helperSafe = new BaseDBHelperSafe(context, dbName, dbVersion, this);
//			dbSafe = helperSafe.open();
//		} else {
//			util.saveToSp("sql_pd", "yes");
//			com.nxt.nxtapp.common.LogUtil.LogE(TAG, "dbNormal");
//			helperNormal = BaseDBHelperNormal.getInstance(context, dbName,
//					dbVersion, this);
//			com.nxt.nxtapp.common.LogUtil.LogE(TAG, (helperNormal == null)
//					+ "   helperNormal");
//			dbNormal = helperNormal.getWritableDatabase();
//			com.nxt.nxtapp.common.LogUtil.LogE(TAG, (dbNormal == null)
//					+ "  dbNormal");
//		}
//	}
//
//	// 执行
//	public void execSQL(String sql) {
//		if (pd.equals("no")) {
//			dbSafe.execSQL(sql);
//		} else {
//			dbNormal.execSQL(sql);
//		}
//	}
//
//	// 查询 rawQuery
//	public Cursor rawQuery(String sql, String[] selectionArgs) {
//		if (pd.equals("no")) {
//			return dbSafe.rawQuery(sql, selectionArgs);
//		} else {
//			LogUtil.LogI(TAG, "rawQuery====不加密");
//			return dbNormal.rawQuery(sql, selectionArgs);
//		}
//	}
//
//	// 查询 query
//	public Cursor query(String table, String[] columns, String selection,
//			String[] selectionArgs, String groupBy, String having,
//			String orderBy) {
//		if (pd.equals("no")) {
//			return dbSafe.query(table, columns, selection, selectionArgs,
//					groupBy, having, orderBy);
//		} else {
//			return dbNormal.query(table, columns, selection, selectionArgs,
//					groupBy, having, orderBy);
//		}
//	}
//
//	// 增
//	public long insert(String arg0, String arg1, ContentValues arg2) {
//		if (pd.equals("no")) {
//			return dbSafe.insert(arg0, arg1, arg2);
//		} else {
//			return dbNormal.insert(arg0, arg1, arg2);
//		}
//	}
//
//	// 删
//	public int delete(String arg0, String arg1, String[] arg2) {
//		if (pd.equals("no")) {
//			return dbSafe.delete(arg0, arg1, arg2);
//		} else {
//			return dbNormal.delete(arg0, arg1, arg2);
//		}
//	}
//
//	// 更新
//	public int update(String table, ContentValues values, String whereClause,
//			String[] whereArgs) {
//		if (pd.equals("no")) {
//			return dbSafe.update(table, values, whereClause, whereArgs);
//		} else {
//			return dbNormal.update(table, values, whereClause, whereArgs);
//		}
//	}
//
//	// close
//	public void close() {
//		if (pd.equals("no")) {
//			dbSafe.close();
//		} else {
//			dbNormal.close();
//		}
//	}
//
//	// 数据库升级
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		util = new Util(context);
//		String uname = util.getFromSp(Util.UNAME, null);
//		TABLE_NAME = "xnbmsgtable" + uname;
//		CONTRACT_TABLE_NAME = "XNBCONTARCT" + uname;
//		PHONE_NAME = "XNBPHONE" + uname;
//		BANGDING_NAME = "BANGDING_NAME" + uname;
//		if (className.equals("XNBDUtil")) {
//			com.nxt.nxtapp.common.LogUtil.LogE(TAG, "onUpgrade" + "  "
//					+ oldVersion + "  " + newVersion);
//			if (oldVersion < 10) {
//				LogUtil.LogE(TAG, "数据库升级，旧版本小于10");
//				String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
//				String sql_contract = "DROP TABLE IF EXISTS "
//						+ CONTRACT_TABLE_NAME;
//				String sql_phone = "DROP TABLE IF EXISTS " + PHONE_NAME;
//				db.execSQL(sql);
//				db.execSQL(sql_contract);
//				db.execSQL(sql_phone);
//				toCreate(db);
//				insertInitDatas(db);
//			} else if (oldVersion == 10 && newVersion == 11) {
//				LogUtil.LogE(TAG, "数据库升级，版本从10升级到11");
//				String sql = "ALTER TABLE " + TABLE_NAME
//						+ " ADD ver integer DEFAULT 0";
//				String sql_add = "ALTER TABLE " + TABLE_NAME
//						+ " ADD isdefault integer DEFAULT 0";
//				db.execSQL(sql);
//				db.execSQL(sql_add);
//				util.saveToSp(Util.DB_VER, 0);
//			} else if (oldVersion == 11 && newVersion == 12) {
//				LogUtil.LogE(TAG, "数据库升级，版本从11升级到12");
//				// 需要绑定模块的数据表
//				String sql_bangding = "create table " + BANGDING_NAME + " ("
//						+ ID + " INTEGER primary key AUTOINCREMENT, "
//						+ MoKuaiName + " varchar(20), " + paras
//						+ " varchar(500), " + token + " varchar(50),"
//						+ ZhangHao + " varchar(50), " + password
//						+ " varchar(50)," + ziduan2 + " varchar(50), "
//						+ ziduan3 + " varchar(50)" + ");";
//				db.execSQL(sql_bangding);
//			} else if (oldVersion == 12 && newVersion == 13) {
//
//			}
//		} else if (className.equals("DbHelper")) {
//
//		}
//	}
//
//	public void onUpgrade(net.sqlcipher.database.SQLiteDatabase db,
//			int oldVersion, int newVersion) {
//		util = new Util(context);
//		String uname = util.getFromSp(Util.UNAME, null);
//		TABLE_NAME = "xnbmsgtable" + uname;
//		CONTRACT_TABLE_NAME = "XNBCONTARCT" + uname;
//		PHONE_NAME = "XNBPHONE" + uname;
//		BANGDING_NAME = "BANGDING_NAME" + uname;
//		if (className.equals("XNBDUtil")) {
//			com.nxt.nxtapp.common.LogUtil.LogE(TAG, "onUpgrade" + "  "
//					+ oldVersion + "  " + newVersion);
//			if (oldVersion < 10) {
//				LogUtil.LogE(TAG, "数据库升级，旧版本小于10");
//				String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
//				String sql_contract = "DROP TABLE IF EXISTS "
//						+ CONTRACT_TABLE_NAME;
//				String sql_phone = "DROP TABLE IF EXISTS " + PHONE_NAME;
//				db.execSQL(sql);
//				db.execSQL(sql_contract);
//				db.execSQL(sql_phone);
//				toCreate(db);
//				insertInitDatas(db);
//			} else if (oldVersion == 10 && newVersion == 11) {
//				LogUtil.LogE(TAG, "数据库升级，版本从10升级到11");
//				String sql = "ALTER TABLE " + TABLE_NAME
//						+ " ADD ver integer DEFAULT 0";
//				String sql_add = "ALTER TABLE " + TABLE_NAME
//						+ " ADD isdefault integer DEFAULT 0";
//				db.execSQL(sql);
//				db.execSQL(sql_add);
//				util.saveToSp(Util.DB_VER, 0);
//			} else if (oldVersion == 11 && newVersion == 12) {
//				LogUtil.LogE(TAG, "数据库升级，版本从11升级到12");
//				// 需要绑定模块的数据表
//				String sql_bangding = "create table " + BANGDING_NAME + " ("
//						+ ID + " INTEGER primary key AUTOINCREMENT, "
//						+ MoKuaiName + " varchar(20), " + paras
//						+ " varchar(500), " + token + " varchar(50),"
//						+ ZhangHao + " varchar(50), " + password
//						+ " varchar(50)," + ziduan2 + " varchar(50), "
//						+ ziduan3 + " varchar(50)" + ");";
//				db.execSQL(sql_bangding);
//			} else if (oldVersion == 12 && newVersion == 13) {
//
//			}
//		} else if (className.equals("DbHelper")) {
//
//		}
//	}
//
//	private void insertInitDatas(SQLiteDatabase dbNormal2) {
//		LogUtil.LogE(TAG, "重新插入数据");
//		String jsonstr = ReadRaw.getRawJson(context, ReadRaw.DATABASEINIT);
//		NewsRoot rootdata = (NewsRoot) JsonPaser.getObjectDatas(NewsRoot.class,
//				jsonstr);
//		List<XNBmsg> list = JsonPaser.getArrayDatas(XNBmsg.class,
//				rootdata.getNews());
//		for (XNBmsg msg : list) {
//			boolean b = insertXNBMSG(msg, dbNormal2,
//					Integer.parseInt(rootdata.getVer()));
//			if (msg.getType().equals("user")) {
//				Contracts c = new Contracts();
//				c.setUid(msg.getUid());
//				c.setUname(msg.getUname());
//				c.setNick(msg.getUname());
//				c.setUpic(msg.getUpic());
//				c.setArea(msg.getAddress());
//				insertContracts(c, dbNormal2);
//			}
//		}
//	}
//
//	private void insertInitDatas(net.sqlcipher.database.SQLiteDatabase db) {
//		LogUtil.LogE(TAG, "重新插入数据");
//		String jsonstr = ReadRaw.getRawJson(context, ReadRaw.DATABASEINIT);
//		NewsRoot rootdata = (NewsRoot) JsonPaser.getObjectDatas(NewsRoot.class,
//				jsonstr);
//		List<XNBmsg> list = JsonPaser.getArrayDatas(XNBmsg.class,
//				rootdata.getNews());
//		for (XNBmsg msg : list) {
//			boolean b = insertXNBMSG(msg, db,
//					Integer.parseInt(rootdata.getVer()));
//			if (msg.getType().equals("user")) {
//				Contracts c = new Contracts();
//				c.setUid(msg.getUid());
//				c.setUname(msg.getUname());
//				c.setNick(msg.getUname());
//				c.setUpic(msg.getUpic());
//				c.setArea(msg.getAddress());
//				insertContracts(c, db);
//			}
//		}
//	}
//
//	// 添加联系人
//	public void insertContracts(Contracts contract, SQLiteDatabase dbNormal2) {
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put(cid, contract.getUid());
//			cv.put(address, contract.getArea());
//			cv.put(name, contract.getUname());
//			cv.put(nick, contract.getNick());
//			cv.put(phone, contract.getPhone());
//			cv.put(reserve1, contract.getReserve1());
//			cv.put(reserve2, contract.getReserve2());
//			cv.put(upic, contract.getUpic());
//			cv.put(dyflag, contract.getDyflag());
//			cv.put(sex, contract.getSex());
//			cv.put(status, contract.getStatus());
//			dbNormal2.insert(CONTRACT_TABLE_NAME, null, cv);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 添加联系人
//	public void insertContracts(Contracts contract,
//			net.sqlcipher.database.SQLiteDatabase dbNormal2) {
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put(cid, contract.getUid());
//			cv.put(address, contract.getArea());
//			cv.put(name, contract.getUname());
//			cv.put(nick, contract.getNick());
//			cv.put(phone, contract.getPhone());
//			cv.put(reserve1, contract.getReserve1());
//			cv.put(reserve2, contract.getReserve2());
//			cv.put(upic, contract.getUpic());
//			cv.put(dyflag, contract.getDyflag());
//			cv.put(sex, contract.getSex());
//			cv.put(status, contract.getStatus());
//			dbNormal2.insert(CONTRACT_TABLE_NAME, null, cv);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 增加操作
//	public boolean insertXNBMSG(XNBmsg msg, SQLiteDatabase dbNormal2,
//			int version) {
//		boolean result = true;
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put(UID, msg.getUid());
//			cv.put(XNB_NAME, msg.getUname());
//			cv.put(LASTTIME, msg.getLasttime());
//			cv.put(LASTMSG, msg.getLastmsg());
//			cv.put(MSGID, msg.getId());
//			cv.put(TYPE, msg.getType());
//			cv.put(PARAM, msg.getParam());
//			cv.put(UPIC, msg.getUpic());
//			cv.put(MSGNUM, msg.getMsgnum());
//			cv.put(dyflag, msg.getDyflag());
//			cv.put(PROFILE, msg.getProfile());
//			cv.put(nick, msg.getNick());
//			cv.put(ver, version);
//			cv.put(isdefault, 1);
//			long i = dbNormal2.insert(TABLE_NAME, null, cv);
//			util.saveToSp(Util.DB_VER, version);
//			com.nxt.nxtapp.common.LogUtil.LogE(TAG, "插入" + i);
//		} catch (Exception e) {
//			e.printStackTrace();
//			result = false;
//		} finally {
//
//		}
//		return result;
//	}
//
//	// 增加操作
//	public boolean insertXNBMSG(XNBmsg msg,
//			net.sqlcipher.database.SQLiteDatabase dbNormal2, int version) {
//		boolean result = true;
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put(UID, msg.getUid());
//			cv.put(XNB_NAME, msg.getUname());
//			cv.put(LASTTIME, msg.getLasttime());
//			cv.put(LASTMSG, msg.getLastmsg());
//			cv.put(MSGID, msg.getId());
//			cv.put(TYPE, msg.getType());
//			cv.put(PARAM, msg.getParam());
//			cv.put(UPIC, msg.getUpic());
//			cv.put(MSGNUM, msg.getMsgnum());
//			cv.put(dyflag, msg.getDyflag());
//			cv.put(PROFILE, msg.getProfile());
//			cv.put(nick, msg.getNick());
//			cv.put(ver, version);
//			cv.put(isdefault, 1);
//			long i = dbNormal2.insert(TABLE_NAME, null, cv);
//			util.saveToSp(Util.DB_VER, version);
//			com.nxt.nxtapp.common.LogUtil.LogE(TAG, "插入" + i);
//		} catch (Exception e) {
//			e.printStackTrace();
//			result = false;
//		} finally {
//
//		}
//		return result;
//	}
//
//	// 创建table
//	public void toCreate(SQLiteDatabase db) {
//		com.nxt.nxtapp.common.LogUtil.LogD(TAG, "onCreate");
//		String sql = "CREATE TABLE " + TABLE_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + XNB_NAME
//				+ " varchar(50) UNIQUE, " + MSGID + " varchar(20)," + LASTTIME
//				+ " varchar(20), " + LASTMSG + " text, " + MSGNUM
//				+ " varchar(10)," + TYPE + " varchar(20), " + PARAM + " text,"
//				+ UPIC + " text," + PROFILE + " text," + dyflag
//				+ " varchar(10)," + nick + " varchar(50)," + UID
//				+ " varchar(50)," + ver + " integer," + isdefault + " integer"
//				+ ");";
//		String sql_contract = "create table " + CONTRACT_TABLE_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + name
//				+ " varchar(50) UNIQUE, " + cid + " varchar(50)," + phone
//				+ " varchar(20), " + address + " text, " + nick
//				+ " varchar(50)," + reserve1 + " TEXT, " + reserve2 + " text,"
//				+ upic + " text," + dyflag + " varchar(10)," + status
//				+ " varchar(10)," + sex + " varchar(10)" + ");";
//		// 手机联系人数据表
//		String sql_phone = "create table " + PHONE_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + PNumber
//				+ " varchar(20), " + PName + " varchar(50), " + PState
//				+ " varchar(50)," + PLastTime + " varchar(50)" + ");";
//		String sql_bangding = "create table " + BANGDING_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + MoKuaiName
//				+ " varchar(20), " + paras + " varchar(500), " + token
//				+ " varchar(50)," + ZhangHao + " varchar(50), " + password
//				+ " varchar(50)," + ziduan2 + " varchar(50), " + ziduan3
//				+ " varchar(50)" + ");";
//		db.execSQL(sql_bangding);
//		com.nxt.nxtapp.common.LogUtil.LogE(TAG, "sql:" + sql
//				+ " \nsql_contract:" + sql_contract);
//		db.execSQL(sql);
//		db.execSQL(sql_contract);
//		db.execSQL(sql_phone);
//		Constans.TABLE_EXSIT = true;
//		util.saveToSp("onlinecc", "new");
//	}
//
//	// 创建table
//	public void toCreate(net.sqlcipher.database.SQLiteDatabase db) {
//		com.nxt.nxtapp.common.LogUtil.LogD(TAG, "onCreate");
//		String sql = "CREATE TABLE " + TABLE_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + XNB_NAME
//				+ " varchar(50) UNIQUE, " + MSGID + " varchar(20)," + LASTTIME
//				+ " varchar(20), " + LASTMSG + " text, " + MSGNUM
//				+ " varchar(10)," + TYPE + " varchar(20), " + PARAM + " text,"
//				+ UPIC + " text," + PROFILE + " text," + dyflag
//				+ " varchar(10)," + nick + " varchar(50)," + UID
//				+ " varchar(50)," + ver + " integer," + isdefault + " integer"
//				+ ");";
//		String sql_contract = "create table " + CONTRACT_TABLE_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + name
//				+ " varchar(50) UNIQUE, " + cid + " varchar(50)," + phone
//				+ " varchar(20), " + address + " text, " + nick
//				+ " varchar(50)," + reserve1 + " TEXT, " + reserve2 + " text,"
//				+ upic + " text," + dyflag + " varchar(10)," + status
//				+ " varchar(10)," + sex + " varchar(10)" + ");";
//		// 手机联系人数据表
//		String sql_phone = "create table " + PHONE_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + PNumber
//				+ " varchar(20), " + PName + " varchar(50), " + PState
//				+ " varchar(50)," + PLastTime + " varchar(50)" + ");";
//		String sql_bangding = "create table " + BANGDING_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + MoKuaiName
//				+ " varchar(20), " + paras + " varchar(500), " + token
//				+ " varchar(50)," + ZhangHao + " varchar(50), " + password
//				+ " varchar(50)," + ziduan2 + " varchar(50), " + ziduan3
//				+ " varchar(50)" + ");";
//		db.execSQL(sql_bangding);
//		com.nxt.nxtapp.common.LogUtil.LogE(TAG, "sql:" + sql
//				+ " \nsql_contract:" + sql_contract);
//		db.execSQL(sql);
//		db.execSQL(sql_contract);
//		db.execSQL(sql_phone);
//		Constans.TABLE_EXSIT = true;
//		util.saveToSp("onlinecc", "new");
//	}
//
//}