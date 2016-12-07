package com.nxt.nxtapp.http;

import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nxt.nxtapp.NXTAPPApplication;
import com.nxt.nxtapp.common.AESSafe;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.Util;

public class HttpCallBack extends AsyncHttpResponseHandler {
	private int tongji = 0;

	public String getContent(String content) {
		Util util = new Util(NXTAPPApplication.getInstance());
		// LogUtil.LogI("HttpCallBack", "=========== ���������ص� content = " +
		// content);
		try {
			JSONObject json = new JSONObject(content);
			int errorcode = (Integer) json.get("errorcode");
			if (errorcode == 0) {
				String json_safe = (String) json.get("msg");
				// AESSafe.first();
				content = AESSafe.desDecrypt(json_safe);
				// LogUtil.LogI("HttpCallBack",
				// "=================== ���ܺ� --content  = "+ content);
				if (AESSafe.keyString.equals("nongxintong88888")) {
					AESSafe.keyString = util.getFromSp(Util.PWD, "");
					AESSafe.first();
				}
				json = new JSONObject(content);
				content = json.getString("entity");
			} else if (errorcode == 1) {
				String json_msg = (String) json.get("msg");
				LogUtil.LogI("HttpCallBack", "errorcode =1 json= " + json_msg);
			} else {
				String json_msg = (String) json.get("msg");
				LogUtil.LogI("HttpCallBack", "errorcode = " + errorcode
						+ "  json= " + json_msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public int AutoLoading(String method, Map<String, String> paramsMap,
			HttpCallBack callback) {
		if (tongji != 3) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (paramsMap == null) {
				NxtRestClientNew.post(method, null, callback);
			} else {
				NxtRestClientNew.post(method, paramsMap, callback);
			}
			tongji++;
			// try {
			// this.wait();
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}
		return tongji;
	}

}
