package com.jiqu.database;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DOWNLOAD_APPINFO".
 */
public class DownloadAppinfo {

    /** Not-null value. */
    private String appName;
    private Long id;
    private Long currentSize;
    /** Not-null value. */
    private String appSize;
    private int downloadState;
    /** Not-null value. */
    private String url;
    private String iconUrl;
    /** Not-null value. */
    private String path;
    private Boolean hasFinished;
    private String des;
    private Integer score;
    private Float progress;

    public DownloadAppinfo() {
    }

    public DownloadAppinfo(Long id) {
        this.id = id;
    }

    public DownloadAppinfo(String appName, Long id, Long currentSize, String appSize, int downloadState, String url, String iconUrl, String path, Boolean hasFinished, String des, Integer score, Float progress) {
        this.appName = appName;
        this.id = id;
        this.currentSize = currentSize;
        this.appSize = appSize;
        this.downloadState = downloadState;
        this.url = url;
        this.iconUrl = iconUrl;
        this.path = path;
        this.hasFinished = hasFinished;
        this.des = des;
        this.score = score;
        this.progress = progress;
    }
    
    /** Not-null value. */
    public String getAppName() {
        return appName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(Long currentSize) {
        this.currentSize = currentSize;
    }

    /** Not-null value. */
    public String getAppSize() {
        return appSize;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    /** Not-null value. */
    public String getUrl() {
        return url;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /** Not-null value. */
    public String getPath() {
        return path;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(Boolean hasFinished) {
        this.hasFinished = hasFinished;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

}