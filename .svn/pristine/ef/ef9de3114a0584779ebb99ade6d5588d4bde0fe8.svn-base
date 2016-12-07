package com.nxt.nxtapp.ui.test;
/*
 * 发布信息
 */
import android.content.Context;
import android.content.Intent;

import com.nxt.nxtapp.setting.Constant;
import com.nxt.nxtapp.ui.PublicActivity;

public class PublicMsgUsage {
    
   /*
    * 传递发布类型，来发布信息
    */
	public static void publicMsgbyId(Context context,int mode){
		Intent intent;
		//weibo微博
		if(mode==Constant.PUBLIC_WEIBO){
			intent = new Intent(context, PublicActivity.class);
			intent.putExtra("fromwhere", Constant.PUBLIC_WEIBO);
			intent.putExtra("cateid", Constant.PUBLIC_WEIBO+"");
			intent.putExtra("title", "微博发布");
			context.startActivity(intent);
		} else if(mode==Constant.PUBLIC_GQ){//gongqiu供求
			intent = new Intent(context,
					PublicActivity.class);
			intent.putExtra("title", "供求发布");
			intent.putExtra("fromwhere", Constant.PUBLIC_GQ);
			context.startActivity(intent);
		}else if(mode==Constant.PUBLIC_JG){//价格
			intent = new Intent(context,
					PublicActivity.class);
			intent.putExtra("title", "价格发布");
			intent.putExtra("fromwhere", Constant.PUBLIC_JG);
			intent.putExtra("cateid", Constant.PUBLIC_JG+"");
			context.startActivity(intent);
		}else if(mode==Constant.PUBLIC_NQ){//nongqing/农情
			intent = new Intent(context,
					PublicActivity.class);
			intent.putExtra("title", "农情发布");
			intent.putExtra("cateid", Constant.PUBLIC_NQ+"");
			intent.putExtra("fromwhere", Constant.PUBLIC_NQ);
			context.startActivity(intent);
		}else if(mode==Constant.PUBLIC_ZJWD){//zhuanjiawenda专家问答
			intent = new Intent(context,
					PublicActivity.class);
			intent.putExtra("fromwhere", Constant.PUBLIC_ZJWD);
			intent.putExtra("cateid", Constant.PUBLIC_ZJWD+"");
			intent.putExtra("title", "专家问答发布");
			intent.putExtra("id", 1);
			context.startActivity(intent);
		}
	}
}
