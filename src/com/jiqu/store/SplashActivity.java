package com.jiqu.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.application.StoreApplication;
import com.jiqu.tools.Constants;
import com.jiqu.tools.MD5;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.SharePreferenceTool;
import com.vr.store.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashActivity extends BaseActivity {
	private RequestTool requestTool;
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		preferences = getSharedPreferences(Constants.STATISTICS_SHARE_PREFERENCE_NAME, MODE_PRIVATE);
		initFirst();
		active();
		
		new SplashCountTimer(3000, 1000).start();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.splash_layout;
	}
	
	private class SplashCountTimer extends CountDownTimer{

		public SplashCountTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			SplashActivity.this.finish();
		}
	}
	
	private void initFirst(){
		boolean isFirst = SharePreferenceTool.getBooleanFromPreferences(preferences, Constants.IS_FIRST, true);
		if (isFirst) {
			SharePreferenceTool.setValuePreferences(preferences, Constants.IS_FIRST, false);
			SharePreferenceTool.setValuePreferences(preferences, Constants.TIME, System.currentTimeMillis());
			firstUse();
		}else {
			boolean isSuccess = SharePreferenceTool.getBooleanFromPreferences(preferences, Constants.SEND_SUCCESS, false);
			if (!isSuccess) {
				firstUse();
				long time = SharePreferenceTool.getLongFromPreferences(preferences, Constants.TIME, 0);
				if (time == 0) {
					time = System.currentTimeMillis();
					SharePreferenceTool.setValuePreferences(preferences, Constants.TIME, time);
				}
			}
		}
	}
	
	private void active(){
		long time = SharePreferenceTool.getLongFromPreferences(preferences, Constants.TIME, 0);
		if (time == 0) {
			time = System.currentTimeMillis();
			SharePreferenceTool.setValuePreferences(preferences, Constants.TIME, time);
		}
		String url = RequestTool.ACTIVE_URL
				+ "?unique_id=" + StoreApplication.DEVICE_ID
				+ "&channel=" + StoreApplication.CHANNEL
				+ "&version=" + Constants.VERSION_NAME
				+ "&setuptime=" + time
				+ "&token=" + MD5.GetMD5Code(StoreApplication.DEVICE_ID 
						+ StoreApplication.CHANNEL
						+ Constants.VERSION_NAME 
						+ time
						+ RequestTool.PRIKEY);
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
		}, requestTool.getMap(), "active");
	}
	
	private void firstUse(){
		String url = RequestTool.INSTALL_URL
				+ "?unique_id=" + StoreApplication.DEVICE_ID
				+ "&channel=" + StoreApplication.CHANNEL
				+ "&version=" + Constants.VERSION_NAME
				+ "&token=" + MD5.GetMD5Code(StoreApplication.DEVICE_ID 
						+ StoreApplication.CHANNEL
						+ Constants.VERSION_NAME + RequestTool.PRIKEY);
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				JSONObject object = JSON.parseObject(arg0);
				int status = object.getIntValue("status");
				if (status == 1) {
					SharePreferenceTool.setValuePreferences(preferences, Constants.SEND_SUCCESS, true);
				}
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
			
		}, requestTool.getMap(), "firstInstall");
	}
}
