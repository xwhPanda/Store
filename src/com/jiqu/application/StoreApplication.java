package com.jiqu.application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jiqu.database.DaoMaster;
import com.jiqu.database.DaoMaster.DevOpenHelper;
import com.jiqu.database.DaoSession;
import com.jiqu.tools.LruBitmapCache;
import com.jiqu.tools.MetricsTool;

import android.app.Application;
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
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		mMainLooper = getMainLooper();
		getDaoSession();
		
		PACKAGE_NAME = getPackageName();
		DATA_CACHE_PATH = getCacheDir().getAbsolutePath();
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
        // ���tagΪ�յĻ���������Ĭ��TAG
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    // ͨ����Request�����Tag����ȡ������
    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
