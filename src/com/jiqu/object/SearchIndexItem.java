package com.jiqu.object;

import java.io.Serializable;
import java.util.Arrays;

public class SearchIndexItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private SearchIndexItemInfo[] hot;
	private SearchIndexItemInfo[] list;

	public synchronized SearchIndexItemInfo[] getHot() {
		return hot;
	}

	public synchronized void setHot(SearchIndexItemInfo[] hot) {
		this.hot = hot;
	}

	public synchronized SearchIndexItemInfo[] getList() {
		return list;
	}

	public synchronized void setList(SearchIndexItemInfo[] list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "SearchIndexItem [hot=" + Arrays.toString(hot) + ", list=" + Arrays.toString(list) + "]";
	}

}