package com.nxt.ynt.entity;

import java.io.Serializable;

public class AppConfigData implements Serializable {
	private String xnbmsg;
	private String showmsg;
	private String navtype;
	private String homebutton;
	private String meishidaolan;
	private String mainactivity;
	private String appname;
	private String updateurl;
	private String versionplist;
	private String siteid;
	private String areaid;
	private String autoRegister;
	private String menuurl;
	private String isTourist;

	public String getShowmsg() {
		return showmsg;
	}

	public void setShowmsg(String showmsg) {
		this.showmsg = showmsg;
	}

	public String getXnbmsg() {
		return xnbmsg;
	}

	public void setXnbmsg(String xnbmsg) {
		this.xnbmsg = xnbmsg;
	}

	public String getMenuurl() {
		return menuurl;
	}

	public void setMenuurl(String menuurl) {
		this.menuurl = menuurl;
	}

	public String getAutoRegister() {
		return autoRegister;
	}

	public String getIsTourist() {
		return isTourist;
	}

	public void setIsTourist(String isTourist) {
		this.isTourist = isTourist;
	}

	@Override
	public String toString() {
		return "AppConfigData [navtype=" + navtype + ", homebutton="
				+ homebutton + ", meishidaolan=" + meishidaolan
				+ ", mainactivity=" + mainactivity + ", appname=" + appname
				+ ", updateurl=" + updateurl + ", versionplist=" + versionplist
				+ ", siteid=" + siteid + ", areaid=" + areaid
				+ ", autoRegister=" + autoRegister + ", menuurl=" + menuurl
				+ ", isTourist=" + isTourist + "]";
	}

	public void setAutoRegister(String autoRegister) {
		this.autoRegister = autoRegister;
	}

	public String getNavtype() {
		return navtype;
	}

	public void setNavtype(String navtype) {
		this.navtype = navtype;
	}

	public String getHomebutton() {
		return homebutton;
	}

	public void setHomebutton(String homebutton) {
		this.homebutton = homebutton;
	}

	public String getMeishidaolan() {
		return meishidaolan;
	}

	public void setMeishidaolan(String meishidaolan) {
		this.meishidaolan = meishidaolan;
	}

	public String getMainactivity() {
		return mainactivity;
	}

	public void setMainactivity(String mainactivity) {
		this.mainactivity = mainactivity;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getUpdateurl() {
		return updateurl;
	}

	public void setUpdateurl(String updateurl) {
		this.updateurl = updateurl;
	}

	public String getVersionplist() {
		return versionplist;
	}

	public void setVersionplist(String versionplist) {
		this.versionplist = versionplist;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

}
