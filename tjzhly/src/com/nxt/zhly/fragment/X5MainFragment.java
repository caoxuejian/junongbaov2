package com.nxt.zhly.fragment;




import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.ynt.LivesActivity;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.Listdata;
import com.nxt.ynt.utils.Util;
import com.nxt.ynt.x5view.X5WebView;
import com.nxt.zhly.util.WebViewWork1;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.RenderPriority;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yalantis.phoenix.PullToRefreshView;
import com.yalantis.phoenix.PullToRefreshView.OnRefreshListener;
/**
 * 动态创建webview
 * webview下拉刷新
 * @author CaoXueJian
 *
 */
public class X5MainFragment extends Fragment implements OnClickListener {
	private WebView wv;
	private String title;
	private String savekey;
	private Util util;
	private String appname;
	private String main_url;
	private RelativeLayout rl;
	//	private ProgressBar pb;
	private View view;
	//	private ViewGroup mViewParent;
	private ProgressBar mPageLoadingProgressBar;
	//	private RelativeLayout rl;
	private String cacheDirPath;
	private REFRESHReceiver refresh;
	private NetworkInfo isNetWork;
	private String curVersion;
	private boolean reload;
	private TimeCount time;
	private String cookie;
	private PullToRefreshView mPullToRefreshView;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		FragmentActivity context = getActivity();
		util = new Util(context);
		title=getArguments().getString("title");
		main_url=getArguments().getString("URL");
		reload=getArguments().getBoolean("refresh");
		savekey=main_url;
		appname = util.getFromSp(Util.APPNAME, "");
		if (!main_url.contains("?")) {
			main_url = main_url + "?" + "uid=" + util.getFromSp(Util.UID, "0")+ "&"
					+ "token=" + util.getFromSp(Util.TOKEN, "0") + "&"
					+ "imei=" + util.getFromSp(Util.IMEI, "0") ;
		}else{
			main_url=main_url+"&"+"token="+ util.getFromSp("tokens", "")+ "&uid=" + util.getFromSp(Util.UID, "0");
		}
		time = new TimeCount(2000, 1000);// 构造CountDownTimer对象

