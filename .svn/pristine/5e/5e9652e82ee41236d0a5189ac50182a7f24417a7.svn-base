package com.nxt.ynt;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mobstat.StatService;
import com.jutong.live.Live_MainActivity;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.BitmapCommonUtils;
import com.nxt.ynt.fragment.X5MainFragment;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.Util;
import com.nxt.ynt.x5view.FirstLoadingX5Service;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author Wang Tingting 聚农宝主界面 2014-5-4上午11:37:14
 */
public class JNBMainActivity extends AbsMainActivity implements OnClickListener {
	private static String TAG = "XNBMainActivity";
	private TextView category_title;
	private int width;
	private LinearLayout tab1, tab2, tab3, tab4, tab5;
	// 标题栏
	private RelativeLayout categoryTitle;
	private Context context;
	// 缓存路径
	public static String HTML_PATH;
	public static String JSON_PATH;
	public static String PIC_PATH;
	// 手机屏幕
	public static int widthPx;
	public static int HeigtPx;
	// tab键未读消息计数
	public static int msg_un_num = 0;
	public static TextView msg_un_text;
	public static TextView dongtai_un_text;
	// 多功能按钮
	private PopupWindow pw;
	private int[] mLocation = new int[2];
	private SDKReceiver mReceiver;
	private Util util;
	private TextView tv_shouye;
	private TextView tv_xiaoxi;
	private TextView tv_find;
	private TextView tv_fenlei;
	private TextView tv_more;
	private ImageView button_shouye;
	private ImageView button_xiaoxi;
	private ImageView button_find;
	private ImageView button_fenlei;
	private ImageView button_more;
	private LinearLayout tabmain;
	private ImageView btn_gwc;
	private ImageView iv_shezhi;
	private int flag;
	private String isLogin;
	private String isinstall;
	private Fragment mainFragment,secondFragment,thirdFragment,fourthFragment,msgFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.activity_xnb_main);

	/*	*//**
		 * X5内核在使用preinit接口之后，对于首次安装首次加载没有效果
		 * 实际上，X5webview的preinit接口只是降低了webview的冷启动时间；
		 * 因此，现阶段要想做到首次安装首次加载X5内核，必须要让X5内核提前获取到内核的加载条件
		 *//*
		if (!QbSdk.isTbsCoreInited()) {// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
			QbSdk.preInit(JNBMainActivity.this);// 设置X5初始化完成的回调接口
			// 第三个参数为true：如果首次加载失败则继续尝试加载；
		}
*/
	/*	*//**
		 * 开启额外进程 服务 预加载X5内核， 此操作必须在主进程调起X5内核前进行，否则将不会实现预加载
		 *//*
		Intent intent = new Intent(this, FirstLoadingX5Service.class);
		startService(intent);*/

		PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY,getResources().getString(R.string.pushID));
		context = this;
		util = new Util(context);
		// 初始化数据
		isLogin=util.getFromSp("isLogin", "");
		flag=util.getFromSp("flag", 0);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		// 缓存路径
		HTML_PATH = BitmapCommonUtils.getDiskCacheDir(this, "cachehtml")
				.getAbsolutePath();
		JSON_PATH = BitmapCommonUtils.getDiskCacheDir(this, "cachejson")
				.getAbsolutePath();
		PIC_PATH = BitmapCommonUtils.getDiskCacheDir(this, "cachepic")
				.getAbsolutePath();
		// 获取屏幕分辩率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthPx = dm.widthPixels;
		HeigtPx = dm.heightPixels;
		// 初始化界面
		initView();
		if("true".equals(isLogin)){
			// 初始化数据库
			//			initDataBase();
			// 修改是否是开机自启的标识
			util.saveToSp(Util.ISAUTOSTART, "false");
			util.saveToSp(Util.ISINMYCHAT, "false");
			isinstall=util.getFromSp(Util.ISINSTALL,"");
			/*// 停止定位
			LocationClient mLocationClient = ((SoftApplication) getApplication()).mLocationClient;
			if (mLocationClient != null && mLocationClient.isStarted()) {
				mLocationClient.stop();
				mLocationClient = null;
			}*/
		}
		//百度push
		IntentFilter bdfilter = new IntentFilter("com.nxt.ynt.BaiDuPushMessage");  
		registerReceiver(broadcastReceiver, bdfilter);
		// 注册 SDK 广播监听者
		// 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
		
		
		/*IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);*/
	}

	// 初始化界面
	protected void initView() {
		category_title = (TextView) findViewById(R.id.category_title);
		tv_shouye = (TextView) findViewById(R.id.tv_shouye);
		tv_xiaoxi = (TextView) findViewById(R.id.tv_xiaoxi);
		tv_fenlei = (TextView) findViewById(R.id.tv_fenlei);
		tv_find = (TextView) findViewById(R.id.tv_find);
		tv_more = (TextView) findViewById(R.id.tv_more);
		tabmain=(LinearLayout)findViewById(R.id.tabmain);
		tab1 = (LinearLayout) findViewById(R.id.tab1);
		tab2 = (LinearLayout) findViewById(R.id.tab2);
		tab3 = (LinearLayout) findViewById(R.id.tab3);
		tab4 = (LinearLayout) findViewById(R.id.tab4);
		tab5 = (LinearLayout) findViewById(R.id.tab5);
		button_shouye = (ImageView) findViewById(R.id.button_shouye);
		button_xiaoxi = (ImageView) findViewById(R.id.button_xiaoxi);
		button_find= (ImageView) findViewById(R.id.button_find);
		button_fenlei = (ImageView) findViewById(R.id.button_fenlei);
		button_more = (ImageView) findViewById(R.id.button_more);
		//掌上购右上角的购物车
		btn_gwc=(ImageView)findViewById(R.id.button_add);
		//我的设置
		iv_shezhi=(ImageView)findViewById(R.id.iv_shezhi);
		iv_shezhi.setVisibility(View.GONE);
		tabmain.setOnClickListener(this);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab4.setOnClickListener(this);
		tab5.setOnClickListener(this);
		msg_un_text = (TextView) findViewById(R.id.msg_un);
		dongtai_un_text = (TextView) findViewById(R.id.dongtai_un);
		categoryTitle = (RelativeLayout) this.findViewById(R.id.categoryTitle);
		category_title.setText(getResources().getString(R.string.xnb_shouye));
		/*if(flag!=0){

			WebView wv = new WebView(getApplicationContext());
			wv.setWebViewClient(new WebViewClient());
			wv.setWebChromeClient(new WebChromeClient());
			wv.getSettings().setJavaScriptEnabled(true);
			wv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
			wv.loadUrl(Constans.FX_GOUWU+ "?" + "token="
					+ util.getFromSp("tokens", ""));
			wv.setVisibility(View.GONE);
			try{
				Thread.sleep(2000);
			}catch (InterruptedException e) {}
		}*/
		switch (flag) {
		case 2:
			//			layout05.setVisibility(View.GONE);
			category_title.setText(getResources().getString(R.string.xnb_msg));
			X5MainFragment fragment_home = new X5MainFragment();
			Bundle bundle = new Bundle();
			bundle.putString("URL", Constans.MY_XIU);
			bundle.putString("title", getResources().getString(R.string.xnb_shouye));
			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			setViewBackground(tab1, 1);
			categoryTitle.setVisibility(View.VISIBLE);
			util.saveToSp("flag",0);
			break;
		case 5:
			//			layout05.setVisibility(View.GONE);
			iv_shezhi.setVisibility(View.VISIBLE);
			category_title.setText(getResources().getString(R.string.xnb_me));
			//			setTabSelect(4);
			X5MainFragment fragment_home1 = new X5MainFragment();
			Bundle bundle1 = new Bundle();
			bundle1.putString("URL",Constans.GOUWU_DINGDAN);
			bundle1.putBoolean("refresh", true);
			fragment_home1.setArguments(bundle1);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home1)
			.addToBackStack(null).commit();
			setViewBackground(tab4, 4);
			util.saveToSp("flag",0);
			break;
		case 6:
			Intent intent=new Intent();
			intent.setClass(this, X5WebviewActivity.class);
			intent.putExtra("title", "购物车");
			intent.putExtra("webviewpath", Constans.GOUWU_GWC);
			startActivity(intent);
			util.saveToSp("flag",0);
			break;
		default:
			//			setTabSelect(0);
			X5MainFragment mainFragment = new X5MainFragment();
			Bundle bundle2 = new Bundle();
			bundle2.putString("URL", Constans.FX_JNB);
			bundle2.putString("title", getResources().getString(R.string.xnb_shouye));
			//			bundle2.putBoolean("refresh", true);
			mainFragment.setArguments(bundle2);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mainFragment)
			.addToBackStack(null).commit();
			setViewBackground(tabmain, 0);
			break;
		}
		//		iniPopupWindow();

	}
	/*protected void iniPopupWindow() {
		try {
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.task_detail_popupwindow,
					null);
			LinearLayout layout01 = (LinearLayout) layout
					.findViewById(R.id.llayout01);
			LinearLayout layout02 = (LinearLayout) layout
					.findViewById(R.id.llayout02);
			LinearLayout layout03 = (LinearLayout) layout
					.findViewById(R.id.llayout03);
			LinearLayout layout04 = (LinearLayout) layout
					.findViewById(R.id.llayout04);
			layout05 = (LinearLayout) layout.findViewById(R.id.llayout05);
			layout01.setOnClickListener(this);
			layout02.setOnClickListener(this);
			layout03.setOnClickListener(this);
			layout04.setOnClickListener(this);
			layout05.setOnClickListener(this);
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
	 */
	/*public void onDialogButtonClick(View v) {
		if (pw == null)
			return;
		if (pw.isShowing()) {
			pw.dismiss();
		} else {
			ImageView button_add = (ImageView) findViewById(R.id.button_add);
			button_add.getLocationOnScreen(mLocation);
			pw.showAsDropDown(button_add);// popupWindow显示在imageButton下面
		}
	}
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		util.saveToSp("flag",0);
		// 取消监听 SDK 广播
		//		stopService(new Intent(this, AutoStartService.class));
		/*unregisterReceiver(mReceiver);
		unregisterReceiver(broadcastReceiver);
		try {
			Presence presence = new Presence(Presence.Type.unavailable); // 设置为下线状态
			XmppConnection.getConnection().sendPacket(presence);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				XmppConnection.closeConnection();
				Intent intent = new Intent(this, XmppService.class);
				stopService(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}

	// 点击事件的处理
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.tabmain) {// 客户端的tab1(首页)
			btn_gwc.setVisibility(View.GONE);
			iv_shezhi.setVisibility(View.GONE);
			category_title.setText(getResources().getString(R.string.xnb_shouye));
			//			setTabSelect(0);
			X5MainFragment fragment_home = new X5MainFragment();
			Bundle bundle = new Bundle();
			bundle.putString("URL", Constans.FX_JNB);
			bundle.putString("title", getResources().getString(R.string.xnb_shouye));
			//			bundle.putBoolean("refresh", true);
			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			setViewBackground(tabmain, 0);
		} else if (id == R.id.tab1) {// 客户端的tab2(消息)
			btn_gwc.setVisibility(View.GONE);
			iv_shezhi.setVisibility(View.GONE);
			category_title.setText(getResources().getString(R.string.xnb_msg));
			X5MainFragment fragment_home = new X5MainFragment();
			Bundle bundle = new Bundle();
			bundle.putString("URL", Constans.MY_XIU);
			bundle.putString("title", getResources().getString(R.string.xnb_shouye));
			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			setViewBackground(tab1, 1);
			categoryTitle.setVisibility(View.VISIBLE);
			//			}
		} else if (id == R.id.tab2) {// 客户端的掌上购
			btn_gwc.setVisibility(View.VISIBLE);
			iv_shezhi.setVisibility(View.GONE);
			category_title.setText(getResources().getString(R.string.xnb_fenlei));
			X5MainFragment fragment_home = new X5MainFragment();
			Bundle bundle = new Bundle();
			bundle.putString("URL",Constans.FX_GOUWU);
			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			setViewBackground(tab2, 2);
			categoryTitle.setVisibility(View.VISIBLE);
		} else if (id == R.id.tab3) {// 香油网
			btn_gwc.setVisibility(View.GONE);
			iv_shezhi.setVisibility(View.GONE);
			category_title.setText(getResources()
					.getString(R.string.xnb_faxian));
			X5MainFragment fragment_home = new X5MainFragment();
			Bundle bundle = new Bundle();
			bundle.putString("URL",Constans.XYW);
			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			setViewBackground(tab3, 3);
			categoryTitle.setVisibility(View.VISIBLE);
		} else if (id == R.id.tab4) {// 客户端的我
			btn_gwc.setVisibility(View.GONE);
			if(!"true".equals(isLogin)){
				Intent intent=new Intent();
				Bundle bundle=new Bundle();
				bundle.putInt("flag", 5);
				bundle.putString("ifback", "true");
				intent.putExtras(bundle);
				intent.setClass(this, DengLuActivity.class);
				startActivity(intent);
			}else{
				iv_shezhi.setVisibility(View.VISIBLE);
				category_title.setText(getResources().getString(R.string.xnb_me));
				X5MainFragment fragment_home = new X5MainFragment();
				Bundle bundle = new Bundle();
				bundle.putString("URL",Constans.GOUWU_DINGDAN);
				fragment_home.setArguments(bundle);
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
				.addToBackStack(null).commit();
				//				setTabSelect(4);
				setViewBackground(tab4, 4);
			}
		} else if (id == R.id.button_add) {
			Intent intent=new Intent();
			intent.setClass(this, X5WebviewActivity.class);
			intent.putExtra("title", "购物车");
			intent.putExtra("webviewpath", Constans.GOUWU_GWC);
			startActivity(intent);
		}else if (id == R.id.iv_shezhi) {
			Intent intent=new Intent();
			intent.setClass(this, SettingActivity.class);
			startActivity(intent);
		} else if (id == R.id.button_sousuo) {

		} 
	}

	// 点击效果设置
	private void setViewBackground(View tab, int id) {
		tabmain.setBackgroundResource(R.color.transparent);
		tab1.setBackgroundResource(R.color.transparent);
		tab2.setBackgroundResource(R.color.transparent);
		tab3.setBackgroundResource(R.color.transparent);
		tab4.setBackgroundResource(R.color.transparent);
		tab5.setBackgroundResource(R.color.transparent);
		button_shouye.setBackgroundResource(R.drawable.sshouye1);
		button_xiaoxi.setBackgroundResource(R.drawable.msg1);
		button_find.setBackgroundResource(R.drawable.find1);
		button_more.setBackgroundResource(R.drawable.me_on);
		button_fenlei.setImageResource(R.drawable.fenlei1);
		tv_shouye.setTextColor(getResources().getColor(R.color.tab_nochoose));
		tv_xiaoxi.setTextColor(getResources().getColor(R.color.tab_nochoose));
		tv_fenlei.setTextColor(getResources().getColor(R.color.tab_nochoose));
		tv_find.setTextColor(getResources().getColor(R.color.tab_nochoose));
		tv_more.setTextColor(getResources().getColor(R.color.tab_nochoose));
		switch (id) {
		case 0:
			button_shouye.setBackgroundResource(R.drawable.sshouye);
			tv_shouye.setTextColor(getResources().getColor(R.color.tab_choose));

			break;
		case 1:
			button_xiaoxi.setBackgroundResource(R.drawable.msg);
			tv_xiaoxi.setTextColor(getResources().getColor(R.color.tab_choose));
			break;
		case 2:
			button_fenlei.setImageResource(R.drawable.fenlei);
			tv_fenlei.setTextColor(getResources().getColor(R.color.tab_choose));
			break;
		case 3:
			button_find.setBackgroundResource(R.drawable.find);
			tv_find.setTextColor(getResources().getColor(R.color.tab_choose));
			break;
		case 4:
			button_more.setBackgroundResource(R.drawable.me_up);
			tv_more.setTextColor(getResources().getColor(R.color.tab_choose));
			break;
		default:
			break;
		}
	}

	/*// 第一次启动程序，将配置文件里的数据插入到数据库中
	private void initDataBase() {
		try {
			int currentVersion = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionCode;
			int lastVersion = util.getFromSp("VERSION_KEY", 0);
			String xnbmsg = util.getFromSp("xnbmsg.json", "");
			int db_ver = util.getFromSp(Util.DB_VER, -1);
			if (StringUtils.isNotEmty(xnbmsg)) {
				NewsRoot rootdata = (NewsRoot) JsonPaser.getObjectDatas(
						NewsRoot.class, xnbmsg);
				List<XNBmsg> list = JsonPaser.getArrayDatas(XNBmsg.class,
						rootdata.getNews());
				String json_ver = rootdata.getVer();
				LogUtil.LogE(TAG, "xnbmsg中的ver:" + json_ver + ";数据库中的ver:"
						+ db_ver);
				if (currentVersion > lastVersion) {
					LogUtil.LogE(TAG, "版本升级了或第一次安装则录入配置的数据信息currentVersion:"
							+ currentVersion + " lastVersion:" + lastVersion);
					dbutil.deleteMsg();
					for (XNBmsg msg : list) {
						// TODO 插入数据库
						boolean b = dbutil.insertXNBMSG(msg,
								Integer.parseInt(json_ver));
						if (msg.getType().equals("user")) {
							Contracts c = new Contracts();
							c.setUid(msg.getUid());
							c.setUname(msg.getUname());
							c.setNick(msg.getNick());
							c.setUpic(msg.getUpic());
							c.setArea(msg.getAddress());
							dbutil.insertContracts(c);
						}
					}
					util.saveToSp("VERSION_KEY", currentVersion);
					MsgFragment.sendMsg();
				} else if (db_ver != -1 && db_ver != Integer.parseInt(json_ver)) {
					LogUtil.LogE(TAG, "数据库ver和xnbmsg.json的ver不同，删除数据重新入库");
					dbutil.deleteMsg();// 删除isdefault==1的数据
					for (XNBmsg msg : list) {
						// TODO 插入数据库
						boolean b = dbutil.insertXNBMSG(msg,
								Integer.parseInt(json_ver));
						if (msg.getType().equals("user")) {
							Contracts c = new Contracts();
							c.setUid(msg.getUid());
							c.setUname(msg.getUname());
							c.setNick(msg.getNick());
							c.setUpic(msg.getUpic());
							c.setArea(msg.getAddress());
							dbutil.insertContracts(c);
						}
					}
					MsgFragment.sendMsg();
				}
			} else if (dbutil.getMsgs(new String[] {}, new String[] {}, true,
					false).size() == 0) {
				String jsonstr = ReadRaw.getRawJson(this, ReadRaw.DATABASEINIT);
				LogUtil.LogE(TAG, "本地的josn数据：" + jsonstr);
				NewsRoot raw_rootdata = (NewsRoot) JsonPaser.getObjectDatas(
						NewsRoot.class, jsonstr);
				List<XNBmsg> raw_list = JsonPaser.getArrayDatas(XNBmsg.class,
						raw_rootdata.getNews());
				String raw_json_ver = raw_rootdata.getVer();
				dbutil.deleteMsg();
				for (XNBmsg msg : raw_list) {
					// TODO 插入数据库
					boolean b = dbutil.insertXNBMSG(msg,
							Integer.parseInt(raw_json_ver));
					if (msg.getType().equals("user")) {
						Contracts c = new Contracts();
						c.setUid(msg.getUid());
						c.setUname(msg.getUname());
						c.setNick(msg.getNick());
						c.setUpic(msg.getUpic());
						c.setArea(msg.getAddress());
						dbutil.insertContracts(c);
					}
				}
				util.saveToSp("VERSION_KEY", currentVersion);
				MsgFragment.sendMsg();
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}*/
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	// 构造广播监听类，监听 SDK key 验证以及网络异常广播
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String error_message = intent.getAction();
		}
	}
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  

		@Override  
		public void onReceive(Context context, Intent intent) {  
			// TODO Auto-generated method stub  
			msg_un_text.setVisibility(View.VISIBLE);
		}  
	}; 
	public void onResume() {
		super.onResume();

		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		//		StatService.onResume(this);
		StatService.onPageStart(this, "JNBMainActivity");
	}

	public void onPause() {
		super.onPause();

		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		//		StatService.onPause(this);
		StatService.onPageEnd(this, "JNBMainActivity");
	}
}
