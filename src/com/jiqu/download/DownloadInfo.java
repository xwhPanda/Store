package com.jiqu.download;

import java.io.File;

import com.jiqu.database.DownloadAppinfo;
public class DownloadInfo {

	private long id;//
	private String appName;//
	private String appSize = "0";//
	private long currentSize = 0;//
	private int downloadState = 0;//
	private String url;//
	private String iconUrl;
	private String path;//
	private boolean  hasFinished= false;
	/** ��AppInfo�й�����һ��DownLoadInfo */
	public static DownloadInfo clone(AppInfo info) {
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.id = info.getId();
		downloadInfo.appName = info.getName();
		downloadInfo.appSize = info.getSize();
		downloadInfo.currentSize = 0;
		downloadInfo.downloadState = DownloadManager.STATE_NONE;
		downloadInfo.url = info.getDown_url();
		downloadInfo.iconUrl = info.getLogo_url_160();
		downloadInfo.path = FileUtil.getDownloadDir(AppUtil.getContext()) + File.separator + downloadInfo.appName + ".apk";
		return downloadInfo;
	}
	
	public static DownloadAppinfo toDownloadAppInfo(DownloadInfo info){
		DownloadAppinfo downloadAppinfo = new DownloadAppinfo();
		downloadAppinfo.setAppName(info.getAppName());
		downloadAppinfo.setAppSize(info.getAppSize());
		downloadAppinfo.setCurrentSize(info.getCurrentSize());
		downloadAppinfo.setDownloadState(info.getDownloadState());
		downloadAppinfo.setIconUrl(info.getIconUrl());
		downloadAppinfo.setId(info.getId());
		downloadAppinfo.setPath(info.getPath());
		downloadAppinfo.setUrl(info.getUrl());
		
		return downloadAppinfo;
	}
	
	

	public boolean isHasFinished() {
		return hasFinished;
	}



	public void setHasFinished(boolean hasFinished) {
		this.hasFinished = hasFinished;
	}



	public String getPath() {
		return path;
	}

	public float getProgress() {
		if (getAppSize().equals("0")) {
			return 0;
		}
		return (getCurrentSize() + 0.0f) / Integer.parseInt(getAppSize());
	}

	public synchronized String getUrl() {
		return url;
	}

	public synchronized void setUrl(String url) {
		this.url = url;
	}

	public synchronized long getId() {
		return id;
	}

	public synchronized void setId(long id) {
		this.id = id;
	}

	public synchronized String getAppName() {
		return appName;
	}

	public synchronized void setAppName(String appName) {
		this.appName = appName;
	}

	public synchronized String getAppSize() {
		return appSize;
	}

	public synchronized void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public synchronized long getCurrentSize() {
		return currentSize;
	}

	public synchronized void setCurrentSize(long currentSize) {
		this.currentSize = currentSize;
	}

	public synchronized int getDownloadState() {
		return downloadState;
	}

	public void setDownloadState(int downloadState) {
		this.downloadState = downloadState;
	}

	public synchronized String getIconUrl() {
		return iconUrl;
	}

	public synchronized void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public synchronized void setPath(String path) {
		this.path = path;
	}
}
