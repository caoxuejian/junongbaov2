package com.nxt.ynt.entity;

import java.io.Serializable;

public class Datas implements Serializable {
	private String id;// 景点或者美食的id
	private String title;
	private String area;
	private String phone;
	private String representative;
	private String map_lat;// 纬度
	private String map_lng; // 经度
	private String srcurl;// 详细页面html5
	private String distance;
	private String space;
	private String kind;// 分类id
	private String personname;
	private String grouparea;
	private String kinds;
	private String address;
		
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPersonname() {
		return personname;
	}

	public void setPersonname(String personname) {
		this.personname = personname;
	}

	public String getGrouparea() {
		return grouparea;
	}

	public void setGrouparea(String grouparea) {
		this.grouparea = grouparea;
	}

	public String getKinds() {
		return kinds;
	}

	public void setKinds(String kinds) {
		this.kinds = kinds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getMap_lat() {
		return map_lat;
	}

	public void setMap_lat(String map_lat) {
		this.map_lat = map_lat;
	}

	public String getMap_lng() {
		return map_lng;
	}

	public void setMap_lng(String map_lng) {
		this.map_lng = map_lng;
	}

	public String getSrcurl() {
		return srcurl;
	}

	public void setSrcurl(String srcurl) {
		this.srcurl = srcurl;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

}
