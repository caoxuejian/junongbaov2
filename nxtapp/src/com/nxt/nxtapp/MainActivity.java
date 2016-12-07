package com.nxt.nxtapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.nxt.nxtapp.setting.Constant;
import com.nxt.nxtapp.setting.GetHost;
import com.nxt.nxtapp.version.util.VersionUtil;

public class MainActivity extends Activity implements OnClickListener{
	TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        com.nxt.nxtapp.common.LogUtil.syso("--->"+this.getString(R.string.host));
        com.nxt.nxtapp.common.LogUtil.syso("===>"+GetHost.getHost());
       // new VersionUtil(this,Constant.UPDATAPK_URL,Constant.VERSION_URL);
        VersionUtil vutil = new VersionUtil(this,Constant.UPDATAPK_URL,Constant.VERSION_URL);
        vutil.doVersion();
    }
    

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.download://下载
//			
//			NxtRestClientTest.getPublicJson();
//			break;
//		case R.id.publicmsg://发布信息
//			PublicMsgUsage.publicMsgbyId(MainActivity.this, Constant.PUBLIC_JG);
//			break;
//		case R.id.updatamsg://下拉刷新，更多加载
//			Intent intent = new Intent(MainActivity.this,AlertMsgs.class);
//			startActivity(intent);
//			break;
		default:
			break;
		}
		
	}
    
    
    
}
