package com.nxt.ynt;

/**
 * 激活用户
 * @author 王盼盼
 *
 */

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.nxt.nxtapp.common.MD5;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.entity.JiHuo;
import com.nxt.ynt.entity.LoginInfo;
import com.nxt.ynt.utils.Util;

public class JiHuoActivity extends Activity implements OnClickListener {
	private EditText edittext_phone;
	private Button bt_jh;
	private Util util;
	private Button getcode;
	private EditText et_input_verify_code;
	private String inputcode;
	private TimeCount time;
	private String str_phone;
	private String pwdStr;
	private JiHuo data;
	private String num;
	private Intent intent = null;
	private String qf;
	private String areaStr;
	private EditText pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.yd_jihuo);
		time = new TimeCount(120000, 1000);// 构造CountDownTimer对象
		initview();
	}

	private void initview() {
		util = new Util(this);
		edittext_phone = (EditText) findViewById(R.id.edittext_phone);
		bt_jh = (Button) findViewById(R.id.bt_jh);
		getcode = (Button) findViewById(R.id.btn_acquire_verify_code);
		et_input_verify_code = (EditText) findViewById(R.id.et_input_verify_code);
		pwd = (EditText) findViewById(R.id.et_password);
		bt_jh.setOnClickListener(this);
		getcode.setOnClickListener(this);
		intent = this.getIntent();
		qf = intent.getStringExtra("jihuo");
		/*
		 * FRIENDID=intent.getStringExtra("FRIENDID");
		 * NICK=intent.getStringExtra("NICK");
		 * UHEAD=intent.getStringExtra("UHEAD");
		 * OPPOHEAD=intent.getStringExtra("OPPOHEAD");
		 * title=intent.getStringExtra("title"); fromwhere =
		 * intent.getIntExtra("fromwhere", 0);
		 * cateid=intent.getStringExtra("cateid");
		 * id=intent.getStringExtra("id");
		 */
		bt_jh.setClickable(false);
	}

	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			getcode.setClickable(false);
			getcode.setBackgroundColor(Color.parseColor("#959595"));
			getcode.setText(millisUntilFinished / 1000 + "秒");
		}

		@Override
		public void onFinish() {
			getcode.setText("重新验证");
			getcode.setBackgroundResource(R.drawable.register_denglu_selector);
			getcode.setClickable(true);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_acquire_verify_code) {
			str_phone = edittext_phone.getText().toString().trim();
			if (str_phone == null || str_phone.equals("")) {
				Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
			} else if (RegisterActivity.isHandset(util.getFromSp(Util.UNAME,
					" ")) && !str_phone.equals(util.getFromSp(Util.UNAME, " "))) {
				Toast.makeText(this, "请输入当前登陆的手机号！", Toast.LENGTH_SHORT).show();
			} else {
				if (RegisterActivity.isHandset(str_phone)) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("uid", util.getFromSp(Util.UID, ""));
					params.put("phone", str_phone);
					NxtRestClientNew.post("statususer", params,
							new HttpCallBack() {

								@Override
								public void onSuccess(String content) {
									super.onSuccess(content);
									content = this.getContent(content);
									data = (JiHuo) JsonPaser.getObjectDatas(
											JiHuo.class, content);
									if (data.getErrorcode().equals("0")) {
										time.start();
										num = data.getNum();
										System.out.println("@@@@@@@@@验证码"+num);
										if (num != null) {
											if (str_phone.equals(util
													.getFromSp(Util.UNAME, " "))) {
												pwd.setVisibility(View.GONE);
											} else {
												pwd.setVisibility(View.VISIBLE);
											}
										} else {
											pwd.setVisibility(View.GONE);
										}
										bt_jh.setBackgroundColor(R.drawable.submit);
										bt_jh.setClickable(true);
									} else if (data.getErrorcode().equals("2")) {
										util.showMsg(JiHuoActivity.this,
												"该手机号已经注册，请登录！");
										Intent intent = new Intent();
										Bundle bundle=new Bundle();
										bundle.putInt("flag", 2);
										bundle.putString("ifback", "true");
										intent.putExtras(bundle);
										intent.setClass(JiHuoActivity.this,
												DengLuActivity.class);
										startActivity(intent);
									} else {
										util.showMsg(JiHuoActivity.this,
												data.getMsg());
									}
								}

								@Override
								public void onFailure(Throwable error,
										String content) {
									super.onFailure(error, content);
								}
							});
				} else {
					util.showMsg(this, "请输入正确的手机号");
				}
			}
		} else if (id == R.id.bt_jh) {
			areaStr = SoftApplication.area;
			pwdStr = pwd.getText().toString();
			str_phone = edittext_phone.getText().toString().trim();
			inputcode = et_input_verify_code.getText().toString().trim();
			if (!inputcode.equals(num)) {
				util.showMsg(JiHuoActivity.this, "验证码不正确，请重新输入！");
			} else if (str_phone == null || str_phone.equals("")
					|| inputcode == null || inputcode.equals("")) {
				util.showMsg(JiHuoActivity.this, "请将信息填写完整");
			} else {
				if (RegisterActivity.isHandset(str_phone)) {
					if (!str_phone.equals(util.getFromSp(Util.UNAME, " "))) {
						if (pwdStr == null || pwdStr.equals("")) {
							util.showMsg(JiHuoActivity.this, "请将信息填写完整");
						} else {
							util.saveToSp(Util.UID, "");
							util.saveToSp(Util.UNAME, "");
							util.saveToSp(Util.NICK, "");
							util.saveToSp(Util.PWD, "");
							util.saveToSp(Util.area, "");
							util.saveToSp(Util.TOKEN, "");
							util.saveToSp(Util.UPIC, "");
							util.saveToSp(Util.status, "");
							Map<String, String> params = new HashMap<String, String>();
							params.put("siteid",
									getResources().getString(R.string.siteid));
							params.put("uname", str_phone);
							params.put("pwd", MD5.makeMD5(pwdStr));
							params.put("area", SoftApplication.area);
							params.put("status", "1");
							NxtRestClientNew.post("register", params,
									new HttpCallBack() {

										@Override
										public void onSuccess(String content) {
											content = this.getContent(content);
											com.nxt.nxtapp.common.LogUtil
													.syso(content);
											// Json数据解析实例
											LoginInfo rootdata = (LoginInfo) JsonPaser
													.getObjectDatas(
															LoginInfo.class,
															content);
											com.nxt.nxtapp.common.LogUtil
													.syso("rootdata:"
															+ rootdata);
											com.nxt.nxtapp.common.LogUtil
													.syso("======激活成功！返回的rootdata.toString = "
															+ rootdata
																	.toString());
											Message msg = handler
													.obtainMessage();
											msg.obj = rootdata;
											msg.what = 1;
											handler.sendMessage(msg);
										}

										@Override
										public void onFailure(Throwable error,
												String content) {
											super.onFailure(error, content);
											com.nxt.nxtapp.common.LogUtil
													.syso("onFailure："
															+ content);
											util.showMsg(JiHuoActivity.this,
													"网络不给力啊，检查下网络或者再试一次吧！");
										}
									});
						}
					} else {
						Map<String, String> params = new HashMap<String, String>();
						params.put("uid", util.getFromSp(Util.UID, ""));
						params.put("phone", str_phone);
						NxtRestClientNew.post("trueuser", params,
								new HttpCallBack() {

									@Override
									public void onSuccess(String content) {
										super.onSuccess(content);
										content = this.getContent(content);
										data = (JiHuo) JsonPaser
												.getObjectDatas(JiHuo.class,
														content);
										Message msg = handler.obtainMessage();
										msg.obj = data;
										msg.what = 1;
										handler1.sendMessage(msg);
									}

									@Override
									public void onFailure(Throwable error,
											String content) {
										super.onFailure(error, content);
									}
								});
					}
				} else {
					Util.showMsg(JiHuoActivity.this, "请输入正确的手机号!");
				}
			}
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			LoginInfo jh = (LoginInfo) msg.obj;
			util.saveToSp(Util.UID, jh.getUid());
			util.saveToSp(Util.UNAME, str_phone);
			util.saveToSp(Util.NICK, str_phone);
			util.saveToSp(Util.PWD, MD5.makeMD5(pwdStr));// 密码
			util.saveToSp(Util.area, areaStr);
			util.saveToSp(Util.TOKEN, jh.getToken());
			util.saveToSp(Util.status, jh.getStatus()); // 激活状态
			util.saveToSp(Util.UPIC, "");
			if (msg.what == 1) {
				if (jh.getErrorcode() != null && jh.getErrorcode().equals("0")) {
					Util.showMsg(getApplicationContext(), "激活成功");
					if (qf.equals("webview")) {
						Intent intent = new Intent(JiHuoActivity.this,
								DengLuActivity.class);
						util.saveToSp(Util.status, "1");
						intent.putExtra("sort", "13");
						startActivity(intent);
						finish();
					} /*else if (qf.equals("zhanjiawenda")) {
						Intent intent = new Intent(JiHuoActivity.this,
								FaBiaoActivity.class);
						util.saveToSp(Util.status, "1");
						intent.putExtra("sort", "2");
						startActivity(intent);
						finish();
					} else if (qf.equals("nq")) {
						Intent intent = new Intent(JiHuoActivity.this,
								FaBiaoActivity.class);
						util.saveToSp(Util.status, "1");
						intent.putExtra("sort", "4");
						startActivity(intent);
						finish();
					} else if (qf.equals("dongtai")) {
						Intent intent = new Intent(JiHuoActivity.this,
								FaBiaoActivity.class);
						util.saveToSp(Util.status, "1");
						intent.putExtra("sort", "1");
						startActivity(intent);
						finish();
					}else if (qf.equals("xinwen")) {
						util.saveToSp(Util.status, "1");
						finish();
					}*/
				} else {
					Util.showMsg(getApplicationContext(), jh.getMsg());
				}
			}
		};
	};

	public Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			JiHuo jh = (JiHuo) msg.obj;
			if (msg.what == 1) {
				if (jh.getErrorcode() != null && jh.getErrorcode().equals("0")) {
					Util.showMsg(getApplicationContext(), "激活成功");
					/*if (qf.equals("gongqiu")) {
						Intent intent = new Intent(JiHuoActivity.this,
								FaBiaoActivity.class);
						util.saveToSp(Util.status, "1");
						intent.putExtra("sort", "3");
						startActivity(intent);
						finish();
					} else if (qf.equals("zhanjiawenda")) {
						Intent intent = new Intent(JiHuoActivity.this,
								FaBiaoActivity.class);
						util.saveToSp(Util.status, "1");
						intent.putExtra("sort", "2");
						startActivity(intent);
						finish();
					} else if (qf.equals("nq")) {
						Intent intent = new Intent(JiHuoActivity.this,
								FaBiaoActivity.class);
						util.saveToSp(Util.status, "1");
						intent.putExtra("sort", "4");
						startActivity(intent);
						finish();
					} else if (qf.equals("dongtai")) {
						Intent intent = new Intent(JiHuoActivity.this,
								FaBiaoActivity.class);
						util.saveToSp(Util.status, "1");
						intent.putExtra("sort", "1");
						startActivity(intent);
						finish();
					}*/
				} else {
					Util.showMsg(getApplicationContext(), "激活失败");
				}
			}
		};
	};

	public void onBack(View v) {
		finish();
	}

}
