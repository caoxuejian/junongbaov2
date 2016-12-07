package com.nxt.ynt.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;

import com.nxt.ynt.JNBMainActivity;
import com.nxt.ynt.utils.CacheData;

public class FileUilts {

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 鍒涘缓锟�锟斤拷Buffer瀛楃锟�?
		byte[] buffer = new byte[1024];
		// 姣忔璇诲彇鐨勫瓧绗︿覆�?��害锛屽鏋滀�?1锛屼唬琛ㄥ叏閮ㄨ鍙栧畬锟�
		int len = 0;
		// 浣跨敤锟�锟斤拷杈撳叆娴佷粠buffer閲屾妸鏁版嵁璇诲彇鍑烘潵
		while ((len = inStream.read(buffer)) != -1) {
			// 鐢ㄨ緭鍑烘祦锟�uffer閲屽啓鍏ユ暟鎹紝涓棿鍙傛暟浠ｈ�?浠庡摢涓綅缃紑濮嬭锛宭en浠ｈ〃璇诲彇鐨勯暱锟�?
			com.nxt.nxtapp.common.LogUtil.syso("%%%%%%%%%%%%%%%" + len);
			outStream.write(buffer, 0, len);
		}
		// 鍏抽棴杈撳叆锟�
		inStream.close();
		// 鎶妎utStream閲岀殑鏁版嵁鍐欏叆鍐呭瓨
		return outStream.toByteArray();
	}

	public static void saveImage(InputStream inputStream, String path)
			throws Exception {
		// 寰楀埌鍥剧墖鐨勪簩杩涘埗鏁版嵁锛屼互浜岃繘鍒跺皝瑁呭緱鍒版暟鎹紝鍏锋湁閫氱敤锟�?
		byte[] data = readInputStream(inputStream);
		// new锟�锟斤拷鏂囦欢瀵硅薄鐢ㄦ潵淇濆瓨鍥剧墖锛岄粯璁や繚瀛樺綋鍓嶅伐绋嬫牴鐩綍
		File imageFile = new File(path);
		// 鍒涘缓杈撳嚭锟�
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(imageFile);
			outStream.write(data);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// 鍐欏叆鏁版嵁
		// 鍏抽棴杈撳嚭锟�
	}

	// 锟斤拷锟芥缓锟斤拷锟斤拷锟�
	public static void saveCacheData(InputStream inputStream, String url)
			throws Exception {
		if (inputStream == null || url == null) {
			return;
		}

		File SDFile = android.os.Environment.getExternalStorageDirectory();
		File file = new File(SDFile.getAbsolutePath() + File.separator
				+ "gualiyu/album");
		if (!file.exists() && !(file.isDirectory())) {
			file.mkdirs();
		}

		// 锟斤拷锟侥硷�?
		File myFile = new File(SDFile.getAbsolutePath() + File.separator
				+ "gualiyu/album" + File.separator + url);
		byte[] data = readInputStream(inputStream);

		FileOutputStream fos = null;
		// 锟叫讹拷锟角凤拷锟斤拷锟�锟斤拷锟斤拷锟斤拷锟津创斤�?
		if (myFile.exists()) {
			myFile.delete();
			try {

				myFile.createNewFile();
				fos = new FileOutputStream(myFile);
				fos.write(data);
				fos.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				myFile.createNewFile();
				fos = new FileOutputStream(myFile);
				fos.write(data);
				fos.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 淇濆瓨鏂囦欢
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveFile(Bitmap bm, String fileName, String Format)
			throws IOException {
		// File SDFile = android.os.Environment.getExternalStorageDirectory();
		com.nxt.nxtapp.common.LogUtil.syso("Format:"+Format);
		File dirFile = new File(JNBMainActivity.PIC_PATH + File.separator);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}

		File myCaptureFile = new File(fileName);
		com.nxt.nxtapp.common.LogUtil.syso("myCaptureFile:" + myCaptureFile.getAbsolutePath());
		BufferedOutputStream bos = null;
		if (myCaptureFile.exists()) {
			myCaptureFile.delete();
			try {

				myCaptureFile.createNewFile();
				bos = new BufferedOutputStream(new FileOutputStream(
						myCaptureFile));
				if (Format != null
						&& (Format.equals("png") || Format.equals("PNG"))) {
					bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
				} else {
					bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				}
				bos.flush();
				bos.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				myCaptureFile.createNewFile();
				bos = new BufferedOutputStream(new FileOutputStream(
						myCaptureFile));
				if (Format != null
						&& (Format.equals("png") || Format.equals("PNG"))) {
					bm.compress(Bitmap.CompressFormat.PNG, 80, bos);
				} else {
					bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				}
				bos.flush();
				bos.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] agrs) {
		String urlstr = "/meilisannong/server/uploads/atlas/middle/2013-04-28/201304280941456457_thumb.jpg";
		String fileName = CacheData.MD5(urlstr) + File.separator
				+ urlstr.substring(urlstr.indexOf("."));
		com.nxt.nxtapp.common.LogUtil.syso(fileName);
	}
}
