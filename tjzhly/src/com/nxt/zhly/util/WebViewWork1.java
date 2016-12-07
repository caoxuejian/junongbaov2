package com.nxt.zhly.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.AESSafe;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.DengLuActivity;
import com.nxt.ynt.JiHuoActivity;
import com.nxt.ynt.MediaPlayActivity;
import com.nxt.ynt.MyViewPagerJHActivity;
import com.nxt.ynt.VRWebviewActivity;
import com.nxt.ynt.X5WebviewActivity;
import com.nxt.ynt.entity.RTSPURL;
import com.nxt.ynt.entity.SecondWebview;
import com.nxt.ynt.entity.WebviewDate;
import com.nxt.ynt.entity.XNBmsg;
import com.nxt.ynt.img.AlbumEditActivity;
import com.nxt.ynt.page.ReadRaw;
import com.nxt.ynt.x5view.BrowserActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.smtt.sdk.WebView;
public class WebViewWork1
{

	private static String vid;
	private static NetworkInfo isNetWork;
	private static PayReq req;
	private static HashMap map;
	static Context context1;
	private static String urlist;
	private static String title;
	private static String jsonurl;
	private static String camid="";
	private static Util util;
	private static WebView wv;

	public static void executive(final Context context, String url,WebView view,IWXAPI msgApi){
		System.out.println("@@@@@@@@@@url:"+url);
		context1=context;
		wv=view;
		util = new Util(context);
		//		url="fun://method=showbigimg&imgs=img/20160317/5E0626601C1643E58D41249303087258,img/20160317/5E0626601C1643E58D41249303087258&clickindex=2";
		//		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
		//		url="broadcast://camid=1&vr��1&server=jnbv4.365960.com";
		String[] tels = url.split(":");
		String tel = tels[0];

		//		url="fun://method=capture&id=chazhenwei";
		String[] ll = url.split("&");
		String param = ReadRaw.getRawJson(context, ReadRaw.WEBSCHAME);
		final WebviewDate list = (WebviewDate)JsonPaser.getObjectDatas(WebviewDate.class,param);
		ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		isNetWork = cwjManager.getActiveNetworkInfo();
		if (isNetWork != null) {
			if(url.contains("target=_blank")){//���µ�ҳ��
				if(url.contains("&target=_blank")){
					url=url.replaceAll("&target=_blank","");
				}/*else if(url.contains("?target=_blank")){
						url=url.replaceAll("/?target=_blank","");
					}*/else{
						url=url.replaceAll("target=_blank","");
					}
				if(url.contains("&mobileVR")){
					url=url.replaceAll("&mobileVR", "");
					if(!"false".equals(util.getFromSp("X5FirstLoad", ""))){
						showdialog();
					}else{
						Intent intent=new Intent();
						intent.setClass(context, VRWebviewActivity.class);
						intent.putExtra("webviewpath", url);
						context.startActivity(intent);
					}
				}else if(url.contains("/vr")||url.contains("/vtour")){
					if(!"false".equals(util.getFromSp("X5FirstLoad", ""))){
						showdialog();
					}else{
						Intent intent=new Intent();
						intent.setClass(context, BrowserActivity.class);
						intent.putExtra("webviewpath", url);
						context.startActivity(intent);
					}
				}else{

					String second = list.getPrice();
					SecondWebview activi = (SecondWebview)JsonPaser.getObjectDatas(SecondWebview.class,second);
					Intent intent=new Intent();
					intent.setClassName(context, activi.getActivity());
					//						intent.setClass(context, X5WebviewActivity.class);
					intent.putExtra("webviewpath", url);
					intent.putExtra("flag", "gone");
					context.startActivity(intent);
				}

			}else{
				//broadcast://camid=1&vr��1&server=jnbv4.365960.com
				//rtmp://[server]:1936/cam/cam[camid]  VRֱ����ַƴ��
				if(url.contains("broadcast")){//vrֱ��
					String[] broadcast =url.replaceAll("broadcast://", "").split("&");
					String camid = broadcast[0].substring(broadcast[0].indexOf("=")+1, broadcast[0].length());
					String vr=broadcast[1].substring(broadcast[1].indexOf("=")+1, broadcast[1].length());
					String server=broadcast[2].substring(broadcast[2].indexOf("=")+1, broadcast[2].length());
					//ƴ����Ƶ��ַ
					String vrurl="rtmp://"+server+":1936/hls/cam"+camid;
					//VRֱ��
					MediaPlayActivity.startActivity(context1,vrurl);
				}else if(url.contains("vrvideo")){//vr¼��
					//vrvideo://url=http://www.someurl.com/somepath/vrfile.mp4
					String vrurl = url.substring(url.indexOf("=")+1, url.length());
					MediaPlayActivity.startActivity(context1,vrurl);
				}else if(url.contains("wechatqq")){//����qq
					//fun://method=wechatqq&id=819787939
					String weurl="mqqwpa://im/chat?chat_type=wpa&uin="+ll[1].substring(ll[1].indexOf("=")+1, ll[1].length());  
					context1.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(weurl))); 
				}else if(url.contains("close")){
					Intent close = new Intent();
					close.setAction("ActivityClose");
					//					ref.putExtra("paymsg", orderid);
					context1.sendBroadcast(close);
				}else if(url.endsWith("m3u8")){
					Intent intent = new Intent(Intent.ACTION_VIEW);
					String type = "video/*";

					Uri uri = Uri.parse(url);
					intent.setDataAndType(uri, type);
					context.startActivity(intent);  
				}else if(url.contains("refresh")){//�㲥ˢ�¾ۺ�
					Intent ref = new Intent();
					ref.setAction("refresh");
					//					ref.putExtra("paymsg", orderid);
					context1.sendBroadcast(ref);
				}else if(url.contains("img360")){//�鿴��ţ�ϵĴ�ͼ
					//					Constans.DOWNLOAD_IMAGE+nu
					//����ͼƬ
					ArrayList<String> pics = new ArrayList<String>();
					pics.add(Constans.DOWNLOAD_IMAGE+ url.replaceAll("img360://", ""));
					Intent intent = new Intent(context, MyViewPagerJHActivity.class);
					intent.putExtra("page", 1);
					intent.putStringArrayListExtra("img", pics);
					context.startActivity(intent);

					/*//�鿴һ��
					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("img",url.replaceAll("img360://", ""));
					context.startActivity(intent);
					 */

				}else if(url.contains("showbigimg")){//�ۺ��е�ͼƬ��

					if(ll.length>2){
						String img = ll[1].substring(ll[1].indexOf("=")+1, ll[1].length());
						String[] imgurl = img.split(",");
						ArrayList<String> pics = new ArrayList<String>();//ͼƬ·��
						for(int i=0;i<imgurl.length;i++){
							pics.add(Constans.DOWNLOAD_IMAGE+ imgurl[i]);
						}
						//��ǰ�����
						String page = ll[ll.length-1].substring(ll[ll.length-1].indexOf("=")+1, ll[ll.length-1].length());
						Intent intent = new Intent(context, MyViewPagerJHActivity.class);
						intent.putExtra("page", Integer.parseInt(page));
						intent.putStringArrayListExtra("img", pics);
						context.startActivity(intent);
					}
				}
				else

					if(url.contains("alipay")){//֧����֧��
						String orderid = ll[0].substring(ll[0].indexOf("=")+1, ll[0].length());
						try {
							AESSafe.keyString = "nongxintong88888";
							AESSafe.first();
							orderid=AESSafe.desDecrypt(orderid);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Intent pay = new Intent();
						pay.setAction("alipay");
						pay.putExtra("paymsg", orderid);
						context1.sendBroadcast(pay);
					}/*else if(url.contains("poi")){
						String second = list.getPoi();
						SecondWebview activi = (SecondWebview)JsonPaser.getObjectDatas(SecondWebview.class,second);
						Intent intent=new Intent();
						intent.setClassName(context, activi.getActivity());
						intent.putExtra("flag", ll[1].substring(ll[1].indexOf("=")+1, ll[1].length()));
						context.startActivity(intent);
					}*/
					else if(url.contains("method=login")){
						Intent intent=new Intent();
						Bundle bundle=new Bundle();
						bundle.putInt("flag", 5);
						bundle.putString("ifback", "true");
						intent.putExtras(bundle);
						intent.setClass(context, DengLuActivity.class);
						context.startActivity(intent);
					}
					else if(url.contains("step=login")){
						Intent intent=new Intent();
						Bundle bundle=new Bundle();
						bundle.putInt("flag", 6);
						bundle.putString("ifback", "true");
						intent.putExtras(bundle);
						intent.setClass(context, DengLuActivity.class);
						context.startActivity(intent);
					}/*else if(url.contains("login.php")){
						Intent intent=new Intent();
						Bundle bundle=new Bundle();
						bundle.putInt("flag", 6);
						bundle.putString("ifback", "true");
						intent.putExtras(bundle);
						intent.setClass(context, DengLuActivity.class);
						context.startActivity(intent);
					}*/else if(url.contains("gongqiu")){//��Ҫ����Ҫ������ר��
						String second = list.getGongqiu();
						SecondWebview activi = (SecondWebview)JsonPaser.getObjectDatas(SecondWebview.class,second);
						Intent intent=new Intent();
						intent.setClassName(context, activi.getActivity());
						intent.putExtra("sort", ll[1].substring(ll[1].indexOf("=")+1, ll[1].length()));
						intent.putExtra("name", "������Ϣ");
						context.startActivity(intent);
					}else if(url.contains("wxpay")){//΢��֧��������Ҫ�Ĳ��������ص��������л��
						//				final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
						req = new PayReq();
						map=new HashMap();
						String[] wxurl = url.split("&");
						for(int i=0;i<wxurl.length;i++){
							String[] keyValue = wxurl[i].split("=");
							if(keyValue.length>1){
								map.put(keyValue[0],keyValue[1]);
							}else{
								map.put(keyValue[0],"");
							}
						}
						req.appId =context.getResources().getString(R.string.wx_key);
						req.partnerId = (String) map.get("partnerid");//�̻���
						req.prepayId =(String) map.get("prepay_id");//΢�ŷ��ص�֧�����׻ỰID
						req.packageValue = "Sign=WXPay";
						req.nonceStr = (String) map.get("nonceStr");//����ַ���
						req.timeStamp = (String) map.get("timeStamp");//ʱ���
						req.sign =(String) map.get("paySign");//ǩ��

						msgApi.registerApp(context.getResources().getString(R.string.wx_key));
						msgApi.sendReq(req);

						//�����б�
					}else if(url.startsWith("news://")){
						//						vid=url.substring(url.indexOf("=")+1,url.length());
						//						//����ҳ
						//						Intent intent = new Intent(context, NJLDetails.class);
						//						intent.putExtra("vid",vid);
						//						intent.putExtra("webviewpath",Constans.NEWS_URL+vid);
						//						intent.putExtra("flag", "visible");
						//						context.startActivity(intent);
					}else if(url.contains("capture")){
						//����α
						String second = list.getCapture();
						SecondWebview activi = (SecondWebview)JsonPaser.getObjectDatas(SecondWebview.class,second);
						Intent intent=new Intent();
						intent.setClassName(context, activi.getActivity());
						context.startActivity(intent);
					}else if (url.indexOf(".3gp") != -1 || url.indexOf(".mp4") != -1
							|| url.indexOf(".flv") != -1) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						String strend = "";
						if (url.toLowerCase().endsWith(".mp4")) {
							strend = "mp4";
						} else if (url.toLowerCase().endsWith(".3gp")) {
							strend = "3gp";
						} else if (url.toLowerCase().endsWith(".flv")) {
							strend = "flv";
						}
						intent.setDataAndType(Uri.parse(url), "video/" + strend);
						context.startActivity(intent);
					}
				/*else if (url.contains("http://m.6636.net/andriod")) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
		}*/
				//������Ϣ
					else if(url.contains("release")){

						String status = util.getFromSp("status", "1");
						if("1".equals(status)){
							String second = list.getRelease();
							ArrayList<String> selectedDataList = new ArrayList<String>();
							Bundle bundle = new Bundle();
							bundle.putStringArrayList("dataList", selectedDataList);
							bundle.putString("editContent", "");
							SecondWebview activi = (SecondWebview)JsonPaser.getObjectDatas(SecondWebview.class,second);
							Intent intent=new Intent();
							intent.setClassName(context, activi.getActivity());
							intent.putExtra("sort", ll[1].substring(ll[1].indexOf("=")+1, ll[1].length()));
							intent.putExtras(bundle);
							context.startActivity(intent);
						}else{
							Intent intent = new Intent(context,
									JiHuoActivity.class);
							intent.putExtra("sort", ll[1].substring(ll[1].indexOf("=")+1, ll[1].length()));
							intent.putExtra("jihuo", "webview");
							context.startActivity(intent);
						}

					}else if(url.contains("http://")||url.contains("https://")){
						view.loadUrl(url);
						//����
					}else if(url.contains("query")){
						String second = list.getQuery();
						SecondWebview activi = (SecondWebview)JsonPaser.getObjectDatas(SecondWebview.class,second);
						Intent intent=new Intent();
						intent.setClassName(context, activi.getActivity());
						context.startActivity(intent);
						//����Ѷ
					}else if(url.contains("consultation")){
						String jsonstr = getParam(context,ll[1].substring(ll[1].indexOf("=")+1, ll[1].length()));
						XNBmsg listmsg = (XNBmsg) com.nxt.nxtapp.json.JsonPaser
								.getObjectDatas(XNBmsg.class, jsonstr);
						String second = list.getConsultation();
						SecondWebview activi = (SecondWebview)JsonPaser.getObjectDatas(SecondWebview.class,second);
						Intent intent=new Intent();
						intent.setClassName(context, activi.getActivity());
						intent.putExtra("params", listmsg.getParam());
						Bundle bundle = new Bundle();
						bundle.putSerializable("GZ", listmsg);
						intent.putExtras(bundle);
						context.startActivity(intent);
					}else if (url.equals("tel://12316")) {
						WebViewWork1.callphone(context, "12316").show();
					} else if (tel.equals("tel")) {
						String number = tels[1];
						System.out.println("@@@@@@@@@@"+number);
						WebViewWork1.callphone(context, number.replaceAll("//","")).show();
					} else if (url.contains("PublicActivity")) {
						//						Intent intent = new Intent(context, ExpertListActivity.class);
						//						context.startActivity(intent);
					}else if(url.contains("javarscrpt:")){

					}
					else{
						view.loadUrl(url);
					}
			}}else{
				Toast.makeText(context, "���������Ƿ����ӣ�", Toast.LENGTH_SHORT).show();
			}

	}

	//������˵
	private static void postdjs(final String sid) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context1, R.style.dialog);
		View inflate = View.inflate(context1, R.layout.djs_dialog_del, null);
		TextView dialogTitle = (TextView) inflate.findViewById(R.id.dialog_title);
		final EditText et_content = (EditText)inflate.findViewById(R.id.et_content);
		dialogTitle.setText("������Ҫ���������");
		TextView dialogCancel = (TextView) inflate.findViewById(R.id.del_cancel);
		dialogCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		TextView dialogConfirm = (TextView) inflate.findViewById(R.id.confirm_del);
		dialogConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RequestParams rp = new RequestParams();
				rp.put("uid", util.getFromSp("uid", ""));
				rp.put("token", util.getFromSp("tokens", ""));
				rp.put("sid", sid);
				rp.put("content", et_content.getText().toString());
				NxtRestClient.post(Constans.JNB_FBDJS, rp,
						new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						System.out.println("@@@@@@@@@"+content);
						dialog.dismiss();
						try {
							JSONObject js = new JSONObject(content);
							String errorcode = js.getString("errorcode");
							String msg = js.getString("msg");
							if("0".equals(errorcode)){
								String action = js.getString("action");
								wv.loadUrl("javascript:"+action+"()");
								Toast.makeText(context1, msg, Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(context1, msg, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Toast.makeText(context1, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		dialog.setContentView(inflate);
		dialog.show();
	}


	private static void showdialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context1, R.style.dialog);
		View inflate = View.inflate(context1, R.layout.x5dialog, null);
		TextView dialogTitle = (TextView) inflate.findViewById(R.id.dialog_title);
		TextView dialogCancel = (TextView) inflate.findViewById(R.id.del_cancel);
		dialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}


		});
		TextView dialogConfirm = (TextView) inflate.findViewById(R.id.confirm_del);
		dialogConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				util.saveToSp("X5FirstLoad", "false");//
				android.os.Process.killProcess(Process.myPid());
			}
		});
		dialog.setContentView(inflate);
		dialog.show();
	}

	private static String getParam(Context context, String id) {
		// TODO Auto-generated method stub
		String param = "";
		//�����Ҳ����Ѷ
		if("chazixun".equals(id)){
			param=ReadRaw.getRawJson(context,ReadRaw.CHAZHUBING);
		}else if("njzs".equals(id)){//ũ��������Ѷ
			param=ReadRaw.getRawJson(context,ReadRaw.NJZSZX);
		}
		return param;
	}


	/**
	 * ����绰��ʾ
	 * @param context ������
	 * @param url ����ĺ���
	 * @return
	 */
	public static AlertDialog.Builder callphone(final Context context,final String url) {
		return new AlertDialog.Builder(context)
		// .setIcon(R.drawable.ok)
		.setTitle("����绰")
		.setMessage("�Ƿ񲦴�绰:"+ Uri.parse(url))
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+url));
				context.startActivity(intent);
			}
		})
		.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {

			}
		});
	}

}
