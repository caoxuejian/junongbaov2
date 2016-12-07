package com.nxt.nxtapp.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;

import android.content.Context;
import android.content.res.Resources;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.nxt.nxtapp.NXTAPPApplication;
import com.nxt.nxtapp.common.AESSafe;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.setting.GetHost;

public class NxtRestClientNew {
	private static final String BASE_URL = GetHost.getHost();
	private static AsyncHttpClient client = new AsyncHttpClient();
	private static String encode = "";
	private static Util util;
	private static String json_private;
	private static String json_public;
	private static String TAG = "NxtRestClientNew";

	public static void post(String method, Map<String, String> paramsMap,
			HttpCallBack responseHandler) {
		// Post
		int panduan = 0;
		ToJsonUtil toJson = new ToJsonUtil();
		if (paramsMap != null) {
			// 解析map,保存private里的信息
			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				toJson.put(entry.getKey(), entry.getValue());
			}
			json_private = toJson.toString();
		} else {
			json_private = null;
			paramsMap = new HashMap<String, String>();
			panduan = 1;
		}
		util = new Util(NXTAPPApplication.getInstance());
		// 保存public里的信息
		paramsMap.clear();
		paramsMap.put("uid", util.getFromSp(Util.UID, ""));
		paramsMap.put("version", util.getFromSp(Util.VERSION, ""));
		paramsMap.put("deviceid", util.getFromSp(Util.DEVICEID, ""));
		paramsMap.put("os", "a");
		paramsMap.put("app", util.getFromSp(Util.APPNAME, ""));
		paramsMap.put("token", util.getFromSp(Util.TOKEN, ""));
		paramsMap.put("long", util.getFromSp(Util.LONGITUDE, ""));
		paramsMap.put("lat", util.getFromSp(Util.LATITUDE, ""));
		paramsMap.put("province", util.getFromSp("province", ""));
		paramsMap.put("city", util.getFromSp("city", ""));
		paramsMap.put("district", util.getFromSp("district", ""));
		if (util.getFromSp("ifSysoLog", "").equals("yes")) {
			paramsMap.put("address", util.getFromSp("area", ""));
		} else {
			paramsMap.put("address", util.getFromSp("address", ""));
		}
		paramsMap.put("street", util.getFromSp("street", ""));
		paramsMap.put("StreetNumber", util.getFromSp("StreetNumber", ""));
		toJson = new ToJsonUtil();
		// 解析map
		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
			toJson.put(entry.getKey(), entry.getValue());
		}
		json_public = toJson.toString();
		toJson = new ToJsonUtil();
		toJson.put2("public", json_public);
		if (panduan == 0) {
			toJson.put2("private", json_private);
		} else {
			toJson.put2("private", "{}");
		}
		toJson.put("method", method);
		// 封装成json, 自写一个ToJsonUtil
		String json = toJson.toString();
		LogUtil.LogE(TAG, "post的参数json = " + json);
		// 对json加密
		if (method.equals("login") || method.equals("register")
				|| method.startsWith("test_") || method.equals("holidayimg")
				|| method.equals("forgetpass")
				|| method.equals("registermobile")) {
			// 第一次登陆时候用约定的key 必须是16倍数
			if (util.getFromSp(Util.PWD, "").equals("")
					|| util.getFromSp(Util.PWD, "") == null) {
				AESSafe.keyString = "nongxintong88888";
			} else {
				AESSafe.keyString = util.getFromSp(Util.PWD, "");
			}
		} else {
			if ("".equals(util.getFromSp(Util.TOKEN, ""))) {
				AESSafe.keyString = "nongxintong88888";
			} else {
				AESSafe.keyString = util.getFromSp(Util.PWD, "");
			}
		}
		String json_safe;
		// 加密时候第一次要执行的方法。
		AESSafe.first();
		json_safe = AESSafe.desEncrypt(json);
		// LogUtil.LogE(TAG,"使用的密钥为：   "+AESSafe.keyString+"  加密后json_safe  = "+
		// json_safe);
		RequestParams params = new RequestParams();
		if (method.equals("login") || method.equals("register")) {
			params.put("token", "\"\"");
		} else {
			params.put("token", util.getFromSp(Util.TOKEN, ""));
		}
		params.put("os", "a");
		params.put("body", json_safe);

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.post(ServerAPI.url, params, responseHandler);
	}

	public static void get(String method, Map paramsMap,
			HttpCallBack responseHandler) {
		// Get执行加密
		RequestParams params = null;
		// responseHandler.setCharset("utf_8");
		client.get(ServerAPI.url, getParams(params), responseHandler);
	}

	public static void getWithCookie(String url, RequestParams params,
			Context context, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), getParams(params), responseHandler);
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(myCookieStore);
		List<Cookie> cks = myCookieStore.getCookies();
//		for (int i = 0; i < cks.size(); i++) {
//			LogUtil.LogI(TAG, "读取cookie" + "- domain " + cks.get(i).getDomain());
//			LogUtil.LogI(TAG, "读取cookie" + "- path " + cks.get(i).getPath());
//			LogUtil.LogI(TAG, "读取cookie" + "- value " + cks.get(i).getValue());
//			LogUtil.LogI(TAG, "读取cookie" + "- name " + cks.get(i).getName());
//			LogUtil.LogI(TAG, "读取cookie" + "- port " + cks.get(i).getPorts());
//			LogUtil.LogI(TAG, "读取cookie" + "- comment " + cks.get(i).getComment());
//			LogUtil.LogI(TAG, "读取cookie" + "- commenturl" + cks.get(i).getCommentURL());
//			LogUtil.LogI(TAG, "读取cookie" + "- all " + cks.get(i).toString());
//		}
	}

	@SuppressWarnings("unused")
	private Resources getResources() {
		Resources mResources = null;
		mResources = getResources();
		return mResources;
	}

	public static void get(String url, RequestParams params,
			FileAsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), getParams(params), responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		client.setTimeout(20 * 1000);
		String url;
		if (relativeUrl != null && relativeUrl.startsWith("http")) {
			url = relativeUrl;
		} else if (relativeUrl != null && relativeUrl.startsWith("/")) {
			url = BASE_URL + relativeUrl;
		} else {
			url = BASE_URL + "/" + relativeUrl;
		}
		return url;
	}

	private static RequestParams getParams(RequestParams params) {
		try {
			Util util = Util.getUtil(NXTAPPApplication.getInstance());
			if (params == null)
				params = new RequestParams();
			params.put("uid", util.getFromSp(Util.UID, ""));
			params.put("os", "android");
			params.put("app", util.getFromSp(Util.APPNAME, ""));
			params.put("deviceid", util.getFromSp(Util.DEVICEID, ""));
			params.put("long", util.getFromSp(Util.LONGITUDE, "0"));
			params.put("lat", util.getFromSp(Util.LATITUDE, "0"));
			params.put("token", util.getFromSp(Util.TOKEN, "0"));
			params.put("province", util.getFromSp("province", ""));
			params.put("city", util.getFromSp("city", ""));
			params.put("district", util.getFromSp("district", ""));
			params.put("address", util.getFromSp("address", ""));
			params.put("street", util.getFromSp("street", ""));
			params.put("StreetNumber", util.getFromSp("StreetNumber", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return params;
	}
}
