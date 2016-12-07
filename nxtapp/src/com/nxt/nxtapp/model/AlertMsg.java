package com.nxt.nxtapp.model;
/*
 * 提示信息
 */
import java.io.Serializable;

public class AlertMsg implements Serializable{
private String alertid;
private String alertmsg;
public String getAlertid() {
	return alertid;
}
public void setAlertid(String alertid) {
	this.alertid = alertid;
}
public String getAlertmsg() {
	return alertmsg;
}
public void setAlertmsg(String alertmsg) {
	this.alertmsg = alertmsg;
}
@Override
public String toString() {
	return "AlertMsg [alertid=" + alertid + ", alertmsg=" + alertmsg + "]";
}

}
