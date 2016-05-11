package com.jiqu.object;

import java.io.Serializable;

public class ThematicItem implements Serializable{
	private static final long serialVersionUID = 1L;
	private String title;
	private String intro;
	private int id;
	private String pic;
	private String siteID;
	private String statisticsID;
	private String name;
	private int hot;
	private String scorce;
	private int favorite;
	private String type;
	
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
	public synchronized String getName() {
		return name;
	}
	public synchronized void setName(String name) {
		this.name = name;
	}
	public synchronized int getHot() {
		return hot;
	}
	public synchronized void setHot(int hot) {
		this.hot = hot;
	}
	public synchronized String getScorce() {
		return scorce;
	}
	public synchronized void setScorce(String scorce) {
		this.scorce = scorce;
	}
	public synchronized int getFavorite() {
		return favorite;
	}
	public synchronized void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	public synchronized String getType() {
		return type;
	}
	public synchronized void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "ThematicItem [title=" + title + ", intro=" + intro + ", id=" + id + ", pic=" + pic + ", siteID=" + siteID + ", statisticsID=" + statisticsID + ", name=" + name + ", hot=" + hot + ", scorce=" + scorce + ", favorite=" + favorite + ", type=" + type + "]";
	}
}
