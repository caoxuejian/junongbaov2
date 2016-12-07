package com.nxt.ynt.utils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.nxt.nxtapp.common.HtmlUtil;
import com.nxt.nxtapp.common.Util;
import com.nxt.ynt.X5WebviewActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;


/**
 * @author �첽����HTMLҳ�� 2016-11-30
 */
public class AnalyticalHTMLTask extends AsyncTask<String, Void, String> {

	private Context context;
	private Util util;
	private String titleImg;
	private String atitle;
	private String description;
	private String shareUrl;
	private String shareid;

	public AnalyticalHTMLTask(Context context, BackUI backUI) {
		super();
		this.context = context;
		util=new Util(context);

	}


	@Override
	protected String doInBackground(String... params) {
		String url = params[0];
		AnalyticalHTML(url);
		return null;
	}
	//ҳ�����
	private void AnalyticalHTML(String main_url2) {
		// TODO Auto-generated method stub
		//����HTML
		String htmlStr = HtmlUtil.getHtmlString(main_url2);
		Document document = Jsoup.parse(htmlStr);
		//head��meta��ǩ���ŵ�ͼƬ����
		titleImg = document.getElementsByAttributeValueStarting("name", "shareimg").attr("content");
		//���±���
		atitle = document.getElementsByAttributeValueStarting("name", "sharetitle").attr("content");
		//��������
		description = document.getElementsByAttributeValueStarting("name", "sharedesc").attr("content");
		//��������
		shareUrl = document.getElementsByAttributeValueStarting("name", "shareurl").attr("content");
		//����id
		shareid=document.getElementsByAttributeValueStarting("name", "shareid").attr("content");
		//	tv_title.setText(((atitle != null && !atitle.equals("")) ? atitle : "��ϸ��Ϣ"));
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(shareid==null||"".equals(shareid)){
			X5WebviewActivity.iv_add.setVisibility(View.GONE);
		}else{
			X5WebviewActivity.titleImg=titleImg;
			X5WebviewActivity.atitle=atitle;
			X5WebviewActivity.description=description;
			X5WebviewActivity.shareUrl=shareUrl;
			X5WebviewActivity.shareid=shareid;
			X5WebviewActivity.iv_add.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	public interface BackUI {
		public void back(String result);
	}
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		
	}
}
