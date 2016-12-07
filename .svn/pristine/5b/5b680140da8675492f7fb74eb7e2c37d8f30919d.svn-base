package com.nxt.ynt;

/**
 * 昵称修改
 * @author 赵佳丽
 *
 */
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.nxt.jnb.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.utils.PublicResult;
import com.nxt.ynt.utils.Util;

public class EditDetailActivity extends Activity {
	private static String TAG = "EditDetailActivity";
	private Util util;
	private EditText et_content;
	private String nickname, uid;
	private Context context;

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				PublicResult loginfo = (PublicResult) msg.obj;
				if (loginfo.getErrorcode().equals("0")) {
					// 保存用户信息
					util.saveToSp(Util.NICK, nickname);
					PersonalDetailsActivity.nickname.setText(nickname);
					SettingActivity.login_username.setText(nickname);
					Util.showMsg(context, loginfo.getMsg());
					finish();
				} else {
					Util.showMsg(context, loginfo.getMsg());
				}
			} else {
				Util.showMsg(context, "修改失败，请检查网路连接是否正常！");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		util = new Util(context);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.edit_details);
		uid = util.getFromSp(Util.UID, "");
		et_content = (EditText) findViewById(R.id.content);
	}

	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.cancle) {
			finish();
		} else if (id == R.id.post) {
			nickname = et_content.getText().toString().trim();
			if (nickname != null) {
				LogUtil.LogI(TAG, uid+ "的昵称：" + nickname);
				final Message msg = handler.obtainMessage();
				Map<String, String> params = new HashMap<String, String>();
				params.put("nick", nickname);
				NxtRestClientNew.post("useredit", params , new HttpCallBack(){
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						content = this.getContent(content);
						LogUtil.LogI(TAG, "成功:" + content);
						PublicResult rs = (PublicResult) JsonPaser
								.getObjectDatas(PublicResult.class,
										content);
						msg.obj = rs;
						handler.sendMessage(msg);
					}

					@Override
					public void onFailure(Throwable error,
							String content) {
						super.onFailure(error, content);
						content = this.getContent(content);
						LogUtil.LogI(TAG, "失败:" + content);
						msg.obj = null;
						handler.sendMessage(msg);
					}
				});
			} else {
				Util.showMsg(context, "保存内容不能为空");
			}
		}
	}

}
