package com.nxt.nxtapp.common;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

public class HtmlUtil {

	 /**
	52
	     * ��ȡ��ַ��html
	     * 01
//�򿪱�����assetĿ¼�µ�index.html�ļ�
02
 
03
wView.loadUrl(" file:///android_asset/index.html ");  
04
 
05
//�򿪱���sd���ڵ�index.html�ļ�
06
 
07
wView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
08
 
09
//��ָ��URL��html�ļ�
10
 
11
wView.loadUrl(" http://m.oschina.net");

	53
	     * @throws Exception
	54
	     */

	    public static byte[] testGetHtml(String urlpath) throws Exception

	    {
	        URL url=new URL(urlpath);
	        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
	        conn.setConnectTimeout(6*1000);  //�������ӳ�ʱʱ��6s
	 
	        conn.setRequestMethod("GET"); 

	        if(conn.getResponseCode()==200)

	        {

	            InputStream inputStream=conn.getInputStream();

	            byte[] data=StreamTool.read(inputStream);

	            return data;
	        }
	        return null;
	    }

	    
	    public static String getHtmlString(String urlString) {  
	        try {  
	            URL url = new URL(urlString);  
	            URLConnection ucon = url.openConnection();  
	            InputStream instr = ucon.getInputStream();  
	            BufferedInputStream bis = new BufferedInputStream(instr);  
	            ByteArrayBuffer baf = new ByteArrayBuffer(500);  
	            int current = 0;  
	            while ((current = bis.read()) != -1) {  
	                baf.append((byte) current);  
	            }  
	            return EncodingUtils.getString(baf.toByteArray(), "utf-8");  
	        } catch (Exception e) {  
	            return "";  
	        }  
	    }
  
	    
	    /*
	     * 1.��ת�������ֱ�ӷ���ҳ�棬��δ�������Activity�п������ģ�������startActivity()����
Uri uri = Uri.parse("http://www.XXXX.com"); //Ҫ���ӵĵ�ַ
Intent intent = new Intent(Intent.ACTION_VIEW, uri);
startActivity(intent);
2.ʹ��TextView��ʾHTML����
TextView text1 = (TextView)findViewById(R.id.TextView02);
text1.setText(Html.fromHtml(��<font size='20'>��ҳ����</font>��));
3.ֱ��ʹ��android���Դ�����ʾ��ҳ���WebView
webview = (WebView) findViewById(R.id.WebView01);
webview.getSettings().setJavaScriptEnabled(true);
webview.loadUrl("http://www.xxxx.com");
4 ��ʾ����html
@1
webview = (WebView) findViewById(R.id.webview); 
webview.getSettings().setJavaScriptEnabled(true); 
webview.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
@2
Uri uri = Uri.parse("content://com.android.htmlfileprovider/sdcard/01.htm");
Intent intent = new Intent();
intent.setData(uri);
intent.setClassName("com.android.htmlviewer", "com.android.htmlviewer.HTMLViewerActivity");
startActivity(intent);

@3
String encoding = "UTF-8";
String mimeType = "text/html";
final String html = 
"<p><a href=\"file:///sdcard/web/acdf2705\">����google</a></p>"+ 
"<p><a href=\"file:///sdcard/ebook/user_defined/browser/localweb/\532fa8dc\"& gt;����google</a></p>"; 
mWebView.loadDataWithBaseURL("file://", html,"text/html", "UTF-8", "about:blank");
	     */
	    
}
