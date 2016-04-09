package com.jiqu.object;

public class SortItem {
	private String title;
	private String imgUrl;
	private int total;
	private int newAddCount;
	
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized String getImgUrl() {
		return imgUrl;
	}
	public synchronized void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public synchronized int getTotal() {
		return total;
	}
	public synchronized void setTotal(int total) {
		this.total = total;
	}
	public synchronized int getNewAddCount() {
		return newAddCount;
	}
	public synchronized void setNewAddCount(int newAddCount) {
		this.newAddCount = newAddCount;
	}

	
}
