package com.jiqu.object;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.FileUtil;

public class GameDetailInfo {
	private String id;
	private String apply_name;
	private String descript;
	private String product_name;
	private String column;
	private String version;
	private String version_name;
	private String size;
	private String type;
	private String down;
	private String down_url;
	private String icon;
	private String score;
	private String star;
	private String device_support;
	private GameDetailFeeling game_feeling;
	private List<String> pic = new ArrayList<String>();
	private String intro;
	private String package_name;
	
	private int state = -1;
	
	public synchronized String getId() {
		return id;
	}

	public synchronized void setId(String id) {
		this.id = id;
	}

	public synchronized String getApply_name() {
		return apply_name;
	}

	public synchronized void setApply_name(String apply_name) {
		this.apply_name = apply_name;
	}

	public synchronized String getDescript() {
		return descript;
	}

	public synchronized void setDescript(String descript) {
		this.descript = descript;
	}

	public synchronized String getProduct_name() {
		return product_name;
	}

	public synchronized void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public synchronized String getColumn() {
		return column;
	}

	public synchronized void setColumn(String column) {
		this.column = column;
	}

	public synchronized String getVersion() {
		return version;
	}

	public synchronized void setVersion(String version) {
		this.version = version;
	}

	public synchronized String getSize() {
		return size;
	}

	public synchronized void setSize(String size) {
		this.size = size;
	}

	public synchronized String getType() {
		return type;
	}

	public synchronized void setType(String type) {
		this.type = type;
	}

	public synchronized String getDown() {
		return down;
	}

	public synchronized void setDown(String down) {
		this.down = down;
	}

	public synchronized String getDown_url() {
		return down_url;
	}

	public synchronized void setDown_url(String down_url) {
		this.down_url = down_url;
	}

	public synchronized String getIcon() {
		return icon;
	}

	public synchronized void setIcon(String icon) {
		this.icon = icon;
	}

	public synchronized String getScore() {
		return score;
	}

	public synchronized void setScore(String score) {
		this.score = score;
	}

	public synchronized String getStar() {
		return star;
	}

	public synchronized void setStar(String star) {
		this.star = star;
	}

	public synchronized String getDevice_support() {
		return device_support;
	}

	public synchronized void setDevice_support(String device_support) {
		this.device_support = device_support;
	}

	public synchronized GameDetailFeeling getGame_feeling() {
		return game_feeling;
	}

	public synchronized void setGame_feeling(GameDetailFeeling game_feeling) {
		this.game_feeling = game_feeling;
	}

	public synchronized List<String> getPic() {
		return pic;
	}

	public synchronized void setPic(List<String> pic) {
		this.pic = pic;
	}

	public synchronized String getIntro() {
		return intro;
	}

	public synchronized void setIntro(String intro) {
		this.intro = intro;
	}
	
	public synchronized String getVersion_name() {
		return version_name;
	}

	public synchronized void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public synchronized String getPackage_name() {
		return package_name;
	}

	public synchronized void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public synchronized int getState() {
		return state;
	}

	public synchronized void setState(int state) {
		this.state = state;
	}

	public static DownloadAppinfo toDownloadAppinfo(GameDetailInfo info){
		DownloadAppinfo downloadAppinfo = new DownloadAppinfo();
		downloadAppinfo.setUrl(info.down_url);
		downloadAppinfo.setPackageName(info.getPackage_name());
		downloadAppinfo.setId(info.getId());
		downloadAppinfo.setAppName(info.getApply_name());
		downloadAppinfo.setAppSize(info.getSize());
		downloadAppinfo.setCurrentSize(0l);
		if (info.state != -1) {
			downloadAppinfo.setDownloadState(info.state);
		}else {
			downloadAppinfo.setDownloadState(DownloadManager.STATE_NONE);
		}
		downloadAppinfo.setIconUrl(info.icon);
		downloadAppinfo.setDes(info.descript);
		downloadAppinfo.setVersionCode(info.getVersion());
		downloadAppinfo.setVersionName(info.getVersion_name());
		downloadAppinfo.setApkPath(FileUtil.getApkDownloadDir(AppUtil.getContext()) + File.separator + info.package_name + ".apk");
		downloadAppinfo.setZipPath(FileUtil.getZipDownloadDir(AppUtil.getContext()) + File.separator + info.package_name + ".zip");
		downloadAppinfo.setUnzipPath(FileUtil.getZipDownloadDir(AppUtil.getContext()) + File.separator + info.package_name);
		downloadAppinfo.setThread1(0l);
		downloadAppinfo.setThread2(0l);
		downloadAppinfo.setThread3(0l);
		downloadAppinfo.setThread4(0l);
		downloadAppinfo.setThread5(0l);
		downloadAppinfo.setProgress(0.0f);
		downloadAppinfo.setHasFinished(false);
		downloadAppinfo.setScore(info.getScore());
		if (!info.down_url.endsWith(".apk")) {
			downloadAppinfo.setIsZip(true);
		}else {
			downloadAppinfo.setIsZip(false);
		}
		
		return downloadAppinfo;
	}
}
