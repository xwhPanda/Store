package com.jiqu.umeng;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

/**
 * @author xiongweihua
 * @version 2016-6-23
 */
public class PushIntentService extends UmengBaseIntentService {

	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onMessage(context, intent);
		try {
			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
			UMessage msg = new UMessage(new JSONObject(message));
			Log.i("TAG", "message : " + message);
			
			boolean isClickOrDismissed = true;
			if(isClickOrDismissed) {
				//完全自定义消息的点击统计
				UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
			} else {
				//完全自定义消息的忽略统计
				UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
