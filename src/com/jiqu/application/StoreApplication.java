package com.jiqu.application;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
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
import com.jiqu.store.R;
import com.jiqu.tools.Constant;
import com.jiqu.tools.LruBitmapCache;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.NetReceiver;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.provider.Settings.Secure;
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
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CalligraphyConfig.initDefault("fonts/lantinghei.ttf", R.attr.fontPath);
		instance = this;
		mMainLooper = getMainLooper();
		getDaoSession();
		
		PACKAGE_NAME = getPackageName();
		DATA_CACHE_PATH = getCacheDir().getAbsolutePath();
		
		context = this;
		
		initConstant();
	}
	
	private void initConstant(){
		PackageManager pm = getPackageManager();
		Constant.MAC = getLocalMacAddressFromIp(context);
		Constant.PACKAGENAME = getPackageName();
		try {
			PackageInfo pi = pm.getPackageInfo(Constant.PACKAGENAME, 0);
			Constant.VERSION_NAME = pi.versionName;
			Constant.VERSION_CODE = pi.versionCode;
			Constant.DEVICE_ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
			Constant.SERIAL_NUMBER = android.os.Build.SERIAL; 
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    
    public void getNetType(){
    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    	NetworkInfo info = cm.getActiveNetworkInfo();
    	if (info != null) {
			Log.i("TAG", "netInfo : " + info.getType());
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
            Log.e("TAG", ex.toString());  
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
