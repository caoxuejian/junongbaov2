package com.nxt.ynt;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.baidu.mobstat.StatService;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.FileUtil;
import com.nxt.nxtapp.common.HtmlUtil;
import com.nxt.ynt.database.SCDButil;
import com.nxt.ynt.entity.PayResult;
import com.nxt.ynt.utils.AccessTokenKeeper;
import com.nxt.ynt.utils.AnalyticalHTMLTask;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.ShareAppIDUtil;
import com.nxt.ynt.utils.Util;
import com.nxt.ynt.utils.WebViewWork1;
import com.nxt.ynt.x5view.X5WebView;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yalantis.phoenix.PullToRefreshView;
import com.yalantis.phoenix.PullToRefreshView.OnRefreshListener;

/*此处使用的是腾讯X5内核*/
@SuppressLint("ClickableViewAccessibility")
public class X5WebviewActivity extends Activity implements android.view.View.OnClickListener{
	private X5WebView mWebView;
	private ViewGroup mViewParent;

	private static String main_url=null;
	private static final String TAG = "SdkDemo";
	private static final int MAX_LENGTH = 14;
	private boolean mNeedTestPage = false;


	private ProgressBar mPageLoadingProgressBar = null;

	private ValueCallback<Uri> uploadFile;

	private URL mIntentUrl;
	private TextView tv_title;
	private ImageView iv_back;
	public static ImageView iv_add;
	private TextView tv_close;
	private RelativeLayout categoryTitle;
	private NetworkInfo isNetWork;
	private Util util;
	private String title;
	private WXPayReceiver payReceiver;
	private ALIPAYReceiver alipayreceiver;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private String path;
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	private View xCustomView;
	private String curVersion;
	private PullToRefreshView mPullToRefreshView;
	private WebviewGoback wv_goback;
	private REFRESHReceiver refresh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		util = new Util(this);
		title=getIntent().getStringExtra("title");
		main_url=getIntent().getStringExtra("webviewpath");
		path=main_url;
		try {
			if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
				getWindow()
				.setFlags(
						android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
						android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		setContentView(R.layout.x5activity_main1);
		mViewParent = (ViewGroup) findViewById(R.id.webView1);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);


		initview();
		//注册微信支付成功的广播
		payReceiver=new WXPayReceiver();
		registerReceiver(payReceiver, new IntentFilter("WXPAYCallback"));
		msgApi.registerApp(getResources().getString(R.string.wx_key));
		//支付宝
		alipayreceiver=new ALIPAYReceiver();
		registerReceiver(alipayreceiver, new IntentFilter("alipay"));
		//聚合刷新
		refresh=new REFRESHReceiver();
		registerReceiver(refresh, new IntentFilter("refreshs"));
		//监听从登陆页面返回
		wv_goback=new WebviewGoback();
		registerReceiver(wv_goback, new IntentFilter("webviewgoback"));

		if (!main_url.contains("?")) {
			main_url = main_url + "?" + "uid=" + util.getFromSp(Util.UID, "0") + "&"
					+ "token=" + util.getFromSp(Util.TOKEN, "0");
		}else{
			main_url=main_url+"&"+"token="+ util.getFromSp("tokens", "")+ "&uid=" + util.getFromSp(Util.UID, "0");
		}
		QbSdk.preInit(this);
		/*//更新cookie
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true); 
		cookieManager.removeAllCookie();
		cookieManager.setCookie(main_url, "ECS_ID="+util.getFromSp("ECS_ID", ""));
		CookieSyncManager.getInstance().sync();  
		System.out.println("@@@@@@cookievalue@"+util.getFromSp("ECS_ID", ""));
		System.out.println("@@@@@@cookievalue@"+cookieManager.getCookie(main_url));*/
		this.webViewTransportTest();

		mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);// �ӳ�1.5s����webview
		mPullToRefreshView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mPullToRefreshView.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mWebView.reload();
						mPullToRefreshView.setRefreshing(false);
					}
				}, 500);

			}
		});
	}
	private void initview() {
		// TODO Auto-generated method stub
		tv_title=(TextView)findViewById(R.id.category_title);
		iv_back=(ImageView)findViewById(R.id.iv_back);
		iv_add=(ImageView)findViewById(R.id.iv_add);
		iv_back.setOnClickListener(this);
		tv_close=(TextView)findViewById(R.id.tv_close);
		categoryTitle=(RelativeLayout)findViewById(R.id.categoryTitle);
		tv_close.setOnClickListener(this);
	}
	private void webViewTransportTest(){
		X5WebView.setSmallWebViewEnabled(true);
	}

	private void initProgressBar() {
		mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);// new
		mPageLoadingProgressBar.setMax(100);
		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
				.getDrawable(R.drawable.color_progressbar));
	}

	@SuppressLint({ "ResourceAsColor", "NewApi" }) private void init() {

		//网络是否可用
		mWebView = new X5WebView(this);
		mWebView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//mPullToRefreshView.setEnabled是否开启下拉刷新的功能
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(mWebView.getWebScrollY()==0){
						mPullToRefreshView.setEnabled(true);
					}
					else{
						mPullToRefreshView.setEnabled(false);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(mWebView.getWebScrollY()==0){
						mPullToRefreshView.setEnabled(true);
					}
					else{
						mPullToRefreshView.setEnabled(false);
					}
					break;
				case MotionEvent.ACTION_UP:
					if(mWebView.getWebScrollY()==0){
						mPullToRefreshView.setEnabled(true);
					}
					else{
						mPullToRefreshView.setEnabled(false);
					}
					break;

				}
				return false;
			}
		});
		mPullToRefreshView.addView(mWebView, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT));

		mWebView.loadUrl(main_url);
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		isNetWork = cwjManager.getActiveNetworkInfo();
		try {

			curVersion = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		initProgressBar();
		mWebView.setWebViewClient(new MyWebViewClient());

		mWebView.setWebChromeClient(new MyWebChromeClient());

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		String cacheDirPath = getFilesDir().getAbsolutePath()+"/webcache"; 
		webSetting.setAppCachePath(cacheDirPath);
		webSetting.setDatabasePath(cacheDirPath);
		if(isNetWork==null){
			webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}else{
			webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
		}
		//设置user agent
		webSetting.setUserAgentString(webSetting.getUserAgentString()+" junongbao/"+curVersion);
		//		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
		//				.getPath());
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// webSetting.setPreFectch(true);
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().sync(); 
	}

	private class MyWebViewClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			if(url.contains("login.php")){
				Intent intent=new Intent();
				Bundle bundle=new Bundle();
				bundle.putInt("flag", 6);
				bundle.putString("ifback", "true");
				intent.putExtras(bundle);
				intent.setClass(X5WebviewActivity.this, DengLuActivity.class);
				startActivity(intent);
				finish();
			}else{
				//根据拦截到静态页面的URL作相应的操作
				WebViewWork1.executive(X5WebviewActivity.this,url,view,msgApi);
			}
			return true;
		}
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				WebResourceRequest request) {
			// TODO Auto-generated method stub

			return super.shouldInterceptRequest(view, request);
		}
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView webview,
				String url) {
			// TODO Auto-generated method stub
			/*if(url.endsWith("jquery-1.11.1.min.js")){
				InputStream is = getClass().getResourceAsStream("file:///android_asset/jquery-1.11.1.min.js");
				WebResourceResponse response = new WebResourceResponse("image/png",
						"utf-8", is);
				System.out.println("@@@@@@@@@@@"+response);
				return response;
			}*/
			return super.shouldInterceptRequest(webview, url);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			//获取当前页面中分享所需参数
			AnalyticalHTMLTask roster = new AnalyticalHTMLTask(getApplicationContext(),
					new AnalyticalHTMLTask.BackUI() {
				@Override
				public void back(String result) {
				}
			});
			roster.execute(main_url);
			mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?

		}

	}
	private class MyWebChromeClient extends WebChromeClient{
		private CustomViewCallback customcallback;
		private LinearLayout frameLayout;
		public void onReceivedTitle(WebView view, String title) {
			tv_title.setText(title);
		}
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			mPageLoadingProgressBar.setProgress(newProgress);
			if (mPageLoadingProgressBar != null && newProgress != 100) {
				mPageLoadingProgressBar.setVisibility(View.VISIBLE);
			} else if (mPageLoadingProgressBar != null) {
				mPageLoadingProgressBar.setVisibility(View.GONE);


			}
		}
		/*@Override
		public boolean onShowFileChooser(WebView arg0,
				ValueCallback<Uri[]> arg1, FileChooserParams arg2) {
			// TODO Auto-generated method stub
			System.out.println("@@@@@@@@@@@onshowfilechooser");
			return super.onShowFileChooser(arg0, arg1, arg2);
		}*/
		@Override
		public void onShowCustomView(View arg0, int arg1,
				CustomViewCallback callback) {
			// TODO Auto-generated method stub
			super.onShowCustomView(arg0, arg1, callback);
			//如果一个视图已经存在，那么立刻终止并新建一个
			if (xCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}

			xCustomView = view;

			customcallback=callback;
			//			rl.setVisibility(View.GONE);
			frameLayout=(LinearLayout)findViewById(R.id.linearlayout);
			frameLayout.addView(xCustomView);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
					WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		}
		/*@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			// TODO Auto-generated method stub
			super.onShowCustomView(view, callback);
			//如果一个视图已经存在，那么立刻终止并新建一个
			if (xCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}

			xCustomView = view;

			customcallback=callback;
			//			rl.setVisibility(View.GONE);
			frameLayout=(LinearLayout)findViewById(R.id.linearlayout);
			frameLayout.addView(xCustomView);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
					WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		}*/
		@Override
		public void onHideCustomView() {
			// TODO Auto-generated method stub
			super.onHideCustomView();

			if (xCustomView == null)//不是全屏播放状态
				return;	       

			// Hide the custom view.
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
			xCustomView.setVisibility(View.GONE);

			// Remove the custom view from its container.
			frameLayout.removeView(xCustomView);
			xCustomView = null;
			//			customcallback.onCustomViewHidden();
			//			rl.setVisibility(View.VISIBLE);
		}
		@Override
		public boolean onJsAlert(WebView arg0, String arg1, String arg2,
				JsResult arg3) {
			// TODO Auto-generated method stub

			return super.onJsAlert(arg0, arg1, arg2, arg3);
		}
		@Override
		public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
				JsResult arg3) {
			// TODO Auto-generated method stub
			return super.onJsConfirm(arg0, arg1, arg2, arg3);
		}
		@Override
		public boolean onJsPrompt(WebView arg0, String arg1, String arg2,
				String arg3, JsPromptResult arg4) {
			// TODO Auto-generated method stub
			return super.onJsPrompt(arg0, arg1, arg2, arg3, arg4);
		}
	}




	//	boolean[] m_selected = new boolean[] { true, true, true, true, false,
	//			false, true };


	/*
	private enum TEST_ENUM_FONTSIZE {
		FONT_SIZE_SMALLEST, FONT_SIZE_SMALLER, FONT_SIZE_NORMAL, FONT_SIZE_LARGER, FONT_SIZE_LARGEST
	};

	private TEST_ENUM_FONTSIZE m_font_index = TEST_ENUM_FONTSIZE.FONT_SIZE_NORMAL;


	 */


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			/*if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			} else
				return super.onKeyDown(keyCode, event);*/
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent == null || mWebView == null || intent.getData() == null)
			return;
		mWebView.loadUrl(intent.getData().toString());
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(payReceiver);
		unregisterReceiver(alipayreceiver);
		unregisterReceiver(wv_goback);
		unregisterReceiver(refresh);
		if (mWebView != null)
			mWebView.destroy();
		super.onDestroy();
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
		}
		return super.dispatchKeyEvent(event);
	}
	public static final int MSG_OPEN_TEST_URL = 0;
	public static final int MSG_INIT_UI = 1;
	private final int mUrlStartNum = 0;
	private final int mUrlEndNum = 108;
	private int mCurrentUrl = mUrlStartNum;
	/* private class TestHandler extends */private Handler mTestHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_OPEN_TEST_URL:
				if (!mNeedTestPage) {
					return;
				}

				String testUrl = "file:///sdcard/outputHtml/html/"
						+ Integer.toString(mCurrentUrl) + ".html";
				if (mWebView != null) {
					mWebView.loadUrl(testUrl);
				}

				mCurrentUrl++;
				break;
			case MSG_INIT_UI:
				init();
				break;
			}
			super.handleMessage(msg);
		}
	};
	public static String titleImg;
	public static String atitle;
	public static String description;
	public static String shareUrl;
	public static String shareid;
	private View view;
	private ImageView iv_sharewxq;
	private ImageView iv_sharewx;
	private ImageView iv_sharewb;
	private ImageView iv_sharedx;
	private Button btnCancel;
	private PopupWindow pop;
	private PopupWindow pw;
	private PopupWindow window;
	private LinearLayout ll_sharenx;
	private String WBKEY;
	private Bitmap thumbBmp;
	private Bitmap bmp;
	private WeiboAuth mWeiboAuth;
	private IWeiboShareAPI mWeiboShareAPI;
	private SsoHandler mSsoHandler;


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id==R.id.iv_back){
			/*if(mWebView.canGoBack()){
				mWebView.goBack();
			}else{
				finish();
			}*/
			finish();
		}else if(id==R.id.tv_close){
			finish();
		}else if(id==R.id.iv_add){
			//解析HTML
			//			new Thread(){
			//				public void run() {
			//					AnalyticalHTML(main_url);
			//				};
			//			}.start();
			initpowview();
			changePopupWindowState();
			/*if (pw.isShowing()) {
				pw.dismiss();
			} else {
				iv_add.getLocationOnScreen(mLocation);
				pw.showAsDropDown(iv_add);// popupWindow显示在imageButton下面
			}*/
		}else if (id == R.id.llayout06) {// 分享
			//			popShare();
			if (pw != null && pw.isShowing())
				pw.dismiss();
			//解析HTML
			//			new Thread(){
			//				public void run() {
			//					AnalyticalHTML(main_url);
			//				};
			//			}.start();
			initpowview();

			changePopupWindowState();
		}else if (id == R.id.iv_share_wxq) {// 分享到微信朋友圈
			new Thread(){
				public void run() {
					ShareAppIDUtil.shareWX1(X5WebviewActivity.this,main_url,1);
				};
			}.start();

			changePopupWindowState();
		} else if (id == R.id.iv_sharewx) {// 分享给好友
			new Thread(){
				public void run() {
					ShareAppIDUtil.shareWX1(X5WebviewActivity.this,main_url,0);
				};
			}.start();
			changePopupWindowState();
		} else if (id == R.id.iv_share_dx) {// 分享到短信
			changePopupWindowState();
			shareDX();
		} else if (id == R.id.iv_share_wb) {// 分享到微博
			changePopupWindowState();
			new Thread(){
				public void run() {
					shareWB();
				};
			}.start();
		} else if(id==R.id.btnCancel){//取消分享
			changePopupWindowState();
		}
	}

	//页面解析
	/*private void AnalyticalHTML(String main_url2) {
		// TODO Auto-generated method stub
		//解析HTML
		String htmlStr = HtmlUtil.getHtmlString(main_url2);
		Document document = Jsoup.parse(htmlStr);
		//head中meta标签新闻的图片链接
		titleImg = document.getElementsByAttributeValueStarting("name", "shareimg").attr("content");
		//文章标题
		atitle = document.getElementsByAttributeValueStarting("name", "sharetitle").attr("content");
		//文章描述
		description = document.getElementsByAttributeValueStarting("name", "sharedesc").attr("content");
		//分享链接
		shareUrl = document.getElementsByAttributeValueStarting("name", "shareurl").attr("content");
		//文章id
		shareid=document.getElementsByAttributeValueStarting("name", "articleid").attr("content");
		//	tv_title.setText(((atitle != null && !atitle.equals("")) ? atitle : "详细信息"));
	}*/
	/**
	 * 改变 PopupWindow 的显示和隐藏
	 */
	private void changePopupWindowState() {
		if (pop.isShowing()) {
			// 隐藏窗口，如果设置了点击窗口外消失，则不需要此方式隐藏
			pop.dismiss();
		} else {
			// 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
			pop.showAtLocation(mWebView, Gravity.BOTTOM, 0, 0);
		}
	}
	//初始化弹出分享菜单
	private void initpowview() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件 - 即弹窗的界面
		view = inflater.inflate(R.layout.menu_view, null);

		iv_sharewxq = (ImageView) view.findViewById(R.id.iv_share_wxq);
		iv_sharewx = (ImageView) view.findViewById(R.id.iv_sharewx);
		iv_sharewb = (ImageView) view.findViewById(R.id.iv_share_wb);
		iv_sharedx = (ImageView) view.findViewById(R.id.iv_share_dx);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);

		iv_sharewxq.setOnClickListener(this);
		iv_sharewx.setOnClickListener(this);
		iv_sharewb.setOnClickListener(this);
		iv_sharedx.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		view.setFocusableInTouchMode(true);
		// PopupWindow实例化
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		/**
		 * PopupWindow 设置
		 */
		// pop.setFocusable(true); //设置PopupWindow可获得焦点
		// pop.setTouchable(true); //设置PopupWindow可触摸
		// pop.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		// 设置PopupWindow显示和隐藏时的动画
		pop.setAnimationStyle(R.style.MenuAnimationFade);
		/**
		 * 改变背景可拉的弹出窗口。后台可以设置为null。 这句话必须有，否则按返回键popwindow不能消失 或者加入这句话
		 * ColorDrawable dw = new
		 * ColorDrawable(-00000);pop.setBackgroundDrawable(dw);
		 */
		pop.setBackgroundDrawable(new BitmapDrawable());

	}

	private void iniPopupWindow() {
		try {
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.detail_tool, null);
			LinearLayout layout01 = (LinearLayout) layout
					.findViewById(R.id.llayout01);
			LinearLayout layout02 = (LinearLayout) layout
					.findViewById(R.id.llayout02);
			LinearLayout layout03 = (LinearLayout) layout
					.findViewById(R.id.llayout03);
			LinearLayout layout04 = (LinearLayout) layout
					.findViewById(R.id.llayout04);
			LinearLayout layout06 = (LinearLayout) layout
					.findViewById(R.id.llayout06);
			layout01.setOnClickListener(this);
			layout02.setOnClickListener(this);
			layout06.setOnClickListener(this);
			layout03.setVisibility(View.GONE);
			layout04.setVisibility(View.GONE);
			// 创建PopupWindow对象
			pw = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, false);
			pw.setFocusable(true);// 加上这个保证popupWindow中的listview能接受点击事件
			pw.setBackgroundDrawable(getResources().getDrawable(
					R.color.transparent));
			pw.setOutsideTouchable(true);// 触摸popupwindow外部使其消失，必须要给该popupwindow添加背景，如上所示
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//分享到微博
	private void shareWB() {
		//分享统计量
		ShareAppIDUtil.putShareCount(X5WebviewActivity.this,atitle,shareid);
		// TODO Auto-generated method stub
		WBKEY = getResources().getString(R.string.wb_key);
		if ("".equals(titleImg) || titleImg == null) {
			thumbBmp = BitmapFactory.decodeResource(getResources(),R.drawable.loading_img);
		} else {
			try {
				bmp = BitmapFactory.decodeStream(new URL(titleImg)
				.openStream());
				thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
				bmp.recycle();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 创建微博实例
		mWeiboAuth =new WeiboAuth(this, WBKEY, ShareAppIDUtil.WBURL,
				ShareAppIDUtil.WBSCOPE);
		// 创建微博 SDK 接口实例
		// c756f5460ac7745bd562c5ea19457889
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, WBKEY);
		// 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
		boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
		// 如果未安装微博客户端，设置下载微博对应的回调
		if (!isInstalledWeibo) {
			mWeiboShareAPI
			.registerWeiboDownloadListener(new IWeiboDownloadListener() {
				@Override
				public void onCancel() {
					Toast.makeText(X5WebviewActivity.this, "取消",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		mWeiboShareAPI.registerApp();
		if (!mWeiboShareAPI.isWeiboAppInstalled()) {
			mWeiboShareAPI
			.registerWeiboDownloadListener(new IWeiboDownloadListener() {
				@Override
				public void onCancel() {
					Toast.makeText(X5WebviewActivity.this, "取消",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		// 授权
		mSsoHandler = new SsoHandler(X5WebviewActivity.this, mWeiboAuth);
		mSsoHandler.authorize(new AuthListener());
		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			// 1. 初始化微博的分享消息
			WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
			if (titleImg != null) {
				weiboMessage.imageObject = getImageObj();
			}
			weiboMessage.mediaObject = getWebpageObj();
			// 2. 初始化从第三方到微博的消息请求
			SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
			// 用transaction唯一标识一个请求
			request.transaction = String
					.valueOf(System.currentTimeMillis());
			request.multiMessage = weiboMessage;
			// 3. 发送请求消息到微博，唤起微博分享界面
			mWeiboShareAPI.sendRequest(request);
		} else {
			Toast.makeText(this, "微博客户端不支持 SDK 分享或微博客户端未安装或微博客户端是非官方版本。",
					Toast.LENGTH_SHORT).show();
		}
	}

	//分享到短信
	private void shareDX() {
		// TODO Auto-generated method stub
		//分享统计量
		ShareAppIDUtil.putShareCount(X5WebviewActivity.this,atitle,shareid);
		Uri smsToUri = Uri.parse("smsto:");
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
		// sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
		// http://m.365960.com/t/?m69072
		sendIntent.putExtra("sms_body", "【" + atitle + "】" + "#" +shareUrl);
		sendIntent.setType("vnd.android-dir/mms-sms");
		startActivityForResult(sendIntent, 1002);
	}

	//弹出分享选择窗口
	private void popShare() {
		// TODO Auto-generated method stub
		if (pw != null && pw.isShowing())
			pw.dismiss();
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.share_wx, null);
		TextView share_wx = (TextView) view.findViewById(R.id.share_wx);
		TextView share_wxpy = (TextView) view.findViewById(R.id.share_wxpy);
		TextView share_dx = (TextView) view.findViewById(R.id.share_dx);
		TextView share_wb = (TextView) view.findViewById(R.id.share_wb);
		ll_sharenx=(LinearLayout)view.findViewById(R.id.ll_sharenx);
		//只有在农信中显示分享到我的圈子农信朋友
		if("农信".equals(util.getFromSp(Util.APPNAME, ""))){
			ll_sharenx.setVisibility(View.VISIBLE);
		}
		TextView share_nxdt = (TextView) view.findViewById(R.id.share_nxdt);
		TextView share_nxpy = (TextView) view.findViewById(R.id.share_nxpy);
		share_wx.setOnClickListener(this);
		share_wxpy.setOnClickListener(this);
		share_dx.setOnClickListener(this);
		share_wb.setOnClickListener(this);
		share_nxdt.setOnClickListener(this);
		share_nxpy.setOnClickListener(this);

		// 收起软键盘
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
		.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		if (window == null) {
			window = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, true);
			window.setAnimationStyle(R.style.popuStyle);
			window.setBackgroundDrawable(new BitmapDrawable());
		}
		window.showAtLocation(findViewById(R.id.linearlayout),
				Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {
		private Oauth2AccessToken mAccessToken;

		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(X5WebviewActivity.this,
						mAccessToken);
				Toast.makeText(X5WebviewActivity.this, "授权成功", Toast.LENGTH_SHORT)
				.show();
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "分享失败";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(X5WebviewActivity.this, message, Toast.LENGTH_LONG)
				.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(X5WebviewActivity.this, "分享取消！", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(X5WebviewActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	/**
	 * 创建图片消息对象。
	 * 
	 * @return 图片消息对象。
	 */
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		// BitmapDrawable bitmapDrawable = (BitmapDrawable)
		// mImageView.getDrawable();
		imageObject.setImageObject(thumbBmp);
		return imageObject;
	}

	private WebpageObject getWebpageObj() {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = atitle;
		mediaObject.description = description;
		// InputStream is = getResources().openRawResource(R.drawable.swb);
		// Bitmap mBitmap = BitmapFactory.decodeStream(is);
		// 设置 Bitmap 类型的图片到视频对象里
		mediaObject.setThumbImage(thumbBmp);
		mediaObject.actionUrl = shareUrl;
		mediaObject.defaultText = atitle;
		return mediaObject;
	}
	//刷新
	public class REFRESHReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			mWebView.loadUrl("javascript:flush()");
		}

	}

	//接收到广播加载页面
	public class WXPayReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			mWebView.loadUrl(Constans.WX_PayEntry);
		}
	}
	//获取到支付参数
	public class ALIPAYReceiver extends BroadcastReceiver {
		private String paymsg;

		@Override
		public void onReceive(Context context, Intent intent) {
			paymsg=intent.getStringExtra("paymsg");
			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(X5WebviewActivity.this);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(paymsg,true);
					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			};

			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();
		}
	}
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(X5WebviewActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					mWebView.loadUrl(Constans.WX_PayEntry);
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(X5WebviewActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(X5WebviewActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(X5WebviewActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};
	//返回
	public class WebviewGoback extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//				if(mWebView.canGoBack())
			mWebView.goBack();
		}

	}
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation== this.getResources().getConfiguration().ORIENTATION_PORTRAIT){
			categoryTitle.setVisibility(View.VISIBLE);
		}
		if(newConfig.orientation== this.getResources().getConfiguration().ORIENTATION_LANDSCAPE){
			categoryTitle.setVisibility(View.GONE);
		}
	};
	public void onResume() {
		super.onResume();

		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		//		StatService.onResume(this);
		StatService.onPageStart(this, path);
	}

	public void onPause() {
		super.onPause();

		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		//		StatService.onPause(this);
		StatService.onPageEnd(this, path);
	}
}
