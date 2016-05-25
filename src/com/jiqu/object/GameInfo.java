package com.jiqu.object;

import java.io.File;
import java.io.Serializable;

import android.text.TextUtils;
import android.util.Log;

import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.FileUtil;

public class GameInfo implements Serializable{
//	private String p_id;
//	private String sub_category;
//	private String version_code;
//	private String app_size;
//	private String short_description;
//	private String pay_category;
//	private String category_id;
//	private String en_name;
//	private String product_type;
//	private String url;
//	private String filemd5;
//	private String version_name;
//	private String ldpi_icon_url;
//	private String is_star;
//	private String grade_difficulty;
//	private String update_time;
//	private String packagename;
//	private String download_count;
//	private String grade_vertigo;
//	private String grade_immersive;
//	private String source_type;
//	private String grade_frames;
//	private String name;
//	private String ratings_count;
//	private String grade_gameplay;
//	private String noread_key;
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String apply_name;
	private String descript;
	private String product_name;
	private String column;
	private String version;
	private String version_name;
	private String size;
	private String package_name;
	private String type;
	private String down;
	private String down_url;
	private String icon;
	private String market;
	private String plist;
	private String score;
	private String star;
	private String siteID;
	private String statisticsID;
	private String time;
	private String pic;
	private String rotate_title;
	
	private String title;
	private String intro;
	
	private int adapterType;//布局类型
	private int gameType;//1:新游榜,2:热游榜,3:必玩榜
	private int state = -1;
	
	public synchronized String getPackage_name() {
		return package_name;
	}
	public synchronized void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public synchronized String getVersion_name() {
		return version_name;
	}
	public synchronized void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	public synchronized int getState() {
		return state;
	}
	public synchronized void setState(int state) {
		this.state = state;
	}
	public synchronized int getAdapterType() {
		return adapterType;
	}
	public synchronized void setAdapterType(int adapterType) {
		this.adapterType = adapterType;
	}
	
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
	public synchronized String getMarket() {
		return market;
	}
	public synchronized void setMarket(String market) {
		this.market = market;
	}
	public synchronized String getPlist() {
		return plist;
	}
	public synchronized void setPlist(String plist) {
		this.plist = plist;
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
	public synchronized String getSiteID() {
		return siteID;
	}
	public synchronized void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	public synchronized String getStatisticsID() {
		return statisticsID;
	}
	public synchronized void setStatisticsID(String statisticsID) {
		this.statisticsID = statisticsID;
	}
	public synchronized String getTime() {
		return time;
	}
	public synchronized void setTime(String time) {
		this.time = time;
	}
	public synchronized String getPic() {
		return pic;
	}
	public synchronized void setPic(String pic) {
		this.pic = pic;
	}
	public synchronized String getRotate_title() {
		return rotate_title;
	}
	public synchronized void setRotate_title(String rotate_title) {
		this.rotate_title = rotate_title;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	
	public synchronized int getGameType() {
		return gameType;
	}
	public synchronized void setGameType(int gameType) {
		this.gameType = gameType;
	}
	public synchronized String getIntro() {
		return intro;
	}
	public synchronized void setIntro(String intro) {
		this.intro = intro;
	}
	
	public static DownloadAppinfo toDownloadAppInfo(GameInfo info){
		DownloadAppinfo downloadInfo = new DownloadAppinfo();
		
		downloadInfo.setPackageName(info.product_name);
		downloadInfo.setId(info.id);
		downloadInfo.setAppName(info.apply_name);
		downloadInfo.setAppSize(info.size);
		downloadInfo.setCurrentSize((long) 0);
		if (info.state != -1) {
			downloadInfo.setDownloadState(info.state);
		}else {
			downloadInfo.setDownloadState(DownloadManager.STATE_NONE);
		}
		downloadInfo.setUrl(info.down_url);
		downloadInfo.setIconUrl(info.icon);
		downloadInfo.setDes(info.descript);
		downloadInfo.setPackageName(info.package_name);
		downloadInfo.setVersionCode(info.version);
		downloadInfo.setVersionName(info.version_name);
		downloadInfo.setApkPath(FileUtil.getApkDownloadDir(AppUtil.getContext()) + File.separator + info.apply_name + ".apk");
		downloadInfo.setZipPath(FileUtil.getZipDownloadDir(AppUtil.getContext()) + File.separator + info.apply_name + ".zip");
		downloadInfo.setUnzipPath(FileUtil.getZipDownloadDir(AppUtil.getContext()) + File.separator + info.apply_name);
		downloadInfo.setThread1(0l);
		downloadInfo.setThread2(0l);
		downloadInfo.setThread3(0l);
		downloadInfo.setThread4(0l);
		downloadInfo.setThread5(0l);
		downloadInfo.setProgress(0.0f);
		downloadInfo.setHasFinished(false);
		downloadInfo.setScore(info.score);
		if (!info.down_url.endsWith(".apk")) {
			downloadInfo.setIsZip(true);
		}else {
			downloadInfo.setIsZip(false);
		}
		
		return downloadInfo;
	}
}
