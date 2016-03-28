package com.jiqu.download;

import java.io.File;

import com.jiqu.database.DownloadAppinfo;


public class AppInfo {
    private long id;//游戏id
    private String name;//游戏名
    private String apkid;//游戏包名
    private String logo_url_160;//简介图
    private String download_times;//下载次数
    private String version_name;//版本号
    private String size;//游戏大小
    private String down_url;//下载连接
    private String single_word;//
    private String desinfo; //描述
    
    private int adapterType;//布局类型
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApkid() {
		return apkid;
	}
	public void setApkid(String apkid) {
		this.apkid = apkid;
	}
	public String getLogo_url_160() {
		return logo_url_160;
	}
	public void setLogo_url_160(String logo_url_160) {
		this.logo_url_160 = logo_url_160;
	}
	public String getDownload_times() {
		return download_times;
	}
	public void setDownload_times(String download_times) {
		this.download_times = download_times;
	}
	public String getVersion_name() {
		return version_name;
	}
	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getDown_url() {
		return down_url;
	}
	public void setDown_url(String down_url) {
		this.down_url = down_url;
	}
	public String getSingle_word() {
		return single_word;
	}
	public void setSingle_word(String single_word) {
		this.single_word = single_word;
	}
	public String getDesinfo() {
		return desinfo;
	}
	public void setDesinfo(String desinfo) {
		this.desinfo = desinfo;
	}
	public int getAdapterType() {
		return adapterType;
	}
	public void setAdapterType(int adapterType) {
		this.adapterType = adapterType;
	}
	
	public static DownloadAppinfo toDownloadAppInfo(AppInfo info){
		DownloadAppinfo downloadInfo = new DownloadAppinfo();
		downloadInfo.setPackageName(info.apkid);
		downloadInfo.setId(info.getId());
		downloadInfo.setAppName(info.getName());
		downloadInfo.setAppSize(info.size);
		downloadInfo.setCurrentSize((long) 0);
		downloadInfo.setDownloadState(DownloadManager.STATE_NONE);
		downloadInfo.setUrl(info.down_url);
		downloadInfo.setIconUrl(info.logo_url_160);
		downloadInfo.setPath(FileUtil.getDownloadDir(AppUtil.getContext()) + File.separator + info.name + ".apk");
		return downloadInfo;
	}
	
	@Override
	public String toString() {
		return "AppInfo [id=" + id + ", name=" + name + ", apkid=" + apkid + ", logo_url_160=" + logo_url_160 + ", download_times=" + download_times + ", version_name=" + version_name + ", size=" + size + ", down_url=" + down_url + ", single_word=" + single_word + ", desinfo=" + desinfo + "]";
	}
}
