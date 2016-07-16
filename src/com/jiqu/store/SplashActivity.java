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
import com.jiqu.tools.UIUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.vr.store.R;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

public class SplashActivity extends BaseActivity{
	private RequestTool requestTool;
	private SharedPreferences preferences;
	private Bitmap bitmap;
	private ImageView img;
	private ImageView bgRel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		requestTool = RequestTool.getInstance();

		img = (ImageView) findViewById(R.id.img);
		bgRel = (ImageView) findViewById(R.id.bgRel);

		bitmap = UIUtil.readBitmap(StoreApplication.context, R.drawable.splash_bg);
		bgRel.setImageBitmap(bitmap);

		img.setBackgroundResource(R.drawable.welcome);
		preferences = getSharedPreferences(Constants.STATISTICS_SHARE_PREFERENCE_NAME, MODE_PRIVATE);
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.splash_layout;
	}

	private class SplashCountTimer extends CountDownTimer {

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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bitmap.recycle();
	}

	private void initFirst() {
		boolean isFirst = SharePreferenceTool.getBooleanFromPreferences(preferences, Constants.IS_FIRST, true);
		if (isFirst) {
			SharePreferenceTool.setValuePreferences(preferences, Constants.IS_FIRST, false);
			SharePreferenceTool.setValuePreferences(preferences, Constants.TIME, System.currentTimeMillis());
			firstUse();
		} else {
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

	private void active() {
		long time = SharePreferenceTool.getLongFromPreferences(preferences, Constants.TIME, 0);
		if (time == 0) {
			time = System.currentTimeMillis();
			SharePreferenceTool.setValuePreferences(preferences, Constants.TIME, time);
		}
		String url = RequestTool.ACTIVE_URL + "?unique_id=" + Constants.DEVICE_ID + "&channel=" + StoreApplication.CHANNEL + "&version=" + Constants.VERSION_NAME + "&setuptime=" + time + "&token=" + MD5.GetMD5Code(Constants.DEVICE_ID + StoreApplication.CHANNEL + Constants.VERSION_NAME + time + RequestTool.PRIKEY);
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

	private void firstUse() {
		String url = RequestTool.INSTALL_URL + "?unique_id=" + Constants.DEVICE_ID + "&channel=" + StoreApplication.CHANNEL + "&version=" + Constants.VERSION_NAME + "&token=" + MD5.GetMD5Code(Constants.DEVICE_ID + StoreApplication.CHANNEL + Constants.VERSION_NAME + RequestTool.PRIKEY);
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
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}

		}, requestTool.getMap(), "firstInstall");
	}

	@Override
	public void removeFromActivityList() {
		// TODO Auto-generated method stub
		Constants.ACTIVITY_LIST.remove(this);
	}

	@Override
	public void addToActivityList() {
		// TODO Auto-generated method stub
		Constants.ACTIVITY_LIST.add(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("SplashActivity");
		MobclickAgent.onResume(this);
		
		initFirst();
		active();
		new SplashCountTimer(3000, 1000).start();
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("SplashActivity");
		MobclickAgent.onPause(this);
	}
}
