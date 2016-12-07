package com.nxt.ynt;

/*
 * �˳�Ӧ�ø���
 */
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nxt.jnb.R;
import com.nxt.ynt.utils.Util;

public class AbsMainActivity extends FragmentActivity {
	public boolean first = false;
	private Timer timer = new Timer();
	private Util util;
	protected DisplayImageOptions options;
	protected ImageLoader loader;
	protected AbsMainActivity mActThis = null;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mActThis = this;
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
	    .imageScaleType(ImageScaleType.EXACTLY)
	    .bitmapConfig(Bitmap.Config.RGB_565)
	    .showImageOnLoading(R.drawable.pic_loading)
	    .cacheInMemory(true)
	    .cacheOnDisc(true)
	    .build();
	}

	// @Override
	// public boolean dispatchKeyEvent(KeyEvent event) {
	// if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
	// && event.getAction() == KeyEvent.ACTION_DOWN
	// && event.getRepeatCount() == 0) {
	// //���·��ؼ�ʲôҲ������
	// }
	// return super.dispatchKeyEvent(event);
	// }

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		class MyTask3 extends TimerTask {

			@Override
			public void run() {
				first = false;
			}
		}

		try {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (first) {
					// finish();
					// SoftApplication appState = (SoftApplication) this
					// appState.finishAll();
					// System.exit(0);
					util = new Util(this);
					util.saveToSp("flag", 0);
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// ע��
					intent.addCategory(Intent.CATEGORY_HOME);
					startActivity(intent);
				} else {
					first = true;
					Toast.makeText(this, "�ٰ�һ�η��ؼ������˵�", 0).show();
					timer.schedule(new MyTask3(), 2000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
