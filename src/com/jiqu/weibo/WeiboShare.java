package com.jiqu.weibo;

import com.jiqu.application.StoreApplication;
import com.jiqu.tools.Constants;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

public class WeiboShare {
	
	private static WeiboShare instance;
	private IWeiboShareAPI mWeiboShareAPI;
	
	public static WeiboShare getInstance(){
		if (instance == null) {
			instance = new WeiboShare();
		}
		return instance;
	}
	
	public void initialize(){
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(StoreApplication.context, Constants.APP_KEY);
		mWeiboShareAPI.registerApp();
	}
	
}
