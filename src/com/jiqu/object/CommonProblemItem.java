package com.jiqu.object;

import java.io.Serializable;

public class CommonProblemItem implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String content;
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
	public synchronized String getContent() {
		return content;
	}
	public synchronized void setContent(String content) {
		this.content = content;
	}
	
}
