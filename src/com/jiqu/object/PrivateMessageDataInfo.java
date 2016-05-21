package com.jiqu.object;

import java.io.Serializable;

public class PrivateMessageDataInfo implements Serializable {
	private static final long serialVersionUID = 8743286491640031535L;
	private String id;
	private String title;
	private String time;
	private String content;
	private String userids;
	public synchronized String getId() {
		return id;
	}
	public synchronized void setId(String id) {
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
	public synchronized String getContent() {
		return content;
	}
	public synchronized void setContent(String content) {
		this.content = content;
	}
	public synchronized String getUserids() {
		return userids;
	}
	public synchronized void setUserids(String userids) {
		this.userids = userids;
	}
	
}
