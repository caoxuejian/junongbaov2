package com.jutong.live;

import com.nxt.jnb.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Live_MainActivity extends Activity implements OnClickListener,
		Callback, LiveStateChangeListener {

	private ImageView button01;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private boolean isStart;
	private LivePusher livePusher;
	private Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case -100:
				Toast.makeText(Live_MainActivity.this, "视频预览开始失败", 0).show();
				livePusher.stopPusher();
				break;
			case -101:
				Toast.makeText(Live_MainActivity.this, "音频录制失败", 0).show();
				livePusher.stopPusher();
				break;
			case -102:
				Toast.makeText(Live_MainActivity.this, "音频编码器配置失败", 0).show();
				livePusher.stopPusher();
				break;
			case -103:
				Toast.makeText(Live_MainActivity.this, "视频频编码器配置失败", 0).show();
				livePusher.stopPusher();
				break;
			case -104:
				Toast.makeText(Live_MainActivity.this, "流媒体服务器/网络等问题", 0).show();
				livePusher.stopPusher();
				break;
			}
//			button01.setText("推流");
			tv_title.setVisibility(View.GONE);
			button01.setBackground(getResources().getDrawable(R.drawable.start));
			isStart = false;
		};
	};
	private ImageView iv_back;
	private String rtmpurl;
	private TextView tv_title;
	private String videoid;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_activity_main);
		button01 = (ImageView) findViewById(R.id.button_first);
		iv_back=(ImageView)findViewById(R.id.iv_back);
		rtmpurl=getIntent().getStringExtra("rtmpurl");
		videoid=getIntent().getStringExtra("videoid");
		tv_title=(TextView)findViewById(R.id.tv_title);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		button01.setOnClickListener(this);
		button01.setBackground(getResources().getDrawable(R.drawable.start));
		findViewById(R.id.button_take).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						livePusher.switchCamera();
					}
				});
		mSurfaceView = (SurfaceView) this.findViewById(R.id.surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		livePusher = new LivePusher(this, 960, 720, 256000, 15,
				CameraInfo.CAMERA_FACING_BACK);
		livePusher.setLiveStateChangeListener(this);
		livePusher.prepare(mSurfaceHolder);

	}

	// @Override
	// public void onRequestPermissionsResult(int requestCode,
	// String[] permissions, int[] grantResults) {
	// super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		livePusher.relase();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (isStart) {
//			button01.setText("推流");
			tv_title.setVisibility(View.GONE);			
			button01.setBackground(getResources().getDrawable(R.drawable.start));
			isStart = false;
			livePusher.stopPusher();
		} else {
//			button01.setText("停止");
			tv_title.setVisibility(View.VISIBLE);
			button01.setBackground(getResources().getDrawable(R.drawable.stop));
			isStart = true;
			livePusher.startPusher(rtmpurl);// TODO: 设置流媒体服务器地址

		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("@@@@@@@: CREATE");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		System.out.println("@@@@@@@@@: CHANGE");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("@@@@@@@@@@@: DESTORY");
	}

	/**
	 * 可能运行在子线程
	 */
	@Override
	public void onErrorPusher(int code) {
		System.out.println("@@@@@@:" + code);
		mHandler.sendEmptyMessage(code);
	}

	/**
	 * 可能运行在子线程
	 */
	@Override
	public void onStartPusher() {
		Log.d("@@@@@@@@@@", "开始推流");
	}

	/**
	 * 可能运行在子线程
	 */
	@Override
	public void onStopPusher() {
		Log.d("@@@@@@@@", "结束推流");
	}
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			livePusher.stopPusher();
			livePusher.relase();
			finish();
		}
		return true;
	}*/

}
