package com.nxt.nxtapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDBHelperNormal extends SQLiteOpenHelper{
	private DBUtilSafe db;
	public BaseDBHelperNormal(Context context, String name, int version,DBUtilSafe db) {
		super(context, name, null, version);
		this.db = db;
	}

	private static BaseDBHelperNormal mInstance = null;
	 public  static BaseDBHelperNormal getInstance(Context context, String name, int version,DBUtilSafe db) {  
	       if (mInstance == null) {  
	           mInstance = new  BaseDBHelperNormal(context, name, version,db);  
	       }  
	       return mInstance;  
	   };  
	
	
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.onUpgrade(arg1,arg2);
	}

}
