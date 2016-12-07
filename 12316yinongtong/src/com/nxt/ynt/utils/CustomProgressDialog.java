package com.nxt.ynt.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.nxt.jnb.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @Description:自定义对话框
 * @author
 */
public class CustomProgressDialog extends ProgressDialog {

	private AnimationDrawable mAnimation;
	private Context mContext;
	private ImageView mImageView;
	private CharSequence mLoadingTip;
	private TextView mLoadingTv;
	private int count = 0;
	private String oldLoadingTip;
	private int mResid;

	public CustomProgressDialog(Context context, int id) {
		super(context);
		this.mContext = context;

		this.mResid = id;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	@Override
	public void setMessage(CharSequence content) {
		// TODO Auto-generated method stub
		super.setMessage(content);
		this.mLoadingTip = content;
	}
	private void initData() {

		mImageView.setBackgroundResource(mResid);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		// 为了防止在onCreate方法中只显示第一帧的解决方案之一
		mImageView.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();

			}
		});


	}

	public void setContent(String str) {
		mLoadingTv.setText(str);
	}

	private void initView() {
		setContentView(R.layout.progress_dialog);
		mLoadingTv = (TextView) findViewById(R.id.loadingTv);
		mImageView = (ImageView) findViewById(R.id.loadingIv);
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		mLoadingTv.setText(mLoadingTip);
	}
	/*@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		mAnimation.start(); 
		super.onWindowFocusChanged(hasFocus);
	}*/
}
