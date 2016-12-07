package com.nxt.nxtapp.json;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.StreamTool;
import com.nxt.nxtapp.common.WaperObject;
import com.nxt.nxtapp.model.Version;

/*
 * json���ݽ�����
 */
public class JsonPaser {

	public static final int ARRAY_ = 1000;// �������ͣ�����
	public static final int OBIECT_ = 1001;// �������ͣ�����

	/**
	 * ����json����
	 * 
	 * @param clazz
	 *            ��װ��
	 * @param jsonStr
	 *            json����
	 * @return ���ݼ���
	 */
	@SuppressLint("NewApi")
	public static List getArrayDatas(Class<? extends Serializable> clazz,
			String jsonStr) {
		List list = new ArrayList();
		if (jsonStr == null || jsonStr.equals(""))
			return list;
		try {
			parseArray(clazz, jsonStr, list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String getCOONJsonString(String path) {
		String jsonString = null;
		try {
			jsonString = getOriginalJSON(path);
			if (jsonString != null) {
				return jsonString;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * �ӷ��������ص������н�������ԭʼ��json�ַ���
	 * 
	 * @param path
	 * @return
	 */
	public static String getOriginalJSON(String path) {
		URL url;
		try {
			url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(6000);
			conn.connect();
			conn.setReadTimeout(10 * 1000);
			InputStream inStream = conn.getInputStream();
			if (conn.getResponseCode() != 200) {
				return null;
			}
			byte[] data = StreamTool.read(inStream);
			String oriJson = new String(data);
			com.nxt.nxtapp.common.LogUtil.LogD("Json", "oriJson" + oriJson);
			return oriJson;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ����json����
	 * 
	 * @param clazz
	 *            ��װ��
	 * @param jsonStr
	 *            json����
	 * @return ���ݼ���
	 */
	@SuppressLint("NewApi")
	public static Object getObjectDatas(Class<? extends Serializable> clazz,
			String jsonStr) {
		Object object = null;
		if (jsonStr == null || jsonStr.equals(""))
			return object;
		try {

			object = parseObject(clazz, jsonStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * ����json����--��������
	 * 
	 * @param clazz
	 *            ��װ��
	 * @param jsonStr
	 *            json����
	 * @param list
	 * 
	 */
	public static Object parseObject(Class<? extends Serializable> clazz,
			String jsonStr) throws JSONException {
		Object object = null;
		// LogUtil.LogE("JsonPaser", jsonStr.substring(917,950));
		JSONObject job = new JSONObject(jsonStr);
		object = setValue(clazz, job);
		return object;

	}

	/**
	 * ����json����--��������
	 * 
	 * @param clazz
	 *            ��װ��
	 * @param jsonStr
	 *            json����
	 * @param list
	 * 
	 */
	public static void parseArray(Class<? extends Serializable> clazz,
			String jsonStr, List list) throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonStr);
		Object object = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject job = jsonArray.getJSONObject(i);
			object = setValue(clazz, job);
			list.add(object);
		}
	}

	/**
	 * ��װ�ำֵ
	 * 
	 * @param clazz
	 *            ��װ��
	 * @param JSONObject
	 * @return
	 */
	private static Object setValue(Class<? extends Serializable> clazz,
			JSONObject job) {
		Object aclass = WaperObject.getObject(clazz);
		Field[] fil = clazz.getDeclaredFields();

		for (int i = 0; i < fil.length; i++) {
			Object o = null;
			try {
				if (job.has(fil[i].getName()))
					o = job.getString(fil[i].getName());
			} catch (Exception e) {
				e.printStackTrace();
				o = null;
			}
			if (o == null)
				continue;
			try {
				WaperObject
						.setFieldValue(chanagType(o, fil[i]), fil[i], aclass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return aclass;
	}

	/**
	 * ��Oracle��ֱ��ȡ�����Ķ���o�����ͣ�������Ҫת����POJO�����ֶ�field�ʺϵ����� ����ֱ�������ת��
	 * BigDecimal--->int ,INTEGER BigDecimal--->double,Double
	 * BigDecimal--->float,Float BigDecimal--->long,Long
	 * Timestamp--->java.util.Date
	 * 
	 * @param o
	 * @param field
	 * @return
	 */
	private static Object chanagType(Object o, Field field) {
		if (o == null)
			return null;
		Class o_class = o.getClass();

		Class field_class = null;
		String field_name = "";
		if (field != null) {
			field_class = field.getType();
			field_name = field.getType().getName();
		}
		if (o_class.equals(BigDecimal.class)) {
			if (field_name.equals("int") || Integer.class.equals(field_class))
				return ((BigDecimal) o).intValue();
			else if (field_name.equals("double")
					|| Double.class.equals(field_class))
				return ((BigDecimal) o).doubleValue();
			else if (field_name.equals("float")
					|| Float.class.equals(field_class))
				return ((BigDecimal) o).floatValue();
			else if (field_name.equals("long")
					|| Long.class.equals(field_class))
				return ((BigDecimal) o).longValue();
			else
				return ((BigDecimal) o).longValue();
		} else if (o_class.equals(Timestamp.class))
			return new Date(((Timestamp) o).getTime());

		return o;
	}

	public static ArrayList<String> getpics_src(String jsonstr) {
		ArrayList<String> pics = new ArrayList<String>();
		if (jsonstr == null)
			return pics;
		try {
			JSONArray jsonArray = new JSONArray(jsonstr);
			for (int i = 0; i < jsonArray.length(); i++) {
				pics.add((String) jsonArray.get(i));
				LogUtil.LogI("jsonPaser", "pic:" + pics.get(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return pics;
	}

	/**
	 * ��ȡ�汾���µ�����
	 * 
	 * @param versionString
	 * @return
	 */
	public static Version getVersionContent(String versionString) {
		if (versionString == null) {
			return null;
		}
		Version vs = new Version();
		String versioncode;
		String updatecontent;
		try {
			JSONObject job = new JSONObject(versionString);
			versioncode = job.getString("versioncode");
			updatecontent = job.getString("updatecontent");
			vs.setVersioncode(versioncode);
			vs.setUpdatecontent(updatecontent);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return vs;
	}

}
