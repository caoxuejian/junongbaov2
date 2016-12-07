package com.nxt.nxtapp;



import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.http.NxtRestClient;

public class SplashBaseActivity extends Activity
{
	public static final int HAVENONETWORK = 10;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 去头条目 全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);	
		
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo isNetWork = cwjManager.getActiveNetworkInfo();
//		if(isNetWork!=null){
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
					//获取手机imei
//					String imei = ((TelephonyManager)getSystemService("phone"))
//							.getDeviceId();
//					Util util = new Util(SplashBaseActivity.this);
//					util.saveToSp(Util.IMEI, imei);
//					tongji_url.append(imei+"||");
//					tongji_url.append(Util.getPackageVersion(SplashBaseActivity.this));
//					NxtRestClient.post(tongji_url.toString(), null, new AsyncHttpResponseHandler());
//					Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				
//				}
//			}).start();
//		}else{
//			Toast.makeText(this, "网络无连接,请检查网络设置", 1).show();
//		}
		
		
		ImageView img=(ImageView) findViewById(R.id.img_welcome);
		AlphaAnimation alpAnimation=new AlphaAnimation(0.0f, 1.0f);
		alpAnimation.setDuration(3000);
		alpAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				startWhichActivity();
			}
		});
		img.setAnimation(alpAnimation);
		alpAnimation.start();
		
		
		
		
		
		
	}
	
	public void startWhichActivity() {
	}
}
