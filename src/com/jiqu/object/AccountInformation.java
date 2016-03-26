package com.jiqu.object;

import java.io.Serializable;

public class AccountInformation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String value;

	public AccountInformation() {
		// TODO Auto-generated constructor stub
	}

	public synchronized String getTitle() {
		return title;
	}

	public synchronized void setTitle(String title) {
		this.title = title;
	}

	public synchronized String getValue() {
		return value;
	}

	public synchronized void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "AccountInformation [title=" + title + ", value=" + value + "]";
	}

}
