package com.nxt.ynt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.*;

import com.nxt.nxtapp.common.StreamTool;

import android.os.Environment;

public class CacheData {
	static String md5fileName;

	// 连接网络获取输入流
	public static InputStream connect(String path) {
		if (path == null) {
			return null;
		}

		HttpURLConnection uc;// connection对象
		URL url;// Url对象
		InputStream is = null;// 字节流
		try {
			url = new URL(path);// 创建URL
			uc = (HttpURLConnection) url.openConnection();
			uc.connect();// 得到链接
			// 设置连接超时时间
			uc.setConnectTimeout(6000);
			// 设置读取超时时间
			uc.setReadTimeout(3000);
			is = uc.getInputStream();// 得到返回的对象流
			if (uc.getResponseCode() != 200) {

				return null;
			}
			// uc.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

			return null;
		}

		return is;
	}

	// MD5加密
	public static String MD5(String str) {
		if (str == null) {
			return null;
		}
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

	// 获取缓存数据
	public static String getCacheData(String url) {
		if (url == null) {
			return null;
		}
		md5fileName = CacheData.MD5(url);
		String cacheContent = getFileContent(md5fileName);

		if (cacheContent != null) {
			return cacheContent;
		} else {
			return null;
		}
	}

	// 从文件中读取内容的类
	public static String readFileByLines(String fileName) {
		if (fileName == null) {
			return null;
		}
		try {
			File file = new File(fileName);
			if (!file.exists() || file.length() == 0) {
				com.nxt.nxtapp.common.LogUtil.syso("+++++++++++++++++++++++++" + file.length());
				return null;
			}
			FileInputStream fInputStream = new FileInputStream(file);
			byte[] data = StreamTool.read(fInputStream);

			String aString = new String(data);

			return aString;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	// 从网络更新数据
	public static String getUpdateData(String url) {
		if (url == null) {
			return null;
		}
		// 然后去网络下载文件
		try {
			InputStream iStream = CacheData.connect(url);
			byte[] data = StreamTool.read(iStream);
			String upData = new String(data);
			if (upData != null) {
				// 如果获取的数据不为空，保存到SDCard上同时返回upData
				saveCacheData(upData, url);
				return upData;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getFileContent(String md5fileName) {
		if (md5fileName == null) {
			return null;
		}
		String myFile = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			myFile = getMd5File(md5fileName);

		}
		return myFile;
	}

	private static String getMd5File(String md5fileName) {
		if (md5fileName == null) {
			return null;
		}
		// 获取扩展存储设备的文件目录
		// /mlcx/htmlcache
		File SDFile = Environment.getExternalStorageDirectory();
		File file = new File(SDFile.getAbsolutePath() + File.separator
				+ "nyt/htmlcache");
		if (!file.exists() && !(file.isDirectory())) {
			file.mkdirs();
		}

		// 打开文件
		String myFileName = SDFile.getAbsolutePath() + File.separator
				+ "ynt/htmlcache" + File.separator + md5fileName;
		String md5Content = readFileByLines(myFileName);
		// 判断是否存在,不存在则创建
		if (md5Content != null) {

			return md5Content;

		}

		return null;
	}

	// 保存缓存数据
	public static void saveCacheData(String ucacheData, String url) {
		if (ucacheData == null || url == null) {
			return;
		}
		md5fileName = MD5(url);

		File SDFile = android.os.Environment.getExternalStorageDirectory();
		File file = new File(SDFile.getAbsolutePath() + File.separator
				+ "ynt/htmlcache");
		if (!file.exists() && !(file.isDirectory())) {
			file.mkdirs();
		}

		// 打开文件
		File myFile = new File(SDFile.getAbsolutePath() + File.separator
				+ "ynt/htmlcache" + File.separator + md5fileName);
		FileOutputStream fos = null;
		// 判断是否存在,不存在则创建
		if (myFile.exists()) {
			myFile.delete();
			try {

				myFile.createNewFile();
				fos = new FileOutputStream(myFile);
				fos.write(ucacheData.getBytes());
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
				fos.write(ucacheData.getBytes());
				fos.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public  static void deleteAllCacheData(File dir) throws IOException {
		// 获取所有子文件
		File[] subFiles = dir.listFiles();

		// 遍历所有子文件
		if (subFiles != null)
			for (File subFile : subFiles)
				if (subFile.isFile())
					subFile.delete(); // 如果是文件就删除
				else
					deleteAllCacheData(subFile); // 如果是文件夹就递归 这里进入了子文件夹中

		// 所有子文件都删除之后, 删除当前文件夹
		dir.delete();
	}

}