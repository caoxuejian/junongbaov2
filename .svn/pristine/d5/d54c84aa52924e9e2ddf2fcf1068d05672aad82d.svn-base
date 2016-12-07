package com.nxt.nxtapp.common;

import android.util.Log;

//¥Ú”°»’÷æ
public class LogUtil {
	public static String isSysoLog;

	public static void LogD(String tag, String msg) {
		if (isSysoLog.equals("yes"))
			Log.d(tag, msg);
	}

	public static void LogI(String tag, String msg) {
			if (isSysoLog.equals("yes"))
			Log.i(tag, msg);
	}

	public static void LogE(String tag, String msg) {
			if (isSysoLog.equals("yes"))
			Log.e(tag, msg);
	}

	public static void syso(String msg) {
			if (isSysoLog.equals("yes"))
				System.out.println(msg);
	}

	public static void syso(Object msg) {
			if (isSysoLog.equals("yes"))
				System.out.println(String.valueOf(msg));
	}

	public static void syso(Boolean msg) {
			if (isSysoLog.equals("yes"))
				System.out.println(String.valueOf(msg));
	}
}
