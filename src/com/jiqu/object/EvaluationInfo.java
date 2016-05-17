package com.jiqu.object;

import java.util.Arrays;

public class EvaluationInfo {

	private int status;
	private EvaluationItemInfo[] data;
	
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized EvaluationItemInfo[] getData() {
		return data;
	}
	public synchronized void setData(EvaluationItemInfo[] data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "EvaluationInfo [status=" + status + ", data=" + Arrays.toString(data) + "]";
	}
	
}
