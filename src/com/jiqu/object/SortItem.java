package com.jiqu.object;

import java.io.Serializable;

public class SortItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String pic;
	private int favorite;
	private String color_code;
	private String statisticsID;
	private String siteID;
	private int total;
	
	public synchronized int getId() {
		return id;
	}
	public synchronized void setId(int id) {
		this.id = id;
	}
	public synchronized String getName() {
		return name;
	}
	public synchronized void setName(String name) {
		this.name = name;
	}
	public synchronized String getPic() {
		return pic;
	}
	public synchronized void setPic(String pic) {
		this.pic = pic;
	}
	public synchronized int getFavorite() {
		return favorite;
	}
	public synchronized void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	public synchronized String getColor_code() {
		return color_code;
	}
	public synchronized void setColor_code(String color_code) {
		this.color_code = color_code;
	}
	public synchronized String getStatisticsID() {
		return statisticsID;
	}
	public synchronized void setStatisticsID(String statisticsID) {
		this.statisticsID = statisticsID;
	}
	public synchronized String getSiteID() {
		return siteID;
	}
	public synchronized void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	public synchronized int getTotal() {
		return total;
	}
	public synchronized void setTotal(int total) {
		this.total = total;
	}
	
	@Override
	public String toString() {
		return "SortItem [id=" + id + ", name=" + name + ", pic=" + pic + ", favorite=" + favorite + ", color_code=" + color_code + ", statisticsID=" + statisticsID + ", siteID=" + siteID + ", total=" + total + "]";
	}
}
