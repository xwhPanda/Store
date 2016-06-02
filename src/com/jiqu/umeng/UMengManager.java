package com.jiqu.umeng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jiqu.application.StoreApplication;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class UMengManager {
	private static UMengManager uMengManager;
	private Context context;
	private UMShareAPI umShareAPI;
	
	private UMengManager(Context context){
		umShareAPI = UMShareAPI.get(context);
	}
	
	public static UMengManager getInstance(){
		if (uMengManager == null) {
			uMengManager = new UMengManager(StoreApplication.context);
		}
		return uMengManager;
	}

	/** 新浪授权 **/
	public void sinaAuth(Activity activity,UMAuthListener umAuthListener){
		SHARE_MEDIA media = SHARE_MEDIA.SINA;
		umShareAPI.doOauthVerify(activity, media, umAuthListener);
	}
	
	/** 取消新浪授权 **/
	public void cancleSinaAuth(Activity activity,UMAuthListener umAuthListener){
		umShareAPI.deleteOauth(activity, SHARE_MEDIA.SINA, umAuthListener);
	}
	
	/** QQ授权 **/
	public void qqAuth(Activity activity,UMAuthListener umAuthListener){
		SHARE_MEDIA media = SHARE_MEDIA.QQ;
		umShareAPI.doOauthVerify(activity, media, umAuthListener);
	}
	
	public void getQqInfo(Activity activity,UMAuthListener umAuthListener){
		SHARE_MEDIA media = SHARE_MEDIA.QQ;
		umShareAPI.getPlatformInfo(activity, media, umAuthListener);
	}
	
	/** 取消QQ授权 **/
	public void cancleQqAuth(Activity activity,UMAuthListener umAuthListener){
		umShareAPI.deleteOauth(activity, SHARE_MEDIA.QQ, umAuthListener);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		umShareAPI.onActivityResult(requestCode, resultCode, data);
	}
}
