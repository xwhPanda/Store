package com.jiqu.umeng;

import android.content.Context;
import android.util.Log;

import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * @author xiongweihua
 * @version 2016-6-23
 */
public class UmengNotificationHandler extends UmengNotificationClickHandler {
	
	/** 启动APP **/
	@Override
	public void launchApp(Context context, UMessage message) {
		// TODO Auto-generated method stub
		super.launchApp(context, message);
	}
	
	/*** 打开指定的Activity */
	@Override
	public void openActivity(Context context, UMessage message) {
		// TODO Auto-generated method stub
		super.openActivity(context, message);
	}
	
	/** 打开指定的Url **/
	@Override
	public void openUrl(Context context, UMessage message) {
		// TODO Auto-generated method stub
		super.openUrl(context, message);
	}
	
	/** 处理自定义消息 **/
	@Override
	public void dealWithCustomAction(Context context, UMessage message) {
		// TODO Auto-generated method stub
		super.dealWithCustomAction(context, message);
	}
}
