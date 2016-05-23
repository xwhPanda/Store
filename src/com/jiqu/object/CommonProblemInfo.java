package com.jiqu.object;

public class CommonProblemInfo {
	private int status;
	private CommonProblemItem[] data;
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized CommonProblemItem[] getData() {
		return data;
	}
	public synchronized void setData(CommonProblemItem[] data) {
		this.data = data;
	}

}
