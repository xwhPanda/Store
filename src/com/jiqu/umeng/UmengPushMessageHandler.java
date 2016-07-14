package com.jiqu.umeng;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import com.jiqu.application.StoreApplication;
import com.jiqu.database.MessageTable;
import com.jiqu.database.NoticeTable;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * @author xiongweihua
 * @version 2016-6-24
 */
public class UmengPushMessageHandler extends UmengMessageHandler {

	public void dealWithCustomMessage(Context context, UMessage uMessage) {
		MessageTable message = new MessageTable();
		message.setMessage(uMessage.getRaw().toString());
		message.setTime(String.valueOf(System.currentTimeMillis()));
		StoreApplication.daoSession.getMessageTableDao().insertOrReplace(message);
	}

	@Override
	public Notification getNotification(Context context, UMessage uMessage) {
		// TODO Auto-generated method stub
		NoticeTable notice = new NoticeTable();
		notice.setNotice(uMessage.getRaw().toString());
		notice.setTime(String.valueOf(System.currentTimeMillis()));
		StoreApplication.daoSession.getNoticeTableDao().insertOrReplace(notice);
		return super.getNotification(context, uMessage);
	}
}