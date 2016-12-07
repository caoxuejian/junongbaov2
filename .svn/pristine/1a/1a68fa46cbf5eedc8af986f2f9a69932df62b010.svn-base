package com.nxt.ynt;

/**
 * 二维码名片
 * @author 赵佳丽
 *
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.nxt.jnb.R;
import com.nxt.ynt.capture.EncodingHandler;
import com.nxt.ynt.imageutil.ImageLoader;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.Util;

public class NamecardActivity extends Activity {
	private Util util;
	private ImageView qrImgImageView,iv, ic_sex;
	private String pic_url, nickname, username, sex, area, hy, uid;
	private TextView tv_nick;
	private ImageLoader loader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.namecard);
		loader = ImageLoader.getInstance(this);
		qrImgImageView = (ImageView) findViewById(R.id.iv_qr_image);
		iv = (ImageView) findViewById(R.id.iv_head);
		ic_sex = (ImageView) findViewById(R.id.ic_sex);
		tv_nick = (TextView) findViewById(R.id.nickname);
		util = new Util(this);
		uid = util.getFromSp(Util.UID, "");
		username = util.getFromSp(Util.UNAME, "");
		nickname = util.getFromSp(Util.NICK, "");
		sex = util.getFromSp(Util.SEX, "");
		pic_url =util.getFromSp(Util.UPIC, "");
		area = util.getFromSp(Util.area, "");
		hy = util.getFromSp(Util.work, "");
		if (nickname != null && !nickname.equals("")) {
			tv_nick.setText(nickname);
		}
		if (sex != null && !sex.equals("")) {
			if (sex.equals("女"))
				ic_sex.setBackgroundResource(R.drawable.ic_sex_female);
			else
				ic_sex.setBackgroundResource(R.drawable.ic_sex_male);
		}
		if (pic_url != null && !"".equals(pic_url)) {
			loader.displayImage(Constans.getHeadUri(pic_url), iv);
		}else{
			iv.setImageResource(R.drawable.contractdefalut);
		}
		String contentString = "NXnamecard"+Constans.find_url + "?number=" + username;
//		String contentString = "nxgrzl{\"uid\":\"" + uid + "\",\"nick\":\""
//				+ nickname + "\",\"user_name\":\"" + username + "\",\"sex\":\""
//				+ sex + "\",\"area\":\"" + area + "\",\"hy\":\"" + hy
//				+ "\",\"user_pic\":\"" + pic_url + "\"}";
		if (contentString != null && contentString.trim().length() > 0) {
			// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
			Bitmap qrCodeBitmap;
			try {
				qrCodeBitmap = EncodingHandler.createQRCode(contentString, 500);
				qrImgImageView.setImageBitmap(qrCodeBitmap);
			} catch (WriterException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "Text can not be empty", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.news_view_back) {
			finish();
		}
	}
}
