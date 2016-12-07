package com.nxt.nxtapp.database;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;

public class BaseDBHelperSafe extends DBSafe {
	private DBUtilSafe db;

	public BaseDBHelperSafe(Context context, String name, int version,
			DBUtilSafe db) {
		super(context, name, null, version);
		this.db = db;
	}

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
