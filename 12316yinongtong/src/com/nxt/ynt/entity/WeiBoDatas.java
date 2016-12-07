package com.nxt.ynt.entity;

import java.io.Serializable;
import java.util.List;

import com.nxt.ynt.utils.Picture;


public class WeiBoDatas implements Serializable{
private String uname;
private String nick;
private String pubtime;
private String address;
private String content;
private String zhuanfanum;
private String punglunnum;
private String zanshunum;
private String zanflag;
private String recount;
private String s_pics;
private String l_pics;
private String id;
private String uid;
private String lat;
private String lng;
private String upic;
private List<Picture> mlist;


public String getNick() {
	return nick;
}
public void setNick(String nick) {
	this.nick = nick;
}
public List<Picture> getMlist() {
	return mlist;
}
public void setMlist(List<Picture> mlist) {
	this.mlist = mlist;
}
public String getUpic() {
	return upic;
}
public void setUpic(String upic) {
	this.upic = upic;
}
public String getRecount() {
	return recount;
}
public void setRecount(String recount) {
	this.recount = recount;
}
public String getS_pics() {
	return s_pics;
}
public void setS_pics(String s_pics) {
	this.s_pics = s_pics;
}
public String getL_pics() {
	return l_pics;
}
public void setL_pics(String l_pics) {
	this.l_pics = l_pics;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getUid() {
	return uid;
}
public void setUid(String uid) {
	this.uid = uid;
}
public String getLat() {
	return lat;
}
public void setLat(String lat) {
	this.lat = lat;
}
public String getLng() {
	return lng;
}
public void setLng(String lng) {
	this.lng = lng;
}

public String getUname() {
	return uname;
}
public void setUname(String uname) {
	this.uname = uname;
}
public String getPubtime() {
	return pubtime;
}
public void setPubtime(String pubtime) {
	this.pubtime = pubtime;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getZhuanfanum() {
	return zhuanfanum;
}
public void setZhuanfanum(String zhuanfanum) {
	this.zhuanfanum = zhuanfanum;
}
public String getPunglunnum() {
	return punglunnum;
}
public void setPunglunnum(String punglunnum) {
	this.punglunnum = punglunnum;
}
public String getZanshunum() {
	return zanshunum;
}
public void setZanshunum(String zanshunum) {
	this.zanshunum = zanshunum;
}
public String getZanflag() {
	return zanflag;
}
public void setZanflag(String zanflag) {
	this.zanflag = zanflag;
}
/*@Override
public String toString() {
	return "WeiBoDatas [uname=" + uname + ", time=" + pubtime + ", address="
			+ address + ", content=" + content + ", zhuanfanum=" + zhuanfanum
			+ ", punglunnum=" + punglunnum + ", zanshunum=" + zanshunum
			+ ", zanflag=" + zanflag + ", recount=" + recount + ", s_pics="
			+ s_pics + ", l_pics=" + l_pics + ", id=" + id + ", uid=" + uid
			+ ", lat=" + lat + ", lng=" + lng + ", upic=" + upic + "]";
}*/
@Override
public String toString() {
	return "WeiBoDatas [uname=" + uname + ", nick=" + nick + ", pubtime="
			+ pubtime + ", address=" + address + ", content=" + content
			+ ", zhuanfanum=" + zhuanfanum + ", punglunnum=" + punglunnum
			+ ", zanshunum=" + zanshunum + ", zanflag=" + zanflag
			+ ", recount=" + recount + ", s_pics=" + s_pics + ", l_pics="
			+ l_pics + ", id=" + id + ", uid=" + uid + ", lat=" + lat
			+ ", lng=" + lng + ", upic=" + upic + ", mlist=" + mlist + "]";
}


}
