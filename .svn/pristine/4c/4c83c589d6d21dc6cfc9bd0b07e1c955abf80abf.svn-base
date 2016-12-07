package com.nxt.ynt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nxt.jnb.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.UEHandler;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.imageutils.ImageLoader;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.entity.AppConfigData;
import com.nxt.ynt.entity.CLAUSESItem;
import com.nxt.ynt.entity.LoginInfo;
import com.nxt.ynt.entity.NewPicture;
import com.nxt.ynt.entity.RSSItem;
import com.nxt.ynt.page.IntentActivityUtil;
import com.nxt.ynt.page.ReadRaw;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.Util;
import com.nxt.ynt.utils.VersionUtil;
import com.shelwee.update.UpdateHelper;

/**
 * @author Administrator
 * @category 启动页面
 */
public class SplashActivity extends FragmentActivity {
	private static String TAG = "SplashActivity";
	public static final int SLEEPAWHILE = 10;
	private static Util util;
	// 判断是否add定制列表的标识
	public static boolean dzFlag = true;
	private String mDeviceID;
	private static Context context;
	private String mainactivity;
	private String appname;
	private String update_txt_url;
	private String upload_apk_url;
	private String navtype;
	private String siteid;
	private String areaid;
	private String homebutton;
	public String autoRegister;
	private String isTourist;
	private String xnbmsg,showmsg;
	public static String params, menuurl;
	private ImageLoader imageLoader;
	private ImageView img;
	public static int pd_splash = 0;
	private String time;
	private int widthPx;
	private int HeigtPx;
	private SoftApplication appState;
	private RSSItem rssItem;
	private String second_jsondata;
	private List<CLAUSESItem> clausesItems;
	private List<String> newsid;

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd_splash = 1;
			if (msg.what == 1) {
			} else if (msg.what == 100) {
				LogUtil.LogE(TAG, "版本检测放在动画接受之前");
			} else {
				NewPicture snp = (NewPicture) msg.obj;
				if (snp != null) {
					if (snp.getAdImg() != null && snp.getTime().equals(time)) {
						imageLoader = ImageLoader.getInstance(context);
						imageLoader.displayImage(
								Constans.DOWNLOAD_IMAGE + snp.getAdImg(), img);
					}
				}
			}
		}
	};
	private String ver;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Build.VERSION.SDK_INT>=14){
			//隐藏底部的虚拟按键顶部导航栏,此方法只能是在api 14以上才可用
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

		}else{
			requestWindowFeature(Window.FEATURE_NO_TITLE);// 去头条目 全屏
		}
		context = this;
		appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.activity_splash);
		util = new Util(context);
		AppConfigData appConfigData = ReadRaw.getAppConfigData(context);
		mainactivity = appConfigData.getMainactivity();// 主activity名称
		appname = appConfigData.getAppname();// 程序名称
		UEHandler.product = appname;
		isTourist = appConfigData.getIsTourist();// 是否是游客模式
		update_txt_url = com.nxt.nxtapp.setting.GetHost.getHost() + "/andriod/"
				+ appConfigData.getUpdateurl();// 版本更新验证接口
		upload_apk_url = com.nxt.nxtapp.setting.GetHost.getHost() + "/andriod/"
				+ appConfigData.getVersionplist();// 版本更新下载接口
		navtype = appConfigData.getNavtype();// 首页形式
		siteid = appConfigData.getSiteid();// siteid
		areaid = appConfigData.getAreaid();// 地区id
		autoRegister = appConfigData.getAutoRegister();
		homebutton = appConfigData.getHomebutton();
		menuurl = appConfigData.getMenuurl();
//		xnbmsg = appConfigData.getXnbmsg();
		showmsg = appConfigData.getShowmsg();
		LogUtil.LogE(TAG, "navtype=" + navtype + ",mainactivity=="
				+ mainactivity + ",update_txt_url==" + update_txt_url
				+ ",appname==" + appname + ",upload_apk_url==" + upload_apk_url
				+ ",siteid==" + siteid + ",areaid==" + areaid + ",menuurl=="
				+ menuurl + ",homebutton==" + homebutton + ",autoRegister=="
				+ autoRegister + ",isTourist==" + isTourist + ",xnbmsg=="
				+ xnbmsg);
//		util.saveToSp("xnbmsg", xnbmsg);
		util.saveToSp("showmsg", showmsg);
		util.saveToSp("isTourist", isTourist);
		util.saveToSp(Util.CHECK, update_txt_url);
		util.saveToSp(Util.DOWNLOAD, upload_apk_url);
		util.saveToSp(Util.MAINACTIVITY, mainactivity);
		util.saveToSp(Util.APPNAME, appname);
		util.saveToSp(Util.HOMEBUTTON, homebutton);
		util.saveToSp(Util.IFSYSOLOG,
				context.getResources().getString(R.string.ifSysoLog));
		/*if (VersionUtil.isNetWork(context)) {
			NxtRestClient.post(xnbmsg, null, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					LogUtil.LogE(TAG, "得到网络msg数据:" + content);
					if (content == null) {
						LogUtil.LogE(TAG, "msg数据===null");
					} else {
						util.saveToSp("xnbmsg.json", content);
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					LogUtil.LogE(TAG, "onFailure：" + content);
				}
			});
		}*/
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mDeviceID = tm.getDeviceId();
		util.saveToSp(Util.IMEI, mDeviceID);
