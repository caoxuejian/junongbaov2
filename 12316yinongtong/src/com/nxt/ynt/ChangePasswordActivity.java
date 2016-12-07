package com.nxt.ynt;

/**
 * ��¼
 * @author �Լ���
 *
 */
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.MD5;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.ynt.utils.Util;

public class ChangePasswordActivity extends Activity implements OnClickListener {
	private static String TAG = "ChangePasswordActivity";
	private EditText paw1, paw2, phone, code;
	private Button getcode, post;
	private String str_phone, str_code, str_paw1, str_paw2, imei;
	private Util util;
	private SoftApplication appState;
	private Context context;
	private TimeCount time;
	private String num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		util = new Util(context);
		imei = util.getFromSp(Util.IMEI, "");
		appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.change_password);
		time = new TimeCount(120000, 1000);//����CountDownTimer����
		findViewId();
	}
	class TimeCount extends CountDownTimer{

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			getcode.setClickable(false);
			getcode.setBackgroundColor(Color.parseColor("#959595"));
			getcode.setText(millisUntilFinished/1000+"��");
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			getcode.setText("������֤");
			getcode.setBackgroundResource(R.drawable.register_denglu_selector);
			getcode.setClickable(true);
		}

	}
	private void findViewId() {
		getcode = (Button) findViewById(R.id.btn_acquire_verify_code);
		post = (Button) findViewById(R.id.post);
		phone = (EditText) findViewById(R.id.et_input_phone);
		code = (EditText) findViewById(R.id.et_input_verify_code);
		paw1 = (EditText) findViewById(R.id.et_input_paw1);
		paw2 = (EditText) findViewById(R.id.et_input_pwd2);
		getcode.setOnClickListener(this);
		post.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.btn_acquire_verify_code) {
			str_phone = phone.getText().toString().trim();
			if(str_phone.equals("")){
				Toast.makeText(this, "�ֻ��Ų���Ϊ��", Toast.LENGTH_SHORT).show();
			}else{
				if (RegisterActivity.isHandset(str_phone)) {
					time.start();
					//RequestParams params = new RequestParams();
					final Map<String, String> params = new HashMap<String, String>();
					params.put("phone", str_phone);
					params.put("deviceid", imei);
					NxtRestClientNew.post("forgetpass", params,
							new HttpCallBack() {
						@Override
						public void onSuccess(String content) {
							content= this.getContent(content);
							LogUtil.LogI(TAG, "�������� ��ߵ�contentֵ = " + content);
							try {
								//��ȡ��֤��
								JSONObject jsonObject = new JSONObject(content);
								LogUtil.LogI(TAG, "jsonObject:" + jsonObject);
								num=jsonObject.optString("num", "");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							com.nxt.nxtapp.common.LogUtil.syso("onFailure��"
									+ content);
							util.showMsg(ChangePasswordActivity.this,
									"���粻������������������������һ�ΰɣ�");
						}
					});
				} else {
					util.showMsg(context, "��������ȷ���ֻ���");
				}}
		} else if (id == R.id.post) {
			str_phone = phone.getText().toString().trim();
			str_code = code.getText().toString().trim();
			str_paw1 = paw1.getText().toString().trim();
			str_paw2 = paw2.getText().toString().trim();
			if(!str_code.equals(num)){
				util.showMsg(context, "��֤�벻��ȷ�����������룡");
			}else if (str_phone == null || str_phone.equals("") || str_code == null
					|| str_code.equals("") || str_paw1 == null
					|| str_paw1.equals("") || str_paw2 == null
					|| str_paw2.equals("")) {
				util.showMsg(context, "�뽫��Ϣ��д����");
			} else if (!str_paw1.equals(str_paw2)) {
				util.showMsg(context, "������д�����벻һ��");
			} else {
				Map<String,String> params = new HashMap<String, String>();
				params.put("phone", str_phone);
				params.put("num", str_code);
				params.put("passwd", MD5.makeMD5(str_paw1));
				params.put("npasswd", MD5.makeMD5(str_paw2));
				params.put("deviceid", imei);
				NxtRestClientNew.post("changepass", params,
						new HttpCallBack() {
					@Override
					public void onSuccess(String content) {
						content= this.getContent(content);
						LogUtil.LogI(TAG, "���������ύ��" + content);
						try {
							//��ȡ��֤��
							JSONObject jsonObject = new JSONObject(content);
							LogUtil.LogI(TAG, "jsonObject:" + jsonObject);
							Message msg = handler.obtainMessage();
							msg.obj = jsonObject;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						com.nxt.nxtapp.common.LogUtil.syso("onFailure��"
								+ content);
						//						pdlogin.cancel();
						util.showMsg(ChangePasswordActivity.this,
								"���粻������������������������һ�ΰɣ�");
					}
				});
			}
		} else if (id == R.id.news_view_back) {
			finish();
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			JSONObject jsonObject = (JSONObject)msg.obj;
			if("0".equals(jsonObject.optString("errorcode", ""))){
				//{"msg":"�޸�����ɹ�","errorcode":0}
				finish();
				Util.showMsg(context, jsonObject.optString("msg", ""));
			}else{
				Util.showMsg(context, jsonObject.optString("msg", ""));
			}
		};
	};

}
