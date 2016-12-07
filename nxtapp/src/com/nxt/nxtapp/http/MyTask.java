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
		// ���Ȼ�ȡ�����ļ�
		String cacheData = CacheData.getCacheData(path);
		if (str != null) {
			if (isNetWork == null) {
				jsonString = cacheData;
			} else if (isNetWork != null) {
				LogUtil.LogI("MyTask", "ˢ�»�ȡ��������");
				jsonString = JsonPaser.getCOONJsonString(path);// ��ȡ����������
				if (jsonString != null) {
					CacheData.saveCacheData(jsonString, path);
					LogUtil.LogI("MyTask", "����ˢ�º��ȡ����������");
				} else {
					jsonString = cacheData;
				}
			}
		} else {
			if (cacheData != null) {
				// ����������ݲ�Ϊnull��
				// �����ж�ʱ���־λ���ж��Ƿ���Ҫȥ������������������
				// �����־λΪyes
				// ȡ����������ݣ���Ҫ���ص�json�ַ���
				if (sf.getString(Constant.SPF_DATE_FLAG, "no").equals("no")) {
					jsonString = cacheData;
				} else {
					if (isNetWork == null) {
						jsonString = cacheData;
					} else if (isNetWork != null) {
						// ������Ȼ�ӷ��������������ݲ�����
						jsonString = JsonPaser.getCOONJsonString(path);// ��ȡ����������
						// com.nxt.nxtapp.common.LogUtil.syso("cacheData  jsonString:"+jsonString);
						if (jsonString != null) {
							// ������������ص����ݲ���null�ͷ���jsonString��������������ȡ�������ݱ��浽sd����
							CacheData.saveCacheData(jsonString, path);
						} else {
							// ����ӷ�������Ȼû�л�ȡ�����ݣ��򽫻������ݷ���
							jsonString = cacheData;
						}
					}
				}
			} else {
				// ������������Ϊnull��ȥ�������������ݣ����棬���ҽ����ص����ݵ�json�ַ������浽�����ļ���
				jsonString = JsonPaser.getCOONJsonString(path);// ��ȡ����������
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