		view =inflater.inflate(R.layout.webview_x5, null);
		//		mViewParent = (ViewGroup) view.findViewById(R.id.webView1);
		rl=new RelativeLayout(this.getActivity());
		mPullToRefreshView=new PullToRefreshView(this.getActivity());
		//下拉刷新
		//		swref= new WebViewSwipeRefresh(this.getActivity());
		//		swref=(WebViewSwipeRefresh)view.findViewById(R.id.swipe_container);
		//进度条		
		mPageLoadingProgressBar = new ProgressBar(this.getActivity(),null,android.R.attr.progressBarStyleHorizontal);
		mPageLoadingProgressBar.setMax(100);
		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
				.getDrawable(R.drawable.color_progressbar));
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,8); 
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE); 
		mPageLoadingProgressBar.setLayoutParams(lp);
		wv = new X5WebView(context);
		wv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//mPullToRefreshView.setEnabled是否开启下拉刷新的功能
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(wv.getWebScrollY()==0){
						mPullToRefreshView.setEnabled(true);
					}
					else{
						mPullToRefreshView.setEnabled(false);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(wv.getWebScrollY()==0){
						mPullToRefreshView.setEnabled(true);
					}
					else{
						mPullToRefreshView.setEnabled(false);
					}
					break;
				case MotionEvent.ACTION_UP:
					if(wv.getWebScrollY()==0){
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


		mPullToRefreshView.addView(wv);
		rl.addView(mPullToRefreshView);
		rl.addView(mPageLoadingProgressBar);
		/*//更新cookie
		CookieSyncManager.createInstance(getActivity());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		cookieManager.setCookie(main_url, "ECS_ID="+util.getFromSp("ECS_ID", ""));
		CookieSyncManager.getInstance().sync();
		System.out.println("@@@@@@cookievalue@"+util.getFromSp("ECS_ID", ""));*/
		//聚合刷新
		refresh=new REFRESHReceiver();
		context.registerReceiver(refresh, new IntentFilter("refresh"));
		if(null!=savedInstanceState){
			wv.restoreState(savedInstanceState);
		}else{
			wv.loadUrl(main_url);
		}
		wvsetting();


		if(reload)
			time.start();
		mPullToRefreshView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mPullToRefreshView.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						wv.reload();
						mPullToRefreshView.setRefreshing(false);
					}
				}, 500);

			}
		});
		return rl;
	}
	private void wvsetting() {
		// TODO Auto-generated method stub
		try {

			curVersion = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		//网络是否可用
		ConnectivityManager cwjManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		isNetWork = cwjManager.getActiveNetworkInfo();
		wv.setWebViewClient(new MyWebViewClient());
		wv.setWebChromeClient(new MyWebChromeClient());
		//		wv.addJavascriptInterface(new JavaScriptInterface(), "ncp");
		WebSettings setting = wv.getSettings();
		setting.setBuiltInZoomControls(false );
		setting.setSupportZoom(false);
		setting.setDisplayZoomControls(false);

		//设置user agent
		setting.setUserAgentString(setting.getUserAgentString()+" junongbao/"+curVersion);


		setting.setJavaScriptEnabled(true); 
		setting.setRenderPriority(RenderPriority.HIGH); 

		if(isNetWork==null){
			setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}else{
			setting.setCacheMode(WebSettings.LOAD_DEFAULT);

		}
		// 开启 DOM storage API 功能 
		setting.setDomStorageEnabled(true); 
		//开启 database storage API 功能 
		setting.setDatabaseEnabled(true);  
		//开启 Application Caches 功能 
		setting.setAppCacheEnabled(true); 
		setting.setAllowFileAccess(true);
		//		String cacheDirPath = getActivity().getFilesDir().getAbsolutePath()+"/webcache"; 
		String cacheDirPath=getActivity().getFilesDir().getAbsolutePath()+"/webcache";
		//	      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME; 
		//设置数据库缓存路径 
		//		setting.setDatabasePath(cacheDirPath); 
		//设置  Application Caches 缓存目录 
		setting.setAppCachePath(cacheDirPath); 
		CookieSyncManager.createInstance(getActivity());
		CookieSyncManager.getInstance().sync();
	}
	public void onRefresh() {  
		//下拉重新加载  
		//		wv.loadUrl(main_url); 
		wv.reload();
	} 
	private class MyWebChromeClient extends WebChromeClient{
		//更改进度条状态
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			view.requestFocus();
			mPageLoadingProgressBar.setProgress(newProgress);
			if (mPageLoadingProgressBar != null && newProgress != 100) {
				mPageLoadingProgressBar.setVisibility(View.VISIBLE);
			} else if (mPageLoadingProgressBar != null) {
				mPageLoadingProgressBar.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}
		@Override
		public void onReceivedTitle(WebView view, String title) {
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, title);
		}
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			return super.onJsAlert(view, url, message, result);
		}
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			// TODO Auto-generated method stub
			/*new AlertDialog.Builder(getActivity())
			.setTitle("警告")
			.setMessage(message)
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int arg1) {
					result.confirm();
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					result.cancel();
				}

			}).show();*/
			return super.onJsConfirm(view, url, message, result);
		}
	}
	private class MyWebViewClient extends WebViewClient {

		private String token;
		private Listdata data;
		private Intent ii;
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//根据拦截到静态页面的URL作相应的操作
			if(url.contains("login.php")){
				token=util.getFromSp(Util.TOKEN, "");
				view.loadUrl(url);
				WebView webView=new WebView(getActivity());
				WebViewClient client=new WebViewClient(){
					@Override
					public void onPageFinished(WebView arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onPageFinished(arg0, arg1);
						wv.loadUrl(main_url);
					}
				};
				webView.setWebViewClient(client);
				webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient());
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
				webView.loadUrl(Constans.FX_GOUWU+ "?" + "token="
						+ token);
				webView.setVisibility(View.GONE);
				return false;
			}else{
				if(url.contains("type=")){
					data = new Listdata();
					ii = new Intent(getActivity(), LivesActivity.class);
					if (url.contains("type=shopping")) {
						ii.putExtra("title", "生活购物");
						ii.putExtra("content", data.titles);
						
					} else if (url.contains("type=homestay")) {
						ii.putExtra("title", "民宿客栈");
						ii.putExtra("content", data.homestay);
						
					} else if (url.contains("type=townshiptour")) {
						ii.putExtra("title", "乡游推荐");
						ii.putExtra("content", data.Township);
						
					} else if (url.contains("type=sports")) {
						ii.putExtra("title", "运动健身");
						ii.putExtra("content", data.sports);
						
					} else if (url.contains("type=parking")) {
						ii.putExtra("title", "停车位");
						ii.putExtra("content", data.parking);
						
					} else if (url.contains("type=medical")) {
						ii.putExtra("title", "乡村医疗");
						ii.putExtra("content", data.medical);
						
					} else if (url.contains("type=bank")) {
						ii.putExtra("title", "银行保险");
						ii.putExtra("content", data.bank);
						
					} else if (url.contains("type=kuaidi")) {
						ii.putExtra("title", "快递物流");
						ii.putExtra("content", data.kuaidi);
						
					} else if (url.contains("type=spots")) {
						ii.putExtra("title", "景点旅游");
						ii.putExtra("content", data.spots);
						
					} else if (url.contains("type=youleyuan")) {
						ii.putExtra("title", "游乐园");
						ii.putExtra("content", data.youleyuan);
						
					} else if (url.contains("type=regimen")) {
						ii.putExtra("title", "养生保健");
						ii.putExtra("content", data.regimen);
						
					} else if (url.contains("type=parent")) {
						ii.putExtra("title", "亲子互动");
						ii.putExtra("content", data.parent);
						
					} else if (url.contains("type=leisure")) {
						ii.putExtra("title", "休闲娱乐");
						ii.putExtra("content", data.Leisure);
						
					} else if (url.contains("type=bus")) {
						ii.putExtra("title", "公交路线");
						ii.putExtra("content", data.bus);
						
					} else if (url.contains("type=phone")) {
						ii.putExtra("title", "手机充值");
						ii.putExtra("content", data.phone);
						
					} else if (url.contains("type=villagesong")) {
						ii.putExtra("title", "村歌伴舞");
						ii.putExtra("content", data.village_song);
						
					} else if (url.contains("type=other")) {
						ii.putExtra("title", "其他");
						ii.putExtra("content", data.other);
					}
					startActivity(ii);
				}else{
					WebViewWork1.executive(getActivity(),url,wv,null);
				}
				return true;
			}
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
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			return super.shouldOverrideKeyEvent(view, event);
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			//			rl.removeView(pb);
			//			//缓存本地数据库
			//			savecache();

		}
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			wv.setVisibility(View.GONE);
			Toast.makeText(getActivity(), "请检查网络是否可用！", Toast.LENGTH_LONG).show();
		}

	}


	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		wv.saveState(outState);
	}

	public void onBackPressed() {

		if(wv.canGoBack())

			wv.goBack();
		else{
			// Process.killProcess(Process.myPid());

		}

	}
	public void onDestroy() {
		getActivity().unregisterReceiver(refresh);
		mPullToRefreshView.removeAllViews();
		wv.stopLoading();
		wv.removeAllViews();
		wv.destroy();
		wv = null;
		mPullToRefreshView = null;
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	//刷新
	public class REFRESHReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			wv.reload();
		}

	}
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			wv.reload();
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
		}
	}
}
