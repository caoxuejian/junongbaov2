package com.nxt.nxtapp.common;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class BitmapCommonUtils {

	private static final String TAG = "BitmapCommonUtils";

	/**
	 * ��ȡ����ʹ�õĻ���Ŀ¼
	 * 
	 * @param context
	 * @param uniqueName
	 *            Ŀ¼����
	 * @return
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(context)
				.getPath() : getPhoneCacheDir(context).getPath();

		File file = new File(cachePath + File.separator + uniqueName);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (!file.canWrite()) {
			chmod("777", cachePath);
			// file.setWritable(true);
		}
		return file;
	}

	/**
	 * ��ȡbitmap���ֽڴ�С
	 * 
	 * @param bitmap
	 * @return
	 */
	public static int getBitmapSize(Bitmap bitmap) {
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * ��ȡ�����ⲿ�Ļ���Ŀ¼
	 * 
	 * @param context
	 * @return
	 */
	public static File getExternalCacheDir(Context context) {
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	/**
	 * ��ȡ�����ڲ��Ļ���Ŀ¼
	 * 
	 * @param context
	 * @return
	 */
	public static File getPhoneCacheDir(Context context) {
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(context.getCacheDir().getPath() + cacheDir);
	}

	/**
	 * ��ȡ�ļ�·���ռ��С
	 * 
	 * @param path
	 * @return
	 */
	public static long getUsableSpace(File path) {
		try {
			final StatFs stats = new StatFs(path.getPath());
			return (long) stats.getBlockSize()
					* (long) stats.getAvailableBlocks();
		} catch (Exception e) {
			com.nxt.nxtapp.common.LogUtil.LogE(TAG,
					"��ȡ sdcard �����С ������鿴AndroidManifest.xml �Ƿ������sdcard�ķ���Ȩ��");
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * �ı��ļ���Ȩ��
	 * 
	 * @param permission
	 * @param path
	 */
	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
