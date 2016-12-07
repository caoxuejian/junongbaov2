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
	     * 获取网址的html
	     * 01
//打开本包内asset目录下的index.html文件
02
 
03
wView.loadUrl(" file:///android_asset/index.html ");  
04
 
05
//打开本地sd卡内的index.html文件
06
 
07
wView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
08
 
09
//打开指定URL的html文件
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
	        conn.setConnectTimeout(6*1000);  //设置链接超时时间6s
	 
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
	     * 1.跳转到浏览器直接访问页面，这段代码是在Activity中拷贝来的，所以有startActivity()方法
Uri uri = Uri.parse("http://www.XXXX.com"); //要链接的地址
Intent intent = new Intent(Intent.ACTION_VIEW, uri);
startActivity(intent);
2.使用TextView显示HTML方法
TextView text1 = (TextView)findViewById(R.id.TextView02);
text1.setText(Html.fromHtml(“<font size='20'>网页内容</font>”));
3.直接使用android中自带的显示网页组件WebView
webview = (WebView) findViewById(R.id.WebView01);
webview.getSettings().setJavaScriptEnabled(true);
webview.loadUrl("http://www.xxxx.com");
4 显示本地html
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
"<p><a href=\"file:///sdcard/web/acdf2705\">链接google</a></p>"+ 
"<p><a href=\"file:///sdcard/ebook/user_defined/browser/localweb/\532fa8dc\"& gt;链接google</a></p>"; 
mWebView.loadDataWithBaseURL("file://", html,"text/html", "UTF-8", "about:blank");
	     */
	    
}
