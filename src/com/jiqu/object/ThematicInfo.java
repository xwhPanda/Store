package com.jiqu.object;

import java.util.Arrays;

public class ThematicInfo {
	private ThematicItem[] alldata;
	private int sum;
	
	public synchronized ThematicItem[] getAlldata() {
		return alldata;
	}
	public synchronized void setAlldata(ThematicItem[] alldata) {
		this.alldata = alldata;
	}
	public synchronized int getSum() {
		return sum;
	}
	public synchronized void setSum(int sum) {
		this.sum = sum;
	}
	
	@Override
	public String toString() {
		return "ThematicInfo [alldata=" + Arrays.toString(alldata) + ", sum=" + sum + "]";
	}
}
