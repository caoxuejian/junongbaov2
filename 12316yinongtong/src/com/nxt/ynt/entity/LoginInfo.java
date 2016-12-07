package com.nxt.ynt.entity;

/**
 * µÇÂ¼ÊµÌåÀà
 * @author ÕÔ¼ÑÀö
 *
 */
import java.io.Serializable;

public class LoginInfo implements Serializable {
	private String uid;
	private String user_name;
	private String nick;
	private String upic;
	private String hy;
	private String area;
	private String errorcode;
	private String token;
	private String sex;
	private String msg;
	private String status;
	private String groupId;
	private String groupFlag;
	private String yn_pay;
	

	public String getYn_pay() {
		return yn_pay;
	}

	public void setYn_pay(String yn_pay) {
		this.yn_pay = yn_pay;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupFlag() {
		return groupFlag;
	}

	public void setGroupFlag(String groupFlag) {
		this.groupFlag = groupFlag;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getUpic() {
		return upic;
	}

	public void setUpic(String upic) {
		this.upic = upic;
	}

	public String getHy() {
		return hy;
	}

	public void setHy(String hy) {
		this.hy = hy;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LoginInfo [uid=" + uid + ", user_name=" + user_name + ", nick="
				+ nick + ", upic=" + upic + ", hy=" + hy + ", area=" + area
				+ ", errorcode=" + errorcode + ", token=" + token + ", sex="
				+ sex + ", msg=" + msg + ", status=" + status + ", yn_pay="
				+ yn_pay + "]";
	}

	/*@Override
	public String toString() {
		return "LoginInfo [uid=" + uid + ", user_name=" + user_name + ", nick="
				+ nick + ", upic=" + upic + ", hy=" + hy + ", area=" + area
				+ ", errorcode=" + errorcode + ", token=" + token + ", sex="
				+ sex + ", msg=" + msg + ",status=" + status + "]";
	}*/
	

}
