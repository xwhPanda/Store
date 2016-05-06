package com.jiqu.object;

import java.util.Arrays;

public class RecommendDataInfo {
	private int status;
	/** 推荐幻灯 **/
	private GameInfo[] data1;
	/** 最新头条右边文字链接 **/
	private RecommendHeadlineInfo[] data2;
	/** 最新头条 **/
	private GameInfo[] data3;
	/** 最新头条下banner **/
	private GameInfo[] data4;
	/** 新游榜 **/
	private GameInfo[] data5;
	/** 热游榜 **/
	private GameInfo[] data6;
	/** 必玩榜**/
	private GameInfo[] data7;
	
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized GameInfo[] getData1() {
		return data1;
	}
	public synchronized void setData1(GameInfo[] data1) {
		this.data1 = data1;
	}
	public synchronized RecommendHeadlineInfo[] getData2() {
		return data2;
	}
	public synchronized void setData2(RecommendHeadlineInfo[] data2) {
		this.data2 = data2;
	}
	public synchronized GameInfo[] getData3() {
		return data3;
	}
	public synchronized void setData3(GameInfo[] data3) {
		this.data3 = data3;
	}
	public synchronized GameInfo[] getData4() {
		return data4;
	}
	public synchronized void setData4(GameInfo[] data4) {
		this.data4 = data4;
	}
	public synchronized GameInfo[] getData5() {
		return data5;
	}
	public synchronized void setData5(GameInfo[] data5) {
		this.data5 = data5;
	}
	public synchronized GameInfo[] getData6() {
		return data6;
	}
	public synchronized void setData6(GameInfo[] data6) {
		this.data6 = data6;
	}
	public synchronized GameInfo[] getData7() {
		return data7;
	}
	public synchronized void setData7(GameInfo[] data7) {
		this.data7 = data7;
	}
	@Override
	public String toString() {
		return "RecommendDataInfo [status=" + status + ", data1=" + Arrays.toString(data1) + ", data2=" + Arrays.toString(data2) + ", data3=" + Arrays.toString(data3) + ", data4=" + Arrays.toString(data4) + ", data5=" + Arrays.toString(data5) + ", data6=" + Arrays.toString(data6) + ", data7=" + Arrays.toString(data7) + "]";
	}
}
