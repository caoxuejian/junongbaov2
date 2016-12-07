package com.nxt.ynt.entity;

import java.io.Serializable;

import android.graphics.Bitmap;

public class RSSItem implements Serializable{

	private String type;
	private String lid;
	private String name;
	private String imgsrc;
	private String sourceurl;
	private String linkurl;
	private Bitmap bitmap;
	private String tidkind;
	private String gid;
	private String ver;//À¸Ä¿°æ±¾ºÅ
	
	
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getTidkind() {
		return tidkind;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public void setTidkind(String tidkind) {
		this.tidkind = tidkind;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLid() {
		return lid;
	}
	public void setLid(String lid) {
		this.lid = lid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgsrc() {
		return imgsrc;
	}
	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}
	public String getSourceurl() {
		return sourceurl;
	}
	public void setSourceurl(String sourceurl) {
		this.sourceurl = sourceurl;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	@Override
	public String toString() {
		return "RSSItem [type=" + type + ", lid=" + lid + ", name=" + name
				+ ", imgsrc=" + imgsrc + ", sourceurl=" + sourceurl
				+ ", linkurl=" + linkurl + ", bitmap=" + bitmap + ", tidkind="
				+ tidkind + ", gid=" + gid + ", ver=" + ver + "]";
	}
	
}
