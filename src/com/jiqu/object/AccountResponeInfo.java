package com.jiqu.object;

public class AccountResponeInfo {

	private int status;
	private AccountInformation data;
	public synchronized int getStatus() {
		return status;
	}
	
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized AccountInformation getData() {
		return data;
	}
	public synchronized void setData(AccountInformation data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "AccountResponeInfo [status=" + status + ", data=" + data + "]";
	}
	
}
