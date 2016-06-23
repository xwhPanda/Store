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
		Log.i("TAG", "activity : " + message.activity);
		Log.i("TAG", "after_opne : " + message.after_open);
		Log.i("TAG", "alias : " + message.alias);
		Log.i("TAG", "custom : " + message.custom);
		Log.i("TAG", "display_type : " + message.display_type);
		Log.i("TAG", "icon : " + message.icon);
		Log.i("TAG", "img : " + message.img);
		Log.i("TAG", "largeIcon : " + message.largeIcon);
		Log.i("TAG", "msg_id : " + message.msg_id);
		Log.i("TAG", "task_id : " + message.task_id);
		Log.i("TAG", "text : " + message.text);
		Log.i("TAG", "ticker : " + message.ticker);
		Log.i("TAG", "title : " + message.title);
		Log.i("TAG", "url : " + message.url);
		Log.i("TAG", "sound : " + message.sound);
		Log.i("TAG", "extra : " + message.extra.toString());
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
		Log.i("TAG", "dealWithCustomAction");
	}
}
