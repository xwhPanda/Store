package com.jiqu.object;

import java.io.Serializable;

public class InformationGallaryItem implements Serializable{
	private static final long serialVersionUID = 1L;
	private String news_id;
	private String gid;
	private String title;
	private String img;
	private String gallaryImg;
	private String desc;
	
	public synchronized String getNews_id() {
		return news_id;
	}
	public synchronized void setNews_id(String news_id) {
		this.news_id = news_id;
	}
	public synchronized String getGid() {
		return gid;
	}
	public synchronized void setGid(String gid) {
		this.gid = gid;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized String getImg() {
		return img;
	}
	public synchronized void setImg(String img) {
		this.img = img;
	}
	public synchronized String getGallaryImg() {
		return gallaryImg;
	}
	public synchronized void setGallaryImg(String gallaryImg) {
		this.gallaryImg = gallaryImg;
	}
	public synchronized String getDesc() {
		return desc;
	}
	public synchronized void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return "InformationGallaryItem [news_id=" + news_id + ", gid=" + gid + ",title=" + title + ", img=" + img + ", gallaryImg=" + gallaryImg + ", desc=" + desc + "]";
	}
}
