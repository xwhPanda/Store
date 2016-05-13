package com.jiqu.object;

import java.util.Arrays;

public class CategoryAppsInfo {
	private int status;
	private GameInfo[] data;
	
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized GameInfo[] getData() {
		return data;
	}
	public synchronized void setData(GameInfo[] data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "CategoryAppsInfo [status=" + status + ", item=" + Arrays.toString(data) + "]";
	}
}
