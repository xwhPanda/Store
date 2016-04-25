package com.jiqu.object;

import java.util.Arrays;

public class SearchInfo {
	private String text;
	private int total_size;
	private GameInfo[] item;
	
	public synchronized String getText() {
		return text;
	}
	public synchronized void setText(String text) {
		this.text = text;
	}
	public synchronized int getTotal_size() {
		return total_size;
	}
	public synchronized void setTotal_size(int total_size) {
		this.total_size = total_size;
	}
	public synchronized GameInfo[] getItem() {
		return item;
	}
	public synchronized void setItem(GameInfo[] item) {
		this.item = item;
	}
	
	@Override
	public String toString() {
		return "SearchInfo [text=" + text + ", total_size=" + total_size + ", item=" + Arrays.toString(item) + "]";
	}
	
}
