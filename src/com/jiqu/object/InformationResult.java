package com.jiqu.object;

import java.io.Serializable;
import java.util.Arrays;

public class InformationResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private InformationGallaryItem[] gallary;
	private int count;
	private int perpage;
	private int pageCount;
	private int page;
	private int start;
	private int more;
	private int startKey;
	private InformationGallaryItem[] data;
	public synchronized InformationGallaryItem[] getGallary() {
		return gallary;
	}
	public synchronized void setGallary(InformationGallaryItem[] gallary) {
		this.gallary = gallary;
	}
	public synchronized int getCount() {
		return count;
	}
	public synchronized void setCount(int count) {
		this.count = count;
	}
	public synchronized int getPerpage() {
		return perpage;
	}
	public synchronized void setPerpage(int perpage) {
		this.perpage = perpage;
	}
	public synchronized int getPageCount() {
		return pageCount;
	}
	public synchronized void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public synchronized int getPage() {
		return page;
	}
	public synchronized void setPage(int page) {
		this.page = page;
	}
	public synchronized int getStart() {
		return start;
	}
	public synchronized void setStart(int start) {
		this.start = start;
	}
	public synchronized int getMore() {
		return more;
	}
	public synchronized void setMore(int more) {
		this.more = more;
	}
	public synchronized int getStartKey() {
		return startKey;
	}
	public synchronized void setStartKey(int startKey) {
		this.startKey = startKey;
	}
	public synchronized InformationGallaryItem[] getData() {
		return data;
	}
	public synchronized void setData(InformationGallaryItem[] data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "InformationResult [gallary=" + Arrays.toString(gallary) + ", count=" + count + ", perpage=" + perpage + ", pageCount=" + pageCount + ", page=" + page + ", start=" + start + ", more=" + more + ", startKey=" + startKey + ", data=" + Arrays.toString(data) + "]";
	}
	
	
}
