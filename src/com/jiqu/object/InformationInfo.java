package com.jiqu.object;

import java.io.Serializable;
import java.util.Arrays;

public class InformationInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int status;
	private InformationItemInfo[] data;
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized InformationItemInfo[] getData() {
		return data;
	}
	public synchronized void setData(InformationItemInfo[] data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "InformationInfo [status=" + status + ", data=" + Arrays.toString(data) + "]";
	}
}
