package com.nxt.ynt;

/**
 * ����
 * @author �Լ���
 *
 */
import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.nxtapp.NXTAPPApplication;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.ynt.entity.AppConfigData;
import com.nxt.ynt.imageutil.ImageLoader;
import com.nxt.ynt.page.ReadRaw;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.DataCleanManager;
import com.nxt.ynt.utils.Util;
import com.nxt.ynt.utils.VersionUtil;
import com.shelwee.update.UpdateHelper;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

public class SettingActivity extends Activity implements OnClickListener {
	private static final String TAG = "MoreFragment";
	private RelativeLayout more_yijianfangui;
	private RelativeLayout more_ruanjiangengxin;
	private RelativeLayout more_qinglihuancun;
	private ImageView iv_switch_sound;
	private TextView newversion;
	public static RelativeLayout progress;
	private Context context;
	private Util util;
	private String newversionStr;

	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				Toast.makeText(SettingActivity.this, "��ǰ�������°汾!", 0).show();
				break;
			}
			super.handleMessage(msg);
		}
	};
	private String update_txt_url;
	private String upload_apk_url;
	private RelativeLayout zihao;
	private Button logout;
	private TextView more_version;
	private RelativeLayout rl_share;
	public static ImageView faceImage;
	private String userpic;
	private ImageLoader loader;
	private String name;
	private String nick;
	private Activity view;
	public static TextView login_username;
	private RelativeLayout rl_myhead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		util = new Util(context);
		loader = ImageLoader.getInstance(context);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.more_setting);
		userpic = util.getFromSp(Util.UPIC, "");
		name = util.getFromSp(Util.UNAME, "");
		nick = util.getFromSp(Util.NICK, "");
		initview();
		initdata();
		/*
		 * ��ȡĳ���ļ��еĴ�С
		try {
		DataCleanManager.getFolderSize(this.getCacheDir());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 */
		try {
			String curVersion = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			String version = "Version" + curVersion + " @"
					+ getResources().getString(R.string.app_vername);
			util.saveToSp(com.nxt.nxtapp.common.Util.VERSION, curVersion);
			more_version.setText(version);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initdata() {
		// TODO Auto-generated method stub
		newversionStr = NXTAPPApplication.newVersion;
		AppConfigData appConfigData = ReadRaw.getAppConfigData(context);
		update_txt_url = com.nxt.nxtapp.setting.GetHost.getHost() + "/andriod/"
				+ appConfigData.getUpdateurl();// �汾������֤�ӿ�
		upload_apk_url = com.nxt.nxtapp.setting.GetHost.getHost() + "/andriod/"
				+ appConfigData.getVersionplist();// �汾�������ؽӿ�
		/*if (newversionStr != null)
			newversion.setText(newversionStr);*/
		if (nick != null && !"".equals(nick)) {
			login_username.setText(nick);
		} else {
			login_username.setText(name);
		}
		if (userpic != null && !"".equals(userpic)) {
			loader.displayImage(Constans.getHeadUri(userpic), faceImage);
		}else{
			faceImage.setImageResource(R.drawable.contractdefalut);
		}
	}

	private void initview() {
		// TODO Auto-generated method stub
		progress = (RelativeLayout) findViewById(R.id.topic_newsest_pro);
		more_yijianfangui = (RelativeLayout) findViewById(R.id.more_yijianfangui);
		more_ruanjiangengxin = (RelativeLayout) findViewById(R.id.more_ruanjiangengxin);
		// more_guanyu = (RelativeLayout) findViewById(R.id.more_guanyu);
		more_qinglihuancun = (RelativeLayout) findViewById(R.id.more_qinglihuancun);
		iv_switch_sound = (ImageView) findViewById(R.id.iv_switch_sound);
		logout=(Button)findViewById(R.id.logout);
		more_version = (TextView) findViewById(R.id.more_version);
		rl_share=(RelativeLayout)findViewById(R.id.rl_share);
		rl_myhead=(RelativeLayout)findViewById(R.id.more_myhead);
		faceImage = (ImageView) findViewById(R.id.iv_myhead);
		login_username = (TextView) findViewById(R.id.nickname);

		//		more_bangdingguanli = (RelativeLayout) findViewById(R.id.more_bangding_setting);
		zihao = (RelativeLayout) findViewById(R.id.more_zihao);
		newversion = (TextView) findViewById(R.id.newversion);

		more_yijianfangui.setOnClickListener(this);
		more_ruanjiangengxin.setOnClickListener(this);
		zihao.setOnClickListener(this);
		// more_guanyu.setOnClickListener(this);
		more_qinglihuancun.setOnClickListener(this);
		iv_switch_sound.setOnClickListener(this);
		logout.setOnClickListener(this);
		rl_share.setOnClickListener(this);
		rl_myhead.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.back) {
			finish();
		}else if (id == R.id.more_myhead) {
			Intent intent0 = new Intent(context, PersonalDetailsActivity.class);
			startActivity(intent0);
		}else if (id == R.id.rl_share) {
			Intent share = new Intent(context, ShareActivity.class);
			startActivity(share);

		}
		// else if (id == R.id.more_guanyu) {
		// Intent intent = new Intent(context, GuanYuActivity.class);
		// startActivity(intent);
		// }
		else if (id == R.id.more_ruanjiangengxin) {
			UpdateHelper updateHelper = new UpdateHelper.Builder(SettingActivity.this)
			.checkUrl(Constans.UPLOAD_VERSION)
			.isAutoInstall(true) //����Ϊfalse�����������ֶ������װ;Ĭ��ֵΪtrue�����غ��Զ���װ��
			.build();
			updateHelper.check(); 
			/*progress.setVisibility(View.VISIBLE);
			if (newversionStr != null && !newversionStr.equals("")) {
				try {
					LogUtil.LogE("SettingActivity", "�汾���");
					// ���汾�Ƿ���Ҫ����
					final VersionUtil util = new VersionUtil(context,
							upload_apk_url, update_txt_url,myHandler);
					new Thread() {
						@Override
						public void run() {
							util.doVersion();
						}
					}.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					// ���汾�Ƿ���Ҫ����
					String update_txt_url = util.getFromSp(Util.CHECK, "");
					String upload_apk_url = util.getFromSp(Util.DOWNLOAD, "");
					final VersionUtil util = new VersionUtil(this,
							upload_apk_url, update_txt_url, myHandler);
					new Thread() {

						@Override
						public void run() {
							util.doVersion();
						}

					}.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			progress.setVisibility(View.GONE);*/
		} else if (id == R.id.more_yijianfangui) {
			Intent intent1 = new Intent(context, FeedbackActivity.class);
			startActivity(intent1);
		} else if (id == R.id.more_qinglihuancun) {
			DataCleanManager.cleanInternalCache(this);
			DataCleanManager.cleanApplicationData(SettingActivity.this, null);
			Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
			/*new QingLiHuanCunTask(this).execute(JNBMainActivity.JSON_PATH,
					JNBMainActivity.HTML_PATH, JNBMainActivity.PIC_PATH);*/
		} else if (id == R.id.iv_switch_sound) {
			// TODO
			if ("true".equals(util.getFromSp(Util.ISSOUND, "true"))) {
				util.saveToSp(Util.ISSOUND, "false");
				iv_switch_sound.setBackgroundResource(R.drawable.close_icon);
			} else {
				util.saveToSp(Util.ISSOUND, "true");
				iv_switch_sound.setBackgroundResource(R.drawable.open_icon);
			}
		} /*else if (id == R.id.more_bangding_setting) {
			Intent intent = new Intent(context, BangDingSettingActivity.class);
			startActivity(intent);
		}*/else if (id == R.id.more_zihao) {//�����ֺŴ�С
			new AlertDialog.Builder(this)  
			.setTitle("���������С")  
			.setIcon(android.R.drawable.ic_dialog_info)                  
			.setSingleChoiceItems(new String[] {"���","�к�","С��"}, 0,   
					new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int which) {  
					dialog.dismiss(); 
					switch(which){
					case 0:
						util.saveToSp(util.TEXTSIZE, "0");
						break;
					case 1:
						util.saveToSp(util.TEXTSIZE, "1");
						break;
					case 2:
						util.saveToSp(util.TEXTSIZE, "2");
						break;
					}
				}  
			}  
					)  
					.setNegativeButton("ȡ��", null)  
					.show(); 
		}else if(id==R.id.logout){
			String uid = util.getFromSp(Util.UID, "");
			if (uid != null && !"".equals(uid)) {
				LogUtil.LogE(TAG, "��ʼע��");
				getloginout().show();
			} else {
				LogUtil.LogE(TAG, "������ע������");
			}
		}
	}
	public synchronized AlertDialog.Builder getloginout() {
		return new AlertDialog.Builder(context)
		.setCancelable(false)
		.setTitle("�û�ע��")
		.setMessage("ȷ��ע����ǰ�û���")
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				ProgressDialog progress = ProgressDialog.show(context,
						null, "���Ժ�...", true, true);
				progress.show();
				logout();
				progress.dismiss();
			}
		})
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
	}
	/**
	 * ע���߳�
	 */
	private void logout() {
		Intent intent = new Intent(context, DengLuActivity.class);
		Bundle bundle=new Bundle();
		bundle.putInt("flag", 0);
		intent.putExtras(bundle);
		CleanInf(util, context);
		startActivity(intent);
		this.finish();

		HttpCallBack callback = new HttpCallBack() {

			@Override
			public void onSuccess(String content) {
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		};

		NxtRestClientNew.post("loginouts", null, callback);
	}

	@SuppressWarnings("deprecation")
	public static void CleanInf(Util util, Context context) {
		util.saveToSp(Util.UID, "");
		util.saveToSp(Util.PWD, "");
		util.saveToSp(Util.PHONE, "");
		util.saveToSp(Util.area, "");
		util.saveToSp(Util.work, "");
		util.saveToSp(Util.NICK, "");
		util.saveToSp(Util.SEX, "");
		util.saveToSp(Util.TOKEN, "");
		util.saveToSp(Util.CONTRACTFLAG, "");
		util.saveToSp(Util.ISUPFRIENDS, "false");
		util.saveToSp(Util.LOGOUTFLAG, "logout");
		util.saveToSp("isLogin", "false");
		util.saveToSp(Util.ISINSTALL, "");
		util.saveToSp("flag",0);
		com.nxt.ynt.widget.Constans.TABLE_EXSIT = false;
		CookieSyncManager.createInstance(context);  
		CookieManager cookieManager = CookieManager.getInstance(); 
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
		/*// TODO ע��ʱ������ݿ����
		ChatDbHelper.instance = null;
		XmppApplication.chatDbHelper.closeDb();
		XmppApplication.AllFriendsMessageMapData.clear();
		// ֹͣ����
		context.stopService(new Intent(context, AutoStartService.class));
		context.stopService(new Intent(context, XmppService.class));
		XmppConnection.closeConnection();*/
	}
	// �ж��Ƿ�������
	private boolean isNetWork(Context context) {
		boolean b = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo isNetWork = cwjManager.getActiveNetworkInfo();
		if (isNetWork != null)
			b = true;
		return b;
	}
	class QingLiHuanCunTask extends AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog;

		public QingLiHuanCunTask(Context context) {
			dialog = ProgressDialog.show(context, null, "���Ժ�...", true, true);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast.makeText(context, "�����������", 0).show();
				dialog.dismiss();
			} else {
			}
		}

		@Override
		protected Boolean doInBackground(String... params) {
			CookieSyncManager.createInstance(context);  
			CookieManager cookieManager = CookieManager.getInstance(); 
			cookieManager.removeAllCookie();
			CookieSyncManager.getInstance().sync();
			File file = new File(params[0]);
			File file_str = new File(params[1]);
			File file_pic = new File(params[2]);
			deleteFile(file_str);
			deleteFile(file);
			deleteFile(file_pic);
			return true;
		}
	}

	public void deleteFile(File file) {
		// File file = new File(MainActivity.path+"/");/
		if (file.exists()) { // �ж��ļ��Ƿ����
			if (file.isFile()) { // �ж��Ƿ����ļ�
				file.delete(); // delete()���� ��Ӧ��֪�� ��ɾ������˼;
			} else if (file.isDirectory()) { // �����������һ��Ŀ¼
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
					this.deleteFile(files[i]); // ��ÿ���ļ� ������������е���
				}
			}
			file.delete();
		}
	}

	public static void delAllFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + tempList[i]);
				} else {
					temp = new File(path + File.separator + tempList[i]);
				}
				temp.delete();
			}
		}
	}

}
