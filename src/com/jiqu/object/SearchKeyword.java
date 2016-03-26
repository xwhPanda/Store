package com.jiqu.object;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class SearchKeyword implements Serializable {

	private static final long serialVersionUID = -7776516424655989727L;
	private String keyword;
	private int layoutType;
	private Drawable drawable;
	private int resId;
	private int backgroundType;
	
	public synchronized String getKeyword() {
		return keyword;
	}
	public synchronized void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public synchronized int getLayoutType() {
		return layoutType;
	}
	public synchronized void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}
	
	public synchronized Drawable getDrawable() {
		return drawable;
	}
	public synchronized void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	public synchronized int getResId() {
		return resId;
	}
	public synchronized void setResId(int resId) {
		this.resId = resId;
	}
	
	public synchronized int getBackgroundType() {
		return backgroundType;
	}
	public synchronized void setBackgroundType(int backgroundType) {
		this.backgroundType = backgroundType;
	}
	
	@Override
	public String toString() {
		return "SearchKeyword [keyword=" + keyword + ", layoutType=" + layoutType + ", drawable=" + drawable + ", resId=" + resId + ", backgroundType=" + backgroundType + "]";
	}
}
