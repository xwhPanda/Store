package com.jiqu.object;

import java.io.Serializable;

public class UpgradeVersionInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private int status;
	private VersionInfo data;
	public synchronized int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
	public synchronized VersionInfo getData() {
		return data;
	}
	public synchronized void setData(VersionInfo data) {
		this.data = data;
	}

	class VersionInfo implements Serializable{
		private static final long serialVersionUID = 1L;
		private String channel_key;
		private String down_urlString;
		private int id;
		private String package_name;
		private String upgrade_content;
		private int version;
		private String version_name;
		public synchronized String getChannel_key() {
			return channel_key;
		}
		public synchronized void setChannel_key(String channel_key) {
			this.channel_key = channel_key;
		}
		public synchronized String getDown_urlString() {
			return down_urlString;
		}
		public synchronized void setDown_urlString(String down_urlString) {
			this.down_urlString = down_urlString;
		}
		public synchronized int getId() {
			return id;
		}
		public synchronized void setId(int id) {
			this.id = id;
		}
		public synchronized String getPackage_name() {
			return package_name;
		}
		public synchronized void setPackage_name(String package_name) {
			this.package_name = package_name;
		}
		public synchronized String getUpgrade_content() {
			return upgrade_content;
		}
		public synchronized void setUpgrade_content(String upgrade_content) {
			this.upgrade_content = upgrade_content;
		}
		public synchronized int getVersion() {
			return version;
		}
		public synchronized void setVersion(int version) {
			this.version = version;
		}
		public synchronized String getVersion_name() {
			return version_name;
		}
		public synchronized void setVersion_name(String version_name) {
			this.version_name = version_name;
		}
	}
}
