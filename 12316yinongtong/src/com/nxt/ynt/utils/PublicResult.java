package com.nxt.ynt.utils;

import java.io.Serializable;

public class PublicResult implements Serializable {

	private String msg;
	private String result;
	private String infoid;
	private String error;
	private String user_pic;
	private String errorcode;

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getUserpic() {
		return user_pic;
	}

	public void setUserpic(String userpic) {
		this.user_pic = userpic;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	@Override
	public String toString() {
		return "PublicResult [msg=" + msg + ", result=" + result + ", infoid="
				+ infoid + "]";
	}

}
