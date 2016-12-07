package com.nxt.nxtapp.model;
/*
 * 上传信息返回结果类
 */
import java.io.Serializable;

public class ResultInfo implements Serializable{
	
	private String msg;//返回信息：成功、失败等
	private String result;//上传结果判断标志：0成功
	private String infoid;//返回信息id
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
		return "ResultInfo [msg=" + msg + ", result=" + result + ", infoid="
				+ infoid + "]";
	}
	

}
