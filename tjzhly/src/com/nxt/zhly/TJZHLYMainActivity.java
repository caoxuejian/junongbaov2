package com.nxt.zhly;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mobstat.StatService;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.BitmapCommonUtils;
import com.nxt.ynt.AbsMainActivity;
import com.nxt.ynt.DengLuActivity;
import com.nxt.ynt.LivesActivity;
import com.nxt.ynt.SettingActivity;
import com.nxt.ynt.SoftApplication;
import com.nxt.ynt.X5WebviewActivity;
import com.nxt.ynt.utils.Listdata;
import com.nxt.ynt.utils.Util;
import com.nxt.ynt.x5view.FirstLoadingX5Service;
import com.nxt.zhly.fragment.X5MainFragment;
import com.nxt.zhly.util.Constans;
import com.tencent.smtt.sdk.QbSdk;

/**
 * @author 
 */
public class TJZHLYMainActivity extends AbsMainActivity implements OnClickListener {
	private static String TAG = "XNBMainActivity";
	private TextView category_title;
	private int width;
	private LinearLayout tab1, tab2, tab3, tab4, tab5;
	// ������
	private RelativeLayout categoryTitle;
	private Context context;
	// ����·��
	public static String HTML_PATH;
	public static String JSON_PATH;
	public static String PIC_PATH;
	// �ֻ���Ļ
	public static int widthPx;
	public static int HeigtPx;
	// tab��δ����Ϣ����
	public static int msg_un_num = 0;
	public static TextView msg_un_text;
	public static TextView dongtai_un_text;
	// �๦�ܰ�ť
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

		/**
		 * X5�ں���ʹ��preinit�ӿ�֮�󣬶����״ΰ�װ�״μ���û��Ч��
		 * ʵ���ϣ�X5webview��preinit�ӿ�ֻ�ǽ�����webview��������ʱ�䣻
		 * ��ˣ��ֽ׶�Ҫ�������״ΰ�װ�״μ���X5�ںˣ�����Ҫ��X5�ں���ǰ��ȡ���ں˵ļ�������
		 */
		if (!QbSdk.isTbsCoreInited()) {// preinitֻ��Ҫ����һ�Σ�����Ѿ�����˳�ʼ������ô��ֱ�ӹ���view
			QbSdk.preInit(TJZHLYMainActivity.this);// ����X5��ʼ����ɵĻص��ӿ�
			// ����������Ϊtrue������״μ���ʧ����������Լ��أ�
		}

		/**
		 * ����������� ���� Ԥ����X5�ںˣ� �˲��������������̵���X5�ں�ǰ���У����򽫲���ʵ��Ԥ����
		 */
		Intent intent = new Intent(this, FirstLoadingX5Service.class);
		startService(intent);

