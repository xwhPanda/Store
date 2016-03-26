package com.jiqu.tools;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {

	public LruBitmapCache(int maxSize) {
		super(maxSize);
		// TODO Auto-generated constructor stub
	}

	public LruBitmapCache(){
        this(getDefaultLruCacheSize());
    }
	
	public static int getDefaultLruCacheSize(){
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 8;
		return cacheSize;
	}
	
	@Override
	protected int sizeOf(String key, Bitmap value) {
		// TODO Auto-generated method stub
		return value.getRowBytes()*value.getHeight()/1024;
	}
	
	@Override
	public Bitmap getBitmap(String arg0) {
		// TODO Auto-generated method stub
		return get(arg0);
	}

	@Override
	public void putBitmap(String arg0, Bitmap arg1) {
		// TODO Auto-generated method stub
		put(arg0, arg1);
	}

}
