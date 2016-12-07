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

	// ���������ȡ������
	public static InputStream connect(String path) {
		if (path == null) {
			return null;
		}

		HttpURLConnection uc;// connection����
		URL url;// Url����
		InputStream is = null;// �ֽ���
		try {
			url = new URL(path);// ����URL
			uc = (HttpURLConnection) url.openConnection();
			uc.connect();// �õ�����
			// �������ӳ�ʱʱ��
			uc.setConnectTimeout(6000);
			// ���ö�ȡ��ʱʱ��
			uc.setReadTimeout(3000);
			is = uc.getInputStream();// �õ����صĶ�����
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

	// MD5����
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

	// ��ȡ��������
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

	// ���ļ��ж�ȡ���ݵ���
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

	// �������������
	public static String getUpdateData(String url) {
		if (url == null) {
			return null;
		}
		// Ȼ��ȥ���������ļ�
		try {
			InputStream iStream = CacheData.connect(url);
			byte[] data = StreamTool.read(iStream);
			String upData = new String(data);
			if (upData != null) {
				// �����ȡ�����ݲ�Ϊ�գ����浽SDCard��ͬʱ����upData
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
		// ��ȡ��չ�洢�豸���ļ�Ŀ¼
		// /mlcx/htmlcache
		File SDFile = Environment.getExternalStorageDirectory();
		File file = new File(SDFile.getAbsolutePath() + File.separator
				+ "nyt/htmlcache");
		if (!file.exists() && !(file.isDirectory())) {
			file.mkdirs();
		}

		// ���ļ�
		String myFileName = SDFile.getAbsolutePath() + File.separator
				+ "ynt/htmlcache" + File.separator + md5fileName;
		String md5Content = readFileByLines(myFileName);
		// �ж��Ƿ����,�������򴴽�
		if (md5Content != null) {

			return md5Content;

		}

		return null;
	}

	// ���滺������
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

		// ���ļ�
		File myFile = new File(SDFile.getAbsolutePath() + File.separator
				+ "ynt/htmlcache" + File.separator + md5fileName);
		FileOutputStream fos = null;
		// �ж��Ƿ����,�������򴴽�
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
		// ��ȡ�������ļ�
		File[] subFiles = dir.listFiles();

		// �����������ļ�
		if (subFiles != null)
			for (File subFile : subFiles)
				if (subFile.isFile())
					subFile.delete(); // ������ļ���ɾ��
				else
					deleteAllCacheData(subFile); // ������ļ��о͵ݹ� ������������ļ�����

		// �������ļ���ɾ��֮��, ɾ����ǰ�ļ���
		dir.delete();
	}

}