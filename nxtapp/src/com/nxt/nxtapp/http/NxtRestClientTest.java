package com.nxt.nxtapp.http;

/*
 * http数据传输实例
 */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.nxtapp.model.News;
import com.nxt.nxtapp.model.NewsRoot;

public class NxtRestClientTest {
	private static final String TAG = "NxtRestClientTest";
	private String data;

	/*
	 * 获取数据---get类型
	 */
	public static void getPublicJson() {

		NxtRestClient.get("json/expert.json", null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						LogUtil.LogI(TAG, content);
						// Json数据解析实例
						NewsRoot rootdata = (NewsRoot) JsonPaser
								.getObjectDatas(NewsRoot.class, content);
						List<News> datas = JsonPaser.getArrayDatas(News.class,
								rootdata.getNews());
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
					}
				});
	}

	/*
	 * 获取数据---post类型
	 */
	public void postPublicTimeline() {
		// Adding GET/POST Parameters with RequestParams
		// Create empty RequestParams and immediately add some parameters:
		RequestParams params = new RequestParams();
		params.put("key", "value");
		params.put("more", "data");
		NxtRestClient.post("json/ynt/expertlist.json", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						LogUtil.LogI(TAG, content);
						data = content;
					}

				});
	}

	/*
	 * 上传数据--文本文件
	 */
	public void postText(InputStream blan) {
		InputStream myInputStream = blan;
		RequestParams params = new RequestParams();
		params.put("secret_passwords", myInputStream, "passwords.txt");
		NxtRestClient.post("json/ynt/expertlist.json", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						LogUtil.LogI(TAG, content);
						data = content;
					}

				});
	}

	/*
	 * 上传数据--图片
	 */
	public void postPic() {

		try {
			File myFile = new File("/path/to/file.png");
			RequestParams params = new RequestParams();
			params.put("profile_picture", myFile);
			NxtRestClient.post("json/ynt/expertlist.json", params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							LogUtil.LogI(TAG, content);
							data = content;
						}

					});
		} catch (FileNotFoundException e) {

		}

	}

	/*
	 * 上传数据--音频
	 */
	public void postMp3(byte[] blan) {
		byte[] myByteArray = blan;
		RequestParams params = new RequestParams();
		params.put("soundtrack", new ByteArrayInputStream(myByteArray),
				"she-wolf.mp3");
		NxtRestClient.post("json/ynt/expertlist.json", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						LogUtil.LogI(TAG, content);
						data = content;
					}
				});
	}

}
