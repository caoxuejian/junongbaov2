package com.nxt.ynt;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.nxt.jnb.R;

public class ShowImagePageActivity extends Activity
{
	private ImageView	mBtn;
	private Bitmap		bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		SoftApplication appState = (SoftApplication)this.getApplication();
        appState.addActivity(this);
		setContentView(R.layout.show_img);
		mBtn = (ImageView) this.findViewById(R.id.imageBtn);
		Intent intent = getIntent();
		if (intent != null)
		{
			byte[] bis = intent.getByteArrayExtra("bitmap");
			bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
		}
		mBtn.setImageBitmap(bitmap);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		finish();
		return true;
	}
}
