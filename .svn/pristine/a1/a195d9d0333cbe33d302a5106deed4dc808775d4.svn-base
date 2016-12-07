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
 * @author 异步解析HTML页面 2016-11-30
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
	//页面解析
	private void AnalyticalHTML(String main_url2) {
		// TODO Auto-generated method stub
		//解析HTML
		String htmlStr = HtmlUtil.getHtmlString(main_url2);
		Document document = Jsoup.parse(htmlStr);
		//head中meta标签新闻的图片链接
		titleImg = document.getElementsByAttributeValueStarting("name", "shareimg").attr("content");
		//文章标题
		atitle = document.getElementsByAttributeValueStarting("name", "sharetitle").attr("content");
		//文章描述
		description = document.getElementsByAttributeValueStarting("name", "sharedesc").attr("content");
		//分享链接
		shareUrl = document.getElementsByAttributeValueStarting("name", "shareurl").attr("content");
		//文章id
		shareid=document.getElementsByAttributeValueStarting("name", "shareid").attr("content");
		//	tv_title.setText(((atitle != null && !atitle.equals("")) ? atitle : "详细信息"));
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
