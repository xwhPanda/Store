package com.jiqu.object;

import java.io.File;

import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.FileUtil;

public class GameDetailInfo {
	private String p_id;
	private String sub_category;
	private String version_code;
	private String app_size;
	private String short_description;
	private String pay_category;
	private String category_id;
	private String en_name;
	private String product_type;
	private String url;
	private String filemd5;
	private String version_name;
	private String ldpi_icon_url;
	private String is_star;
	private String grade_difficulty;
	private String update_time;
	private String packagename;
	private String download_count;
	private String grade_vertigo;
	private String grade_immersive;
	private String source_type;
	private String grade_frames;
	private String name;
	private String ratings_count;
	private String grade_gameplay;
	private String icon_url;
	private String rating;
	private String screenshot_1;
	private String screenshot_2;
	private String screenshot_3;
	private String screenshot_4;
	private String screenshot_5;
	private String up_time;
	private String up_reason;
	private String long_description;
	private String comments_count;
	private String tags;
	
	private int state = -1;
	
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
	public synchronized String getIcon_url() {
		return icon_url;
	}
	public synchronized void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public synchronized String getRating() {
		return rating;
	}
	public synchronized void setRating(String rating) {
		this.rating = rating;
	}
	public synchronized String getScreenshot_1() {
		return screenshot_1;
	}
	public synchronized void setScreenshot_1(String screenshot_1) {
		this.screenshot_1 = screenshot_1;
	}
	public synchronized String getScreenshot_2() {
		return screenshot_2;
	}
	public synchronized void setScreenshot_2(String screenshot_2) {
		this.screenshot_2 = screenshot_2;
	}
	public synchronized String getScreenshot_3() {
		return screenshot_3;
	}
	public synchronized void setScreenshot_3(String screenshot_3) {
		this.screenshot_3 = screenshot_3;
	}
	public synchronized String getScreenshot_4() {
		return screenshot_4;
	}
	public synchronized void setScreenshot_4(String screenshot_4) {
		this.screenshot_4 = screenshot_4;
	}
	public synchronized String getScreenshot_5() {
		return screenshot_5;
	}
	public synchronized void setScreenshot_5(String screenshot_5) {
		this.screenshot_5 = screenshot_5;
	}
	public synchronized String getUp_time() {
		return up_time;
	}
	public synchronized void setUp_time(String up_time) {
		this.up_time = up_time;
	}
	public synchronized String getUp_reason() {
		return up_reason;
	}
	public synchronized void setUp_reason(String up_reason) {
		this.up_reason = up_reason;
	}
	public synchronized String getLong_description() {
		return long_description;
	}
	public synchronized void setLong_description(String long_description) {
		this.long_description = long_description;
	}
	public synchronized String getComments_count() {
		return comments_count;
	}
	public synchronized void setComments_count(String comments_count) {
		this.comments_count = comments_count;
	}
	public synchronized String getTags() {
		return tags;
	}
	public synchronized void setTags(String tags) {
		this.tags = tags;
	}
	
	public static DownloadAppinfo toDownloadAppinfo(GameDetailInfo info){
		DownloadAppinfo downloadAppinfo = new DownloadAppinfo();
		downloadAppinfo.setUrl(info.url);
		downloadAppinfo.setPackageName(info.getPackagename());
		downloadAppinfo.setId(Long.parseLong(info.getP_id()));
		downloadAppinfo.setAppName(info.getName());
		downloadAppinfo.setAppSize(info.getApp_size());
		downloadAppinfo.setCurrentSize(0l);
		if (info.state != -1) {
			downloadAppinfo.setDownloadState(info.state);
		}else {
			downloadAppinfo.setDownloadState(DownloadManager.STATE_NONE);
		}
		downloadAppinfo.setIconUrl(info.ldpi_icon_url);
		downloadAppinfo.setDes(info.short_description);
		downloadAppinfo.setVersionCode(info.getVersion_code());
		downloadAppinfo.setVersionName(info.getVersion_name());
		downloadAppinfo.setApkPath(FileUtil.getApkDownloadDir(AppUtil.getContext()) + File.separator + info.name + ".apk");
		downloadAppinfo.setZipPath(FileUtil.getZipDownloadDir(AppUtil.getContext()) + File.separator + info.name + ".zip");
		downloadAppinfo.setUnzipPath(FileUtil.getZipDownloadDir(AppUtil.getContext()) + File.separator + info.name);
		downloadAppinfo.setThread1(0l);
		downloadAppinfo.setThread2(0l);
		downloadAppinfo.setThread3(0l);
		downloadAppinfo.setThread4(0l);
		downloadAppinfo.setThread5(0l);
		downloadAppinfo.setProgress(0.0f);
		downloadAppinfo.setHasFinished(false);
		downloadAppinfo.setScore(Float.parseFloat(info.grade_difficulty)
				+ Float.parseFloat(info.grade_frames)
				+ Float.parseFloat(info.grade_gameplay)
				+ Float.parseFloat(info.grade_immersive)
				+ Float.parseFloat(info.grade_vertigo));
		if (!info.url.endsWith(".apk")) {
			downloadAppinfo.setIsZip(true);
		}else {
			downloadAppinfo.setIsZip(false);
		}
		
		return downloadAppinfo;
	}
	
	@Override
	public String toString() {
		return "GameDetailInfo [p_id=" + p_id + ", sub_category=" + sub_category + ", version_code=" + version_code + ", app_size=" + app_size + ", short_description=" + short_description + ", pay_category=" + pay_category + ", category_id=" + category_id + ", en_name=" + en_name + ", product_type=" + product_type + ", url=" + url + ", filemd5=" + filemd5 + ", version_name=" + version_name + ", ldpi_icon_url=" + ldpi_icon_url + ", is_star=" + is_star + ", grade_difficulty=" + grade_difficulty + ", update_time=" + update_time + ", packagename=" + packagename + ", download_count=" + download_count + ", grade_vertigo=" + grade_vertigo + ", grade_immersive=" + grade_immersive + ", source_type=" + source_type + ", grade_frames=" + grade_frames + ", name=" + name + ", ratings_count="
				+ ratings_count + ", grade_gameplay=" + grade_gameplay + ", icon_url=" + icon_url + ", rating=" + rating + ", screenshot_1=" + screenshot_1 + ", screenshot_2=" + screenshot_2 + ", screenshot_3=" + screenshot_3 + ", screenshot_4=" + screenshot_4 + ", screenshot_5=" + screenshot_5 + ", up_time=" + up_time + ", up_reason=" + up_reason + ", long_description=" + long_description + ", comments_count=" + comments_count + ", tags=" + tags + "]";
	}
	
}