//		mLocClient = ((SoftApplication) getApplication()).mLocationClient;
//		LocationUtil.setLocationOption(context, mLocClient);
//		mLocClient.start();
		//		initWeatherDatabase();
		// if (util.getBooleanFromSp("firststart", true) &&
		// appname.equals("农信")) {
		// GuideFrament localGuideFrament = new GuideFrament();
		// localGuideFrament.setHandler(handler);
		// FragmentTransaction localFragmentTransaction =
		// getSupportFragmentManager()
		// .beginTransaction();
		// localFragmentTransaction.replace(R.id.rl_main, localGuideFrament);
		// localFragmentTransaction.commitAllowingStateLoss();
		// } else {
		// 获取屏幕分辩率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthPx = dm.widthPixels;
		HeigtPx = dm.heightPixels;
		// 获取当前时间
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd   HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		time = formatter.format(curDate);
		// 执行获取网络图片功能
		//		changeImage();
		com.nxt.nxtapp.common.LogUtil.syso("isNewsPicture执行到。。。。。");
		img = (ImageView) findViewById(R.id.img_welcome);
		// 开机动画
		AlphaAnimation alpAnimation = new AlphaAnimation(0.01f,1.0f);
		alpAnimation.setDuration(4000);//设置动画持续时间
		alpAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				UpdateHelper updateHelper = new UpdateHelper.Builder(SplashActivity.this,"0")
				.checkUrl(Constans.UPLOAD_VERSION)
				.isAutoInstall(true) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
				.build();
				updateHelper.check(); 
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				startactivity();
				/*if(util.getFromSp("isfirstInstall", null)==null){//为空表示初次打开，提示加载VR引擎
					if(!"false".equals(util.getFromSp("X5FirstLoad", ""))){
						//显示加载VR引擎提示
						startactivity();
//						showdialog();
					}else{
						startactivity();
					}
				}else{
					startactivity();
				}*/
			}
		});
		img.setAnimation(alpAnimation);
		alpAnimation.start();
		// }
	}


	public void startactivity() {
		// TODO Auto-generated method stub
		String pd = context.getResources().getString(
				R.string.ifDatabaseSee);
		String sql_pd = util.getFromSp("sql_pd", null);
		LogUtil.LogE(TAG, "sql_pd===" + sql_pd);
		/*if (sql_pd == null || !pd.equals(sql_pd)) {
			LogUtil.LogE(TAG, "删除旧数据库        执行注销操作，清空数据库相关");
			// 删除旧数据库
			File dbFile = context.getDatabasePath("xnb.db");
			dbFile.delete();
			// 执行注销操作，清空数据库相关
			util.saveToSp(Util.UID, "");
			util.saveToSp(Util.PWD, "");
			util.saveToSp(Util.PHONE, "");
			util.saveToSp(Util.area, "");
			util.saveToSp(Util.work, "");
			util.saveToSp(Util.NICK, "");
			util.saveToSp(Util.SEX, "");
			util.saveToSp(Util.CONTRACTFLAG, "");
			util.saveToSp(Util.ISUPFRIENDS, "false");
			util.saveToSp(Util.DB_VER, 1000);
		}*/
		String uid = util.getFromSp(Util.UID, "");
		LogUtil.LogE(TAG, "用户id" + uid);
		if (uid.equals("") || uid == null) {
			// 是否要自动注册登录
			if ("true".equals(autoRegister)) {
				try {
					register();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				new Thread() {
					public void run() {
						Intent intent = new Intent();
						intent.setClass(SplashActivity.this,
								DengLuActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("mainactivity", mainactivity);
						bundle.putString("appname", appname);
						bundle.putString("updateurl", update_txt_url);
						bundle.putString("versionplist", upload_apk_url);
						bundle.putString("homebutton", homebutton);
						bundle.putString("mDeviceID", mDeviceID);
						bundle.putString("isTourist", isTourist);
						intent.putExtras(bundle);
						startActivity(intent);
					};
				}.start();
			}
		} else {
			IntentActivityUtil.toActivity(context,
					mainactivity, appname, update_txt_url,
					upload_apk_url, homebutton);
		}
	}


	/**
	 * 注册的线程
	 */
	public void register() {
		LogUtil.LogE(TAG, "游客身份进入");
		Map<String, String> params = new HashMap<String, String>();
		params.put("siteid", getResources().getString(R.string.siteid));
		params.put("uname", mDeviceID);
		params.put("pwd", "123456");
		params.put("area", SoftApplication.area);
		params.put("status", "0");
		NxtRestClientNew.post("register", params, new HttpCallBack() {

			@Override
			public void onSuccess(String content) {
				//				content = this.getContent(content);
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
				util.saveToSp("flag",0);
				IntentActivityUtil.toActivity(context, mainactivity, appname,
						update_txt_url, upload_apk_url, homebutton);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.syso("onFailure：" + content);
				util.saveToSp(Util.UNAME, mDeviceID);
				util.saveToSp(Util.UID, mDeviceID);
				util.saveToSp(Util.NICK, "游客" + mDeviceID);
				IntentActivityUtil.toActivity(context, mainactivity, appname,
						update_txt_url, upload_apk_url, homebutton);
			}
		});
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	private  void showdialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context, R.style.dialog);
		View inflate = View.inflate(context, R.layout.x5dialog, null);
		TextView dialogTitle = (TextView) inflate.findViewById(R.id.dialog_title);
		TextView dialogCancel = (TextView) inflate.findViewById(R.id.del_cancel);
		dialogCancel.setVisibility(View.GONE);
		dialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				util.saveToSp("isfirstInstall", "true");
				dialog.dismiss();
				startactivity();
			}


		});
		TextView dialogConfirm = (TextView) inflate.findViewById(R.id.confirm_del);
		dialogConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				util.saveToSp("isfirstInstall", "true");//保存使其不为空，为空时表示初次打开
				util.saveToSp("X5FirstLoad", "false");//是否加载VR引擎
				android.os.Process.killProcess(Process.myPid());
			}
		});
		dialog.setContentView(inflate);
		dialog.show();
	}

}
