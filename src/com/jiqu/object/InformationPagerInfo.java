package com.jiqu.object;

import java.io.Serializable;
import java.util.Arrays;

public class InformationPagerInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int status;
	private InformationPagerItemInfo[] data;
	
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized InformationPagerItemInfo[] getData() {
		return data;
	}
	public synchronized void setData(InformationPagerItemInfo[] data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "InformationInfo [status=" + status + ", data=" + Arrays.toString(data) + "]";
	}
}
