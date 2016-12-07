package com.nxt.nxtapp;
/*
 * 点击返回键退出
 */
import java.util.Timer;
import java.util.TimerTask;

import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

public class AbsMainActivity extends FragmentActivity {
	public boolean first = false;
	private Timer timer = new Timer();

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		class MyTask3 extends TimerTask {

			@Override
			public void run() {
				first = false;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (first) {
				// 关闭推送服务
				finish();
				System.exit(0);
			} else {
				first = true;
				Toast.makeText(this, "再按一次返回键退出应用！", 0).show();
				timer.schedule(new MyTask3(), 2000);
			}
		}
		//return super.onKeyDown(keyCode, event);
		return true;
	}

}
