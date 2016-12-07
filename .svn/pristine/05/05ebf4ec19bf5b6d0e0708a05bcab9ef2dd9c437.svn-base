package com.nxt.ynt;

/**
 * 注册
 * @author 赵佳丽
 *
 */
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.MD5;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.entity.LoginInfo;
import com.nxt.ynt.utils.CustomProgressDialog;
import com.nxt.ynt.utils.Util;

public class RegisterActivity extends Activity implements OnClickListener {
	private static String TAG = "RegisterActivity";
	private EditText phone, pwd;
	private String usernameStr, pwdStr, areaStr;
	private Util util;
	private Context context;
	private CustomProgressDialog pdlogin;
	private String num;
	private Button btn_yzm;
	private TimeCount time;
	private Button btn_reg;
	private EditText et_yzm;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			LoginInfo login = (LoginInfo) msg.obj;
			if (login != null) {
				if (login.getErrorcode() != null
						&& login.getErrorcode().equals("0")) {
					// 注册成功记录用户信息
					util.saveToSp(Util.UID, login.getUid());
					util.saveToSp(Util.UNAME, usernameStr);
					util.saveToSp(Util.NICK, usernameStr);
					util.saveToSp(Util.PWD, MD5.makeMD5(pwdStr));// 密码
					util.saveToSp(Util.area, areaStr);
					util.saveToSp(Util.TOKEN, login.getToken());
					util.saveToSp(Util.status, login.getStatus()); // 激活状态
					util.saveToSp(Util.UPIC, "");
					util.saveToSp(Util.YN_PAY, "0");//是否订阅
					Util.showMsg(context, "注册成功");
					Util.toMainActivity(context);
					finish();
				} else {
					Util.showMsg(context, login.getMsg());
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.register);
		util = new Util(context);
		time = new TimeCount(120000, 1000);// 构造CountDownTimer对象
		phone = (EditText) findViewById(R.id.et_phone);
		pwd = (EditText) findViewById(R.id.et_password);
		et_yzm = (EditText) findViewById(R.id.et_yzm);
		btn_yzm = (Button) findViewById(R.id.bt_yzm);
		btn_reg = (Button) findViewById(R.id.reg);
		btn_reg.setClickable(false);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.register_back) {
			finish();
		} else if (id == R.id.reg) {
			if (!num.equals(et_yzm.getText().toString())) {
				Util.showMsg(context, "验证码错误，请重新输入!");
			} else {
				register();
			}
		} else if (id == R.id.bt_yzm) {
			usernameStr = phone.getText().toString();
			if (usernameStr.equals("")) {
				Toast.makeText(RegisterActivity.this, "手机号不能为空！",
						Toast.LENGTH_SHORT).show();
			} else {
				if (isHandset(usernameStr)) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("phone", usernameStr);
					NxtRestClientNew.post("registermobile", params,
							new HttpCallBack() {
								@Override
								public void onSuccess(String content) {
									content = this.getContent(content);
									try {
										// 提取验证码
										JSONObject jsonObject = new JSONObject(
												content);
										LogUtil.LogD("短信验证码@@@@@@@@@@@@@", jsonObject.toString());
										if (!jsonObject.optString("errorcode",
												"").equals("0")) {
											util.showMsg(RegisterActivity.this,
													jsonObject.optString("msg",
															""));
										} else {
											btn_reg.setClickable(true);
											btn_reg.setBackgroundColor(R.drawable.submit);
											time.start();
											num = jsonObject.optString("num",
													"");
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

								@Override
								public void onFailure(Throwable error,
										String content) {
									super.onFailure(error, content);
									com.nxt.nxtapp.common.LogUtil
											.syso("onFailure：" + content);
									util.showMsg(RegisterActivity.this,
											"网络不给力啊，检查下网络或者再试一次吧！");
								}
							});
				} else {
					util.showMsg(RegisterActivity.this, "手机号格式不正确！");
				}
			}
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btn_yzm.setText("重新验证");
			btn_yzm.setBackgroundResource(R.drawable.register_denglu_selector);
			btn_yzm.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btn_yzm.setClickable(false);
			btn_yzm.setText("剩余" + millisUntilFinished / 1000 + "秒");
		}
	}

	/**
	 * 注册的线程
	 */
	private void register() {
		usernameStr = phone.getText().toString();
		pwdStr = pwd.getText().toString();
		areaStr = SoftApplication.area;
		if (usernameStr == null || "".equals(usernameStr) || pwdStr == null
				|| "".equals(pwdStr)) {
			Util.showMsg(context, "有用户信息未填写!");
		} else if (isHandset(usernameStr)) {
			// 显示进度条
			pdlogin =new CustomProgressDialog(this,R.anim.loadingframe);
			pdlogin.setCancelable(false);
			pdlogin.setMessage("请稍后...");
			pdlogin.show();
			Map<String, String> params = new HashMap<String, String>();
			params.put("siteid", getResources().getString(R.string.siteid));
			params.put("uname", usernameStr);
			params.put("pwd", MD5.makeMD5(pwdStr));
			params.put("area", SoftApplication.area);
			params.put("status", "1");
			NxtRestClientNew.post("register", params, new HttpCallBack() {

				@Override
				public void onSuccess(String content) {
					content = this.getContent(content);
					com.nxt.nxtapp.common.LogUtil.syso(content);
					// Json数据解析实例
					LoginInfo rootdata = (LoginInfo) JsonPaser.getObjectDatas(
							LoginInfo.class, content);
					com.nxt.nxtapp.common.LogUtil.syso("rootdata:" + rootdata);
					com.nxt.nxtapp.common.LogUtil
							.syso("======注册成功！返回的rootdata.toString = "
									+ rootdata.toString());
					Message msg = handler.obtainMessage();
					msg.obj = rootdata;
					handler.sendMessage(msg);
					pdlogin.cancel();
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					com.nxt.nxtapp.common.LogUtil.syso("onFailure：" + content);
					pdlogin.cancel();
					util.showMsg(RegisterActivity.this, "网络不给力啊，检查下网络或者再试一次吧！");
				}
			});
		} else {
			Util.showMsg(context, "请输入正确的手机号!");
		}
	}

	/**
	 * 判断是不是合法手机 handset 手机号码
	 */
	public static boolean isHandset(String handset) {
		if (handset == null || handset.length() != 11) {
			return false;
		}
		if (!handset.substring(0, 1).equals("1")) {
			return false;
		}
		return true;
	}
	
}
