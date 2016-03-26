package com.jiqu.object;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class InstalledApp implements Serializable {

	private static final long serialVersionUID = -5838252422626961048L;
	public static final int INSTALLED = 0;
	public static final int UNINSTALLING = 1;
	public static final int UNINSTAED = 2;
			
	public String name;
	public String packageName;
	public Drawable appIcon;
	public int versionCode;
	public String versionName;
	public String activityName;
	//用来计算应用大小
	public String filePath;
	public boolean isSystem = false;
	
	public int state = INSTALLED;
	public boolean checkState = false;
	
	@Override
	public String toString() {
		return "InstalledApp [name=" + name + ", packageName=" + packageName + ", appIcon=" + appIcon + ", versionCode=" + versionCode + ", versionName=" + versionName + ", activityName=" + activityName + ", filePath=" + filePath + ", isSystem=" + isSystem + ", state=" + state + ", checkState=" + checkState + "]";
	}
}
