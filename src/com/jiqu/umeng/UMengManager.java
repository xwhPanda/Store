package com.jiqu.umeng;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.jiqu.application.StoreApplication;
import com.jiqu.store.MainActivity;
import com.jiqu.tools.Constants;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UHandler;
import com.umeng.message.UmengRegistrar;
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
		mPushAgent.setDebugMode(false);
		/** 开启推送 **/
		mPushAgent.enable(new IUmengRegisterCallback() {
			
			@Override
			public void onRegistered(String arg0) {
				// TODO Auto-generated method stub
				Log.i("TAG", arg0);
				String tags = StoreApplication.CHANNEL + "," + Constants.BRAND;
				AddTag(tags);
				AddAlias(StoreApplication.DEVICE_ID, "device_id");
			}
		});
		setDisplayNotificationNumber(3);
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
	
	/** 设置NotificationHandler **/
	public void setNotificationClickHandler(UmengNotificationHandler notificationHandler){
		mPushAgent.setNotificationClickHandler(notificationHandler);
	}
	
	/** 设置service **/
	public void setPushIntentServiceClass(Class serviceClass){
		mPushAgent.setPushIntentServiceClass(serviceClass);
	}
	
	/** 设置handler **/
	public void setMessageHandler(UHandler uHandler){
		mPushAgent.setMessageHandler(uHandler);
	}
	
	/** 设置显示通知数量 **/
	public void setDisplayNotificationNumber(int number){
		mPushAgent.setDisplayNotificationNumber(number);
	}
	
	/** 添加alias **/
	public void AddAlias(String alias,String aliasType){
		new AddAliasTask(alias, aliasType).execute();
	}
	
	/** 添加tag 
	 * tag是以“,”分开的字符串**/
	public void AddTag(String tag){
		new AddTagTask(tag).execute();
	}
	
	/** 添加alias **/
	public class AddAliasTask extends AsyncTask<Void, Void, Boolean>{
		
		String alias;
		String aliasType;
		
		public AddAliasTask(String aliasString,String aliasTypeString) {
			// TODO Auto-generated constructor stub
			this.alias = aliasString;
			this.aliasType = aliasTypeString;
		}
		
		protected Boolean doInBackground(Void... params) {
			try {
				return UMengManager.getInstance().addAlias(StoreApplication.DEVICE_ID, "device_id");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
		}
	}
	
	/** 添加tag **/
	public class AddTagTask extends AsyncTask<Void, Void, String> {

		String tagString;
		String[] tags;

		public AddTagTask(String tag) {
			// TODO Auto-generated constructor stub
			tagString = tag;
			tags = tagString.split(",");
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				TagManager.Result result = UMengManager.getInstance().addTag(tags);
				return result.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Fail";
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("TAG", result);
		}
	}
}
