package com.nxt.nxtapp.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.apache.http.cookie.Cookie;
import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.nxt.nxtapp.NXTAPPApplication;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.setting.GetHost;

public class NxtRestClient {
	private static final String BASE_URL = GetHost.getHost();
	private static AsyncHttpClient client = new AsyncHttpClient();
	private static String encode = "";

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		// responseHandler.setCharset("utf_8");
		client.get(getAbsoluteUrl(url), getParams(params), responseHandler);
	}

	public static void getWithCookie(String url, RequestParams params,
			Context context, AsyncHttpResponseHandler responseHandler) {
		if (url != null) {
			client.get(getAbsoluteUrl(url), getParams(params), responseHandler);
		}
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(myCookieStore);
		List<Cookie> cks = myCookieStore.getCookies();
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), getParams(params), responseHandler);
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
			params.put("token", util.getFromSp(Util.TOKEN, ""));
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

	/**
	 * 使锟斤拷HttpURLConnection锟斤拷锟结供锟侥凤拷锟斤拷锟斤拷锟接凤拷锟斤拷锟斤拷 锟接凤拷锟斤拷锟斤拷锟斤拷取锟斤拷锟斤拷锟斤拷
	 * 
	 * @throws IOException
	 */
	public static InputStream httpConnStream(String strUrl, String reqMethod,
			Map<String, String> params) throws IOException {
		StringBuffer sb = new StringBuffer();
		// 锟斤拷锟斤拷锟斤拷源锟斤拷锟斤拷装url锟斤拷址
		URL url = new URL(strUrl);
		// 锟斤拷取锟斤拷锟斤拷
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		// 锟斤拷锟斤拷锟斤拷锟斤拷式
		huc.setRequestMethod(reqMethod);
		huc.setDoOutput(true);
		// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		if ("POST".equals(reqMethod) && params != null && params.size() > 0) {
			sb.append(strUrl);
			OutputStream os = huc.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			// osw.write("?");
			boolean flag = false;
			for (String key : params.keySet()) {
				if (flag) {// 锟斤拷一锟斤拷拼锟接碉拷时锟津不硷拷&
					osw.write("&");
					sb.append("&");
				}
				osw.write(key + "=" + params.get(key));
				sb.append(key + "=" + params.get(key));
				flag = true;
			}
			LogUtil.LogI("NxtRestClient", "URL:" + sb.toString());
			osw.flush();
		}
		// 头锟斤拷息
		encode = huc.getContentType();
		if (encode != null && encode.toLowerCase().contains("gbk")) {
			encode = "gbk";
		} else {
			encode = "utf-8";
		}
		// 锟斤拷取锟斤拷锟斤拷锟斤拷
		InputStream is = huc.getInputStream();
		return is;
	}

	/**
	 * 锟斤拷锟接凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷
	 * 
	 * @throws IOException
	 */
	public static String httpConnString(String url, String reqMethod,
			Map<String, String> params) throws IOException {
		InputStream is = httpConnStream(url, reqMethod, params);
		int length = -1;
		StringBuffer sb = new StringBuffer();
		char[] buffer = new char[1024];
		InputStreamReader isr = new InputStreamReader(is, encode);
		while ((length = isr.read(buffer)) != -1) {
			sb.append(buffer, 0, length);
		}
		return sb.toString();
	}
}
