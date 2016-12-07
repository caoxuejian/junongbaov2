//package com.nxt.ynt.database;
//
//import java.util.ArrayList;
//import java.util.List;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import com.nxt.nxtapp.common.LogUtil;
//import com.nxt.nxtapp.json.JsonPaser;
//import com.nxt.ynt.JNBMainActivity;
//import com.nxt.ynt.entity.NewsRoot;
//import com.nxt.ynt.entity.XNBmsg;
//import com.nxt.ynt.page.ReadRaw;
//import com.nxt.ynt.utils.Util;
//import com.nxt.ynt.widget.Constans;
//
///**
// * @author Wang Tingting 消息与联系人数据库操作类 2014-5-4上午11:45:30
// */
//public class XNBDUtil {
//	private String TAG = "XNBDUtil";
//	private final static String DATABASE_NAME = "xnb.db";
//	private final static int DATABASE_VERSION = 12;// 任何人不得修改这个变量，修改之前联系赵佳丽
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
//	public final static String dyflag = "dyflag";// 加星
//	public final static String sex = "sex";
//	public final static String status = "status";// 状态是否有效
//	private Context context;
//	private static String PHONE_NAME;// 手机联系人表
//	private static String PNumber = "phone";
//	private static String PName = "name";
//	private static String PState = "state";
//	private static String PLastTime = "lastTime";
//	static final int PageSize = 50;// 分页时，每页的数据总数
//	private Util util;
//	private DBUtilSafe db;
//	private static String TABLE_Myfabu = "myFaBu";
//	private static String BANGDING_NAME; // 绑定模块表
//	private static String MoKuaiName = "MoKuaiName";// 模块名
//	private static String ZhangHao = "ZhangHao";// 绑定账号
//	private static String password = "password";// 密码
//	private static String token = "token";// 标识
//	private static String paras = "paras";// 绑定参数
//	private static String ziduan2 = "ziduan2";// 标识
//	private static String ziduan3 = "ziduan3";// 标识
//	private static String GROUP_TABLE_NAME;
//	public final static String groupId = "groupId";// 群id
//	public final static String groupName = "groupName";// 群名
//	public final static String groupPic = "groupPic";// 群头像
//	public final static String groupTagSort = "groupTagSort";// 群分类
//	public final static String groupDesc = "groupDesc";// 群描述
//	public final static String groupCreateTime = "groupCreateTime";// 群创建时间
//	public final static String groupCreateAdds = "groupCreateAdds";// 群创建地点
//	public final static String groupLevel = "groupLevel";// 群级别
//	public final static String currentMemberNum = "currentMemberNum";// 群当前成员人数
//	public final static String maxMemberNum = "maxMemberNum";// 群人数上限
//	public final static String groupFlag = "groupFlag";// 群标识
//	public final static String groupMember = "currentMemberNum";// 群当前成员人数
//	public final static String groupReserve1 = "groupReserve1"; // 保留列
//	public final static String groupReserve2 = "groupReserve2";
//
//	public XNBDUtil(Context context) {
//		this.context = context;
//		util = new Util(context);
//		String uname = util.getFromSp(Util.UNAME, null);
//		com.nxt.nxtapp.common.LogUtil.LogE(TAG, "XNBDUtil");
//		db = new DBUtilSafe(context, uname + DATABASE_NAME, DATABASE_VERSION,
//				"XNBDUtil", this);
//		TABLE_NAME = "xnbmsgtable" + uname;
//		CONTRACT_TABLE_NAME = "XNBCONTARCT" + uname;
//		PHONE_NAME = "XNBPHONE" + uname;
//		BANGDING_NAME = "BANGDING_NAME" + uname;
//		GROUP_TABLE_NAME = "GROUP" + uname;
//		toCreate(db);
//	}
//
//	public XNBDUtil(Context context, String myfabu) {
//		db = new DBUtilSafe(context, DATABASE_NAME, DATABASE_VERSION,
//				"XNBDUtil", this);
//		if (!tabbleIsExist(db, TABLE_Myfabu)) {
//			LogUtil.LogE(TAG, "我的发布表不存在创建表");
//			String sql = "create table "
//					+ TABLE_Myfabu
//					+ " ("
//					+ ID
//					+ " INTEGER primary key AUTOINCREMENT, content varchar(50), "
//					+ "kind varchar(50),pic varchar(50), time text, zhuanfa_num varchar(50),"
//					+ "pinglun_sum TEXT, zan_sum text,address text,baoliu1 varchar(50),"
//					+ "baoliu2 varchar(50),fabu_id varchar(10)" + ");";
//			db.execSQL(sql);
//		}
//	}
//
//	private void insertInitDatas(DBUtilSafe db) {
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
//	// 创建table
//	public void toCreate(DBUtilSafe db) {
//		com.nxt.nxtapp.common.LogUtil.LogD(TAG, "onCreate");
//		String sql = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + XNB_NAME
//				+ " varchar(50)," + MSGID + " varchar(20)," + LASTTIME
//				+ " varchar(20)," + LASTMSG + " text, " + MSGNUM
//				+ " varchar(10)," + TYPE + " varchar(20), " + PARAM + " text,"
//				+ UPIC + " text," + PROFILE + " text," + dyflag
//				+ " varchar(10)," + nick + " varchar(50)," + UID
//				+ " varchar(50)," + ver + " integer DEFAULT 0," + isdefault
//				+ " integer DEFAULT 0" + ");";
//		String sql_contract = "CREATE TABLE  IF NOT EXISTS "
//				+ CONTRACT_TABLE_NAME + " (" + ID
//				+ " INTEGER primary key AUTOINCREMENT, " + name
//				+ " varchar(50) UNIQUE, " + cid + " varchar(50)," + phone
//				+ " varchar(20), " + address + " text, " + nick
//				+ " varchar(50)," + reserve1 + " TEXT, " + reserve2 + " text,"
//				+ upic + " text," + dyflag + " varchar(10)," + status
//				+ " varchar(10)," + sex + " varchar(10)" + ");";
//		// 手机联系人数据表
//		String sql_phone = "CREATE TABLE  IF NOT EXISTS " + PHONE_NAME + " ("
//				+ ID + " INTEGER primary key AUTOINCREMENT, " + PNumber
//				+ " varchar(20), " + PName + " varchar(50), " + PState
//				+ " varchar(50)," + PLastTime + " varchar(50)" + ");";
//		String sql_bangding = "CREATE TABLE  IF NOT EXISTS " + BANGDING_NAME
//				+ " (" + ID + " INTEGER primary key AUTOINCREMENT, "
//				+ MoKuaiName + " varchar(20), " + paras + " varchar(500), "
//				+ token + " varchar(50)," + ZhangHao + " varchar(50), "
//				+ password + " varchar(50)," + ziduan2 + " varchar(50), "
//				+ ziduan3 + " varchar(50)" + ");";
//		String sql_group = "CREATE TABLE  IF NOT EXISTS " + GROUP_TABLE_NAME
//				+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + groupId
//				+ " text," + groupName + " text," + groupPic + " text,"
//				+ groupTagSort + " text," + groupDesc + " text,"
//				+ groupCreateTime + " text," + groupCreateAdds + " text,"
//				+ groupLevel + " text," + currentMemberNum + " text,"
//				+ maxMemberNum + " text," + groupFlag + " text,"
//				+ groupReserve1 + " text," + groupReserve2 + " text" + ")";
//		LogUtil.LogE(TAG, "sql:" + sql_group);
//		db.execSQL(sql);
//		db.execSQL(sql_contract);
//		db.execSQL(sql_phone);
//		db.execSQL(sql_bangding);
//		db.execSQL(sql_group);
//		Constans.TABLE_EXSIT = true;
//		Util util = new Util(context);
//		util.saveToSp("onlinecc", "new");
//	}
//
//	// 增加手机联系人
//	public void insertPhone(String phoneNumber2, String contactName2, long t,
//			String state) {
//		Cursor cursor = null;
//		try {
//			// 获取手机联系人先判断在本地是否存在
//			cursor = db.rawQuery("select * from " + PHONE_NAME
//					+ " where phone=?", new String[] { phoneNumber2 });
//			if (!cursor.moveToFirst()) {
//				ContentValues cv = new ContentValues();
//				cv.put(PNumber, phoneNumber2);
//				cv.put(PName, contactName2);
//				cv.put(PLastTime, t);
//				cv.put(PState, state);
//				db.insert(PHONE_NAME, null, cv);
//			} else {
//				updatePhone(phoneNumber2, t, state);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			cursor.close();
//		}
//	}
//
//	// 刷新分页
//	public int RefreshPage() {
//		String sql = "select count(id) from " + PHONE_NAME;
//		Cursor rec = db.rawQuery(sql, null);
//		rec.moveToLast();
//		// 取得总数
//		double recSize = rec.getLong(0);
//		rec.close();
//		// 取得分页数
//		int pageNum = (int) Math.ceil(recSize / PageSize);
//		return pageNum;
//	}
//
//	// 返回指定页数的数据
//	public List<AddPhoneContracts> loadPhone(int pageID) {
//		List<AddPhoneContracts> list = new ArrayList<AddPhoneContracts>();
//		String sql = "select * from " + PHONE_NAME + " limit " + pageID
//				* PageSize + "," + PageSize;
//		Cursor cursor = db.rawQuery(sql, null);
//		int colCount = cursor.getCount();
//		for (int i = 0; i < colCount; i++) {
//			cursor.moveToPosition(i);
//			AddPhoneContracts phoneContracts = new AddPhoneContracts();
//			phoneContracts.setPhone(cursor.getString(cursor
//					.getColumnIndex(cursor.getColumnName(1))));
//			phoneContracts.setName(cursor.getString(cursor
//					.getColumnIndex(cursor.getColumnName(2))));
//			phoneContracts.setState(cursor.getString(cursor
//					.getColumnIndex(cursor.getColumnName(3))));
//			phoneContracts.setLastTime(cursor.getLong(cursor
//					.getColumnIndex(cursor.getColumnName(4))));
//			list.add(phoneContracts);
//		}
//		cursor.close();
//		return list;
//	}
//
//	// 修改联系人数据
//	public void updatePhone(String phoneNumber2, long t, String state) {
//		try {
//			ContentValues values = new ContentValues();
//			values.put("state", state);
//			values.put("lastTime", t);
//			db.update(PHONE_NAME, values, "phone=?", new String[] { ""
//					+ phoneNumber2 });
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 通过某个绑定模块的模块名查找表里是否存在所对应的token值
//	public boolean getOauth(String MoKuaiName, String url) {
//		Cursor cursor = db.rawQuery("select token from " + BANGDING_NAME
//				+ " where MoKuaiName=?", new String[] { MoKuaiName });
//		if (cursor != null && cursor.moveToNext()) {
//			cursor.close();
//			return true;
//		}
//		cursor.close();
//		return false;
//	}
//
//	// 更新某个绑定模块的数据
//	public void updateBangDing(String MKname, String token) {
//		ContentValues values = new ContentValues();
//		values.put("token", token);
//		db.update(BANGDING_NAME, values, "MoKuaiName=?",
//				new String[] { MKname });
//	}
//
//	// TODO 通过模块名得到各个字段
//	public BangDing getBangDing(String MoKuaiName, String url) {
//		Cursor cursor = db.rawQuery("select * from " + BANGDING_NAME
//				+ " where MoKuaiName=?", new String[] { MoKuaiName });
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				BangDing bangding = new BangDing();
//				bangding.setMokuainame(MoKuaiName);
//				bangding.setParas(cursor.getString(cursor
//						.getColumnIndex("paras")));
//				bangding.setToken(cursor.getString(cursor
//						.getColumnIndex("token")));
//				bangding.setZhanghao(cursor.getString(cursor
//						.getColumnIndex("ZhangHao")));
//				bangding.setPassword(cursor.getString(cursor
//						.getColumnIndex("password")));
//				cursor.close();
//				return bangding;
//			}
//		}
//		cursor.close();
//		return null;
//	}
//
//	// TODO 得到所有绑定模块的数据
//	public List<BangDing> getBangDings() {
//		List<BangDing> list = new ArrayList<BangDing>();
//		Cursor cursor = db.rawQuery("select * from " + BANGDING_NAME, null);
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				BangDing bangding = new BangDing();
//				bangding.setMokuainame(cursor.getString(cursor
//						.getColumnIndex("MoKuaiName")));
//				bangding.setParas(cursor.getString(cursor
//						.getColumnIndex("paras")));
//				bangding.setToken(cursor.getString(cursor
//						.getColumnIndex("token")));
//				bangding.setZhanghao(cursor.getString(cursor
//						.getColumnIndex("ZhangHao")));
//				bangding.setPassword(cursor.getString(cursor
//						.getColumnIndex("password")));
//				list.add(bangding);
//			}
//		}
//		cursor.close();
//		return list;
//	}
//
//	// 插入一条创建群组的数据
//	public boolean insertSetUpGroup(String str_groupId, String str_groupName,
//			String str_groupPic, String str_groupTagSort, String str_groupDesc,
//			String str_groupCreateTime, String str_groupLevel,
//			String str_currentMemberNum, String str_maxMemberNum,
//			String str_groupFlag) {
//		boolean result = true;
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put(groupId, str_groupId);
//			cv.put(groupName, str_groupName);
//			cv.put(groupPic, str_groupPic);
//			cv.put(groupTagSort, str_groupTagSort);
//			cv.put(groupDesc, str_groupDesc);
//			cv.put(groupLevel, str_groupLevel);
//			cv.put(currentMemberNum, str_currentMemberNum);
//			cv.put(maxMemberNum, str_maxMemberNum);
//			cv.put(groupFlag, str_groupFlag);
//			long i = db.insert(GROUP_TABLE_NAME, null, cv);
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
//	// TODO 得到所有我的群组
//	public List<GroupInfo> getAllMyGroups() {
//		List<GroupInfo> list = new ArrayList<GroupInfo>();
//		Cursor cursor = db.rawQuery("select * from " + GROUP_TABLE_NAME, null);
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				GroupInfo bangding = new GroupInfo();
//				bangding.setGroupId(cursor.getString(cursor
//						.getColumnIndex(groupId)));
//				bangding.setGroupName(cursor.getString(cursor
//						.getColumnIndex(groupName)));
//				bangding.setBackgroundImg(cursor.getString(cursor
//						.getColumnIndex(groupPic)));
//				bangding.setTags(cursor.getString(cursor
//						.getColumnIndex(groupTagSort)));
//				bangding.setInfo(cursor.getString(cursor
//						.getColumnIndex(groupDesc)));
//				bangding.setCreateDate(cursor.getString(cursor
//						.getColumnIndex(groupCreateTime)));
//				bangding.setLevel(cursor.getString(cursor
//						.getColumnIndex(groupLevel)));
//				bangding.setCurrentMemberNum(cursor.getString(cursor
//						.getColumnIndex(currentMemberNum)));
//				bangding.setMaxMemberNum(cursor.getString(cursor
//						.getColumnIndex(maxMemberNum)));
//				bangding.setGroupFlag(cursor.getString(cursor
//						.getColumnIndex(groupFlag)));
//				list.add(bangding);
//			}
//		}
//		cursor.close();
//		return list;
//	}
//
//	public List<GroupInfo> getGroupInfo(String[] feildName, String[] feildvalue) {
//		List<GroupInfo> glist = new ArrayList<GroupInfo>();
//		Cursor cursor = null;
//		try {
//			String where = null;
//			if (feildName != null) {
//				StringBuffer wherecase = null;
//				wherecase = new StringBuffer();
//				for (int i = 0; i < feildName.length; i++) {
//					wherecase.append(feildName[i] + "=?");
//					if (i != feildName.length - 1)
//						wherecase.append(" and ");
//				}
//				where = wherecase.toString();
//			}
//			cursor = db.query(GROUP_TABLE_NAME, null, where, feildvalue, null,
//					null, null);
//			if (cursor != null && cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					GroupInfo ginfo = new GroupInfo();
//					ginfo.setGroupId(cursor.getString(cursor
//							.getColumnIndex(groupId)));
//					ginfo.setGroupName(cursor.getString(cursor
//							.getColumnIndex(groupName)));
//					ginfo.setBackgroundImg(cursor.getString(cursor
//							.getColumnIndex(groupPic)));
//					ginfo.setTags(cursor.getString(cursor
//							.getColumnIndex(groupTagSort)));
//					ginfo.setInfo(cursor.getString(cursor
//							.getColumnIndex(groupDesc)));
//					ginfo.setLevel(cursor.getString(cursor
//							.getColumnIndex(groupLevel)));
//					ginfo.setCurrentMemberNum(cursor.getString(cursor
//							.getColumnIndex(currentMemberNum)));
//					ginfo.setMaxMemberNum(cursor.getString(cursor
//							.getColumnIndex(maxMemberNum)));
//					ginfo.setGroupFlag(cursor.getString(cursor
//							.getColumnIndex(groupFlag)));
//					glist.add(ginfo);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (cursor != null)
//					cursor.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return glist;
//	}
//
//	// 插入一条绑定模块的数据
//	public boolean insertBangDing(String MKname, String tokens, String Paras,
//			String zhanghao, String pwd) {
//		boolean result = true;
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put(MoKuaiName, MKname);
//			cv.put(ZhangHao, zhanghao);
//			cv.put(password, pwd);
//			cv.put(token, tokens);
//			cv.put(paras, Paras);
//			long i = db.insert(BANGDING_NAME, null, cv);
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
//	// 查找所有绑定模块的模块名
//	public List<String> getBangDingMOKuai() {
//		List<String> list = new ArrayList<String>();
//		Cursor cursor = db.rawQuery("select MoKuaiName from " + BANGDING_NAME,
//				null);
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				list.add(cursor.getString(cursor.getColumnIndex("MoKuaiName")));
//			}
//		}
//		cursor.close();
//		return list;
//	}
//
//	// 查找所有绑定模块的账号
//	public List<String> getBangDingZhangHao() {
//		List<String> list = new ArrayList<String>();
//		Cursor cursor = db.rawQuery("select ZhangHao from " + BANGDING_NAME,
//				null);
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				list.add(cursor.getString(cursor.getColumnIndex("ZhangHao")));
//			}
//		}
//		cursor.close();
//		return list;
//	}
//
//	// 删除某个绑定模块
//	public void deleteBangDing(String moKuaiName) {
//		try {
//			String where = MoKuaiName + " = ?";
//			String[] whereValue = { moKuaiName };
//			db.delete(BANGDING_NAME, where, whereValue);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 查找状态为空的联系人
//	public List<AddPhoneContracts> getPhone() {
//		List<AddPhoneContracts> list = new ArrayList<AddPhoneContracts>();
//		Cursor cursor = db.rawQuery("select * from " + PHONE_NAME
//				+ " where state=?", new String[] { "0" });
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				AddPhoneContracts phoneContracts = new AddPhoneContracts();
//				phoneContracts.setPhone(cursor.getString(cursor
//						.getColumnIndex("phone")));
//				phoneContracts.setName(cursor.getString(cursor
//						.getColumnIndex("name")));
//				phoneContracts.setState(cursor.getString(cursor
//						.getColumnIndex("state")));
//				phoneContracts.setLastTime(cursor.getLong(cursor
//						.getColumnIndex("lastTime")));
//				list.add(phoneContracts);
//			}
//		}
//		cursor.close();
//		return list;
//	}
//
//	// 查找状态不为空的联系人
//	public List<AddPhoneContracts> getNotNull() {
//		List<AddPhoneContracts> list = new ArrayList<AddPhoneContracts>();
//		// SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery("select * from " + PHONE_NAME
//				+ " where state<>?", new String[] { "0" });
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				AddPhoneContracts phoneContracts = new AddPhoneContracts();
//				phoneContracts.setPhone(cursor.getString(cursor
//						.getColumnIndex("phone")));
//				phoneContracts.setName(cursor.getString(cursor
//						.getColumnIndex("name")));
//				phoneContracts.setState(cursor.getString(cursor
//						.getColumnIndex("state")));
//				phoneContracts.setLastTime(cursor.getLong(cursor
//						.getColumnIndex("lastTime")));
//				list.add(phoneContracts);
//			}
//		}
//		cursor.close();
//		return list;
//	}
//
//	// 查找数据库所有联系人
//	public List<AddPhoneContracts> getAllPhone() {
//		List<AddPhoneContracts> list = new ArrayList<AddPhoneContracts>();
//		Cursor cursor = db.rawQuery("select * from " + PHONE_NAME, null);
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				AddPhoneContracts phoneContracts = new AddPhoneContracts();
//				phoneContracts.setPhone(cursor.getString(cursor
//						.getColumnIndex("phone")));
//				phoneContracts.setName(cursor.getString(cursor
//						.getColumnIndex("name")));
//				phoneContracts.setState(cursor.getString(cursor
//						.getColumnIndex("state")));
//				phoneContracts.setLastTime(cursor.getLong(cursor
//						.getColumnIndex("lastTime")));
//				list.add(phoneContracts);
//			}
//		}
//		cursor.close();
//		return list;
//	}
//
//	private boolean tabbleIsExist(DBUtilSafe db, String tableName) {
//		boolean result = false;
//		if (tableName == null) {
//			return false;
//		}
//		Cursor cursor = null;
//		try {
//			String sql = "select count(*) as c from " + "sqlite_master"
//					+ " where type ='table' and name ='" + tableName.trim()
//					+ "' ";
//			cursor = db.rawQuery(sql, null);
//			LogUtil.LogE(TAG, sql);
//			if (cursor.moveToNext()) {
//				int count = cursor.getInt(0);
//				LogUtil.LogE(TAG, "count==" + count);
//				if (count > 0) {
//					result = true;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (cursor != null)
//				cursor.close();
//		}
//		Constans.TABLE_EXSIT = result;
//		com.nxt.nxtapp.common.LogUtil.LogE(TAG,
//				"########Constans.TABLE_EXSIT =" + Constans.TABLE_EXSIT);
//		return result;
//	}
//
//	public List<XNBmsg> getMsgs(String[] feildName, String[] feildvalue,
//			boolean falg, boolean isByGroup) {
//		List<XNBmsg> msgs = new ArrayList<XNBmsg>();
//		Cursor cursor = null;
//		String bygroup = null;
//		if (isByGroup)
//			bygroup = XNB_NAME;
//		if (falg)
//			JNBMainActivity.msg_un_num = 0;
//		try {
//			String where = null;
//			if (feildName != null) {
//				StringBuffer wherecase = null;
//				wherecase = new StringBuffer();
//				for (int i = 0; i < feildName.length; i++) {
//					if (feildName[i].equals(LASTMSG)) {
//						wherecase.append(feildName[i] + " is not null");
//					} else {
//						wherecase.append(feildName[i] + "=?");
//					}
//					if (i != feildName.length - 1)
//						wherecase.append(" and ");
//				}
//				where = wherecase.toString();
//			}
//			// TODO 小徽宝首页不按时间排序
//			if (util.getFromSp(Util.APPNAME, "").equals("小徽宝"))
//				cursor = db.query(TABLE_NAME, null, where, feildvalue, null,
//						null, null);
//			else
//				cursor = db.query(TABLE_NAME, null, where, feildvalue, bygroup,
//						null, LASTTIME + " desc");
//			LogUtil.LogE(TAG, "getMsgs  tablecolum sum :" + cursor.getCount());
//			if (cursor != null && cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					// LogUtil.LogE(TAG, "MsgFragment   cursor.moveToNext()");
//					XNBmsg data = new XNBmsg();
//					data.setId(cursor.getString(cursor.getColumnIndex(MSGID)));
//					data.setUname(cursor.getString(cursor
//							.getColumnIndex(XNB_NAME)));
//					data.setLastmsg(cursor.getString(cursor
//							.getColumnIndex(LASTMSG)));
//					data.setLasttime(cursor.getString(cursor
//							.getColumnIndex(LASTTIME)));
//					data.setMsgnum(cursor.getString(cursor
//							.getColumnIndex(MSGNUM)));
//					data.setParam(cursor.getString(cursor.getColumnIndex(PARAM)));
//					data.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
//					data.setUpic(cursor.getString(cursor.getColumnIndex(UPIC)));
//					data.setDyflag(cursor.getString(cursor
//							.getColumnIndex(dyflag)));
//					data.setProfile(cursor.getString(cursor
//							.getColumnIndex(PROFILE)));
//					data.setUid(cursor.getString(cursor.getColumnIndex(UID)));
//					data.setNick(cursor.getString(cursor.getColumnIndex(nick)));
//					msgs.add(data);
//					// 未读条件统计
//					if (falg)
//						if (data.getType().equals("user")) {
//							MsgUtil.addMsgNum(data.getMsgnum());
//						} else {
//							MsgUtil.addMsgNum("0");
//						}
//
//				}
//			}
//			if (falg)
//				MsgUtil.showMsgun();// 判断是否显示未读
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (cursor != null)
//					cursor.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return msgs;
//	}
//
//	// 更新订栏数据
//	public void updateDlan(String UID, String time, String msg) {
//		ContentValues values = new ContentValues();
//		values.put("UID", UID);
//		values.put("lasttime", time);
//		values.put("lastmsg", msg);
//		db.update(TABLE_NAME, values, "UID=?", new String[] { UID });
//	}
//
//	// 查询xnbmsg表格type类型为news的uid
//	public List<XNBmsg> getType() {
//		List<XNBmsg> list = new ArrayList<XNBmsg>();
//		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
//				+ " where type=?", new String[] { "news" });
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				XNBmsg xb = new XNBmsg();
//				xb.setUid(cursor.getString(cursor.getColumnIndex("UID")));
//
//				list.add(xb);
//			}
//		}
//		cursor.close();
//		return list;
//	}
//
//	public List<Contracts> getContracts(String[] feildName,
//			String[] feildvalue, boolean isByGroup) {
//		List<Contracts> msgs = new ArrayList<Contracts>();
//		Cursor cursor = null;
//		String bygroup = null;
//		if (isByGroup)
//			bygroup = name;
//		try {
//			String where = null;
//			if (feildName != null) {
//				StringBuffer wherecase = null;
//				wherecase = new StringBuffer();
//				for (int i = 0; i < feildName.length; i++) {
//					wherecase.append(feildName[i] + "=?");
//					if (i != feildName.length - 1)
//						wherecase.append(" and ");
//				}
//				where = wherecase.toString();
//			}
//			cursor = db.query(CONTRACT_TABLE_NAME, null, where, feildvalue,
//					bygroup, null, null);
//			LogUtil.LogE(TAG,
//					"getContracts  tablecolum sum :" + cursor.getCount());
//			if (cursor != null && cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					Contracts data = new Contracts();
//					data.setArea(cursor.getString(cursor
//							.getColumnIndex(address)));
//					data.setUid(cursor.getString(cursor.getColumnIndex(cid)));
//					data.setUname(cursor.getString(cursor.getColumnIndex(name)));
//					String nickname = cursor.getString(cursor
//							.getColumnIndex(nick));
//					if (nickname == null || "".equals(nickname))
//						nickname = cursor
//								.getString(cursor.getColumnIndex(name));
//					data.setNick(nickname);
//					data.setPhone(cursor.getString(cursor.getColumnIndex(phone)));
//					data.setReserve1(cursor.getString(cursor
//							.getColumnIndex(reserve1)));
//					data.setReserve2(cursor.getString(cursor
//							.getColumnIndex(reserve2)));
//					data.setUpic(cursor.getString(cursor.getColumnIndex(upic)));
//					data.setDyflag(cursor.getString(cursor
//							.getColumnIndex(dyflag)));
//					data.setSex(cursor.getString(cursor.getColumnIndex(sex)));
//					data.setStatus(cursor.getString(cursor
//							.getColumnIndex(status)));
//					msgs.add(data);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (cursor != null)
//					cursor.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return msgs;
//	}
//
//	// 增加操作
//	public boolean insertXNBMSG(XNBmsg msg) {
//		boolean result = true;
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put(UID, msg.getUid());
//			cv.put(LASTTIME, msg.getLasttime());
//			cv.put(LASTMSG, msg.getLastmsg());
//			cv.put(MSGID, msg.getId());
//			cv.put(TYPE, msg.getType());
//			cv.put(PARAM, msg.getParam());
//			cv.put(MSGNUM, msg.getMsgnum());
//			cv.put(dyflag, msg.getDyflag());
//			cv.put(PROFILE, msg.getProfile());
//			cv.put(nick, msg.getNick());
//			if (msg.getUname() != null && !"".equals(msg.getUname())) {
//				cv.put(XNB_NAME, msg.getUname());
//				cv.put(UPIC, msg.getUpic());
//			} else {// 订阅号
//				cv.put(XNB_NAME, msg.getName());
//				cv.put(UPIC, msg.getImage());
//			}
//			long i = db.insert(TABLE_NAME, null, cv);
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
//	public boolean insertXNBMSG(XNBmsg msg, int version) {
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
//			long i = db.insert(TABLE_NAME, null, cv);
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
//	public boolean insertXNBMSG(XNBmsg msg, DBUtilSafe database, int version) {
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
//			long i = database.insert(TABLE_NAME, null, cv);
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
//	/*
//	 * 删除消息 param用户uid
//	 */
//	public void deleteMsg(int id) {
//		try {
//			if (id == -1) {
//				db.delete(TABLE_NAME, null, null);
//				return;
//			}
//			String where = UID + " = ?";
//			String[] whereValue = { Integer.toString(id) };
//			db.delete(TABLE_NAME, where, whereValue);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	/*
//	 * 删除消息 isisdefault==1的所有行
//	 */
//	public void deleteMsg() {
//		try {
//			String where = isdefault + " = ?";
//			String[] whereValue = { 1 + "" };
//			db.delete(TABLE_NAME, where, whereValue);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	public void deleteMsgs() {
//		try {
//			db.delete(TABLE_NAME, null, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	/*
//	 * 删除消息 param用户uid
//	 */
//	public void deleteMsg(String uname) {
//		try {
//			String where = XNB_NAME + " = ?";
//			String[] whereValue = { uname };
//			db.delete(TABLE_NAME, where, whereValue);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 修改操作
//	public void updateMsg(String cName, String cValue, String[] fieldname,
//			String[] fieldvalue) {
//		if (fieldname == null || fieldname.length == 0)
//			return;
//		try {
//			ContentValues cv = new ContentValues();
//			for (int i = 0; i < fieldname.length; i++) {
//				cv.put(fieldname[i], fieldvalue[i]);
//			}
//			String where = cName + "=?";
//			db.update(TABLE_NAME, cv, where, new String[] { cValue });
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// 修改操作
//	public void updateMsgByUname(String uname, String[] fieldname,
//			String[] fieldvalue) {
//		if (fieldname == null || fieldname.length == 0)
//			return;
//		try {
//			ContentValues cv = new ContentValues();
//			for (int i = 0; i < fieldname.length; i++) {
//				cv.put(fieldname[i], fieldvalue[i]);
//			}
//			String where = XNB_NAME + "=?";
//			db.update(TABLE_NAME, cv, where, new String[] { uname });
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 添加联系人
//	public void insertContracts(Contracts contract) {
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put(cid, contract.getUid());
//			cv.put(address, contract.getArea());
//			cv.put(name, contract.getUname());
//			cv.put(nick, contract.getNick());
//			cv.put(phone, contract.getPhone());
//			cv.put(reserve1, contract.getHy());
//			cv.put(reserve2, contract.getRemark());
//			cv.put(upic, contract.getUpic());
//			cv.put(dyflag, contract.getDyflag());
//			cv.put(sex, contract.getSex());
//			cv.put(status, contract.getStatus());
//			db.insert(CONTRACT_TABLE_NAME, null, cv);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 添加联系人
//	public void insertContracts(Contracts contract, DBUtilSafe database) {
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put(cid, contract.getUid());
//			cv.put(address, contract.getArea());
//			cv.put(name, contract.getUname());
//			cv.put(nick, contract.getNick());
//			cv.put(phone, contract.getPhone());
//			cv.put(reserve1, contract.getHy());
//			cv.put(reserve2, contract.getReserve2());
//			cv.put(upic, contract.getUpic());
//			cv.put(dyflag, contract.getDyflag());
//			cv.put(sex, contract.getSex());
//			cv.put(status, contract.getStatus());
//			database.insert(CONTRACT_TABLE_NAME, null, cv);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 修改联系人操作
//	public void updateContract(String uid, String[] fieldname,
//			String[] fieldvalue) {
//		if (fieldname == null || fieldname.length == 0)
//			return;
//		try {
//			StringBuffer wherecase = new StringBuffer(cid + "=?");
//			ContentValues cv = new ContentValues();
//			for (int i = 0; i < fieldname.length; i++) {
//				cv.put(fieldname[i], fieldvalue[i]);
//			}
//			int row = db.update(CONTRACT_TABLE_NAME, cv, wherecase.toString(),
//					new String[] { uid });
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 修改联系人操作
//	public void updateContractbyUname(String uanme, String[] fieldname,
//			String[] fieldvalue) {
//		if (fieldname == null || fieldname.length == 0)
//			return;
//		try {
//			StringBuffer wherecase = new StringBuffer(name + "=?");
//			ContentValues cv = new ContentValues();
//			for (int i = 0; i < fieldname.length; i++) {
//				cv.put(fieldname[i], fieldvalue[i]);
//			}
//			int row = db.update(CONTRACT_TABLE_NAME, cv, wherecase.toString(),
//					new String[] { uanme });
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 修改备注
//	public void updateRemarks(String cid, String remarks) {
//		try {
//			ContentValues contentValues = new ContentValues();
//			contentValues.put("reserve2", remarks);
//			db.update(CONTRACT_TABLE_NAME, contentValues, "cid=?",
//					new String[] { cid });
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 根据联系人id删除联系人操作
//	public void deleteContract(int id) {
//		try {
//			String where = cid + " = ?";
//			String[] whereValue = { Integer.toString(id) };
//			db.delete(CONTRACT_TABLE_NAME, where, whereValue);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	// 根据联系人id删除联系人操作
//	public void deleteContractByUname(String uname) {
//		try {
//			String where = name + " = ?";
//			String[] whereValue = { uname };
//			int i = db.delete(CONTRACT_TABLE_NAME, where, whereValue);
//			com.nxt.nxtapp.common.LogUtil.LogE(TAG, "deleteContractByUname:"
//					+ i + " uname:" + uname);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//	}
//
//	public long insert(String content, String pic, String time, String kind,
//			String address) {
//		ContentValues cv = new ContentValues();
//		cv.put("content", content);
//		cv.put("pic", pic);
//		cv.put("time", time);
//		cv.put("kind", kind);
//		cv.put("address", address);
//		long row = db.insert(TABLE_Myfabu, null, cv);
//		return row;
//	}
//
//	public List<PublishData> select() {
//		List<PublishData> list = new ArrayList<PublishData>();
//		Cursor cursor = db.query(TABLE_Myfabu, null, null, null, null, null,
//				"time" + " desc");
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				PublishData xb = new PublishData();
//				xb.setContent(cursor.getString(cursor.getColumnIndex("content")));
//				xb.setPic(cursor.getString(cursor.getColumnIndex("pic")));
//				xb.setTime(cursor.getString(cursor.getColumnIndex("time")));
//				xb.setKind(cursor.getString(cursor.getColumnIndex("kind")));
//				xb.setAddress(cursor.getString(cursor.getColumnIndex("address")));
//				list.add(xb);
//			}
//		}
//		cursor.close();
//		return list;
//	}
//
//}
