package com.jiqu.object;

public class SortItem {
	private int category_id;
	private String category_name;
	private String icon_url;
	private String ldpi_icon_url;
	private int app_count;
	
	public synchronized int getCategory_id() {
		return category_id;
	}
	public synchronized void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public synchronized String getCategory_name() {
		return category_name;
	}
	public synchronized void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public synchronized String getIcon_url() {
		return icon_url;
	}
	public synchronized void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public synchronized String getLdpi_icon_url() {
		return ldpi_icon_url;
	}
	public synchronized void setLdpi_icon_url(String ldpi_icon_url) {
		this.ldpi_icon_url = ldpi_icon_url;
	}
	public synchronized int getApp_count() {
		return app_count;
	}
	public synchronized void setApp_count(int app_count) {
		this.app_count = app_count;
	}
	
	@Override
	public String toString() {
		return "SortItem [category_id=" + category_id + ", category_name=" + category_name + ", icon_url=" + icon_url + ", ldpi_icon_url=" + ldpi_icon_url + ", app_count=" + app_count + "]";
	}
}
