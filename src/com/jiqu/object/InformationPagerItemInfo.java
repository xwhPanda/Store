package com.jiqu.object;

import java.io.Serializable;

public class InformationPagerItemInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private String url;
	private String title;
	private String intro;
	private int id;
	private String pic;
	private int type;
	private String siteID;
	private String statisticsID;
	
	public synchronized String getUrl() {
		return url;
	}
	public synchronized void setUrl(String url) {
		this.url = url;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized String getIntro() {
		return intro;
	}
	public synchronized void setIntro(String intro) {
		this.intro = intro;
	}
	public synchronized int getId() {
		return id;
	}
	public synchronized void setId(int id) {
		this.id = id;
	}
	public synchronized String getPic() {
		return pic;
	}
	public synchronized void setPic(String pic) {
		this.pic = pic;
	}
	public synchronized int getType() {
		return type;
	}
	public synchronized void setType(int type) {
		this.type = type;
	}
	public synchronized String getSiteID() {
		return siteID;
	}
	public synchronized void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	public synchronized String getStatisticsID() {
		return statisticsID;
	}
	public synchronized void setStatisticsID(String statisticsID) {
		this.statisticsID = statisticsID;
	}
	
	@Override
	public String toString() {
		return "InformationItemInfo [url=" + url + ", title=" + title + ", intro=" + intro + ", id=" + id + ", pic=" + pic + ", type=" + type + ", siteID=" + siteID + ", statisticsID=" + statisticsID + "]";
	}
}
