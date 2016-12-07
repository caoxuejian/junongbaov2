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
		// ΢��ע��
		// WXKEY=ShareAppIDUtil.getWXAppKEY(appname);
		WXKEY = context.getResources().getString(R.string.wx_key);
		// ͨ��WXAPIFactory��������ȡIWXAPI��ʵ��
		api = WXAPIFactory.createWXAPI(context, WXKEY, false);
		api.registerApp(WXKEY);
		//����html
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
		//state:0��ʾ��������
		//state:1��ʾ����Ȧ
		// TODO Auto-generated method stub
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = X5WebviewActivity.shareUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = X5WebviewActivity.atitle;
		msg.description = X5WebviewActivity.description;

		//����ͳ����
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
	//ͳ�Ʒ�����
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
		// ��ȡ����
		String htmlStr = HtmlUtil.getHtmlString(myurl);
		Document document = Jsoup.parse(htmlStr);
		//<meta name="shareimg" content="http://cmsfile10.chinaso.com/group1/M00/22/BF/Cgqg11VMX6GAPHJrAAAt8DorBjk411.jpg" />
		//head��meta��ǩ���ŵ�ͼƬ����
		titleImg = document.getElementsByAttributeValueStarting("name", "shareimg").attr("content");
		//���±���
		atitle = document.getElementsByAttributeValueStarting("name", "sharetitle").attr("content");
		//��������
		description = document.getElementsByAttributeValueStarting("name", "sharedesc").attr("content");
		//��������
		shareUrl = document.getElementsByAttributeValueStarting("name", "shareurl").attr("content");
		//����id
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
	//����΢��
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
		// ����΢��ʵ��
		mWeiboAuth = new WeiboAuth(context, WBKEY, ShareAppIDUtil.WBURL,
				ShareAppIDUtil.WBSCOPE);
		// ����΢�� SDK �ӿ�ʵ��
		// c756f5460ac7745bd562c5ea19457889
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, WBKEY);
		// ��ȡ΢���ͻ��������Ϣ�����Ƿ�װ��֧�� SDK �İ汾
		boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
		// ���δ��װ΢���ͻ��ˣ���������΢����Ӧ�Ļص�
		if (!isInstalledWeibo) {
			mWeiboShareAPI
			.registerWeiboDownloadListener(new IWeiboDownloadListener() {
				@Override
				public void onCancel() {
					Toast.makeText(context, "ȡ��",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		mWeiboShareAPI.registerApp();
		if (!mWeiboShareAPI.isWeiboAppInstalled()) {
			mWeiboShareAPI
			.registerWeiboDownloadListener(new IWeiboDownloadListener() {
				public void onCancel() {
					Toast.makeText(context, "ȡ��",Toast.LENGTH_SHORT).show();
				}
			});
		}
		// ��Ȩ
		mSsoHandler = new SsoHandler(activity, mWeiboAuth);
		mSsoHandler.authorize(new AuthListener());
		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			// 1. ��ʼ��΢���ķ�����Ϣ
			WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
			if (X5WebviewActivity.titleImg != null) {
				weiboMessage.imageObject = getImageObj();
			}
			weiboMessage.mediaObject = getWebpageObj();
			// 2. ��ʼ���ӵ�������΢������Ϣ����
			SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
			// ��transactionΨһ��ʶһ������
			request.transaction = String
					.valueOf(System.currentTimeMillis());
			request.multiMessage = weiboMessage;
			// 3. ����������Ϣ��΢��������΢���������
			mWeiboShareAPI.sendRequest(request);
		} else {
			Toast.makeText(activity, "΢���ͻ��˲�֧�� SDK �����΢���ͻ���δ��װ��΢���ͻ����Ƿǹٷ��汾��",
					Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * ΢����֤��Ȩ�ص��ࡣ 1. SSO ��Ȩʱ����Ҫ�� {@link #onActivityResult} �е���
	 * {@link SsoHandler#authorizeCallBack} �� �ûص��Żᱻִ�С� 2. �� SSO
	 * ��Ȩʱ������Ȩ�����󣬸ûص��ͻᱻִ�С� ����Ȩ�ɹ����뱣��� access_token��expires_in��uid ����Ϣ��
	 * SharedPreferences �С�
	 */
	private static class AuthListener implements WeiboAuthListener {
		private Oauth2AccessToken mAccessToken;

		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// ���� Token �� SharedPreferences
				AccessTokenKeeper.writeAccessToken(context,
						mAccessToken);
				//				Toast.makeText(NJLDetails.this, "��Ȩ�ɹ�", Toast.LENGTH_SHORT)
				//				.show();
			} else {
				// ���¼�������������յ� Code��
				// 1. ����δ��ƽ̨��ע���Ӧ�ó���İ�����ǩ��ʱ��
				// 2. ����ע���Ӧ�ó��������ǩ������ȷʱ��
				// 3. ������ƽ̨��ע��İ�����ǩ��������ǰ���Ե�Ӧ�õİ�����ǩ����ƥ��ʱ��
				String code = values.getString("code");
				String message = "����ʧ��";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(context, message, Toast.LENGTH_LONG)
				.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(context, "����ȡ����", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(context,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}
	/**
	 * ����ͼƬ��Ϣ����
	 * 
	 * @return ͼƬ��Ϣ����
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
		// ���� Bitmap ���͵�ͼƬ����Ƶ������
		mediaObject.setThumbImage(thumbBmp);
		mediaObject.actionUrl = X5WebviewActivity.shareUrl;
		mediaObject.defaultText = X5WebviewActivity.atitle;
		return mediaObject;
	}

}


