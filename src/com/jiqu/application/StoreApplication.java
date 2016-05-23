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
import com.umeng.socialize.PlatformConfig;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.LruBitmapCache;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.NetReceiver;
import com.jiqu.weibo.WeiboShare;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class StoreApplication extends Application {
	private static final String TAG = "StoreApplication";
	private static StoreApplication instance;
	public static DaoSession daoSession;
	private static Looper mMainLooper;
	
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	public static String PACKAGE_NAME;
	public static String DATA_CACHE_PATH;
	public static Context context;
	public static String DEVICE_ID;
	public static String CHANNEL;
	public static List<LoginOutObserver> loginOutObservers;
	
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
		
		getRequestQueue();
		
		context = this;
		
		initFiles();
		initConstant();
		
		initUMeng();
	}
	
	public static void setLoginOutObserver(LoginOutObserver observer){
		loginOutObservers.add(observer);
	}
	
	/** 初始化常量 **/
	private void initConstant(){
		PackageManager pm = getPackageManager();
		Constants.MAC = getLocalMacAddressFromIp(context);
		Constants.ACCOUNT_ICON = getCacheDir() + "/crop_icon"  + ".png";
		Constants.PACKAGENAME = getPackageName();
		try {
			PackageInfo pi = pm.getPackageInfo(Constants.PACKAGENAME, 0);
			Constants.VERSION_NAME = pi.versionName;
			Constants.VERSION_CODE = pi.versionCode;
			Constants.DEVICE_ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
			Constants.SERIAL_NUMBER = android.os.Build.SERIAL;
			DEVICE_ID = ((TelephonyManager)context.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
			CHANNEL = getMetaDataValue("channel");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 友盟 **/
	private void initUMeng(){
		//微信 appid appsecret
		PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
		//新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo("4171998531","cdaaf47bda171bdf9546478720b7eef2");
		// QQ和Qzone appid appkey     
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba"); 
	}
	
	private String getMetaDataValue(String name){
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
	
	private void initFiles(){
		FileUtil.getZipDownloadDir(context);
		FileUtil.getApkDownloadDir(context);
		FileUtil.getUpgradeDownloadDir(context);
	}
	
	public static synchronized StoreApplication getInstance(){
		if (instance == null) {
			instance = new StoreApplication();
		}
		return instance;
	}
	
	private static DaoSession getDaoSession(){
		if (daoSession == null) {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(instance, "jiqu_store_database", null);
			SQLiteDatabase db = helper.getWritableDatabase();
			DaoMaster daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}
	
	public static Looper getMainThreadLooper(){
		return mMainLooper;
	}
	
	public RequestQueue getRequestQueue(){
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return requestQueue;
	}
	
	public ImageLoader getImageLoader(){
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
            List<NetworkInterface>  nilist = Collections.list(NetworkInterface.getNetworkInterfaces());  
            for (NetworkInterface ni: nilist)   
            {  
                List<InetAddress>  ialist = Collections.list(ni.getInetAddresses());  
                for (InetAddress address: ialist){  
                    if (!address.isLoopbackAddress() 
                    		&& InetAddressUtils.isIPv4Address(ipv4=address.getHostAddress()))   
                    {   
                        return ipv4;  
                    }  
                }  
   
            }  
   
        } catch (SocketException ex) {  
           ex.printStackTrace();
        }  
        return null;  
    }
    
    public  String getLocalMacAddressFromIp(Context context) {
        String mac_s= "";
       try {
            byte[] mac;
            NetworkInterface ne=NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
       } catch (Exception e) {
           e.printStackTrace();
       }
        return mac_s;
    }
    
    public static  String byte2hex(byte[] b) {
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
