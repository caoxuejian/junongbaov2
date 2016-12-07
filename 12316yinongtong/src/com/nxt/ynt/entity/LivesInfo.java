package com.nxt.ynt.entity;

import java.io.Serializable;

public class LivesInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7971819212165974801L;
	private String title;
	private String address;
	private String tel;
	private String img;
	
	/**
	 * ¾«¶È
	 */
	private double lat;
	/**
	 * Î³¶È
	 */
	private double lng;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public LivesInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LivesInfo(String img) {
		super();
		this.img = img;
	}

	public LivesInfo(String title, String address, String tel) {
		super();
		this.title = title;
		this.address = address;
		this.tel = tel;
	}

	public LivesInfo(String title, String address, String tel, String img,
			double lat, double lng) {
		super();
		this.title = title;
		this.address = address;
		this.tel = tel;
		this.img = img;
		this.lat = lat;
		this.lng = lng;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "LivesInfo [title=" + title + ", address=" + address + ", tel="
				+ tel + ", img=" + img + ", lat=" + lat + ", lng=" + lng + "]";
	}

}
