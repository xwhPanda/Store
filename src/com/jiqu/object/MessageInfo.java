package com.jiqu.object;

import java.io.Serializable;

public class MessageInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private int status;
	private MessageDataInfo[] data;
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized MessageDataInfo[] getData() {
		return data;
	}
	public synchronized void setData(MessageDataInfo[] data) {
		this.data = data;
	}

}
