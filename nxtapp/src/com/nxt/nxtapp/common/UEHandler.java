package com.nxt.nxtapp.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Build;
import com.nxt.nxtapp.NXTAPPApplication;

public class UEHandler implements Thread.UncaughtExceptionHandler {

	private NXTAPPApplication softApp;
	private File fileErrorLog;
	private String info;
	public static final String path = "http://219.232.243.58:82/report.php?";
	public static String product = "农信";
	private static final String TAG = "UEHandler";

	public UEHandler(NXTAPPApplication app) {
		softApp = app;
		fileErrorLog = new File(NXTAPPApplication.PATH_ERROR_LOG);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// fetch Excpetion Info
		info = null;
		ByteArrayOutputStream baos = null;
		PrintStream printStream = null;
		try {
			baos = new ByteArrayOutputStream();
			printStream = new PrintStream(baos);
			ex.printStackTrace(printStream);
			byte[] data = baos.toByteArray();
			info = new String(data);
			data = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (printStream != null) {
					printStream.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// print
		long threadId = thread.getId();
		LogUtil.LogD(TAG, "Thread.getName()=" + thread.getName() + " id="
				+ threadId + " state=" + thread.getState());
		LogUtil.LogD(TAG, "Error[" + info + "]");

		getOutputstream(path, info);
		android.os.Process.killProcess(android.os.Process.myPid());

		// if (threadId != 1) {
		// // 对于非UI线程可显示出提示界面，如果是UI线程抛的异常则界面卡死直到ANR。
		// Intent intent = new Intent(softApp, ActErrorReport.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.putExtra("error", info);
		// intent.putExtra("by", "uehandler");
		// softApp.startActivity(intent);
		// } else {
		// // write 2 /data/data/<app_package>/files/error.log
		// write2ErrorLog(fileErrorLog, info);
		// // kill App Progress
		// android.os.Process.killProcess(android.os.Process.myPid());
		// }
	}

	/**
	 * 向服务器写入错误信息
	 * 
	 * @param path
	 * @return
	 */
	public static void getOutputstream(String path, String errorInfo) {
		String shmandroid;
		String android2;
		shmandroid = Build.MODEL;
		android2 = Build.VERSION.RELEASE;
		String phonemessage = shmandroid + "||" + android2 + "||"
				+ NXTAPPApplication.versonCode + "||" + NXTAPPApplication.imei;
		String error = "product="
				+ Util.getUtil(NXTAPPApplication.getInstance()).getFromSp(
						Util.APPNAME, "") + "&errstr=" + phonemessage + "||"
				+ NXTAPPApplication.phoneNumber + "||" + "\n" + errorInfo;
		LogUtil.LogE("UEHandler", error);
		try {
			URL Url = new URL(path);
			HttpURLConnection HttpConn = (HttpURLConnection) Url
					.openConnection();

			HttpConn.setRequestMethod("POST");

			HttpConn.setReadTimeout(5000);

			HttpConn.setDoOutput(true);

			HttpConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			HttpConn.setRequestProperty("Content-Length",
					String.valueOf(error.getBytes().length));

			OutputStream os = HttpConn.getOutputStream();

			os.write(error.getBytes());

			if (HttpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				LogUtil.LogD(TAG, HttpConn.getResponseCode() + "");
			}
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void write2ErrorLog(File file, String content) {
		FileOutputStream fos = null;
		try {
			if (file.exists()) {
				// 清空之前的记录
				file.delete();
			} else {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}