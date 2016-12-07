package com.nxt.nxtapp;

import java.io.File;

import android.app.Application;

import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.UEHandler;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.setting.GetHost;

public class NXTAPPApplication extends Application {
	private static final String TAG = "NXTAPPApplication";
	public static NXTAPPApplication xmppApplication;
	public static final String PATH_ERROR_LOG = File.separator + "data"
			+ File.separator + "data" + File.separator + "com.nongjiatong"
			+ File.separator + "error.log";
	public static int versonCode;
	public static String imei,newVersion;
	public static String phoneNumber;
	public Util util;
	
	/** 异常处理类。 */
	private UEHandler ueHandler;
	
	
	// 单例模式获取唯一的MyApplication实例
	public static NXTAPPApplication getInstance() {
		if (null == xmppApplication) {
			xmppApplication = new NXTAPPApplication();
		}
		return xmppApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		xmppApplication = this;
		util = new Util(xmppApplication);
		versonCode = Util.getPackageVersion(this);
		imei = util.getFromSp(Util.DEVICEID, "");
		phoneNumber = util.getFromSp(Util.USERNAME, "unLogin");
		GetHost.host = getString(R.string.host);
		LogUtil.isSysoLog = this.getString(R.string.ifSysoLog);
		ueHandler = new UEHandler(this);
		// 设置异常处理实例
		Thread.setDefaultUncaughtExceptionHandler(ueHandler);
	}
}
