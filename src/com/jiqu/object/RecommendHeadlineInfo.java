package com.jiqu.object;

public class RecommendHeadlineInfo {
	private String url;
	private String title;
	private String id;
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
	public synchronized String getId() {
		return id;
	}
	public synchronized void setId(String id) {
		this.id = id;
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
		return "RecommendHeadlineInfo [url=" + url + ", title=" + title + ", id=" + id + ", siteID=" + siteID + ", statisticsID=" + statisticsID + "]";
	}
}
