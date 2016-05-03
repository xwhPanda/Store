package com.jiqu.object;

import java.util.Arrays;

public class ThematicSortInfo {
	private String click;
	private String focus;
	private String intrduce;
	private String title;
	private int sum;
	private ThematicSortInfoItem[] alldata;
	
	public synchronized String getClick() {
		return click;
	}
	public synchronized void setClick(String click) {
		this.click = click;
	}
	public synchronized String getFocus() {
		return focus;
	}
	public synchronized void setFocus(String focus) {
		this.focus = focus;
	}
	public synchronized String getIntrduce() {
		return intrduce;
	}
	public synchronized void setIntrduce(String intrduce) {
		this.intrduce = intrduce;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized int getSum() {
		return sum;
	}
	public synchronized void setSum(int sum) {
		this.sum = sum;
	}
	public synchronized ThematicSortInfoItem[] getAlldata() {
		return alldata;
	}
	public synchronized void setAlldata(ThematicSortInfoItem[] alldata) {
		this.alldata = alldata;
	}
	
	@Override
	public String toString() {
		return "ThematicSortInfo [click=" + click + ", focus=" + focus + ", intrduce=" + intrduce + ", title=" + title + ", sum=" + sum + ", alldata=" + Arrays.toString(alldata) + "]";
	}
}
