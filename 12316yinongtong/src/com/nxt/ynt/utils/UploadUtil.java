package com.nxt.ynt.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import com.nxt.nxtapp.common.StreamTool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class UploadUtil {

	private static final String TAG = "uploadFile";

	private static final int TIME_OUT = 10 * 1000; // ��ʱʱ��

	private static final String CHARSET = "utf-8"; // ���ñ���
	private static String oriJson2;

	/**
	 * Android�ϴ��ļ��������
	 * 
	 * @param file
	 *            ��Ҫ�ϴ����ļ�
	 * @param RequestURL
	 *            �����rul
	 * @return ������Ӧ������
	 */
	public static String uploadFile(File file, String RequestURL) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // �߽��ʶ �������
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // ��������

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // ����������
			conn.setDoOutput(true); // ���������
			conn.setUseCaches(false); // ������ʹ�û���
			conn.setRequestMethod("POST"); // ����ʽ
			conn.setRequestProperty("Charset", CHARSET); // ���ñ���
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {
				/**
				 * ���ļ���Ϊ�գ����ļ���װ�����ϴ�
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * �����ص�ע�⣺ name�����ֵΪ�������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
				 * filename���ļ������֣�������׺���� ����:abc.png
				 */

				String NewName = file.getName().substring(
						file.getName().lastIndexOf("/") + 1);

				sb.append("Content-Disposition: form-data; name=\"file_pic\"; filename=\""
						+ NewName + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
				 */
				int res = conn.getResponseCode();
				// if(res==200)
				// {
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				// }
				// else{
				// }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ͨ��ƴ�ӵķ�ʽ�����������ݣ�ʵ�ֲ��������Լ��ļ�����
	 * 
	 * @param url
	 *            Service net address
	 * @param params
	 *            text content
	 * @param files
	 *            pictures
	 * @return String result of Service response
	 * @throws IOException
	 */
	public static boolean post(String url, Map<String, String> params,
			Map<String, File> files) throws IOException {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(10 * 1000); // ������ʱ��
		conn.setDoInput(true);// ��������
		conn.setDoOutput(true);// �������
		conn.setUseCaches(false); // ������ʹ�û���
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// ������ƴ�ı����͵Ĳ���
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// �����ļ�����
		if (files != null)
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				String NewName = file
						.getValue()
						.getName()
						.substring(
								file.getValue().getName().lastIndexOf("/") + 1);
				sb1.append("Content-Disposition: form-data; name=\"file_pic\"; filename=\""
						+ NewName + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}

		// ���������־
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// �õ���Ӧ��
		int res = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		StringBuilder sb2 = new StringBuilder();
		if (res == 200) {
			int ch;
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
			return true;
		}
		outStream.close();
		conn.disconnect();
		return false;
	}
	
	public static String posts(String url, Map<String, String> params,
			Map<String, File> files) throws IOException {
		//com.nxt.nxtapp.common.LogUtil.LogI(LAG, "1111111111111111111111");
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";// ���з� \n, �س��� \r
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		com.nxt.nxtapp.common.LogUtil.LogD("name", "�ֽ���:" + BOUNDARY);
		URL uri = new URL(url);
		com.nxt.nxtapp.common.LogUtil.LogI(TAG, "222222222222222222222222222222");
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(10 * 1000); // ������ʱ��
		conn.setDoInput(true);// ��������
		conn.setDoOutput(true);// �������
		conn.setUseCaches(false); // ������ʹ�û���
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);
		// ������ƴ�ı����͵Ĳ���
		StringBuilder sb = new StringBuilder();
		com.nxt.nxtapp.common.LogUtil.LogI(TAG, "333333333333333333333333333");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			com.nxt.nxtapp.common.LogUtil.LogI(TAG, "44444444444444444444444444444444444");
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}
		com.nxt.nxtapp.common.LogUtil.LogI(TAG, "5555555555555555555555555555555555");
		com.nxt.nxtapp.common.LogUtil.LogD("name", sb.toString());
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// �����ļ�����
		if (files != null){
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				String NewName = "/sdcard/"
						+ file.getValue()
								.getName()
								.substring(
										file.getValue().getName()
												.lastIndexOf("/") + 1);
				com.nxt.nxtapp.common.LogUtil.LogI(TAG, "5.11111111111111111111111111");
				
				sb1.append("Content-Disposition: form-data; name=\""+file.getKey()+"\"; filename=\""
						+ NewName + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				com.nxt.nxtapp.common.LogUtil.LogI(TAG, "5.222222222222222222222222222");
				outStream.write(sb1.toString().getBytes());
				com.nxt.nxtapp.common.LogUtil.LogD("name", sb1.toString());
				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				com.nxt.nxtapp.common.LogUtil.LogI(TAG, "5.33333333333333333333333333");
				while ((len = is.read(buffer)) != -1) {
					com.nxt.nxtapp.common.LogUtil.LogD("name", "�ļ����ݣ�" + buffer.toString());
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}
		}
		com.nxt.nxtapp.common.LogUtil.LogI(TAG, "5.4444444444444444444444444444");
		// ���������־
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		com.nxt.nxtapp.common.LogUtil.LogD("name", "���������־" + end_data.toString());
		outStream.write(end_data);
		outStream.flush();
		// �õ���Ӧ��
	
		int res = conn.getResponseCode();
		com.nxt.nxtapp.common.LogUtil.LogI(TAG, "code------------------------------------------>"+res+"");
		InputStream in = conn.getInputStream();
		StringBuilder sb2 = new StringBuilder();
		if (res == 200) {
			int ch;
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
			oriJson2 = sb2.toString().trim();
		}
		outStream.close();
		in.close();
		conn.disconnect();
		return oriJson2;
	}

	/**
	 * ͨ��ƴ�ӵķ�ʽ�����������ݣ�ʵ�ֲ��������Լ��ļ�����
	 * 
	 * @param url
	 *            Service net address
	 * @param params
	 *            text content
	 * @param files
	 *            pictures
	 * @return String result of Service response
	 * @throws IOException
	 */
	public static String postpics(String url, Map<String, String> params,
			Map<String, File> files) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";// ���з� \n, �س��� \r
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		com.nxt.nxtapp.common.LogUtil.LogD("name", "�ֽ���:" + BOUNDARY);
		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(10 * 1000); // ������ʱ��
		conn.setDoInput(true);// ��������
		conn.setDoOutput(true);// �������
		conn.setUseCaches(false); // ������ʹ�û���
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// ������ƴ�ı����͵Ĳ���
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}
		com.nxt.nxtapp.common.LogUtil.LogD("name", sb.toString());
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// �����ļ�����
		if (files != null)
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				String NewName = "/sdcard/"
						+ file.getValue()
								.getName()
								.substring(
										file.getValue().getName()
												.lastIndexOf("/") + 1);
				com.nxt.nxtapp.common.LogUtil.LogE("nxt", "NewName==" + NewName);
				sb1.append("Content-Disposition: form-data; name=\"imgfile\"; filename=\""
						+ NewName + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				com.nxt.nxtapp.common.LogUtil.LogD("name", sb1.toString());
				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					// com.nxt.nxtapp.common.LogUtil.LogD("name", "�ļ����ݣ�" + buffer.toString());
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}

		// ���������־
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		com.nxt.nxtapp.common.LogUtil.LogD("name", "���������־" + end_data.toString());
		outStream.write(end_data);
		outStream.flush();
		// �õ���Ӧ��
		int res = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		StringBuilder sb2 = new StringBuilder();
		if (res == 200) {
			int ch;
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
			oriJson2 = sb2.toString().trim();
		}

		outStream.close();
		in.close();
		conn.disconnect();
		return oriJson2;
	}

	public static String postheadpic(String url, Map<String, String> params,
			Drawable drawable) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";// ���з� \n, �س��� \r
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		com.nxt.nxtapp.common.LogUtil.LogD("name", "�ֽ���:" + BOUNDARY);
		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(10 * 1000); // ������ʱ��
		conn.setDoInput(true);// ��������
		conn.setDoOutput(true);// �������
		conn.setUseCaches(false); // ������ʹ�û���
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// ������ƴ�ı����͵Ĳ���
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}
		com.nxt.nxtapp.common.LogUtil.LogD("name", sb.toString());
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		if (drawable != null) {
		// �����ļ�����
		StringBuilder sb1 = new StringBuilder();
		sb1.append(PREFIX);
		sb1.append(BOUNDARY);
		sb1.append(LINEND);
		sb1.append("Content-Disposition: form-data; name=\"imgfile\"; filename=\""
				+ "abc.png" + "\"" + LINEND);
		sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET
				+ LINEND);
		sb1.append(LINEND);
		outStream.write(sb1.toString().getBytes());
		com.nxt.nxtapp.common.LogUtil.LogD("name", sb1.toString());
			Bitmap bitmap = drawable2Bitmap(drawable);
			InputStream is = Bitmap2InputStream(bitmap);

			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				// com.nxt.nxtapp.common.LogUtil.LogD("name", "�ļ����ݣ�" + buffer.toString());
				outStream.write(buffer, 0, len);
			}

			is.close();
			outStream.write(LINEND.getBytes());
		}

		// ���������־
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		com.nxt.nxtapp.common.LogUtil.LogD("name", "���������־" + end_data.toString());
		outStream.write(end_data);
		outStream.flush();
		// �õ���Ӧ��
		int res = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		StringBuilder sb2 = new StringBuilder();
		if (res == 200) {
			int ch;
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
			oriJson2 = sb2.toString().trim();
		}

		outStream.close();
		in.close();
		conn.disconnect();
		return oriJson2;
	}

	// Drawableת����Bitmap
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	// ��Bitmapת����InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}
	
	public static String getOriginalJSON(String path) {
		//com.nxt.nxtapp.common.LogUtil.LogI(LAG, "gggggggggggggggggggggggggggggggggggg");
		if (path == null) {
			return null;
		}
		URL url;
		try {
			url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(6000);
			conn.setReadTimeout(5 * 1000);
			conn.connect();
			InputStream inStream = conn.getInputStream();
			int code = conn.getResponseCode();
			//com.nxt.nxtapp.common.LogUtil.LogI(LAG, "code------------��" + code + "");
			//com.nxt.nxtapp.common.LogUtil.LogI(LAG, "inStream:" + inStream.toString());
			if (code != 200) {
				//com.nxt.nxtapp.common.LogUtil.LogI(LAG,
						//"22222222222222222222222222222222222222222222222222222");
				return null;
			}

			byte[] data = StreamTool.read(inStream);
		
                //com.nxt.nxtapp.common.LogUtil.LogI(LAG,"data----------------------->" +data.toString());
			String oriJson = new String(data);
			//com.nxt.nxtapp.common.LogUtil.LogI(LAG, "oriJson������������������������������������������" + oriJson);
			return oriJson;
		} catch (Exception e) {

			return null;
		}
	}

}
