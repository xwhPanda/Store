package com.jiqu.object;

import java.io.Serializable;

public class InformationInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int code;
	private InformationResult result;
	private String message;
	
	public synchronized int getCode() {
		return code;
	}
	public synchronized void setCode(int code) {
		this.code = code;
	}
	public synchronized InformationResult getResult() {
		return result;
	}
	public synchronized void setResult(InformationResult result) {
		this.result = result;
	}
	public synchronized String getMessage() {
		return message;
	}
	public synchronized void setMessage(String message) {
		this.message = message;
	}
	public static synchronized long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "InformationInfo [code=" + code + ", result=" + result + ", message=" + message + "]";
	}
}
