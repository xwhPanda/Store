package com.jiqu.object;

import java.io.Serializable;


public class SpecialResultItemImage implements Serializable{

	private int app_id;
	private int id;
	private String pic_url;
	
	public synchronized int getApp_id() {
		return app_id;
	}
	public synchronized void setApp_id(int app_id) {
		this.app_id = app_id;
	}
	public synchronized int getId() {
		return id;
	}
	public synchronized void setId(int id) {
		this.id = id;
	}
	public synchronized String getPic_url() {
		return pic_url;
	}
	public synchronized void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	
	@Override
	public String toString() {
		return "SpecialResultItemImage [app_id=" + app_id + ", id=" + id + ", pic_url=" + pic_url + "]";
	}

}
