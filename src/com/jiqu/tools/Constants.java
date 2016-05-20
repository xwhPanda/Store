package com.jiqu.tools;

public class Constants {

	/** （新浪） 应用的 APP_KEY **/
//    public static final String APP_KEY      = "2045436852";
	public static final String APP_KEY      = "4171998531";
	
	/** 设置的sharedprefrences名称  **/
	public static final String SETTINGS_SHARE_PREFERENCE_NAME = "settins";
	/** 设置界面自动检查更新的KEY **/
	public static final String AUTO_CHECK_VERSION = "autoCheckVersion";
	/** 默认同时下载数 **/
	public static final int DEFAULT_DOWANLOAD_THREAD_COUNTS = 1;
	/** 最大同时下载数 **/
	public static final int MAX_DOWANLOAD_THREAD_NUMBER = 5;
	/** 设置界面自更改同时下载数的KEY **/
	public static final String DOWNLOAD_THREAD_COUNTS = "downloadThreadCounts";
	/**	电量管理prefrences **/
	public static final String BATTERY_SHARE_PREFERENCE_NAME = "battery";
	/**	用户信息 **/
	public static final String ACCOUNT_INFO_PREFERENCE_NAME = "accountInfo";
	/** 显示百分比 **/
	public static final String DISPLAY_BATTERY_PERCENT = "displayBattery";
	/** 省电模式 **/
	public static final String POWER_SAVING_MODE = "powerSaving";
	/** 解压密码 **/
	public static final String PASSWORD = "JiLe20150413";
	/** 帐号长度限制 **/
	public static final int ACCOUNT_MAX_LENGTH = 10;
	/** 记录第一次使用等信息，用于统计 **/
	public static final String STATISTICS_SHARE_PREFERENCE_NAME = "statistics";
	/** 是否是第一次使用**/
	public static final String IS_FIRST = "isFirst";
	/** 第一次使用的时间戳 **/
	public static final String TIME = "time";
	/** 是否将第一次的信息成功发送给服务器了 **/
	public static final String SEND_SUCCESS = "sendSuccess";
	/** 用户头像 **/
	public static String ACCOUNT_ICON = "";
	
	public static String RECOMMEND_TAG = "recommend";
	public static String INFORMATION_TAG = "information";
	public static String GAME_TAG = "game";
	public static String EVALUATION_TAG = "evaluation";
	public static String TOOL_TAG = "tool";
	
	/** mac地址 **/
	public static String MAC;
	/** 应用包名 **/
	public static String PACKAGENAME;
	/** 应用版本名 **/
	public static String VERSION_NAME;
	/** 应用版本号 **/
	public static int VERSION_CODE;
	/** device_id **/
	public static String DEVICE_ID;
	/** 序列号 **/
	public static String SERIAL_NUMBER;
}
