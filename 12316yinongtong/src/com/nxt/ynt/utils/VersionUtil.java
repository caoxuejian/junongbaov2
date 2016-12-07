package com.nxt.ynt.utils;

/*
 * 版本更新工具类
 */

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nxt.nxtapp.NXTAPPApplication;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.model.AlertMsg;
import com.nxt.ynt.AlertMsgs;
import com.nxt.ynt.entity.Version;

public class VersionUtil {
	private Context context;
	private String upload_apk_url;
	private String update_txt_url;
	private Handler myHandler;
	private String TAG = "VersionUtil";

	public VersionUtil(Context context) {
		super();
		this.context = context;
	}

	public VersionUtil(Context context, String update_txt_url) {
		super();
		this.context = context;
		this.update_txt_url = update_txt_url;
	}

	public VersionUtil(Context context, String upload_apk_url,
			String update_txt_url, Handler myHandler) {
		super();
		this.context = context;
		this.upload_apk_url = upload_apk_url;
		this.update_txt_url = update_txt_url;
		this.myHandler = myHandler;
	}

	public VersionUtil(Context context, String upload_apk_url,
			String update_txt_url) {
		super();
		this.context = context;
		this.upload_apk_url = upload_apk_url;
		this.update_txt_url = update_txt_url;
	}

	// 判断是否有网络
	public static boolean isNetWork(Context context) {
		boolean b = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo isNetWork = cwjManager.getActiveNetworkInfo();
		if (isNetWork != null)
			b = true;
		return b;
	}

	// 判断版本是否需要更新
	private void checkVersion(final String version_url) {
		NxtRestClient.get(version_url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1, arg2);
				try {
					String content = new String(arg2, "UTF-8");
					LogUtil.syso("版本新消息：" + content);
					Version version = (Version) com.nxt.nxtapp.json.JsonPaser
							.getObjectDatas(Version.class, content);
					// 判断是否需要更新
					int curVersion = NXTAPPApplication.versonCode;
					int newVersion = Integer.parseInt(version.getVersioncode());
					LogUtil.syso("@curVersion:" + curVersion + " @newVersion:"
							+ newVersion);
					if (newVersion > curVersion) {
						NXTAPPApplication.newVersion= "";
						showMsg(AlertMsgs.UPDATEVERSIN,
								version.getUpdatecontent(),
								version.getNewstype());
					} else {
						NXTAPPApplication.newVersion= "已是最新版本";
						if (myHandler != null) {
							LogUtil.LogE(TAG, "**********************");
							Message msg = new Message();
							msg.what = 100;
							myHandler.sendMessage(msg);
						}
					}
					// 判断有没有提示信息
					if (version.getAlert() != null
							&& !version.getAlert().equals(""))
						alertmsg(version.getAlert());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}


		});
	}

	private void alertmsg(String alertMsg) throws Exception {
		AlertMsg msg = (AlertMsg) com.nxt.nxtapp.json.JsonPaser.getObjectDatas(
				AlertMsg.class, alertMsg);
		String id = msg.getAlertid();
		int updateVersion = Integer.parseInt(id);
		if (compareToValue(updateVersion, Util.ALERTID)) {
			showMsg(AlertMsgs.ALERTMSG, msg.getAlertmsg(), "alert");
			Util util = new Util(context);
			util.saveToSp(Util.ALERTID, updateVersion);
		}
	}

	private boolean compareToValue(int updateVersion, String tag)
			throws Exception {
		com.nxt.nxtapp.common.LogUtil.syso("tag:" + tag);
		Util util = new Util(context);
		int currentVersion = util.getFromSp(tag, 0);
		if (updateVersion > currentVersion)
			return true;
		return false;
	}

	private void showMsg(int mode, String path, String newstype) {
		Intent intent = new Intent(context, AlertMsgs.class);
		intent.putExtra("mode", mode);
		intent.putExtra("path", path);
		intent.putExtra("newstype", newstype);
		intent.putExtra("versionplist", upload_apk_url);
		LogUtil.LogE("VersionUtil", "path==" + path + ";newstype==" + newstype
				+ ";update_url==" + upload_apk_url);
		context.startActivity(intent);
	}

	public void doVersion() {
		if (isNetWork(context))
			checkVersion(update_txt_url);
	}

}
