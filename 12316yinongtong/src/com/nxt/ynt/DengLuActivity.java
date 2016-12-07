package com.nxt.ynt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.MD5;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.entity.LoginInfo;
import com.nxt.ynt.imageutil.ImageLoader;
import com.nxt.ynt.page.IntentActivityUtil;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.CustomProgressDialog;
import com.nxt.ynt.utils.StringUtils;
import com.nxt.ynt.utils.Util;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class DengLuActivity extends Activity implements OnClickListener {
	private String TAG = "DengLuActivity";
	private LinearLayout historyuser, ll_number;
	private EditText editText_zh, editText_pw;
	private TextView textView_zh, forget_paw;
	private ImageView faceImage;
	private Button button_dl, button_zc, button_changeuser;
	ImageView button_back;
	private String str_zh, str_pw, str_picurl;
	private Util util;
	private SoftApplication appState;
	private Context context;
	private ImageLoader loader;
	private Button button_yk;
	private String mainactivity, appname, updateurl, versionplist, homebutton,
	mDeviceID, isTourist;
	private LoginInfo login;
	private WebView webView;
	public Handler handler = new Handler() {



		public void handleMessage(Message msg) {
			login = (LoginInfo) msg.obj;
			if (login != null) {
				if (login.getErrorcode() != null
						&& login.getErrorcode().equals("0")) {
					uploadlocation();//更新当前坐标
					//加载页面之前先同步一下cookie
					syncookie();


				} else if("2".equals(login.getErrorcode())){
					SettingActivity.CleanInf(util, context);
					login(str_zh, str_pw);
				}else {
					util.saveToSp(Util.TOKEN, "");
					util.saveToSp(Util.PWD, "");
					Util.showMsg(context, login.getMsg());
					dialog.cancel();
				}
			}
		}
		private void syncookie() {
			// TODO Auto-generated method stub
			webView=new WebView(DengLuActivity.this);
			WebViewClient client=new WebViewClient(){
				public void onPageStarted(WebView arg0, String arg1, android.graphics.Bitmap arg2) {
					//					time = new TimeCount(8000, 1000);// 构造CountDownTimer对象
					//					time.start();
				};
				@Override
				public void onPageFinished(WebView arg0, String arg1) {
					// TODO Auto-generated method stub
					super.onPageFinished(arg0, arg1);
					saveinfo();
				}

			};
			webView.setWebViewClient(client);
			webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
			webView.loadUrl(Constans.FX_GOUWU+ "?" + "token="
					+ login.getToken());
			webView.setVisibility(View.GONE);
			/*WebView view=new WebView(getApplicationContext());
			view.setWebViewClient(new WebViewClient());
			view.setWebChromeClient(new WebChromeClient());
			view.getSettings().setJavaScriptEnabled(true);
			view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
			view.loadUrl(Constans.FX_GOUWU+ "?" + "token="
					+ token);
			view.setVisibility(View.GONE);*/
			/*com.tencent.smtt.sdk.WebView webView=new com.tencent.smtt.sdk.WebView(getApplicationContext());
			webView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient());
			webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
			webView.loadUrl(Constans.FX_GOUWU+ "?" + "token="
					+ token);
			webView.setVisibility(View.GONE);*/
			/*AsyncHttpClient  client=new AsyncHttpClient ();
			//保存cookie，自动保存到了shareprefercece  
			PersistentCookieStore myCookieStore = new PersistentCookieStore(DengLuActivity.this);   
			client.setCookieStore(myCookieStore);
			client.post(Constans.FX_GOUWU+ "?" + "token="
					+ token, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String content) {
					// TODO Auto-generated method stub
					super.onSuccess(content);
					pdlogin.cancel();
					LoginInfo rootdata = (LoginInfo) JsonPaser.getObjectDatas(
							LoginInfo.class, content);
					Message msg = handler.obtainMessage();
					msg.obj = rootdata;
					handler.sendMessage(msg);
					pdlogin.cancel();
					//获取cookie
					PersistentCookieStore myCookieStore = new PersistentCookieStore(DengLuActivity.this);  
					List<Cookie> cookies = myCookieStore.getCookies(); 
					for(Cookie cookie:cookies){
						System.out.println("@@@@@@@@@@@"+cookie.getName()+"@"+cookie.getValue());
						if("ECS_ID".equals(cookie.getName())){
							util.saveToSp("ECS_ID", cookie.getValue());
						}
					}

					util.saveToSp("session_id", cookies.get(0).getValue());
				}
				@Override
				public void onFailure(Throwable error,
						String content) {
					super.onFailure(error, content);
				}
			});*/
		};
	};
	private Bundle bundle;
	private int flag=0;
	private String ifback;
	private CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		util = new Util(context);
		setContentView(R.layout.activity_denglu);
		LogUtil.LogE(TAG, "登录界面");
		isTourist = util.getFromSp("isTourist", "");
		mainactivity = util.getFromSp(Util.MAINACTIVITY, "");
		updateurl = util.getFromSp(Util.CHECK, "");
		versionplist = util.getFromSp(Util.DOWNLOAD, "");
		appname = util.getFromSp(Util.APPNAME, "");
		mDeviceID = util.getFromSp(Util.IMEI, "");
		homebutton = util.getFromSp(Util.HOMEBUTTON, "");
		bundle= getIntent().getExtras();
		flag=bundle.getInt("flag");
		ifback=bundle.getString("ifback");
		findViewId();
	}

	private void findViewId() {
		historyuser = (LinearLayout) findViewById(R.id.historyuser);
		ll_number = (LinearLayout) findViewById(R.id.ll_number);
		button_changeuser = (Button) findViewById(R.id.changeuser);
		button_back = (ImageView) findViewById(R.id.news_view_back);
		editText_zh = (EditText) findViewById(R.id.edit_zh);
		textView_zh = (TextView) findViewById(R.id.tv_zh);
		forget_paw = (TextView) findViewById(R.id.forget_paw);
		editText_pw = (EditText) findViewById(R.id.edit_password);
		faceImage = (ImageView) findViewById(R.id.login_head_img);
		button_dl = (Button) findViewById(R.id.denglu_btn);
		button_zc = (Button) findViewById(R.id.zhuce_btn);
		button_yk = (Button) findViewById(R.id.youke_btn);
		str_zh = util.getFromSp(Util.UNAME, "");
		str_picurl = util.getFromSp(Util.UPIC, "");
		if("true".equals(ifback)){
			button_back.setVisibility(View.VISIBLE);
		}else{
			button_back.setVisibility(View.GONE);
		}
		if (!"true".equals(isTourist)) {
			button_yk.setVisibility(View.GONE);
		}
		if (!str_zh.equals("") && str_zh.length() == 11) {
			button_changeuser.setVisibility(View.VISIBLE);
			historyuser.setVisibility(View.VISIBLE);
			ll_number.setVisibility(View.GONE);
			textView_zh.setText(str_zh);
			if (str_picurl != null && !"".equals(str_picurl)) {
				loader = ImageLoader.getInstance(context);
				loader.displayImage(Constans.getHeadUri(str_picurl), faceImage);
			} else {
				faceImage.setImageResource(R.drawable.contractdefalut);
			}
		}
		button_dl.setOnClickListener(this);
		button_zc.setOnClickListener(this);
		forget_paw.setOnClickListener(this);
		button_yk.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.denglu_btn) {
			ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cwjManager.getActiveNetworkInfo() != null) {
				if (ll_number.getVisibility() == View.VISIBLE) {
					str_zh = editText_zh.getText().toString();
				}
				str_pw = editText_pw.getText().toString();
				if (str_zh.equals("") || str_pw.equals("")) {
					Toast.makeText(context, "账号密码不能为空！", 0).show();
				} else {
					login(str_zh, str_pw);
				}
			} else
				Toast.makeText(context, "请检查您的网络是否连接", 0).show();
		} else if (id == R.id.zhuce_btn) {
			startActivity(new Intent(context, RegisterActivity.class));
		} else if (id == R.id.changeuser) {
			ll_number.setVisibility(View.VISIBLE);
			button_back.setVisibility(View.VISIBLE);
			button_changeuser.setVisibility(View.GONE);
			historyuser.setVisibility(View.GONE);
		} else if (id == R.id.news_view_back) {
			finish();
		} else if (id == R.id.forget_paw) {
			startActivity(new Intent(context, ChangePasswordActivity.class));
		} else if (id == R.id.youke_btn) {
			LogUtil.LogE(TAG, "游客登录按钮");
//			pdlogin = new ProgressDialog(context);
//			pdlogin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			pdlogin.setCancelable(false);
//			pdlogin.setMessage("登录中,请稍后...");
//			pdlogin.show();
			dialog =new CustomProgressDialog(this,R.anim.loadingframe);
			dialog.setMessage("登录中...");
			dialog.show();
			register();
		}
	}

	public void register() {
		LogUtil.LogE(TAG, "游客身份进入");
		Map<String, String> params = new HashMap<String, String>();
		params.put("siteid", getResources().getString(R.string.siteid));
		params.put("uname", mDeviceID);
		params.put("pwd", "123456");
		params.put("status", "0");
		NxtRestClientNew.post("register", params, new HttpCallBack() {

			@Override
			public void onSuccess(String content) {
				content = this.getContent(content);
				// Json数据解析实例
				LoginInfo rootdata = (LoginInfo) JsonPaser.getObjectDatas(
						LoginInfo.class, content);
				com.nxt.nxtapp.common.LogUtil.syso("rootdata:" + rootdata);
				LogUtil.LogE(TAG, rootdata.getUid() + "######");
				util.saveToSp(Util.UID, rootdata.getUid());
				util.saveToSp(Util.UNAME, mDeviceID);
				util.saveToSp(Util.NICK, "游客" + mDeviceID);
				util.saveToSp(Util.TOKEN, rootdata.getToken());
				util.saveToSp(Util.status, rootdata.getStatus());
				util.saveToSp(Util.YN_PAY, "0");//是否订阅
				if (StringUtils.isNotEmty(rootdata.getUid())
						&& rootdata.getErrorcode().equals("1")) {
					IntentActivityUtil.toActivity(context, mainactivity,
							appname, updateurl, versionplist, homebutton);
					dialog.cancel();
					finish();
				} else {
					util.showMsg(context, rootdata.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.LogE(TAG, "onFailure：" + content);
				util.saveToSp(Util.UNAME, mDeviceID);
				util.saveToSp(Util.UID, mDeviceID);
				util.saveToSp(Util.NICK, "游客" + mDeviceID);
				IntentActivityUtil.toActivity(context, mainactivity, appname,
						updateurl, versionplist, homebutton);
				finish();
			}
		});

	}

	/**
	 * 开线程登录
	 */
	private void login(final String uname, final String pwd) {
		// 显示进度条
//		pdlogin = new ProgressDialog(context);
//		pdlogin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		pdlogin.setCancelable(false);
//		pdlogin.setMessage("登录中,请稍后...");
//		pdlogin.show();
		dialog =new CustomProgressDialog(this,R.anim.loadingframe);
		dialog.setMessage("登录中...");
		//设置点击进度对话框外的区域对话框不消失 
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final Map<String, String> params = new HashMap<String, String>();
		params.put("siteid", getResources().getString(R.string.siteid));
		params.put("uname", uname);
		params.put("pwd", MD5.makeMD5(pwd));
		HttpCallBack callback = new HttpCallBack() {

			@Override
			public void onSuccess(String content) {
				// 在父类对content做了解密处理
				content = this.getContent(content);
				LogUtil.LogI(TAG, "DengLu 里边的content值 = " + content);
				// LogUtil.LogI(TAG, "登录成功:" + content);
				// Json数据解析实例
				LoginInfo rootdata = (LoginInfo) JsonPaser.getObjectDatas(
						LoginInfo.class, content);
				LogUtil.LogI(TAG, "rootdata:" + rootdata);
				Message msg = handler.obtainMessage();
				msg.obj = rootdata;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				int i = this.AutoLoading("login", params, this);
				if (i != 3) {
//					pdlogin.setMessage("网络不通，进行第" + (i + 1) + "次尝试！");
					LogUtil.LogI(TAG, "登录失败：" + content + ";error:" + error);
				} else {
//					pdlogin.cancel();
					dialog.cancel();
					util.showMsg(DengLuActivity.this, "网络不给力啊，检查下网络或者再试一次吧！");
				}
			}
		};
		NxtRestClientNew.post("login", params, callback);
	}

	protected void uploadlocation() {
		// TODO Auto-generated method stub
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.put("uid", util.getFromSp(Util.UID, ""));
				params.put("lat", util.getFromSp(Util.LATITUDE, ""));
				params.put("lng", util.getFromSp(Util.LONGITUDE, ""));
				params.put("province", util.getFromSp("province", ""));
				params.put("city", util.getFromSp("city", ""));
				params.put("district", util.getFromSp("district", ""));
				params.put("address", util.getFromSp("address", ""));

				NxtRestClient.post(Constans.UPLOCATION,params, new AsyncHttpResponseHandler() {
				});
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(dialog!=null&&dialog.isShowing()){
				dialog.dismiss();
			}else if("true".equals(ifback)){
				finish();
			}else{
				appState.finishAll();
			}
		}
		return true;
	}
	private void saveinfo() {
		// TODO Auto-generated method stub
		util.saveToSp(Util.FIRST_INSTALL, "installed");
		// 保存用户信息
		util.saveToSp(Util.UID, login.getUid());// 用户id
		util.saveToSp(Util.UNAME, str_zh);// 用户名
		util.saveToSp(Util.PWD, MD5.makeMD5(str_pw));// 密码
		util.saveToSp(Util.NICK, login.getNick());// 用户昵称
		util.saveToSp(Util.UPIC, login.getUpic());// 用户头像
		util.saveToSp(Util.SEX, login.getSex());// 用户性别
		util.saveToSp(Util.work, login.getHy());// 用户行业
		util.saveToSp(Util.area, login.getArea());// 用户地区
		util.saveToSp(Util.TOKEN, login.getToken());// 用来验证用户的有效性
		util.saveToSp(Util.status, login.getStatus()); // 激活状态
		util.saveToSp(Util.YN_PAY, login.getYn_pay());//是否订阅
		util.saveToSp("isTourist", "false");//是否是游客
		util.saveToSp("isLogin", "true");
		util.saveToSp("flag",flag);
		Util.showMsg(context, "登录成功");
		Util.toMainActivity(context);
		dialog.cancel();
		finish();
	}
	/*	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			//			webView.stopLoading();
			//			saveinfo();
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
		}
	}*/
}
