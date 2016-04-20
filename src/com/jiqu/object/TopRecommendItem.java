package com.jiqu.object;

public class TopRecommendItem {
	private int p_id;
	private String packagename;
	private String pic;
	private int disable;

	public TopRecommendItem() {
		// TODO Auto-generated constructor stub
	}

	public synchronized int getP_id() {
		return p_id;
	}

	public synchronized void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public synchronized String getPackagename() {
		return packagename;
	}

	public synchronized void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public synchronized String getPic() {
		return pic;
	}

	public synchronized void setPic(String pic) {
		this.pic = pic;
	}

	public synchronized int getDisable() {
		return disable;
	}

	public synchronized void setDisable(int disable) {
		this.disable = disable;
	}

	
}
