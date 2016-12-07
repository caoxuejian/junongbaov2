package com.nxt.nxtapp.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.nxt.nxtapp.model.AppConfigData;

public class ReadRaw {
	private static final int APPCONFIG = 1001;
	public static final int DATABASEINIT = 1002;
	public static String getRawJson(Context context,int mode) {

		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(context.getResources()
		// .openRawResource(R.raw.appconfig)));
		// String appconfig = reader.toString();
		InputStream myFile = null;
		switch (mode) {
		case APPCONFIG:
			//myFile = context.getResources().openRawResource(R.raw.appconfig);
			break;
		default:
			break;
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(myFile, "utf-8"));// ×¢Òâ±àÂë
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			com.nxt.nxtapp.common.LogUtil.LogE("debug", e1.toString());
		}
		String tmp;
		String appconfigstr = "";
		try {
			while ((tmp = br.readLine()) != null) {
				appconfigstr += tmp;
			}
			br.close();
			myFile.close();
			com.nxt.nxtapp.common.LogUtil.LogE("123", appconfigstr);
			return appconfigstr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			com.nxt.nxtapp.common.LogUtil.LogE("debug", e.toString());

		}

		return null;

	}

	public static AppConfigData getAppConfigData(Context context) {
		String appconfig = getRawJson(context,APPCONFIG);
		if (appconfig == null) {
			return null;
		}
		AppConfigData appConfigData = new AppConfigData();

		try {
			JSONObject job = new JSONObject(appconfig);
			String appinfo = job.getString("appinfo");
			JSONObject job_info = new JSONObject(appinfo);
			String navtype = job_info.getString("navtype");
			String homebutton = job_info.getString("homebutton");
			String meishidaolan = job_info.getString("meishidaolan");
			appConfigData.setNavtype(navtype);
			appConfigData.setHomebutton(homebutton);
			appConfigData.setMeishidaolan(meishidaolan);
			String kindinfo = job.getString(homebutton);
			JSONObject job_kindinfo = new JSONObject(kindinfo);
			String mainactivity = job_kindinfo.getString("mainactivity");
			String appname = job_kindinfo.getString("appname");
			String updateurl = job_kindinfo.getString("updateurl");
			String versionplist = job_kindinfo.getString("versionplist");
			String siteid = job_kindinfo.getString("siteid");
			String areaid = job_kindinfo.getString("areaid");
			appConfigData.setMainactivity(mainactivity);
			appConfigData.setAppname(appname);
			appConfigData.setUpdateurl(updateurl);
			appConfigData.setVersionplist(versionplist);
			appConfigData.setSiteid(siteid);
			appConfigData.setAreaid(areaid);
			return appConfigData;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

}
