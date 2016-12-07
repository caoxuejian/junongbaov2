//package com.nxt.ynt.database;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class BaseDBHelperNormal extends SQLiteOpenHelper{
//	private DBUtilSafe db;
//	private static String TAG = "BaseDBHelperNormal";
//	
//	public BaseDBHelperNormal(Context context, String name, int version,DBUtilSafe db) {
//		super(context, name, null, version);
//		this.db = db;
//		com.nxt.nxtapp.common.LogUtil.LogE(TAG, "BaseDBHelperNormal构造方法");
//	}
//
//	private static BaseDBHelperNormal mInstance = null;
//	 public  static BaseDBHelperNormal getInstance(Context context, String name, int version,DBUtilSafe db) {  
//		 com.nxt.nxtapp.common.LogUtil.LogE(TAG, "BaseDBHelperNormal");
//	       if (mInstance == null) {  
//	    	   com.nxt.nxtapp.common.LogUtil.LogE(TAG, "BaseDBHelperNormal   mInstance == null");
//	           mInstance = new  BaseDBHelperNormal(context, name, version,db);  
//	       }  
//	       return mInstance;  
//	   };  
//	
//	
//	
//	@Override
//	public void onCreate(SQLiteDatabase arg0) {
//		
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
//		db.onUpgrade(arg0,oldVersion,newVersion);
//		com.nxt.nxtapp.common.LogUtil.LogD(TAG, "onUpgrade" + "  " + oldVersion
//				+ "  " + newVersion);
//	}
//
//}
