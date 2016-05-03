package com.jiqu.object;

import java.io.Serializable;

public class ThematicSortInfoItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String click;
	private long id;
	private String intrduce;
	private String link;
	private String logo;
	private String packagename;
	private String size;
	private String title;
	private String versioncode;
	private String versionname;
	private int star;
	
	public synchronized String getClick() {
		return click;
	}
	public synchronized void setClick(String click) {
		this.click = click;
	}
	public synchronized long getId() {
		return id;
	}
	public synchronized void setId(long id) {
		this.id = id;
	}
	public synchronized String getIntrduce() {
		return intrduce;
	}
	public synchronized void setIntrduce(String intrduce) {
		this.intrduce = intrduce;
	}
	public synchronized String getLink() {
		return link;
	}
	public synchronized void setLink(String link) {
		this.link = link;
	}
	public synchronized String getLogo() {
		return logo;
	}
	public synchronized void setLogo(String logo) {
		this.logo = logo;
	}
	public synchronized String getPackagename() {
		return packagename;
	}
	public synchronized void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public synchronized String getSize() {
		return size;
	}
	public synchronized void setSize(String size) {
		this.size = size;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	public synchronized String getVersioncode() {
		return versioncode;
	}
	public synchronized void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}
	public synchronized String getVersionname() {
		return versionname;
	}
	public synchronized void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public synchronized int getStar() {
		return star;
	}
	public synchronized void setStar(int star) {
		this.star = star;
	}
	public static synchronized long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "ThematicSortInfo [click=" + click + ", id=" + id + ", intrduce=" + intrduce + ", link=" + link + ", logo=" + logo + ", packagename=" + packagename + ", size=" + size + ", title=" + title + ", versioncode=" + versioncode + ", versionname=" + versionname + ", star=" + star + "]";
	}
	
	public static GameInfo toGameInfo(ThematicSortInfoItem item){
		GameInfo gameInfo = new GameInfo();
		gameInfo.setAdapterType(1);
		Float size = Float.parseFloat(item.getSize());
		long sizeLong = (long) (size * 1024 * 1024);
		gameInfo.setApp_size(String.valueOf(sizeLong));
		gameInfo.setGrade_difficulty("1.2");
		gameInfo.setGrade_frames("1.2");
		gameInfo.setGrade_gameplay("1.2");
		gameInfo.setGrade_immersive("1.2");
		gameInfo.setGrade_vertigo("1.2");
		gameInfo.setLdpi_icon_url(item.logo);
		gameInfo.setName(item.getTitle());
		gameInfo.setP_id(String.valueOf(item.getId()));
		gameInfo.setPackagename(item.getPackagename());
		gameInfo.setShort_description(item.getIntrduce());
		gameInfo.setUrl(item.getLink());
		gameInfo.setVersion_code(item.getVersioncode());
		gameInfo.setVersion_name(item.getVersionname());
		
		return gameInfo;
	}
}
