package com.nxt.nxtapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.nxt.nxtapp.common.BitmapCommonUtils;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.setting.GetHost;

public class AlertMsgs extends Activity {
	private static final String BASE_URL = GetHost.getHost();
	private LinearLayout layout, button_box;
	public static final int ALERTMSG = 1001;
	public static final int UPDATEVERSIN = 1002;
	private TextView title, content;
	private Button exitBtn0, exitBtn1;
	private String newstype,filename,path;
	/* 下载包安装路径 */
	private static String savePath;
	private String saveFileName;
	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar pb;
	int fileSize;
	int downLoadFileSize;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private Thread downLoadThread;
	private boolean interceptFlag = false;
	private Thread loadThread;
	private NotificationManager mNotificationManager;
	private static final int NOTIFY_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alertmsg);
		newstype = getIntent().getStringExtra("newstype");
		savePath = BitmapCommonUtils.getDiskCacheDir(this, "nxtapk")
				.getAbsolutePath();
		layout = (LinearLayout) findViewById(R.id.exit_layout);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!"force".equals(newstype))
					Toast.makeText(getApplicationContext(), "版本更新提示：点击窗口外部关闭窗口！",
							Toast.LENGTH_SHORT).show();
			}
		});
		button_box = (LinearLayout) findViewById(R.id.button_box);
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
		exitBtn0 = (Button) findViewById(R.id.exitBtn0);
		exitBtn1 = (Button) findViewById(R.id.exitBtn1);
		pb = (ProgressBar) findViewById(R.id.myprogreessbar);
		pb.setVisibility(View.GONE);
		int mode = getIntent().getIntExtra("mode", 0);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		dowork(mode);

	}

	Notification mNotification;

	// 通知栏
	/**
	 * 创建通知
	 */
	private void setUpNotification() {
		int icon = R.drawable.logo;
		CharSequence tickerText = "开始下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		;
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.download_notification_layout);
		contentView.setTextViewText(R.id.name, "正在下载...");
		// 指定个性化视图
		mNotification.contentView = contentView;

		Intent intent = new Intent();
		// 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
		// 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
		// 是这么理解么。。。
		// intent.setAction(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 指定内容意图
		mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}
	
	private void dowork(int mode) {
		path = getIntent().getStringExtra("path");
		switch (mode) {
		case ALERTMSG:
			// button_box.setVisibility(View.INVISIBLE);
			title.setText("系统公告");
			content.setText(path);
			exitBtn0.setVisibility(View.GONE);
			exitBtn1.setText("确\t认");
			break;
		case UPDATEVERSIN:
			// button_box.setVisibility(View.VISIBLE);
			title.setText("版本更新提示");
			content.setText(path);
			if ("force".equals(newstype)) {
				exitBtn1.setVisibility(View.GONE);
				exitBtn0.setVisibility(View.VISIBLE);
			} else {
				exitBtn0.setVisibility(View.VISIBLE);
				exitBtn1.setText("取\t消");
			}

			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!"force".equals(newstype))
			finish();
		return true;
	}

	public void okbutton(View v) {
		interceptFlag = true;
//		title.setText("文件下载");
//		exitBtn1.setVisibility(View.VISIBLE);
//		exitBtn0.setVisibility(View.GONE);
//		exitBtn1.setText("取\t消");
//		pb.setVisibility(View.VISIBLE);
		String down_url = getIntent().getStringExtra("versionplist");
		setUpNotification();
		down_url=getAbsoluteUrl(down_url);
		LogUtil.LogI("url", "===========down_url = "+  down_url);
		downloadApk(down_url);
		
	}

	private static String getAbsoluteUrl(String relativeUrl) {
	
		String url;
		if (relativeUrl != null && relativeUrl.startsWith("http")) {
			url = relativeUrl;
		} else if (relativeUrl != null && relativeUrl.startsWith("/")) {
			url = BASE_URL + relativeUrl;
		} else {
			url = BASE_URL + "/" + relativeUrl;
		}
		return url;
	}
	
	
	
	public void canclebut(View v) {
		interceptFlag = false;
		if ("force".equals(newstype)) {
			pb.setVisibility(View.GONE);
			title.setText("版本更新提示");
			exitBtn1.setVisibility(View.GONE);
			exitBtn0.setVisibility(View.VISIBLE);
			content.setText(path);
		}else{
			this.finish();
		}
		
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					//pb.setMax(fileSize);
					AlertMsgs.this.finish();
					com.nxt.nxtapp.common.LogUtil.syso("finish$$$$$$$$$$$");
					break;
				case 1:
					int result = msg.arg1;
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(R.id.tv_progress, result + "%");
					contentview.setProgressBar(R.id.progressbar, 100, result,
							false);
					mNotificationManager.notify(NOTIFY_ID, mNotification);
					break;
				case 2:					
						com.nxt.nxtapp.common.LogUtil.syso("下载完毕!!!!!!!!!!!");
						// 下载完毕后变换通知形式
						mNotification.flags = Notification.FLAG_AUTO_CANCEL;
						mNotification.contentView = null;
						mNotification.icon = R.drawable.ic_launcher;
						Intent intent = new Intent();
						// 告知已完成
						intent.putExtra("completed", "yes");
						// 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
						PendingIntent contentIntent = PendingIntent.getActivity(
								AlertMsgs.this, 0, intent,
								PendingIntent.FLAG_UPDATE_CURRENT);
						mNotification.setLatestEventInfo(AlertMsgs.this, "下载完成",
								"文件已下载完毕", contentIntent);
						//
					//}
					mNotificationManager.notify(NOTIFY_ID, mNotification);
					File file = new File(filename);
					openFile(file);
					break;
				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(AlertMsgs.this, error, 1).show();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	private void downloadApk(final String url){
		//setUpNotification();
		loadThread = new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				File file1 = new File(savePath);
				if (file1.exists())
					file1.delete();
				file1.mkdir();
				try {
					down_file(url, savePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		loadThread.start();
	}
	private int progress,lastRate=0;
	public void down_file(String url, String path) throws IOException {
		// 下载函数
		filename = path+File.separator+url.substring(url.lastIndexOf("/") + 1);
		com.nxt.nxtapp.common.LogUtil.syso("文件地址："+filename+" \nurl:"+url);
		// 获取文件名
		URL myURL = new URL(url);
		URLConnection conn = myURL.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		this.fileSize = conn.getContentLength();// 根据响应获取文件大小
		if (this.fileSize <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (is == null)
			throw new RuntimeException("stream is null");
		FileOutputStream fos = new FileOutputStream(filename);
		// 把数据存入路径+文件名
		byte buf[] = new byte[1024];
		downLoadFileSize = 0;
		sendMsg(0);
		do {
			// 循环读取
			int numread = is.read(buf);
			if (numread == -1) {
				break;
			}
			fos.write(buf, 0, numread);
			downLoadFileSize += numread;
			progress = (int) (((float) downLoadFileSize / fileSize) * 100);
			// 更新进度
			Message msg = mHandler.obtainMessage();
			msg.what = 1;
			msg.arg1=progress;
			if (progress >= lastRate + 1) {
				mHandler.sendMessage(msg);// 更新进度条
				lastRate = progress;
			}
			
		} while (true&&interceptFlag);
		
		sendMsg(2);// 通知下载完成
		try {
			is.close();
		} catch (Exception ex) {
			com.nxt.nxtapp.common.LogUtil.LogE("tag", "error: " + ex.getMessage());
		}

	}

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		mHandler.sendMessage(msg);
	}
	private void mdownApkRunnable(final String url) {
		NxtRestClient.get(url, null, new FileAsyncHttpResponseHandler(
				this) {

			@Override
			public void onSuccess(File file) {
				// TODO Auto-generated method stub
				super.onSuccess(file);
				File file1 = new File(savePath);
				if (file1.exists())
					file1.delete();
				file1.mkdir();
				String apkFile = savePath + "/xinxitong.apk";
				File ApkFile = new File(apkFile);
				try {
					int bytesum = 0;
					int byteread = 0;
					InputStream inStream = new FileInputStream(file); // 读入原文件
					FileOutputStream fs = new FileOutputStream(ApkFile);
					byte[] buffer = new byte[1024];
					int length;
					while ((byteread = inStream.read(buffer)) != -1) {
						bytesum += byteread; // 字节数 文件大小
						fs.write(buffer, 0, byteread);
					}
					inStream.close();
				} catch (Exception e) {
					com.nxt.nxtapp.common.LogUtil.syso("复制单个文件操作出错");
					e.printStackTrace();

				}
				openFile(ApkFile);
			}

		});
	}

	// 打开APK程序代码
	private void openFile(File file) {
		// TODO Auto-generated method stub
		com.nxt.nxtapp.common.LogUtil.LogE("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
		mNotificationManager.cancel(NOTIFY_ID);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!"force".equals(newstype)){
				finish();
			}else{
				Toast.makeText(AlertMsgs.this, "升级才能使用", 1).show();
			}
				
			
		}
		// return super.onKeyDown(keyCode, event);
		return true;
	}

}
