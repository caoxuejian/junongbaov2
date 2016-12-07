package com.nxt.ynt.page;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.nxt.jnb.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.ynt.entity.AppConfigData;
import com.nxt.ynt.entity.Datas;
import com.nxt.ynt.entity.NewsRoot;

public class ReadRaw {
	private static String TAG = "ReadRaw";
	private static final int APPCONFIG = 1001;
	public static final int DATABASEINIT = 1002;
	public static final int DATAXinxitong = 1003;
	public static final int INTERESTED = 1004;
	public static final int CHAZHUBING = 1005;
	public static final int WEBSCHAME = 1006;
	public static final int NJZSZX = 1007;

	public static String getRawJson(Context context, int mode) {
		InputStream myFile = null;
		switch (mode) {
		case APPCONFIG:
			myFile = context.getResources().openRawResource(R.raw.appconfig);
			break;
		case DATABASEINIT:
			LogUtil.LogE(TAG, "R.raw.xnbmsg");
			myFile = context.getResources().openRawResource(R.raw.xnbmsg);
			break;
		case DATAXinxitong:
			myFile = context.getResources().openRawResource(R.raw.xinxitong);
			break;
		case WEBSCHAME:
			myFile = context.getResources().openRawResource(R.raw.webschame);
			break;
		default:
			break;
		}
		BufferedReader br = null;
		try {
			BufferedInputStream in = new BufferedInputStream(myFile);
			br = new BufferedReader(new InputStreamReader(in));// ×¢Òâ±àÂë
		} catch (Exception e1) {
			com.nxt.nxtapp.common.LogUtil.LogE(TAG, e1.toString());
		}
		String tmp;
		String appconfigstr = "";
		try {
			while ((tmp = br.readLine()) != null) {
				appconfigstr += tmp;
			}
			br.close();
			myFile.close();
			com.nxt.nxtapp.common.LogUtil.LogE(TAG, "appconfigstr=="
					+ appconfigstr);
			return appconfigstr;
		} catch (IOException e) {
			com.nxt.nxtapp.common.LogUtil.LogE(TAG, e.toString());
		}
		return null;
	}

	public static AppConfigData getAppConfigData(Context context) {
		String appconfig = getRawJson(context, APPCONFIG);
		if (appconfig == null) {
			LogUtil.LogE(TAG, appconfig);
			return null;
		}
		AppConfigData appConfigData = new AppConfigData();
		try {
			JSONObject job = new JSONObject(appconfig);
			String appinfo = job.getString("appinfo");
			JSONObject job_info = new JSONObject(appinfo);
			String navtype = job_info.getString("navtype");
			String homebutton = job_info.getString("homebutton");
			String isTourist = job_info.getString("isTourist");
			appConfigData.setNavtype(navtype);
			appConfigData.setHomebutton(homebutton);
			appConfigData.setIsTourist(isTourist);
			LogUtil.LogE(TAG, "homebutton==" + homebutton);
			String kindinfo = job.getString(homebutton);
			JSONObject job_kindinfo = new JSONObject(kindinfo);
			String mainactivity = job_kindinfo.getString("mainactivity");
			String appname = job_kindinfo.getString("appname");
			String updateurl = job_kindinfo.getString("updateurl");
			String versionplist = job_kindinfo.getString("versionplist");
			String siteid = job_kindinfo.getString("siteid");
			String areaid = job_kindinfo.getString("areaid");
			String autoRegister = job_kindinfo.getString("autoRegister");
			String menuurl = job_kindinfo.getString("newsurl");
			String xnbmsg = job_kindinfo.getString("xnbmsg");
			String showmsg = job_kindinfo.getString("showmsg");
			appConfigData.setXnbmsg(xnbmsg);
			appConfigData.setShowmsg(showmsg);
			appConfigData.setAutoRegister(autoRegister);
			appConfigData.setMenuurl(menuurl);
			appConfigData.setMainactivity(mainactivity);
			appConfigData.setAppname(appname);
			appConfigData.setUpdateurl(updateurl);
			appConfigData.setVersionplist(versionplist);
			appConfigData.setSiteid(siteid);
			appConfigData.setAreaid(areaid);
			return appConfigData;
		} catch (JSONException e) {
			e.printStackTrace();
			LogUtil.LogE(TAG, e.toString());
		}
		return null;
	}
	
	public static List<Datas> getInterested(Context context) {
		String appconfig = getRawJson(context, INTERESTED);
		if (appconfig == null) {
			LogUtil.LogE(TAG, appconfig);
			return null;
		}
		NewsRoot news = (NewsRoot) com.nxt.nxtapp.json.JsonPaser
				.getObjectDatas(NewsRoot.class, appconfig);
		List<Datas> datas = com.nxt.nxtapp.json.JsonPaser.getArrayDatas(
				Datas.class, news.getNews());
		return datas;
	}

}
