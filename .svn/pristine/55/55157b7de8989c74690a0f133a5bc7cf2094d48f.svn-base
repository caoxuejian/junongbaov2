package com.nxt.nxtapp.version.util;

/*
 * 版本更新工具类
 */
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nxt.nxtapp.AlertMsgs;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.nxtapp.model.AlertMsg;
import com.nxt.nxtapp.model.Version;

public class VersionUtil {
	private static final String TAG = "VersionUtil";
	private Context context;
	private String update_url;
	private String version_url;
	private Handler hd;

	public VersionUtil(Context context) {
		super();
		this.context = context;
	}

	public VersionUtil(Context context, String version_url) {
		super();
		this.context = context;
		this.version_url = version_url;
	}

	public VersionUtil(Handler hd, Context context, String update_url,
			String version_url) {
		super();
		this.hd = hd;
		this.context = context;
		this.update_url = update_url;
		this.version_url = version_url;
		doVersion();
	}

	public VersionUtil(Context context, String update_url, String version_url) {
		super();

		this.context = context;
		this.update_url = update_url;
		this.version_url = version_url;
		doVersion();
	}

	// 判断是否有网络
	public boolean isNetWork(Context context) {
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
				LogUtil.LogI(TAG, "onFailure = " + content);
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				LogUtil.LogI(TAG, content);
				try {
					Version version = (Version) JsonPaser.getObjectDatas(
							Version.class, content);
					// 判断是否需要更新
					int curVersion = context.getPackageManager()
							.getPackageInfo(Util.getPackageName(context), 0).versionCode;
					LogUtil.LogI(
							TAG,
							"version.getVersioncode():"
									+ version.getVersioncode());
					int newVersion = Integer.parseInt(version.getVersioncode());
					if (newVersion > curVersion) {
						showMsg(AlertMsgs.UPDATEVERSIN,
								version.getUpdatecontent(),
								version.getNewstype());
					} else {
						Message msg = new Message();
						msg.what = 1;
						hd.sendMessage(msg);
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
		AlertMsg msg = (AlertMsg) JsonPaser.getObjectDatas(AlertMsg.class,
				alertMsg);
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
		LogUtil.LogI(TAG, "tag:" + tag);
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
		intent.putExtra("versionplist", update_url);
		context.startActivity(intent);
	}

	public void doVersion() {
		new Thread() {
			@Override
			public void run() {
				if (isNetWork(context))
					checkVersion(version_url);
			}
		}.start();

	}
}
