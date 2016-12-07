package com.nxt.nxtapp.setting;

import java.io.File;

import android.os.Environment;

public class Constant {
	public static boolean DEBUG = true;
	//站点id
	public static final String SITEID = "3735";
	//图片上传地址
	public final static String PUBLIC_POSTPICTURE_URL="meilisannong/server/index.php/article_interface/selluploadpic";
	//文字上传地址
	public final static String PUBLIC_POSTCONTENT_URL="meilisannong/server/index.php/article_interface/requestbuysell";
	//发布种类1为专家问答 为空则是供求信息，2信息通，3农情，4微博，5价格
	public final static int PUBLIC_GQ = 0;
	public final static int PUBLIC_WEIBO = 4;
	public final static int PUBLIC_NQ = 3;
	public final static int PUBLIC_JG = 5;
	public final static int PUBLIC_ZJWD = 1;
	public static final String SPF_DATE = "cachedate";
	public static final String SPF_DATE_FLAG = "theflag";
	//用于判断标识程序刚刚启动时的一次自动检测更新
	public static boolean updateFlag = true;
	
	//版本链接
	public final static String VERSION_URL = "json/12316xnb/version.json";
	//apk下载地址
	public final static String UPDATAPK_URL = com.nxt.nxtapp.setting.GetHost.getHost()+"/andriod/nx.apk";
	
	// 文件保存路径
	public static String NX_SEND_SAVE_PATH = Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ File.separator
					+ "nx" + File.separator + "NXfile_send";
	public static String CHAT_REV_PIC = GetHost.getHost()
			+ "/meilisannong/server/index.php/article_interface/getuploadfiles";
	// 聊天发送图片地址
	public static String CHAT_SEND_PIC = GetHost.getHost()
				+ "/meilisannong/server/index.php/article_interface/uploadfiles";
	public static String NX_CHAT_CAMERA_PATH = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator
				+ "nx" + File.separator + "NXchatpic_camera";
	// 修改昵称
	public static String USEREDIT = com.nxt.nxtapp.setting.GetHost.getHost()
				+ "/meilisannong/server/index.php/user_interface/newuseredit";
	// 找人接口
	public static String find_url = "meilisannong/server/index.php/article_interface/searchmember";
}
