package com.jiqu.umeng;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.jiqu.application.StoreApplication;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.k.e;
import com.umeng.message.tag.TagManager;
import com.umeng.message.tag.TagManager.Result;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class UMengManager {
	private static UMengManager uMengManager;
	/** 友盟分享 **/
	private UMShareAPI umShareAPI;
	/** 友盟推送 **/
	private PushAgent mPushAgent;
	private UMengManager(Context context){
		umShareAPI = UMShareAPI.get(context);
		mPushAgent = PushAgent.getInstance(context);
		/** 用于调试，正式发布要注释掉 **/
//		mPushAgent.setDebugMode(true);
		/** 开启推送 **/
//		mPushAgent.enable();
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
			
	/** 获取新浪登录信息 **/
	public void getSinaInfo(Activity activity,UMAuthListener umAuthListener){
		SHARE_MEDIA media = SHARE_MEDIA.SINA;
		umShareAPI.getPlatformInfo(activity, media, umAuthListener);
	}
	
	/** QQ授权 **/
	public void qqAuth(Activity activity,UMAuthListener umAuthListener){
		SHARE_MEDIA media = SHARE_MEDIA.QQ;
		umShareAPI.doOauthVerify(activity, media, umAuthListener);
	}
	
	/** 获取QQ登录信息 **/
	public void getQqInfo(Activity activity,UMAuthListener umAuthListener){
		SHARE_MEDIA media = SHARE_MEDIA.QQ;
		umShareAPI.getPlatformInfo(activity, media, umAuthListener);
	}
	
	/** 取消QQ授权 **/
	public void cancleQqAuth(Activity activity,UMAuthListener umAuthListener){
		umShareAPI.deleteOauth(activity, SHARE_MEDIA.QQ, umAuthListener);
	}
	
	/** 微信登录授权 **/
	public void weixinAuth(Activity activity,UMAuthListener umAuthListener){
		SHARE_MEDIA media = SHARE_MEDIA.WEIXIN;
		umShareAPI.doOauthVerify(activity, media, umAuthListener);
	}
	
	/** 获取微信登录信息 **/
	public void getWeixinInfo(Activity activity,UMAuthListener umAuthListener){
		SHARE_MEDIA media = SHARE_MEDIA.WEIXIN;
		umShareAPI.getPlatformInfo(activity, media, umAuthListener);
	}
	
	/** 取消微信授权 **/
	public void cancleWeixinAuth(Activity activity,UMAuthListener umAuthListener){
		umShareAPI.deleteOauth(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
	}
	
	/** 是否已经授权 **/
	public boolean isAuth(Activity activity , SHARE_MEDIA media){
		return umShareAPI.isAuthorize(activity, media);
	}
	
	/** 回调 **/
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		umShareAPI.onActivityResult(requestCode, resultCode, data);
	}
	
	/** 检测应用是否安装 **/
	public boolean isInstall(Activity activity,SHARE_MEDIA media){
		return umShareAPI.isInstall(activity, media);
	}
	
	/** 设置友盟推送的渠道，跟应用渠道要一致 **/
	public void setMessageChannel(String channel){
		mPushAgent.setMessageChannel(channel);
	}
	
	/** 开启推送 **/
	public void enable(){
		mPushAgent.enable();
	}
	
	/** 开启推送 **/
	public void enable(IUmengRegisterCallback callback){
		mPushAgent.enable(callback);
	}
	
	/** 添加alias **/
	public Boolean addAlias(String alias,String aliasType) throws e, JSONException, Exception{
		return mPushAgent.addAlias(alias, aliasType);
	}
	
	/** 添加tag **/
	public Result addTag(String... tags) throws Exception{
		return mPushAgent.getTagManager().add(tags);
	}
}
