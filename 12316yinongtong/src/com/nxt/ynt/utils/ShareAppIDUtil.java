package com.nxt.ynt.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nxt.jnb.R;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.ynt.X5WebviewActivity;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 *
 * @author caoxuejian
 *
 */
public class ShareAppIDUtil {
	public static String WBSCOPE="email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
	public static String WBURL="http://www.sina.com";
	private static String WXKEY,WBKEY;
	private static IWXAPI api;
	/*private static String description;
	private static String titleImg;
	private static String shareUrl;*/
	private static WeiboAuth mWeiboAuth;
	private static SsoHandler mSsoHandler;
	private static IWeiboShareAPI mWeiboShareAPI;
	private static Activity context;
	static Bitmap bmp;
	static Bitmap thumbBmp = null;
	//	private static String atitle;
	private static Util util;
	/**
	 * 
	 * @param activity
	 * @param myurl
	 * @param description
	 * @param state
	 */
	//	private static String shareid;
	public static void shareWX1(Activity activity,String myurl,int state) {
		// TODO Auto-generated method stub
		context=activity;
		// 微信注册
		// WXKEY=ShareAppIDUtil.getWXAppKEY(appname);
		WXKEY = context.getResources().getString(R.string.wx_key);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(context, WXKEY, false);
		api.registerApp(WXKEY);
		//解析html
		if ("".equals(X5WebviewActivity.titleImg) || X5WebviewActivity.titleImg == null) {
			thumbBmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.loading_img);
		} else {
			try {
				bmp = BitmapFactory.decodeStream(new URL(X5WebviewActivity.titleImg)
				.openStream());
				thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
				bmp.recycle();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//		AnalyticalHtml(myurl);
		//state:0表示分享到好友
		//state:1表示朋友圈
		// TODO Auto-generated method stub
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = X5WebviewActivity.shareUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = X5WebviewActivity.atitle;
		msg.description = X5WebviewActivity.description;

		//分享统计量
		putShareCount(context,X5WebviewActivity.atitle,X5WebviewActivity.shareid);
		msg.thumbData = Util.bmpToByteArray1(thumbBmp, true);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = state==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
		//		req.scene = SendMessageToWX.Req.WXSceneSession;
		// req.scene =SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
	}
	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
	//统计分享量
	public static void putShareCount(Context context,String title,String shareid){
		util = new Util(context);
		RequestParams params = new RequestParams();
		params.put("siteid", context.getResources().getString(R.string.siteid));
		params.put("sharetitle",title);
		params.put("shareid", shareid);
		params.put("uid", util.getFromSp(Util.UID, "0"));
		NxtRestClient.post(Constans.sharecount, params,
				new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		});
	}
	/*public static void AnalyticalHtml(String myurl) {
		// TODO Auto-generated method stub
		// 获取标题
		String htmlStr = HtmlUtil.getHtmlString(myurl);
		Document document = Jsoup.parse(htmlStr);
		//<meta name="shareimg" content="http://cmsfile10.chinaso.com/group1/M00/22/BF/Cgqg11VMX6GAPHJrAAAt8DorBjk411.jpg" />
		//head中meta标签新闻的图片链接
		titleImg = document.getElementsByAttributeValueStarting("name", "shareimg").attr("content");
		//文章标题
		atitle = document.getElementsByAttributeValueStarting("name", "sharetitle").attr("content");
		//文章描述
		description = document.getElementsByAttributeValueStarting("name", "sharedesc").attr("content");
		//分享链接
		shareUrl = document.getElementsByAttributeValueStarting("name", "shareurl").attr("content");
		//文章id
		shareid=document.getElementsByAttributeValueStarting("name", "articleid").attr("content");
		if ("".equals(WebViewActivity.titleImg) || WebViewActivity.titleImg == null) {
			thumbBmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.loading_img);
		} else {
			try {
				bmp = BitmapFactory.decodeStream(new URL(WebViewActivity.titleImg)
				.openStream());
				thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
				bmp.recycle();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/
	//分享到微博
	public static void shareWB(X5WebviewActivity activity, String myurl,String description) {
		// TODO Auto-generated method stub
		context=activity;
		description=description;
		WBKEY = context.getResources().getString(R.string.wb_key);
		//		AnalyticalHtml(myurl);
		if ("".equals(X5WebviewActivity.titleImg) || X5WebviewActivity.titleImg == null) {
			thumbBmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.loading_img);
		} else {
			try {
				bmp = BitmapFactory.decodeStream(new URL(X5WebviewActivity.titleImg)
				.openStream());
				thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
				bmp.recycle();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 创建微博实例
		mWeiboAuth = new WeiboAuth(context, WBKEY, ShareAppIDUtil.WBURL,
				ShareAppIDUtil.WBSCOPE);
		// 创建微博 SDK 接口实例
		// c756f5460ac7745bd562c5ea19457889
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, WBKEY);
		// 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
		boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
		// 如果未安装微博客户端，设置下载微博对应的回调
		if (!isInstalledWeibo) {
			mWeiboShareAPI
			.registerWeiboDownloadListener(new IWeiboDownloadListener() {
				@Override
				public void onCancel() {
					Toast.makeText(context, "取消",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		mWeiboShareAPI.registerApp();
		if (!mWeiboShareAPI.isWeiboAppInstalled()) {
			mWeiboShareAPI
			.registerWeiboDownloadListener(new IWeiboDownloadListener() {
				public void onCancel() {
					Toast.makeText(context, "取消",Toast.LENGTH_SHORT).show();
				}
			});
		}
		// 授权
		mSsoHandler = new SsoHandler(activity, mWeiboAuth);
		mSsoHandler.authorize(new AuthListener());
		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			// 1. 初始化微博的分享消息
			WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
			if (X5WebviewActivity.titleImg != null) {
				weiboMessage.imageObject = getImageObj();
			}
			weiboMessage.mediaObject = getWebpageObj();
			// 2. 初始化从第三方到微博的消息请求
			SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
			// 用transaction唯一标识一个请求
			request.transaction = String
					.valueOf(System.currentTimeMillis());
			request.multiMessage = weiboMessage;
			// 3. 发送请求消息到微博，唤起微博分享界面
			mWeiboShareAPI.sendRequest(request);
		} else {
			Toast.makeText(activity, "微博客户端不支持 SDK 分享或微博客户端未安装或微博客户端是非官方版本。",
					Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	private static class AuthListener implements WeiboAuthListener {
		private Oauth2AccessToken mAccessToken;

		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(context,
						mAccessToken);
				//				Toast.makeText(NJLDetails.this, "授权成功", Toast.LENGTH_SHORT)
				//				.show();
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "分享失败";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(context, message, Toast.LENGTH_LONG)
				.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(context, "分享取消！", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(context,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}
	/**
	 * 创建图片消息对象。
	 * 
	 * @return 图片消息对象。
	 */
	private static ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		// BitmapDrawable bitmapDrawable = (BitmapDrawable)
		// mImageView.getDrawable();
		imageObject.setImageObject(thumbBmp);
		return imageObject;
	}

	private static WebpageObject getWebpageObj() {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = X5WebviewActivity.atitle;
		mediaObject.description = X5WebviewActivity.description;
		// InputStream is = getResources().openRawResource(R.drawable.swb);
		// Bitmap mBitmap = BitmapFactory.decodeStream(is);
		// 设置 Bitmap 类型的图片到视频对象里
		mediaObject.setThumbImage(thumbBmp);
		mediaObject.actionUrl = X5WebviewActivity.shareUrl;
		mediaObject.defaultText = X5WebviewActivity.atitle;
		return mediaObject;
	}

}


