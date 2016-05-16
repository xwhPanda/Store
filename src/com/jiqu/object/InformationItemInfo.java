package com.jiqu.object;

import java.io.Serializable;

public class InformationItemInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String time;
	private String title;
	private String pic;
	private String comments;
	private String url;
	private String siteID;
	private String type;
	private String statisticsID;
	public synchronized int getId() {
		return id;
	}
	public synchronized void setId(int id) {
		this.id = id;
	}
	public synchronized String getTime() {
		return time;
	}
	public synchronized void setTime(String time) {
		this.time = time;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized String getPic() {
		return pic;
	}
	public synchronized void setPic(String pic) {
		this.pic = pic;
	}
	public synchronized String getComments() {
		return comments;
	}
	public synchronized void setComments(String comments) {
		this.comments = comments;
	}
	public synchronized String getUrl() {
		return url;
	}
	public synchronized void setUrl(String url) {
		this.url = url;
	}
	public synchronized String getSiteID() {
		return siteID;
	}
	public synchronized void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	public synchronized String getType() {
		return type;
	}
	public synchronized void setType(String type) {
		this.type = type;
	}
	public synchronized String getStatisticsID() {
		return statisticsID;
	}
	public synchronized void setStatisticsID(String statisticsID) {
		this.statisticsID = statisticsID;
	}
	@Override
	public String toString() {
		return "InformationItemInfo [id=" + id + ", time=" + time + ", title=" + title + ", pic=" + pic + ", comments=" + comments + ", url=" + url + ", siteID=" + siteID + ", type=" + type + ", statisticsID=" + statisticsID + "]";
	}
	
}
