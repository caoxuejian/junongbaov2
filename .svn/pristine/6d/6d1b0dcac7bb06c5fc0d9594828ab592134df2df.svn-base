package com.nxt.ynt.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;

import com.nxt.jnb.R;

public class UpdateUserAvatar extends PopupWindow {
	private LayoutInflater inflater;
	private LinearLayout layout;
	private Context mContext;
	private LinearLayout btnCancel;
	private LinearLayout btnCapture;
	private LinearLayout btnSelect;
	private OnClickListener mClickListener;

	public UpdateUserAvatar(Context context, OnClickListener clickListener) {
		this.mContext = context;
		mClickListener = clickListener;
		inflater = (LayoutInflater) mContext
				.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.profile_change_photo,
				null);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		setContentView(layout);
		initViews();
	}

	private void initViews() {
		btnCancel = (LinearLayout) layout
				.findViewById(R.id.close_update_avatar);
		btnCancel.setOnClickListener(mClickListener);
		btnCapture = (LinearLayout) layout.findViewById(R.id.capture_pic_bt);
		btnCapture.setOnClickListener(mClickListener);
		btnSelect = (LinearLayout) layout.findViewById(R.id.select_pic_bt);
		btnSelect.setOnClickListener(mClickListener);
	}
	
}
