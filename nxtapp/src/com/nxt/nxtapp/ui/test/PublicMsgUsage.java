package com.nxt.nxtapp.ui.test;
/*
 * ������Ϣ
 */
import android.content.Context;
import android.content.Intent;

import com.nxt.nxtapp.setting.Constant;
import com.nxt.nxtapp.ui.PublicActivity;

public class PublicMsgUsage {
    
   /*
    * ���ݷ������ͣ���������Ϣ
    */
	public static void publicMsgbyId(Context context,int mode){
		Intent intent;
		//weibo΢��
		if(mode==Constant.PUBLIC_WEIBO){
			intent = new Intent(context, PublicActivity.class);
			intent.putExtra("fromwhere", Constant.PUBLIC_WEIBO);
			intent.putExtra("cateid", Constant.PUBLIC_WEIBO+"");
			intent.putExtra("title", "΢������");
			context.startActivity(intent);
		} else if(mode==Constant.PUBLIC_GQ){//gongqiu����
			intent = new Intent(context,
					PublicActivity.class);
			intent.putExtra("title", "���󷢲�");
			intent.putExtra("fromwhere", Constant.PUBLIC_GQ);
			context.startActivity(intent);
		}else if(mode==Constant.PUBLIC_JG){//�۸�
			intent = new Intent(context,
					PublicActivity.class);
			intent.putExtra("title", "�۸񷢲�");
			intent.putExtra("fromwhere", Constant.PUBLIC_JG);
			intent.putExtra("cateid", Constant.PUBLIC_JG+"");
			context.startActivity(intent);
		}else if(mode==Constant.PUBLIC_NQ){//nongqing/ũ��
			intent = new Intent(context,
					PublicActivity.class);
			intent.putExtra("title", "ũ�鷢��");
			intent.putExtra("cateid", Constant.PUBLIC_NQ+"");
			intent.putExtra("fromwhere", Constant.PUBLIC_NQ);
			context.startActivity(intent);
		}else if(mode==Constant.PUBLIC_ZJWD){//zhuanjiawendaר���ʴ�
			intent = new Intent(context,
					PublicActivity.class);
			intent.putExtra("fromwhere", Constant.PUBLIC_ZJWD);
			intent.putExtra("cateid", Constant.PUBLIC_ZJWD+"");
			intent.putExtra("title", "ר���ʴ𷢲�");
			intent.putExtra("id", 1);
			context.startActivity(intent);
		}
	}
}
