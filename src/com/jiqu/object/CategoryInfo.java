package com.jiqu.object;

import java.util.Arrays;


public class CategoryInfo {
	private int count;
	private SortItem[] item;
	
	public synchronized int getCount() {
		return count;
	}
	public synchronized void setCount(int count) {
		this.count = count;
	}
	public synchronized SortItem[] getItem() {
		return item;
	}
	public synchronized void setItem(SortItem[] item) {
		this.item = item;
	}
	
	@Override
	public String toString() {
		return "CategoryInfo [count=" + count + ", item=" + Arrays.toString(item) + "]";
	}
}
