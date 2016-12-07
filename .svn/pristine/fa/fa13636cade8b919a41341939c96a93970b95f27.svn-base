package com.nxt.ynt.page;

import android.content.Context;
import android.content.Intent;


public class IntentActivityUtil {
	public static String JNBMainActivity="JNBMainActivity";
	public static String TJZHLYMainActivity="TJZHLYMainActivity";
	public static String YNHNMainActivity="YNHNMainActivity";
	/**
	 * avtivity跳转
	 * 
	 * @param context
	 *            上下文
	 * @param activityinfo
	 *            activity名称
	 * @param titlename
	 *            首页标题名称
	 * @param update_txt_url
	 *            版本更新验证接口
	 * @param upload_apk_url
	 *            版本更新下载接口
	 */
	public static void toActivity(Context context, String activityinfo,
			String titlename, String update_txt_url, String upload_apk_url,
			String homebutton) {
		com.nxt.nxtapp.common.LogUtil.syso(activityinfo == null);
		if (activityinfo.equals(JNBMainActivity)) {
			Intent intent = new Intent();
			intent.setAction("com.nxt.Xxt.JNBMainActivity");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.putExtra("titlename", titlename);
			intent.putExtra("updateurl", update_txt_url);
			intent.putExtra("versionplist", upload_apk_url);
			context.startActivity(intent);
		}else if (activityinfo.equals(TJZHLYMainActivity)) {
			Intent intent = new Intent();
			intent.setAction("com.nxt.zhly.TJZHLYMainActivity");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.putExtra("titlename", titlename);
			intent.putExtra("updateurl", update_txt_url);
			intent.putExtra("versionplist", upload_apk_url);
			context.startActivity(intent);
		}else if (activityinfo.equals(YNHNMainActivity)) {
			Intent intent = new Intent();
			intent.setAction("com.nxt.ynhnz.YNHNMainActivity");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.putExtra("titlename", titlename);
			intent.putExtra("updateurl", update_txt_url);
			intent.putExtra("versionplist", upload_apk_url);
			context.startActivity(intent);
		}
	}

}
