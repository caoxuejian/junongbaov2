package com.nxt.ynt.entity;

import java.io.Serializable;

public class JiHuo implements Serializable{
	private String msg;
	private String errorcode;
	private String num;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "JiHuo [msg=" + msg + ", errorcode=" + errorcode + ", num="
				+ num + "]";
	}

}
