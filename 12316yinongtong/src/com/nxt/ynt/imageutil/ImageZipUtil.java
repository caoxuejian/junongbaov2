package com.nxt.ynt.imageutil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.nxt.nxtapp.common.FileUtil;
import com.nxt.ynt.utils.Constans;

public class ImageZipUtil {

	private static String zipath;

	/**
	 * ����Ҫ�ķ���
	 */
	public static String getThumbUploadPath(String oldPath, int bitmapMaxWidth)
			throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// ��������߲�ѯͼ��������Ϊ�����ط����ڴ�
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(oldPath, options);
		int height = options.outHeight;
		int width = options.outWidth;
		int reqHeight = 0;
		int reqWidth = 0;
		//		if(bitmapMaxWidth>width){
		//			reqWidth=width;
		//		}else{
		reqWidth=bitmapMaxWidth;
		//		}
		reqHeight = (reqWidth * height) / width;
		// ���ڴ��д���bitmap����,������������Ŵ�С����
		options.inSampleSize = calculateInsampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
		Bitmap bbb = compressImage(Bitmap.createScaledBitmap(bitmap,
				reqWidth, reqHeight, false));
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		return saveImg(bbb, md5(timeStamp));
	}
	//����·����ȡѹ�����bitmap
	public static Bitmap getBitmap(String oldPath, int bitmapMaxWidth)
			throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// ��������߲�ѯͼ��������Ϊ�����ط����ڴ�
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(oldPath, options);
		int height = options.outHeight;
		int width = options.outWidth;
		int reqHeight = 0;
		int reqWidth = 0;
		//		if(bitmapMaxWidth>width){
		//			reqWidth=width;
		//		}else{
		reqWidth=bitmapMaxWidth;
		//		}
		reqHeight = (reqWidth * height) / width;
		// ���ڴ��д���bitmap����,������������Ŵ�С����
		options.inSampleSize = calculateInsampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
		Bitmap bbb = compressImage(Bitmap.createScaledBitmap(bitmap,
				reqWidth, reqHeight, false));
		return bbb;
	}

	//ָ��ͼƬ�ĸ߶ȣ�����1000Ĭ��Ϊ1000
	public static String getUploadPath(String oldPath, int bitmapHeight)
			throws IOException {
		int reqHeight=0;
		int reqWidth = 0;
		if(bitmapHeight>1000){
			bitmapHeight=1000;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		// ��������߲�ѯͼ��������Ϊ�����ط����ڴ�
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(oldPath, options);
		int height = options.outHeight;
		int width = options.outWidth;
		if(height<bitmapHeight){
			reqHeight=height;
		}else{
			reqHeight = bitmapHeight;
		}
		reqWidth = (reqHeight * width) / height;
		// ���ڴ��д���bitmap����,������������Ŵ�С����
		options.inSampleSize = calculateInsampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
		Bitmap bbb = compressImage(Bitmap.createScaledBitmap(bitmap,
				reqWidth, reqHeight, false));
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		return saveImg(bbb, md5(timeStamp));
	}
	public static int calculateInsampleSize(Options options, int reqWidth,
			int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// ����ѹ������,����100��ʾ��ѹ��,��ѹ��������ݴ�ŵ�baos��
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 80;
		// ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���������ѹ��
		while ((baos.toByteArray().length)/1024> 100) {
			baos.reset();
			options -= 10;// ÿ�ζ�����10
			// ����ѹ��options%,��ѹ��������ݴ�ŵ�baos��
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		// ��ByteArrayInputStream��������ͼƬ
		Bitmap bitmap = BitmapFactory.decodeStream(bais);
		return bitmap;
	}

	public static String getzippath(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();// ��ȡ����ͼ��ʾ����Ļ��
		opts.inSampleSize = 2;
		Bitmap image = BitmapFactory.decodeFile(path, opts);
		//	        /**
		//	         * ��ͼƬ��תΪ���ķ���
		//	         */
		//	        bitmap = rotaingImageView(degree, cbitmap);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// ����ѹ������,����100��ʾ��ѹ��,��ѹ��������ݴ�ŵ�baos��
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 80;
		// ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���������ѹ��
		while ((baos.toByteArray().length)/1024> 300) {
			baos.reset();
			options -= 10;// ÿ�ζ�����10
			// ����ѹ��options%,��ѹ��������ݴ�ŵ�baos��
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		// ��ByteArrayInputStream��������ͼƬ
		Bitmap bitmap = BitmapFactory.decodeStream(bais);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		try {
			zipath = saveImg(bitmap, md5(timeStamp));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return zipath;
	}

	// TODO
	public static String saveImg(Bitmap b, String name) throws IOException {
		
		String path = Constans.NX_RECV_SAVE_PATH;
		File mediaFile = new File(path + File.separator + name + ".jpg");
		// ����.nomedia�ļ�
		FileUtil.createFile(path, ".nomedia");

		if (mediaFile.exists()) {
			mediaFile.delete();
		}
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
		mediaFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(mediaFile);
		b.compress(Bitmap.CompressFormat.JPEG, 80, fos);
		fos.flush();
		fos.close();
		b.recycle();
		b = null;
		System.gc();
		return mediaFile.getPath();
	}

	/**
	 * MD5����
	 * 
	 * @param str
	 *            Ҫ���ܵ��ַ���
	 * @return ���ܺ���ַ���
	 */
	public static String md5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
}
