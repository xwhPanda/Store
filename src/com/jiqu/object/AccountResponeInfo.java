package com.jiqu.object;

public class AccountResponeInfo {

	private int status;
	private String photo;
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
	public synchronized String getPhoto() {
		return photo;
	}
	public synchronized void setPhoto(String photo) {
		this.photo = photo;
	}
	@Override
	public String toString() {
		return "AccountResponeInfo [status=" + status + ", photo=" + photo + ", data=" + data + "]";
	}
}
