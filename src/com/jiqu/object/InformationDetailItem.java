package com.jiqu.object;

import java.io.Serializable;

public class InformationDetailItem implements Serializable{
	private static final long serialVersionUID = 1L;
	private String author;
	private String body;
	private String date;
	private String descript;
	private String litpic;
	private String news_id;
	private String title;
	
	public synchronized String getAuthor() {
		return author;
	}
	public synchronized void setAuthor(String author) {
		this.author = author;
	}
	public synchronized String getBody() {
		return body;
	}
	public synchronized void setBody(String body) {
		this.body = body;
	}
	public synchronized String getDate() {
		return date;
	}
	public synchronized void setDate(String date) {
		this.date = date;
	}
	public synchronized String getDescript() {
		return descript;
	}
	public synchronized void setDescript(String descript) {
		this.descript = descript;
	}
	public synchronized String getLitpic() {
		return litpic;
	}
	public synchronized void setLitpic(String litpic) {
		this.litpic = litpic;
	}
	public synchronized String getNews_id() {
		return news_id;
	}
	public synchronized void setNews_id(String news_id) {
		this.news_id = news_id;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "InformationDetailItem [author=" + author + ", body=" + body + ", date=" + date + ", descript=" + descript + ", litpic=" + litpic + ", news_id=" + news_id + ", title=" + title + "]";
	}
}
