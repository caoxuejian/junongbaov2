package com.nxt.jnb.wxapi;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//        setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, getResources().getString(R.string.wx_key));

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		//-2@5
		switch(resp.errCode){
		case 0://�ɹ�
			if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
				Toast.makeText(this, "֧���ɹ���", Toast.LENGTH_SHORT).show();
				Intent wxpay = new Intent();
				wxpay.setAction("WXPAYCallback");
				sendBroadcast(wxpay);
				finish();
			}
			break;
		case -1://����
			finish();
			Toast.makeText(this, "����֧��ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
			break;
		case -2://�û�ȡ��
			finish();
			Toast.makeText(this,"����ȡ��֧����", Toast.LENGTH_LONG).show();
			break;
		}
	}
}