		//		PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY,getResources().getString(R.string.pushID));
		context = this;
		util = new Util(context);
		// ��ʼ������
		isLogin=util.getFromSp("isLogin", "");
		flag=util.getFromSp("flag", 0);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		// ����·��
		HTML_PATH = BitmapCommonUtils.getDiskCacheDir(this, "cachehtml")
				.getAbsolutePath();
		JSON_PATH = BitmapCommonUtils.getDiskCacheDir(this, "cachejson")
				.getAbsolutePath();
		PIC_PATH = BitmapCommonUtils.getDiskCacheDir(this, "cachepic")
				.getAbsolutePath();
		// ��ȡ��Ļ�ֱ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthPx = dm.widthPixels;
		HeigtPx = dm.heightPixels;
		// ��ʼ������
		initView();
		if("true".equals(isLogin)){
			// ��ʼ�����ݿ�
			//			initDataBase();
			// �޸��Ƿ��ǿ��������ı�ʶ
			util.saveToSp(Util.ISAUTOSTART, "false");
			util.saveToSp(Util.ISINMYCHAT, "false");
			isinstall=util.getFromSp(Util.ISINSTALL,"");
		}
		//�ٶ�push
		IntentFilter bdfilter = new IntentFilter("com.nxt.ynt.BaiDuPushMessage");  
		registerReceiver(broadcastReceiver, bdfilter);
		// ע�� SDK �㲥������
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
	}

	// ��ʼ������
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
		//���Ϲ����ϽǵĹ��ﳵ
		btn_gwc=(ImageView)findViewById(R.id.button_add);
		//�ҵ�����
		iv_shezhi=(ImageView)findViewById(R.id.iv_shezhi);
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
	@Override
	public void onDestroy() {
		super.onDestroy();
		util.saveToSp("flag",0);
		// ȡ������ SDK �㲥
	}

	// ����¼��Ĵ���
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.tabmain) {// �ͻ��˵�tab1(��ҳ)
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
		} else if (id == R.id.tab1) {// �ͻ��˵�tab2(��Ϣ)
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
		} else if (id == R.id.tab2) {// �ͻ��˵����Ϲ�
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
		} else if (id == R.id.tab4) {// �ͻ��˵���
			btn_gwc.setVisibility(View.GONE);
			/*if(!"true".equals(isLogin)){
				Intent intent=new Intent();
				Bundle bundle=new Bundle();
				bundle.putInt("flag", 5);
				bundle.putString("ifback", "true");
				intent.putExtras(bundle);
				intent.setClass(this, DengLuActivity.class);
				startActivity(intent);
			}else{*/
			category_title.setText(getResources().getString(R.string.xnb_me));
			X5MainFragment fragment_home = new X5MainFragment();
			Bundle bundle = new Bundle();
			bundle.putString("URL",Constans.GOUWU_DINGDAN);
			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			//				setTabSelect(4);
			setViewBackground(tab4, 4);
			//			}
		}else if (id == R.id.iv_shezhi) {
			Listdata data = new Listdata();
			Intent intent=new Intent();
			intent.putExtra("title", "�����");
			intent.putExtra("content", data.titles);
			intent.setClass(this, LivesActivity.class);
			startActivity(intent);
		} else if (id == R.id.button_sousuo) {

		} 
	}

	// ���Ч������
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

	/*// ��һ���������򣬽������ļ�������ݲ��뵽���ݿ���
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
				LogUtil.LogE(TAG, "xnbmsg�е�ver:" + json_ver + ";���ݿ��е�ver:"
						+ db_ver);
				if (currentVersion > lastVersion) {
					LogUtil.LogE(TAG, "�汾�����˻��һ�ΰ�װ��¼�����õ�������ϢcurrentVersion:"
							+ currentVersion + " lastVersion:" + lastVersion);
					dbutil.deleteMsg();
					for (XNBmsg msg : list) {
						// TODO �������ݿ�
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
					LogUtil.LogE(TAG, "���ݿ�ver��xnbmsg.json��ver��ͬ��ɾ�������������");
					dbutil.deleteMsg();// ɾ��isdefault==1������
					for (XNBmsg msg : list) {
						// TODO �������ݿ�
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
				LogUtil.LogE(TAG, "���ص�josn���ݣ�" + jsonstr);
				NewsRoot raw_rootdata = (NewsRoot) JsonPaser.getObjectDatas(
						NewsRoot.class, jsonstr);
				List<XNBmsg> raw_list = JsonPaser.getArrayDatas(XNBmsg.class,
						raw_rootdata.getNews());
				String raw_json_ver = raw_rootdata.getVer();
				dbutil.deleteMsg();
				for (XNBmsg msg : raw_list) {
					// TODO �������ݿ�
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
	// ����㲥�����࣬���� SDK key ��֤�Լ������쳣�㲥
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
		 * ҳ����ʼ��ÿ��Activity�ж���Ҫ��ӣ�����м̳еĸ�Activity���Ѿ�����˸õ��ã���ô��Activity����ز�����ӣ�
		 * ������StatService.onPageStartһ��onPageEnd��������ʹ��
		 */
		//		StatService.onResume(this);
		StatService.onPageStart(this, "JNBMainActivity");
	}

	public void onPause() {
		super.onPause();

		/**
		 * ҳ�������ÿ��Activity�ж���Ҫ��ӣ�����м̳еĸ�Activity���Ѿ�����˸õ��ã���ô��Activity����ز�����ӣ�
		 * ������StatService.onPageStartһ��onPageEnd��������ʹ��
		 */
		//		StatService.onPause(this);
		StatService.onPageEnd(this, "JNBMainActivity");
	}
}
