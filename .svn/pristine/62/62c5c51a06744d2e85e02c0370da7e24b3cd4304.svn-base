package com.nxt.nxtapp.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.nxt.nxtapp.common.CacheData;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.nxtapp.setting.Constant;

public class MyTask extends AsyncTask<String, Void, String> {
	private BackUI backUI;
	private Context context;
	private SharedPreferences sf;
	private NetworkInfo isNetWork;
	private String str;

	public MyTask(Context context, UIPrompt uiPrompt, BackUI backUI) {
		this.backUI = backUI;
		this.context = context;
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		isNetWork = cwjManager.getActiveNetworkInfo();
	}

	public MyTask(Context context, BackUI backUI) {
		this.backUI = backUI;
		this.context = context;
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		isNetWork = cwjManager.getActiveNetworkInfo();
	}

	public MyTask(Context context, BackUI backUI, String str) {
		this.str = str;
		this.backUI = backUI;
		this.context = context;
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		isNetWork = cwjManager.getActiveNetworkInfo();
	}

	@Override
	protected String doInBackground(String... params) {
		sf = context.getSharedPreferences(Constant.SPF_DATE,
				context.MODE_PRIVATE);
		String jsonString = null;
		String path = params[0];
		// 首先获取缓存文件
		String cacheData = CacheData.getCacheData(path);
		if (str != null) {
			if (isNetWork == null) {
				jsonString = cacheData;
			} else if (isNetWork != null) {
				LogUtil.LogI("MyTask", "刷新获取网络数据");
				jsonString = JsonPaser.getCOONJsonString(path);// 获取服务器数据
				if (jsonString != null) {
					CacheData.saveCacheData(jsonString, path);
					LogUtil.LogI("MyTask", "保存刷新后获取的网络数据");
				} else {
					jsonString = cacheData;
				}
			}
		} else {
			if (cacheData != null) {
				// 如果缓存内容不为null，
				// 首先判断时间标志位，判断是否需要去服务器加载最新数据
				// 如果标志位为yes
				// 取出缓存的内容，即要返回的json字符串
				if (sf.getString(Constant.SPF_DATE_FLAG, "no").equals("no")) {
					jsonString = cacheData;
				} else {
					if (isNetWork == null) {
						jsonString = cacheData;
					} else if (isNetWork != null) {
						// 否则依然从服务下载最新数据并保存
						jsonString = JsonPaser.getCOONJsonString(path);// 获取服务器数据
						// com.nxt.nxtapp.common.LogUtil.syso("cacheData  jsonString:"+jsonString);
						if (jsonString != null) {
							// 如果服务器返回的数据不是null就返回jsonString，并将服务器获取到的内容保存到sd卡下
							CacheData.saveCacheData(jsonString, path);
						} else {
							// 如果从服务器仍然没有获取到数据，则将缓存内容返回
							jsonString = cacheData;
						}
					}
				}
			} else {
				// 如果缓存的内容为null，去服务器下载数据，保存，并且将下载的内容的json字符串保存到缓存文件中
				jsonString = JsonPaser.getCOONJsonString(path);// 获取服务器数据
				if (jsonString != null) {
					CacheData cd = new CacheData(context);
					cd.saveCacheData(jsonString, path);
				}
			}
		}
		 LogUtil.LogD("MyTask","jsonString:" + jsonString);

		return jsonString;
	}

	@Override
	protected void onPostExecute(String result) {

		backUI.back(result);
		super.onPostExecute(result);

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	public interface UIPrompt {
		public void show();
	}

	public interface BackUI {
		public void back(String result);
	}

}
