package com.jiqu.object;

import java.io.File;

import android.text.TextUtils;

import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.FileUtil;

public class GameInfo {
	private String p_id;
	private String sub_category = "sub_category";
	private String version_code = "version_code";
	private String app_size = "app_size";
	private String short_description = "short_description";
	private String pay_category = "pay_category";
	private String category_id = "category_id";
	private String en_name = "en_name";
	private String product_type = "product_type";
	private String url = "url";
	private String filemd5 = "filemd5";
	private String version_name = "version_name";
	private String ldpi_icon_url = "ldpi_icon_url";
	private String is_star = "is_star";
	private String grade_difficulty = "grade_difficulty";
	private String update_time = "update_time";
	private String packagename = "packagename";
	private String download_count = "download_count";
	private String grade_vertigo = "grade_vertigo";
	private String grade_immersive = "grade_immersive";
	private String source_type = "source_type";
	private String grade_frames = "grade_frames";
	private String name = "name";
	private String ratings_count = "ratings_count";
	private String grade_gameplay = "grade_gameplay";
	private String noread_key = "noread_key";
	
	private int adapterType;//布局类型
	private int state = -1;
	
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
	public synchronized String getP_id() {
		return p_id;
	}
	public synchronized void setP_id(String p_id) {
		this.p_id = p_id;
	}
	public synchronized String getSub_category() {
		return sub_category;
	}
	public synchronized void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}
	public synchronized String getVersion_code() {
		return version_code;
	}
	public synchronized void setVersion_code(String version_code) {
		this.version_code = version_code;
	}
	public synchronized String getApp_size() {
		return app_size;
	}
	public synchronized void setApp_size(String app_size) {
		this.app_size = app_size;
	}
	public synchronized String getShort_description() {
		return short_description;
	}
	public synchronized void setShort_description(String short_description) {
		this.short_description = short_description;
	}
	public synchronized String getPay_category() {
		return pay_category;
	}
	public synchronized void setPay_category(String pay_category) {
		this.pay_category = pay_category;
	}
	public synchronized String getCategory_id() {
		return category_id;
	}
	public synchronized void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public synchronized String getEn_name() {
		return en_name;
	}
	public synchronized void setEn_name(String en_name) {
		this.en_name = en_name;
	}
	public synchronized String getProduct_type() {
		return product_type;
	}
	public synchronized void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public synchronized String getUrl() {
		return url;
	}
	public synchronized void setUrl(String url) {
		this.url = url;
	}
	public synchronized String getFilemd5() {
		return filemd5;
	}
	public synchronized void setFilemd5(String filemd5) {
		this.filemd5 = filemd5;
	}
	public synchronized String getVersion_name() {
		return version_name;
	}
	public synchronized void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	public synchronized String getLdpi_icon_url() {
		return ldpi_icon_url;
	}
	public synchronized void setLdpi_icon_url(String ldpi_icon_url) {
		this.ldpi_icon_url = ldpi_icon_url;
	}
	public synchronized String getIs_star() {
		return is_star;
	}
	public synchronized void setIs_star(String is_star) {
		this.is_star = is_star;
	}
	public synchronized String getGrade_difficulty() {
		return grade_difficulty;
	}
	public synchronized void setGrade_difficulty(String grade_difficulty) {
		this.grade_difficulty = grade_difficulty;
	}
	public synchronized String getUpdate_time() {
		return update_time;
	}
	public synchronized void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public synchronized String getPackagename() {
		return packagename;
	}
	public synchronized void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public synchronized String getDownload_count() {
		return download_count;
	}
	public synchronized void setDownload_count(String download_count) {
		this.download_count = download_count;
	}
	public synchronized String getGrade_vertigo() {
		return grade_vertigo;
	}
	public synchronized void setGrade_vertigo(String grade_vertigo) {
		this.grade_vertigo = grade_vertigo;
	}
	public synchronized String getGrade_immersive() {
		return grade_immersive;
	}
	public synchronized void setGrade_immersive(String grade_immersive) {
		this.grade_immersive = grade_immersive;
	}
	public synchronized String getSource_type() {
		return source_type;
	}
	public synchronized void setSource_type(String source_type) {
		this.source_type = source_type;
	}
	public synchronized String getGrade_frames() {
		return grade_frames;
	}
	public synchronized void setGrade_frames(String grade_frames) {
		this.grade_frames = grade_frames;
	}
	public synchronized String getName() {
		return name;
	}
	public synchronized void setName(String name) {
		this.name = name;
	}
	public synchronized String getRatings_count() {
		return ratings_count;
	}
	public synchronized void setRatings_count(String ratings_count) {
		this.ratings_count = ratings_count;
	}
	public synchronized String getGrade_gameplay() {
		return grade_gameplay;
	}
	public synchronized void setGrade_gameplay(String grade_gameplay) {
		this.grade_gameplay = grade_gameplay;
	}
	public synchronized String getNoread_key() {
		return noread_key;
	}
	public synchronized void setNoread_key(String noread_key) {
		this.noread_key = noread_key;
	}
	
	public static DownloadAppinfo toDownloadAppInfo(GameInfo info){
		DownloadAppinfo downloadInfo = new DownloadAppinfo();
		
		downloadInfo.setPackageName(info.packagename);
		downloadInfo.setId(Long.parseLong(info.p_id));
		downloadInfo.setAppName(info.name);
		downloadInfo.setAppSize(info.app_size);
		downloadInfo.setCurrentSize((long) 0);
		if (info.state != -1) {
			downloadInfo.setDownloadState(info.state);
		}else {
			downloadInfo.setDownloadState(DownloadManager.STATE_NONE);
		}
		downloadInfo.setUrl(info.url);
		downloadInfo.setIconUrl(info.ldpi_icon_url);
		downloadInfo.setApkPath(FileUtil.getApkDownloadDir(AppUtil.getContext()) + File.separator + info.name + ".apk");
		downloadInfo.setZipPath(FileUtil.getZipDownloadDir(AppUtil.getContext()) + File.separator + info.name + ".zip");
		downloadInfo.setUnzipPath(FileUtil.getZipDownloadDir(AppUtil.getContext()) + File.separator + info.name);
		downloadInfo.setThread1(0l);
		downloadInfo.setThread2(0l);
		downloadInfo.setThread3(0l);
		downloadInfo.setThread4(0l);
		downloadInfo.setThread5(0l);
		downloadInfo.setProgress(0.0f);
		downloadInfo.setHasFinished(false);
		downloadInfo.setDes("");
		downloadInfo.setScore(2);
		if (!info.url.endsWith(".apk")) {
			downloadInfo.setIsZip(true);
		}else {
			downloadInfo.setIsZip(false);
		}
		
		return downloadInfo;
	}
	
	@Override
	public String toString() {
		return "GameInfo [p_id=" + p_id + ", sub_category=" + sub_category + ", version_code=" + version_code + ", app_size=" + app_size + ", short_description=" + short_description + ", pay_category=" + pay_category + ", category_id=" + category_id + ", en_name=" + en_name + ", product_type=" + product_type + ", url=" + url + ", filemd5=" + filemd5 + ", version_name=" + version_name + ", ldpi_icon_url=" + ldpi_icon_url + ", is_star=" + is_star + ", grade_difficulty=" + grade_difficulty + ", update_time=" + update_time + ", packagename=" + packagename + ", download_count=" + download_count + ", grade_vertigo=" + grade_vertigo + ", grade_immersive=" + grade_immersive + ", source_type=" + source_type + ", grade_frames=" + grade_frames + ", name=" + name + ", ratings_count=" + ratings_count
				+ ", grade_gameplay=" + grade_gameplay + ", noread_key=" + noread_key + "]";
	}
}
