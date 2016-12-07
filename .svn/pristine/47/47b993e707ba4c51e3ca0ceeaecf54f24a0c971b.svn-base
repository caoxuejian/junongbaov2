package com.nxt.nxtapp.http;


public class ToJsonUtil {
	private int jishu = 0;

	private String str;

	public void put(String a, String b) {
		if (jishu == 0) {
			str = "{";
			str = str + "\"" + a + "\"" + ":" + "\"" + b + "\"";
			jishu=1;
		} else {
			str = str + "," + "\"" + a + "\"" + ":" + "\"" + b + "\"";
		}

	}
	
	public void put2(String a, String b) {
		if (jishu == 0) {
			str = "{";
			str = str + "\"" + a + "\"" + ":" + b ;
			jishu=1;
		} else {
			str = str + "," + "\"" + a + "\"" + ":"  + b ;
		}

	}
	

	@Override
	public String toString() {
		str=str+"}";
		return str;
	}

}
