package com.jiqu.object;

import java.io.Serializable;

public class PrivateMessageInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int status;
	private PrivateMessageDataInfo[] data;
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized PrivateMessageDataInfo[] getData() {
		return data;
	}
	public synchronized void setData(PrivateMessageDataInfo[] data) {
		this.data = data;
	}
	
}
