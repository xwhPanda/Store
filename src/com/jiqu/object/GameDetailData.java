package com.jiqu.object;

public class GameDetailData {
	private int status;
	private GameDetailInfo data;
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized GameDetailInfo getData() {
		return data;
	}
	public synchronized void setData(GameDetailInfo data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "GameDetailData [status=" + status + ", data=" + data + "]";
	}
}
