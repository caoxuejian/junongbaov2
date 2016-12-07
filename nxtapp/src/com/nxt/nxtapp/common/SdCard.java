package com.nxt.nxtapp.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.util.ByteArrayBuffer;

import com.nxt.nxtapp.common.BitmapUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

public class SdCard {
	static// 得到储存卡的状态
	String sDStateString = android.os.Environment.getExternalStorageState();
	public static String pic_path;

	// 储存图片
	public static void saveImg(String path, Bitmap bitmap,InputStream is) {
		if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			// com.nxt.nxtapp.common.LogUtil.syso(getAvailaleSize());
			// 如果剩余存储空间大于1M
			if (getAvailaleSize() > 1) {
				try {
					// 获取扩展存储设备的文件目录
					File file = new File(pic_path);
					if (!file.exists() && !(file.isDirectory())) {
						file.mkdirs();
					}

					// 打开文件
					File myFile = new File(pic_path + File.separator + path);

					// 判断是否存在,不存在则创建
					if (!myFile.exists()) {
						myFile.createNewFile();
					}
					
					if(is!=null){
						 BufferedOutputStream bos = null;  
				         bos = new BufferedOutputStream(new FileOutputStream(myFile)); 
						 byte[] buf = new byte[1024];  
				            int len = 0;  
				            // 将网络上的图片存储到本地  
				            while ((len = is.read(buf)) > 0) {  
				                bos.write(buf, 0, len);  
				            } 
				            bos.close();
				            return;
					}
					
					// 先把图片转化成字节数组
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

					// 写数据
					FileOutputStream outputStream = new FileOutputStream(myFile);

					outputStream.write(os.toByteArray());
					outputStream.close();

				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}// end of try

			}
		}
	}

	// 得到图片
	public static Bitmap getBitMap(String path) {
		Bitmap result = null;

		// 获取扩展存储设备的文件目录
		// File SDFile = android.os.Environment.getExternalStorageDirectory();

		// 打开文件
		File myFile = new File(pic_path + File.separator + path);
		// 读数据
		if (myFile.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(myFile);
				ByteArrayBuffer baf = new ByteArrayBuffer(100);
				int current = 0;
				while ((current = inputStream.read()) != -1) {
					baf.append((byte) current);
				}
				inputStream.close();
				// 返回这个图片
				result = BitmapFactory.decodeByteArray(baf.toByteArray(), 0,
						baf.toByteArray().length);

			} catch (Exception e) {
				// TODO: handle exception
			}// end of try
		}
		return result;

	}
	
	public static Bitmap getpublicBitmap(String path){
		 Bitmap bitmap = null;
		 BitmapFactory.Options opts =  new  BitmapFactory.Options();
         opts.inJustDecodeBounds =  true ;
         path = pic_path + File.separator + path;
    	 BitmapFactory.decodeFile(path,opts);
    	 opts.inSampleSize = BitmapUtil.computeSampleSize(opts, - 1 ,  85 * 85 );       
         opts.inJustDecodeBounds =  false ;
         try  {
        	 bitmap = BitmapFactory.decodeFile(path, opts);
              }  catch  (OutOfMemoryError err) {
            	 err.printStackTrace();
              }
             
         return bitmap;
	}

	// 计算剩余空间的大小
	public static long getAvailaleSize() {

		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径

		StatFs stat = new StatFs(path.getPath());

		/* 获取block的SIZE */

		long blockSize = stat.getBlockSize();

		/* 空闲的Block的数量 */

		long availableBlocks = stat.getAvailableBlocks();

		/* 返回bit大小值 */

		// return availableBlocks * blockSize;

		// (availableBlocks * blockSize)/1024 KIB 单位

		return (availableBlocks * blockSize) / 1024 / 1024; // MIB单位

	}

}
