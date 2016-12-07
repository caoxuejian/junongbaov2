package com.nxt.ynt.entity;

import java.io.Serializable;


public class WebviewDate implements Serializable{

	private String gongqiu;//获取供求信息
	public String getRelease() {
		return release;
	}
	public void setRelease(String release) {
		this.release = release;
	}
	private String price;
	private String poi;//
	private String query;//查猪病
	private String consultation;//资讯
	private String capture;//查真伪
	private String release;//发供求
	private String video;//视频
	
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getGongqiu() {
		return gongqiu;
	}
	public void setGongqiu(String gongqiu) {
		this.gongqiu = gongqiu;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPoi() {
		return poi;
	}
	public void setPoi(String poi) {
		this.poi = poi;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getConsultation() {
		return consultation;
	}
	public void setConsultation(String consultation) {
		this.consultation = consultation;
	}
	public String getCapture() {
		return capture;
	}
	public void setCapture(String capture) {
		this.capture = capture;
	}
	@Override
	public String toString() {
		return "WebviewDate [gongqiu=" + gongqiu + ", price=" + price
				+ ", poi=" + poi + ", query=" + query + ", consultation="
				+ consultation + ", capture=" + capture + ", release="
				+ release + ", video=" + video + "]";
	}
	
}
