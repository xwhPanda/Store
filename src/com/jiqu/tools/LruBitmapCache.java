package com.jiqu.tools;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.jiqu.application.StoreApplication;
import com.jiqu.download.FileUtil;
import com.jiqu.tools.DiskLruCache.Snapshot;

public class LruBitmapCache implements ImageCache {
	private LruCache<String, Bitmap> mLruCache;
	private DiskLruCache mDiskLruCache;
	/** 磁盘缓存大小 **/
	private static final int DISKMAXSIZE = 10 * 1024 * 1024;
	
	public LruBitmapCache(){
		int maxSize = getDefaultLruCacheSize();
		mLruCache = new LruCache<String, Bitmap>(maxSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes() * value.getHeight();
			}
		};
		
		try {
			mDiskLruCache = DiskLruCache.open(getDiskCacheDir(StoreApplication.context, "imageCache"), 
					Constants.VERSION_CODE, 1, DISKMAXSIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static File getDiskCacheDir(Context context, String uniqueName){
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) 
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		}else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	public static int getDefaultLruCacheSize(){
		int maxMemory = (int) (Runtime.getRuntime().maxMemory());
		int cacheSize = maxMemory / 8;
		return cacheSize;
	}

	@Override
	public Bitmap getBitmap(String arg0) {
		// TODO Auto-generated method stub
		if (mLruCache.get(arg0) != null) {
			return mLruCache.get(arg0);
		}else {
			String key = MD5.GetMD5Code(arg0);
			try {
				if (mDiskLruCache.get(key) != null) {
					Snapshot snapshot = mDiskLruCache.get(key);
					Bitmap bitmap = null;
					if (snapshot != null) {
						bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
						mLruCache.put(arg0, bitmap);
					}
					return bitmap;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return null;
	}

	@Override
	public void putBitmap(String arg0, Bitmap arg1) {
		// TODO Auto-generated method stub
		mLruCache.put(arg0, arg1);
		String key = MD5.GetMD5Code(arg0);
		try {
			if (mDiskLruCache.get(key) == null) {
				DiskLruCache.Editor editor = mDiskLruCache.edit(key);
				if (editor != null) {
					OutputStream outputStream = editor.newOutputStream(0);
					if (arg1.compress(CompressFormat.JPEG, 100, outputStream)) {
						editor.commit();
					}else {
						editor.abort();
					}
				}
				mDiskLruCache.flush();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
