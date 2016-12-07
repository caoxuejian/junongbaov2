package com.nxt.ynt.entity;
/**
 * 地区实体类
 * @author 王超
 *
 */
public class Area {
	private String xian;
	private String city;

	public Area(String xian, String city) {
		super();
		this.xian = xian;
		this.city = city;
	}

	public String getXian() {
		return xian;
	}

	public void setXian(String xian) {
		this.xian = xian;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
