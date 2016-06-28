package com.jiqu.object;

import java.io.Serializable;
import java.util.Map;

public class MessageDataInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String time;
	private String title;
	private String content;
	private String pic;
	private String url;

	private String after_open;
	private String activity;
	private Map<String, String> extra;
	
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
	public synchronized String getPic() {
		return pic;
	}
	public synchronized void setPic(String pic) {
		this.pic = pic;
	}
	public synchronized String getUrl() {
		return url;
	}
	public synchronized void setUrl(String url) {
		this.url = url;
	}
	public synchronized String getActivity() {
		return activity;
	}
	public synchronized void setActivity(String activity) {
		this.activity = activity;
	}
	public synchronized Map<String, String> getExtra() {
		return extra;
	}
	public synchronized void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}
	public synchronized String getAfter_open() {
		return after_open;
	}
	public synchronized void setAfter_open(String after_open) {
		this.after_open = after_open;
	}
}
