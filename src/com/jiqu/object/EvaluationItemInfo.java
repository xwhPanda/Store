package com.jiqu.object;

import java.io.Serializable;

public class EvaluationItemInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String title;
	private String time;
	private String pic;
	private String chpic;
	private String descript;
	private String gtop;
	private String url;
	private String siteID;
	private String statisticsID;
	private String type;
	public synchronized int getId() {
		return id;
	}
	public synchronized void setId(int id) {
		this.id = id;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized String getTime() {
		return time;
	}
	public synchronized void setTime(String time) {
		this.time = time;
	}
	public synchronized String getPic() {
		return pic;
	}
	public synchronized void setPic(String pic) {
		this.pic = pic;
	}
	public synchronized String getChpic() {
		return chpic;
	}
	public synchronized void setChpic(String chpic) {
		this.chpic = chpic;
	}
	public synchronized String getDescript() {
		return descript;
	}
	public synchronized void setDescript(String descript) {
		this.descript = descript;
	}
	public synchronized String getGtop() {
		return gtop;
	}
	public synchronized void setGtop(String gtop) {
		this.gtop = gtop;
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
	public synchronized String getStatisticsID() {
		return statisticsID;
	}
	public synchronized void setStatisticsID(String statisticsID) {
		this.statisticsID = statisticsID;
	}
	public synchronized String getType() {
		return type;
	}
	public synchronized void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "EvaluationInfo [id=" + id + ", title=" + title + ", time=" + time + ", pic=" + pic + ", chpic=" + chpic + ", descript=" + descript + ", gtop=" + gtop + ", url=" + url + ", siteID=" + siteID + ", statisticsID=" + statisticsID + ", type=" + type + "]";
	}
}
