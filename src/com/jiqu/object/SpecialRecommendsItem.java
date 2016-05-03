package com.jiqu.object;


public class SpecialRecommendsItem {
	private int content_id;
	private int content_type;
	private int essence;
	private int id;
	private String is_link;
	private String is_publish;
	private int pic_height;
	private String pic_url;
	private int pic_width;
	private int sort;
	private String title;
	private String url;
	
	public synchronized int getContent_id() {
		return content_id;
	}
	public synchronized void setContent_id(int content_id) {
		this.content_id = content_id;
	}
	public synchronized int getContent_type() {
		return content_type;
	}
	public synchronized void setContent_type(int content_type) {
		this.content_type = content_type;
	}
	public synchronized int getEssence() {
		return essence;
	}
	public synchronized void setEssence(int essence) {
		this.essence = essence;
	}
	public synchronized int getId() {
		return id;
	}
	public synchronized void setId(int id) {
		this.id = id;
	}
	public synchronized String getIs_link() {
		return is_link;
	}
	public synchronized void setIs_link(String is_link) {
		this.is_link = is_link;
	}
	public synchronized String getIs_publish() {
		return is_publish;
	}
	public synchronized void setIs_publish(String is_publish) {
		this.is_publish = is_publish;
	}
	public synchronized int getPic_height() {
		return pic_height;
	}
	public synchronized void setPic_height(int pic_height) {
		this.pic_height = pic_height;
	}
	public synchronized String getPic_url() {
		return pic_url;
	}
	public synchronized void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public synchronized int getPic_width() {
		return pic_width;
	}
	public synchronized void setPic_width(int pic_width) {
		this.pic_width = pic_width;
	}
	public synchronized int getSort() {
		return sort;
	}
	public synchronized void setSort(int sort) {
		this.sort = sort;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized String getUrl() {
		return url;
	}
	public synchronized void setUrl(String url) {
		this.url = url;
	}
}
