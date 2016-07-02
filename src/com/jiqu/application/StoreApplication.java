package com.jiqu.application;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jiqu.database.DaoMaster;
import com.jiqu.database.DaoMaster.DevOpenHelper;
import com.jiqu.database.DaoSession;
import com.jiqu.download.FileUtil;
import com.jiqu.interfaces.LoginOutObserver;
import com.ta.utdid2.device.UTDevice;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.vr.store.R;
import com.jiqu.tools.ChannelUtil;
import com.jiqu.tools.Constants;
import com.jiqu.tools.LruBitmapCache;
import com.jiqu.tools.SharePreferenceTool;
import com.jiqu.umeng.UMengManager;
import com.jiqu.umeng.UmengNotificationHandler;
import com.jiqu.umeng.UmengPushMessageHandler;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class StoreApplication extends Application {
	private static final String TAG = "StoreApplication";
	private static StoreApplication instance;
	/** 数据库 **/
	public static DaoSession daoSession;
	private static Looper mMainLooper;

	/** volley的请求队列 **/
	private RequestQueue requestQueue;
	/** volley的图片loader **/
	private ImageLoader imageLoader;
	/** 包名 **/
	public static String PACKAGE_NAME;
	/** data下面cache路径 **/
	public static String DATA_CACHE_PATH;
	public static Context context;
	public static String DEVICE_ID;
	/** 渠道名 **/
	public static String CHANNEL;
	public static String APK_DOWNLOAD_PATH = "";
	public static String ZIP_DOWNLOAD_PATH = "";
	public static String UPGRADE_DOWNLOAD_PATH = "";
	public static String DATA_FILE_PATH = "";
	/** 退出登录Observers **/
	public static List<LoginOutObserver> loginOutObservers;
	/** 背景图片 **/
	public static Drawable BG_IMG;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CalligraphyConfig.initDefault("fonts/lantinghei.ttf", R.attr.fontPath);
		instance = this;
		mMainLooper = getMainLooper();
		loginOutObservers = new ArrayList<LoginOutObserver>();
		getDaoSession();

		PACKAGE_NAME = getPackageName();
		DATA_CACHE_PATH = getCacheDir().getAbsolutePath();
		DATA_FILE_PATH = getFilesDir().getAbsolutePath();

		getRequestQueue();

		context = this;

		initFiles();
		initConstant();

		initUMeng();

		BG_IMG = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.toolbg));
		APK_DOWNLOAD_PATH = FileUtil.getApkDownloadDir(context);
		ZIP_DOWNLOAD_PATH = FileUtil.getZipDownloadDir(context);
		UPGRADE_DOWNLOAD_PATH = FileUtil.getUpgradeDownloadDir(context);

	}

	public static void setLoginOutObserver(LoginOutObserver observer) {
		loginOutObservers.add(observer);
	}

	/** 初始化常量 **/
	private void initConstant() {
		PackageManager pm = getPackageManager();
		Constants.MAC = getLocalMacAddressFromIp(context);
		Constants.ACCOUNT_ICON = getCacheDir() + "/crop_icon" + ".png";
		Constants.PACKAGENAME = getPackageName();
		try {
			PackageInfo pi = pm.getPackageInfo(Constants.PACKAGENAME, 0);
			Constants.VERSION_NAME = pi.versionName;
			Constants.VERSION_CODE = pi.versionCode;
//			Constants.DEVICE_ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
			/** 使用UTDID.jar包获取唯一标志，阿里系APP都在使用，这里友盟推送已经集成此jar包 **/
			Constants.DEVICE_ID = UTDevice.getUtdid(context);
			Constants.SERIAL_NUMBER = android.os.Build.SERIAL;
			DEVICE_ID = ((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
			/** 修改meta来决定渠道号，效率低 **/
			// CHANNEL = getMetaDataValue("channel");
			/** 美团打包方案 **/
			CHANNEL = ChannelUtil.getChannel(this);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SharedPreferences preferences = getSharedPreferences(Constants.SETTINGS_SHARE_PREFERENCE_NAME, MODE_PRIVATE);
		Constants.DEFAULT_DOWANLOAD_THREAD_COUNTS = SharePreferenceTool.getIntFromPreferences(preferences, Constants.DOWNLOAD_THREAD_COUNTS, Constants.DEFAULT_DOWANLOAD_THREAD_COUNTS);
		Constants.AUTO_CHECK_NEW_VERSION = SharePreferenceTool.getBooleanFromPreferences(preferences, Constants.AUTO_CHECK_VERSION, false);
	}

	/** 友盟 **/
	private void initUMeng() {
		/** 不显示友盟的Toast **/
		Config.IsToastTip = false;
		/** 微信 appid appsecret **/
		PlatformConfig.setWeixin("wx41034df8e97add60", "75fead583e630c6029dc18ebd3975de4");
		/** 新浪微博 appkey appsecret **/
		PlatformConfig.setSinaWeibo("3518038102", "3389298718e97db68c4f53280cb302a0");
		/** 新浪微博回调页 **/
		Config.REDIRECT_URL="http://www.123sjzs.com";
		/** QQ和Qzone appid appkey **/
		PlatformConfig.setQQZone("1105444730", "TOqwncUiOjr6aoVl");
		
		UMengManager.getInstance().setMessageChannel(CHANNEL);
		UMengManager.getInstance().setMessageHandler(new UmengPushMessageHandler());
		UMengManager.getInstance().setNotificationClickHandler(new UmengNotificationHandler());
	}

	/** 获取AndroidManifest.xml中的mete节点 **/
	private String getMetaDataValue(String name) {
		Object value = null;
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo;
		try {
			applicationInfo = packageManager.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			if (applicationInfo != null && applicationInfo.metaData.get(name) != null) {
				value = applicationInfo.metaData.get(name);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return value.toString();
	}

	/** 初始化文件 **/
	private void initFiles() {
		/** 压缩包下载路径 **/
		FileUtil.getZipDownloadDir(context);
		/** APK下载路径 **/
		FileUtil.getApkDownloadDir(context);
		/** 应用升级包下载路径 **/
		FileUtil.getUpgradeDownloadDir(context);
	}

	public static synchronized StoreApplication getInstance() {
		if (instance == null) {
			instance = new StoreApplication();
		}
		return instance;
	}

	private static DaoSession getDaoSession() {
		if (daoSession == null) {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(instance, "jiqu_store_database", null);
			SQLiteDatabase db = helper.getWritableDatabase();
			DaoMaster daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}

	public static Looper getMainThreadLooper() {
		return mMainLooper;
	}

	public RequestQueue getRequestQueue() {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return requestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (imageLoader == null) {
			imageLoader = new ImageLoader(requestQueue, new LruBitmapCache());
		}
		return imageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}

	public String getLocalIpAddress() {
		try {
			String ipv4;
			List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface ni : nilist) {
				List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
				for (InetAddress address : ialist) {
					if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {
						return ipv4;
					}
				}

			}

		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String getLocalMacAddressFromIp(Context context) {
		String mac_s = "";
		try {
			byte[] mac;
			NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
			mac = ne.getHardwareAddress();
			mac_s = byte2hex(mac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mac_s;
	}

	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer(b.length);
		String stmp = "";
		int len = b.length;
		for (int n = 0; n < len; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs = hs.append("0").append(stmp);
			else {
				hs = hs.append(stmp);
			}
		}
		return String.valueOf(hs);
	}
}