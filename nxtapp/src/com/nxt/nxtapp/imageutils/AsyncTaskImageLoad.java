package com.nxt.nxtapp.imageutils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class AsyncTaskImageLoad extends AsyncTask<String, Integer, Bitmap> {
	
	 private ImageView mImage=null;

	 public AsyncTaskImageLoad(ImageView img){
		 mImage = img;
	 }
	 
	@Override
	protected Bitmap doInBackground(String... params) {
		try {
			
			
			com.nxt.nxtapp.common.LogUtil.syso("params[0] = "+params[0]);
			URL url=new URL(params[0]);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
		if(conn.getResponseCode()==200){
			InputStream input=conn.getInputStream();
			Bitmap map=BitmapFactory.decodeStream(input);
			return map;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	protected void onPostExecute(Bitmap result){
		if(mImage!=null && result!=null){
			mImage.setImageBitmap(result);
		}
	      super.onPostExecute(result);
	}
}
