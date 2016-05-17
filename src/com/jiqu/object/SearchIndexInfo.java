package com.jiqu.object;

import java.io.Serializable;
import java.util.Arrays;

public class SearchIndexInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int status;
	private SearchIndexItem data;
	
	public synchronized int getStatus() {
		return status;
	}

	public synchronized void setStatus(int status) {
		this.status = status;
	}

	public synchronized SearchIndexItem getData() {
		return data;
	}

	public synchronized void setData(SearchIndexItem data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "SearchIndexInfo [status=" + status + ", data=" + data + "]";
	}

}
