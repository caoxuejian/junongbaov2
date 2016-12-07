package com.nxt.ynt.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SCDButil extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "nxsc.db";
	private final static int DATABASE_VERSION = 2;
	private final static String TABLE_NAME = "mlsn_table";
	public final static String BOOK_ID = "sc_id";

	public final static String TITLE = "title";
	public final static String CONTENT = "content";
	public final static String URL_STR = "url_str";
	public final static String TIME = "time";
	public final static String MODE = "mode";

	public final static String BOOK_NAME = "book_name";
	public final static String BOOK_AUTHOR = "book_author";

	public SCDButil(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 创建table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + BOOK_ID
				+ " INTEGER primary key autoincrement, " + TITLE + " text, "
				+ CONTENT + " text, " + TIME + " text, " + MODE + " text, "
				+ URL_STR + " text, " + BOOK_NAME + " text);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, TIME
				+ " desc");
		return cursor;
	}

	
	// 增加操作
	public long insert(String title, String content, String url_str,
			String time, String mode, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();

		cv.put(TITLE, title);
		cv.put(CONTENT, content);
		cv.put(URL_STR, url_str);
		cv.put(TIME, time);
		cv.put(MODE, mode);
		cv.put(BOOK_NAME, username);
		long row = db.insert(TABLE_NAME, null, cv);
		return row;
	}

	// 删除操作
	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = BOOK_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(TABLE_NAME, where, whereValue);
	}

	// 修改操作
	public void update(int id, String bookname, String author) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = BOOK_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put(BOOK_NAME, bookname);
		cv.put(BOOK_AUTHOR, author);
		db.update(TABLE_NAME, cv, where, whereValue);
	}
}