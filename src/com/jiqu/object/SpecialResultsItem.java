package com.jiqu.object;

import java.io.Serializable;
import java.util.Arrays;


public class SpecialResultsItem implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private int cat;
	private String desc;
	private int downloads;
	private int essence;
	private long file_size;
	private String file_url;
	private int id;
	private SpecialResultItemImage[] images;
	private String name;
	private String package_name;
	private String pic_url;
	private int version_code;
	private String version_name;
	public synchronized int getCat() {
		return cat;
	}
	public synchronized void setCat(int cat) {
		this.cat = cat;
	}
	public synchronized String getDesc() {
		return desc;
	}
	public synchronized void setDesc(String desc) {
		this.desc = desc;
	}
	public synchronized int getDownloads() {
		return downloads;
	}
	public synchronized void setDownloads(int downloads) {
		this.downloads = downloads;
	}
	public synchronized int getEssence() {
		return essence;
	}
	public synchronized void setEssence(int essence) {
		this.essence = essence;
	}
	public synchronized long getFile_size() {
		return file_size;
	}
	public synchronized void setFile_size(long file_size) {
		this.file_size = file_size;
	}
	public synchronized String getFile_url() {
		return file_url;
	}
	public synchronized void setFile_url(String file_url) {
		this.file_url = file_url;
	}
	public synchronized int getId() {
		return id;
	}
	public synchronized void setId(int id) {
		this.id = id;
	}
	public synchronized SpecialResultItemImage[] getImages() {
		return images;
	}
	public synchronized void setImages(SpecialResultItemImage[] images) {
		this.images = images;
	}
	public synchronized String getName() {
		return name;
	}
	public synchronized void setName(String name) {
		this.name = name;
	}
	public synchronized String getPackage_name() {
		return package_name;
	}
	public synchronized void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public synchronized String getPic_url() {
		return pic_url;
	}
	public synchronized void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public synchronized int getVersion_code() {
		return version_code;
	}
	public synchronized void setVersion_code(int version_code) {
		this.version_code = version_code;
	}
	public synchronized String getVersion_name() {
		return version_name;
	}
	public synchronized void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	@Override
	public String toString() {
		return "SpecialResultsItem [cat=" + cat + ", desc=" + desc + ", downloads=" + downloads + ", essence=" + essence + ", file_size=" + file_size + ", file_url=" + file_url + ", id=" + id + ", images=" + Arrays.toString(images) + ", name=" + name + ", package_name=" + package_name + ", pic_url=" + pic_url + ", version_code=" + version_code + ", version_name=" + version_name + "]";
	}
	
	public static GameInfo toGameInfo(SpecialResultsItem item){
		GameInfo gameInfo = new GameInfo();
//		gameInfo.setAdapterType(1);
//		gameInfo.setApp_size(String.valueOf(item.getFile_size()));
//		gameInfo.setDownload_count(String.valueOf(item.getDownloads()));
//		gameInfo.setGrade_difficulty("1");
//		gameInfo.setGrade_frames("1");
//		gameInfo.setGrade_gameplay("1");
//		gameInfo.setGrade_immersive("1");
//		gameInfo.setGrade_vertigo("1");
//		gameInfo.setName(item.getName());
//		gameInfo.setLdpi_icon_url(item.getPic_url());
//		gameInfo.setP_id(String.valueOf(item.getId()));
//		gameInfo.setPackagename(item.getPackage_name());
//		gameInfo.setShort_description(item.getDesc());
//		gameInfo.setUrl(item.getFile_url());
//		gameInfo.setVersion_code(String.valueOf(item.getVersion_code()));
//		gameInfo.setVersion_name(item.getVersion_name());
		return gameInfo;
	}
	
	public static GameDetailInfo toDetailInfo(SpecialResultsItem item){
		GameDetailInfo gameInfo = new GameDetailInfo();
		gameInfo.setApp_size(String.valueOf(item.getFile_size()));
		gameInfo.setDownload_count(String.valueOf(item.getDownloads()));
		gameInfo.setGrade_difficulty("1");
		gameInfo.setGrade_frames("1");
		gameInfo.setGrade_gameplay("1");
		gameInfo.setGrade_immersive("1");
		gameInfo.setGrade_vertigo("1");
		gameInfo.setName(item.getName());
		gameInfo.setLdpi_icon_url(item.getPic_url());
		gameInfo.setP_id(String.valueOf(item.getId()));
		gameInfo.setPackagename(item.getPackage_name());
		gameInfo.setShort_description(item.getDesc());
		gameInfo.setUrl(item.getFile_url());
		gameInfo.setVersion_code(String.valueOf(item.getVersion_code()));
		gameInfo.setVersion_name(item.getVersion_name());
		return gameInfo;
	}
}
