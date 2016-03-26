package com.jiqu.object;

import java.io.Serializable;

public class PrivateMessage implements Serializable {

	private static final long serialVersionUID = 8743286491640031535L;

	private String iconUrl;
	private String nickName;
	private String message;
	private String time;
	
	public synchronized String getIconUrl() {
		return iconUrl;
	}
	public synchronized void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public synchronized String getNickName() {
		return nickName;
	}
	public synchronized void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public synchronized String getMessage() {
		return message;
	}
	public synchronized void setMessage(String message) {
		this.message = message;
	}
	public synchronized String getTime() {
		return time;
	}
	public synchronized void setTime(String time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "PrivateMessage [iconUrl=" + iconUrl + ", nickName=" + nickName + ", message=" + message + ", time=" + time + "]";
	}
}
