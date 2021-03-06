package com.jiqu.object;

import java.util.Arrays;


public class CategoryInfo {
	private int status;
	private SortItem[] data;
	
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized SortItem[] getData() {
		return data;
	}
	public synchronized void setData(SortItem[] data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "CategoryInfo [status=" + status + ", data=" + Arrays.toString(data) + "]";
	}
}
