package com.jiqu.object;

import java.io.Serializable;

public class SearchIndexItemInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String content;

	public synchronized String getContent() {
		return content;
	}

	public synchronized void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "SearchIndexItemInfo [content=" + content + "]";
	}
}