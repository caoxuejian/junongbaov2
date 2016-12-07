package com.nxt.ynt.entity;

import java.io.Serializable;

public class NewPicture implements Serializable {
	private String adImg;
	private String time;

	public String getAdImg() {
		return adImg;
	}

	public void setAdImg(String adImg) {
		this.adImg = adImg;
	}
	
	

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "NewPicture [adImg=" + adImg + ", time=" + time + "]";
	}

	

}
