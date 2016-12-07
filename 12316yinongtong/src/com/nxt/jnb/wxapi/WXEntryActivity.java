package com.nxt.jnb.wxapi;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.ynt.utils.JsonUtils;
import com.nxt.ynt.utils.Util;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{

	private IWXAPI api;
	private String appname;
	protected static final int RETURN_OPENID_ACCESSTOKEN = 0;// 返回openid，accessToken消息码
	protected static final int RETURN_NICKNAME_UID = 1; // 返回昵称，uid消息码

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Util util = new Util(this);
		appname = util.getFromSp(Util.APPNAME, "");
		api = WXAPIFactory.createWXAPI(this,getResources().getString(R.string.wx_key), false);
		api.registerApp(getResources().getString(R.string.wx_key));
		api.handleIntent(getIntent(), this);
	}


	public void onResp(com.tencent.mm.sdk.modelbase.BaseResp resp)
	{
		/*SendAuth.Resp sendAuthResp = (Resp) resp;// 用于分享时不要有这个，不能强转
		String code = sendAuthResp.code;
		getResult(code);
		int errCode = resp.errCode;
		Toast.makeText(this, "errCode = " + errCode, 3000).show();*/
		int result = 0;

		switch (resp.errCode)
		{
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		finish();
		overridePendingTransition(R.anim.change_in, R.anim.change_out);
	}


	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 获取openid accessToken值用于后期操作
	 * @param code 请求码
	 */
	private void getResult(final String code) {
		new Thread() {// 开启工作线程进行网络请求
			public void run() {
				String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
						+ getResources().getString(R.string.wx_key)
						+ "&secret="
						+ getResources().getString(R.string.wxsecret)
						+ "&code="
						+ code
						+ "&grant_type=authorization_code";
				try {
					JSONObject jsonObject = JsonUtils
							.initSSLWithHttpClinet(path);// 请求https连接并得到json结果
					if (null != jsonObject) {
						String openid = jsonObject.getString("openid")
								.toString().trim();
						String access_token = jsonObject
								.getString("access_token").toString().trim();

						Message msg = handler1.obtainMessage();
						msg.what = RETURN_OPENID_ACCESSTOKEN;
						Bundle bundle = new Bundle();
						bundle.putString("openid", openid);
						bundle.putString("access_token", access_token);
						msg.obj = bundle;
						handler1.sendMessage(msg);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return;
			};
		}.start();
	}
	
	/**
	 * 获取用户唯一标识
	 * @param openId
	 * @param accessToken
	 */
	private void getUID(final String openId, final String accessToken) {
		new Thread() {
			@Override
			public void run() {
				//https://api.weixin.qq.com/sns/userinfo?access_token=OezXcEiiBSKSxW0eoylIeBn-fQbjXNyFLEmXLibNlpLie4dgmxScEXQVewhz-D07sVHb3I3Mos3s6K1dMvwyrroS4Z9Vcd66OFdVp98LM00M-QpWeCUGPXtdICOBm0zNIxAbwIIKYUuhnjTmUXmipw&openid=null
				String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
						+ accessToken + "&openid=" + openId;
				
				JSONObject jsonObject = null;
				try {
	//{"sex":1,"nickname":"相见恨晚","unionid":"ozjjbjnY4uT9yXXp9i6y0JN0B4DM","privilege":[],"province":"","openid":"oKiKZs-zCC8R80fRqFwd7ulmWrLg","language":"zh_CN","headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/er5xIpIDxnqU2W35Aw2GO1OdBxlo6Lh3EiaWpA1Ycbt7Y427ROVLiaTNmszmAdDztoy5GESgBNpLNLqJGhGFYVgOqXPxxOAaOQ\/0","country":"CN","city":""}

					jsonObject = JsonUtils.initSSLWithHttpClinet(path);
					String nickname = jsonObject.getString("nickname");
					String unionid = jsonObject.getString("unionid");

					Message msg = handler1.obtainMessage();
					msg.what = RETURN_NICKNAME_UID;
					Bundle bundle = new Bundle();
					bundle.putString("nickname", nickname);
					bundle.putString("unionid", unionid);
					msg.obj = bundle;
					handler1.sendMessage(msg);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	private Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RETURN_OPENID_ACCESSTOKEN:
				Bundle bundle1 = (Bundle) msg.obj;
				String accessToken = bundle1.getString("access_token");
				String openId = bundle1.getString("openid");

				getUID(openId, accessToken);
				break;

			case RETURN_NICKNAME_UID:
				Bundle bundle2 = (Bundle) msg.obj;
				String nickname = bundle2.getString("nickname");
				String uid = bundle2.getString("unionid");
				break;

			default:
				break;
			}
		};
	};
}