package com.jiqu.object;

import java.util.Arrays;


public class SpecialInfo {

	private int count;
	private String next;
	private String previous;
	private SpecialRecommendsItem[] recommends;
	private SpecialResultsItem[] results;
	
	public synchronized int getCount() {
		return count;
	}
	public synchronized void setCount(int count) {
		this.count = count;
	}
	public synchronized String getNext() {
		return next;
	}
	public synchronized void setNext(String next) {
		this.next = next;
	}
	public synchronized String getPrevious() {
		return previous;
	}
	public synchronized void setPrevious(String previous) {
		this.previous = previous;
	}
	public synchronized SpecialRecommendsItem[] getRecommends() {
		return recommends;
	}
	public synchronized void setRecommends(SpecialRecommendsItem[] recommends) {
		this.recommends = recommends;
	}
	public synchronized SpecialResultsItem[] getResults() {
		return results;
	}
	public synchronized void setResults(SpecialResultsItem[] results) {
		this.results = results;
	}
	
	@Override
	public String toString() {
		return "SpecialInfo [count=" + count + ", next=" + next + ", previous=" + previous + ", recommends=" + Arrays.toString(recommends) + ", results=" + Arrays.toString(results) + "]";
	}
}
