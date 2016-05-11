package com.jiqu.object;

import java.io.Serializable;
import java.util.Arrays;

public class SpecialInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int status;
	private int total;
	/** 广告位数据 **/
	private GameInfo[] data1;
	/** 精品数据 **/
	private GameInfo[] data2;
	
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized int getTotal() {
		return total;
	}
	public synchronized void setTotal(int total) {
		this.total = total;
	}
	public synchronized GameInfo[] getData1() {
		return data1;
	}
	public synchronized void setData1(GameInfo[] data1) {
		this.data1 = data1;
	}
	public synchronized GameInfo[] getData2() {
		return data2;
	}
	public synchronized void setData2(GameInfo[] data2) {
		this.data2 = data2;
	}
	
	@Override
	public String toString() {
		return "SpecialInfo [status=" + status + ", total=" + total + ", data1=" + Arrays.toString(data1) + ", data2=" + Arrays.toString(data2) + "]";
	}
}
