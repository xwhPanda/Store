package com.jiqu.object;

import java.util.Arrays;

public class RecommendAppsInfo {

	private int total_size;
	private int end_position;
	private int noread;
	private GameInfo[] item;
	
	public synchronized int getTotal_size() {
		return total_size;
	}
	public synchronized void setTotal_size(int total_size) {
		this.total_size = total_size;
	}
	public synchronized int getEnd_position() {
		return end_position;
	}
	public synchronized void setEnd_position(int end_position) {
		this.end_position = end_position;
	}
	public synchronized int getNoread() {
		return noread;
	}
	public synchronized void setNoread(int noread) {
		this.noread = noread;
	}
	public synchronized GameInfo[] getItem() {
		return item;
	}
	public synchronized void setItem(GameInfo[] item) {
		this.item = item;
	}
	
	@Override
	public String toString() {
		return "RecommendAppsInfo [total_size=" + total_size + ", end_position=" + end_position + ", noread=" + noread + ", item=" + Arrays.toString(item) + "]";
	}
}
