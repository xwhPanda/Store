package com.jiqu.object;

import java.util.List;

public class ThematicItemInfo {

	private String title;
	private List<ThematicInfo> thematicInfos;
	
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized List<ThematicInfo> getThematicInfos() {
		return thematicInfos;
	}
	public synchronized void setThematicInfos(List<ThematicInfo> thematicInfos) {
		this.thematicInfos = thematicInfos;
	}

}
