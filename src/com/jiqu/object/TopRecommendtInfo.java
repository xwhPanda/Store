package com.jiqu.object;

import java.util.Arrays;


public class TopRecommendtInfo {

	private int count;
	private TopRecommendItem[] item;
	
	public TopRecommendtInfo() {
		
	}

	public synchronized int getCount() {
		return count;
	}

	public synchronized void setCount(int count) {
		this.count = count;
	}

	public synchronized TopRecommendItem[] getItem() {
		return item;
	}

	public synchronized void setItem(TopRecommendItem[] item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "TopRecommendtInfo [count=" + count + ", item=" + Arrays.toString(item) + "]";
	}

	
}
