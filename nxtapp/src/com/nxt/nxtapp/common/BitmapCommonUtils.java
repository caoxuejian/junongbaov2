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
	 * 获取可以使用的缓存目录
	 * 
	 * @param context
	 * @param uniqueName
	 *            目录名称
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
	 * 获取bitmap的字节大小
	 * 
	 * @param bitmap
	 * @return
	 */
	public static int getBitmapSize(Bitmap bitmap) {
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * 获取程序外部的缓存目录
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
	 * 获取程序内部的缓存目录
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
	 * 获取文件路径空间大小
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
					"获取 sdcard 缓存大小 出错，请查看AndroidManifest.xml 是否添加了sdcard的访问权限");
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * 改变文件夹权限
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
