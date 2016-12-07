package com.nxt.ynt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.nxt.jnb.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.ynt.imageutil.ImageLoader;
import com.nxt.ynt.utils.Constans;
//todo:查看一张图片详情      author:wpp
public class ShowImageActivity extends Activity{
	private ImageView mBtn;
	private Intent intent=null;
	private ImageLoader loader;
	private String img;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		SoftApplication appState = (SoftApplication)this.getApplication();
        appState.addActivity(this);
		setContentView(R.layout.show_img);
		initview();
		
	}
	private void initview() {
		// TODO Auto-generated method stub
		loader = ImageLoader.getInstance(getApplicationContext());
		mBtn = (ImageView) this.findViewById(R.id.imageBtn);
		intent = this.getIntent();
		img=intent.getStringExtra("img");
		LogUtil.syso("img::::"+img);
		if(img!=null){
			loader.displayImage(Constans.DOWNLOAD_IMAGE + img, mBtn);
		}
	}

}
