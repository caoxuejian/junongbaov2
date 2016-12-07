package com.nxt.ynt;

/*
 * 自动升级提示界面
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.nxtapp.NXTAPPApplication;
import com.nxt.nxtapp.common.BitmapCommonUtils;
import com.nxt.nxtapp.common.LogUtil;

public class AlertMsgs extends Activity {
	private LinearLayout layout;
	public static final int ALERTMSG = 1001;
	public static final int UPDATEVERSIN = 1002;
	private TextView title, content;
	private Button exitBtn0, exitBtn1;
	private String newstype, filename, path;
	/* 下载包安装路径 */
	private static String savePath;
	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar pb;
	int fileSize;
	int downLoadFileSize;
	private Thread loadThread;
	private boolean downfalg = true;
	private String TAG = "AlertMsgs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alertmsg);
		SplashActivity splash = new SplashActivity();
		if(splash.handler!=null){
			LogUtil.LogE(TAG, "**********************");
			Message msg = new Message();
			msg.what = 100;
			splash.handler.sendMessage(msg);
		}
		newstype = getIntent().getStringExtra("newstype");
		savePath = BitmapCommonUtils.getDiskCacheDir(this, "nxtapk")
				.getAbsolutePath();
		layout = (LinearLayout) findViewById(R.id.exit_layout);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!"force".equals(newstype))
					Toast.makeText(getApplicationContext(),
							"版本更新提示：点击窗口外部关闭窗口！", Toast.LENGTH_SHORT).show();
			}
		});
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
		exitBtn0 = (Button) findViewById(R.id.exitBtn0);
		exitBtn1 = (Button) findViewById(R.id.exitBtn1);
		pb = (ProgressBar) findViewById(R.id.myprogreessbar);
		pb.setVisibility(View.GONE);
		int mode = getIntent().getIntExtra("mode", 0);
		dowork(mode);
	}

	private void dowork(int mode) {
		path = getIntent().getStringExtra("path");
		switch (mode) {
		case ALERTMSG:
			title.setText("系统公告");
			content.setText(path);
			exitBtn0.setVisibility(View.GONE);
			exitBtn1.setText("确\t认");
			break;
		case UPDATEVERSIN:
			LogUtil.LogE("AlertMsgs", "UPDATEVERSIN-------1");
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
		LogUtil.LogE("AlertMsgs", "UPDATEVERSIN-------2");
		downfalg = true;
		title.setText("文件下载");
		exitBtn1.setVisibility(View.VISIBLE);
		exitBtn0.setVisibility(View.GONE);
		exitBtn1.setText("取\t消");
		pb.setVisibility(View.VISIBLE);
		String down_url = getIntent().getStringExtra("versionplist");
		downloadApk(down_url);
	}

	public void canclebut(View v) {
		downfalg = false;
		if ("force".equals(newstype)) {
			pb.setVisibility(View.GONE);
			title.setText("版本更新提示");
			exitBtn1.setVisibility(View.GONE);
			exitBtn0.setVisibility(View.VISIBLE);
			content.setText(path);
		} else {
			this.finish();
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					pb.setMax(fileSize);
				case 1:
					pb.setProgress(downLoadFileSize);
					int result = downLoadFileSize * 100 / fileSize;
//					content.setText(result + "%");
					break;
				case 2:
					if (downfalg) {
						File file = new File(filename);
						openFile(file);
						NXTAPPApplication.newVersion= "已是最新版本";
					}
					content.setText(path);
					title.setText("版本更新提示");
					pb.setVisibility(View.GONE);
					if ("force".equals(newstype)) {
						exitBtn1.setVisibility(View.GONE);
						exitBtn0.setVisibility(View.VISIBLE);
					} else {
						exitBtn0.setVisibility(View.VISIBLE);
						exitBtn1.setText("取\t消");
					}
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

	private void downloadApk(final String url) {
		loadThread = new Thread() {

			@Override
			public void run() {
				super.run();
				File file1 = new File(savePath);
				if (file1.exists())
					file1.delete();
				file1.mkdir();
				try {
					down_file(url, savePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		loadThread.start();
	}

	public void down_file(String url, String path) throws IOException {
		LogUtil.LogE("AlertMsgs", "UPDATEVERSIN-------3");
		// 下载函数
		filename = path + File.separator
				+ url.substring(url.lastIndexOf("/") + 1);
		LogUtil.LogE(TAG, "文件地址：" + filename + " \nurl:" + url);
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
		int i = 0;
		do {
			// 循环读取
			int numread = is.read(buf);
			if (numread == -1) {
				break;
			}
			fos.write(buf, 0, numread);
			downLoadFileSize += numread;
			sendMsg(1);// 更新进度条
		} while (true && downfalg);
		sendMsg(2);// 通知下载完成
		try {
			is.close();
		} catch (Exception ex) {
			com.nxt.nxtapp.common.LogUtil.LogE("tag",
					"error: " + ex.getMessage());
		}
	}

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		mHandler.sendMessage(msg);
	}

	// 打开APK程序代码
	private void openFile(File file) {
		com.nxt.nxtapp.common.LogUtil.LogE("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!"force".equals(newstype)) {
				finish();
			} else {
				Toast.makeText(AlertMsgs.this, "升级才能使用", 1).show();
			}
		}
		return true;
	}

}
