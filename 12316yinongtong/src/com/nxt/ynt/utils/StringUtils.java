package com.nxt.ynt.utils;

import java.text.NumberFormat;

public class StringUtils {
	public static String[][] getImgIdArrByString(String paramString) {
		if (paramString.endsWith(","))
			paramString = paramString.substring(0, -1 + paramString.length());
		String[] arrayOfString1 = paramString.split(",");
		int i = arrayOfString1.length;
		String[][] arrayOfString = new String[i][];
		String[] arrayOfString2 = new String[i];
		String[] arrayOfString3 = new String[i];
		for (int j = 0; j < i; j++) {
			arrayOfString2[j] = arrayOfString1[j].split("_")[0];
			arrayOfString3[j] = arrayOfString1[j].split("_")[1];
		}
		arrayOfString[0] = arrayOfString2;
		arrayOfString[1] = arrayOfString3;
		return arrayOfString;
	}

	public static String getSendtoMinllon(String paramString, int paramInt) {
		try {
			String str1 = "";
			Double localDouble = Double.valueOf(1000.0D * Double.valueOf(
					paramString).doubleValue());
			NumberFormat localNumberFormat = NumberFormat.getInstance();
			localNumberFormat.setGroupingUsed(false);
			String str2 = localNumberFormat.format(localDouble).toString()
					.substring(0, paramInt);
			str1 = str2;
			return str1;
		} catch (Exception localException) {
			while (true) {
				localException.printStackTrace();
			}
		}
	}

	public static boolean isNotEmty(String paramString) {
		if ((paramString != null) && (!"".equals(paramString.trim()))
				&& (!"null".equals(paramString.trim())))
			return true;
		return false;
	}
	
}
