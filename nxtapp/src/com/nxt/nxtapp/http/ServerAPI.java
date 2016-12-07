package com.nxt.nxtapp.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.nxt.nxtapp.R;
import com.nxt.nxtapp.setting.GetHost;

public class ServerAPI {
//	public static String url =GetHost.getHost()+ "/meilisannong/server/index.php/api_v2/api";
	public static String url = GetHost.getHost()+"/meilisannong/server/index.php/api_v2/api";

//
//	public static String getUrl() {
//		if (url == null) {
//			InputStream in = context.getResources().openRawResource(
//					R.raw.serverapi);
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			int i = -1;
//
//			try {
//				while ((i = in.read()) != -1) {
//					baos.write(i);
//				}
//
//				url = baos.toString();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//		return url;
//
//	}
}
