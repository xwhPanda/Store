package com.jiqu.object;

public class ThematicInfo {

	private String thematicImgUrl;
	private int count;
	public synchronized String getThematicImgUrl() {
		return thematicImgUrl;
	}
	public synchronized void setThematicImgUrl(String thematicImgUrl) {
		this.thematicImgUrl = thematicImgUrl;
	}
	public synchronized int getCount() {
		return count;
	}
	public synchronized void setCount(int count) {
		this.count = count;
	}

	
}
