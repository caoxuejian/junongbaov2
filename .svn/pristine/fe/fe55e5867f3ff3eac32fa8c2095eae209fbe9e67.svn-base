package com.nxt.ynt.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nxt.jnb.R;

/**
 * 自定义dialog
 * 
 * @author wangchao
 */
public class ChangeGenderDialog extends Dialog {
	private Context context;
	private android.view.View.OnClickListener mClickListener;
	private RelativeLayout nan;
	private RelativeLayout nv;
	private ImageView iv_nan,iv_nv;
	private String str_gender;

	public ChangeGenderDialog(Context context, String str_gender, android.view.View.OnClickListener clickListener) {
		super(context);
		this.context = context;
		this.str_gender = str_gender;
		mClickListener = clickListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.profile_change_sex);
		setCanceledOnTouchOutside(true);
		nan = (RelativeLayout) findViewById(R.id.nan);
		nan.setOnClickListener((android.view.View.OnClickListener) mClickListener);
		nv = (RelativeLayout) findViewById(R.id.nv);
		nv.setOnClickListener((android.view.View.OnClickListener) mClickListener);
		iv_nan = (ImageView)findViewById(R.id.iv_nan);
		iv_nv = (ImageView)findViewById(R.id.iv_nv);
		if (str_gender == null || "".equals(str_gender)) {
			iv_nan.setVisibility(View.INVISIBLE);
			iv_nv.setVisibility(View.INVISIBLE);
		}else if(str_gender.equals("男")){
			iv_nan.setVisibility(View.VISIBLE);
			iv_nv.setVisibility(View.INVISIBLE);
		}else if(str_gender.equals("女")){
			iv_nan.setVisibility(View.INVISIBLE);
			iv_nv.setVisibility(View.VISIBLE);
		}
	}

}
