package com.jiqu.application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jiqu.database.DaoMaster;
import com.jiqu.database.DaoMaster.DevOpenHelper;
import com.jiqu.database.DaoSession;
import com.jiqu.store.R;
import com.jiqu.tools.LruBitmapCache;
import com.jiqu.tools.MetricsTool;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
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
}
