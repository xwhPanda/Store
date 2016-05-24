package com.jiqu.object;

import java.util.List;

public class ResourceInfo {
	private int status;
	private List<GameInfo> data;
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized List<GameInfo> getData() {
		return data;
	}
	public synchronized void setData(List<GameInfo> data) {
		this.data = data;
	}
}
