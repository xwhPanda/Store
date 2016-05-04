package com.jiqu.store;

import com.jiqu.tools.Constant;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.SharePreferenceTool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;

public class SplashActivity extends BaseActivity {
	private String deviceID;
	private RequestTool requestTool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
		deviceID = TelephonyMgr.getDeviceId(); 
		initFirst();
		
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
		SharedPreferences preferences = getSharedPreferences(Constant.STATISTICS_SHARE_PREFERENCE_NAME, MODE_PRIVATE);
		boolean isFirst = SharePreferenceTool.getBooleanFromPreferences(preferences, Constant.IS_FIRST, false);
		if (isFirst) {
			SharePreferenceTool.setValuePreferences(preferences, Constant.IS_FIRST, true);
			SharePreferenceTool.setValuePreferences(preferences, Constant.TIME, System.currentTimeMillis());
			firstUse();
		}else {
			boolean isSuccess = SharePreferenceTool.getBooleanFromPreferences(preferences, Constant.SEND_SUCCESS, false);
			if (!isSuccess) {
				long time = SharePreferenceTool.getLongFromPreferences(preferences, Constant.TIME, 0);
				if (time == 0) {
					time = System.currentTimeMillis();
					SharePreferenceTool.setValuePreferences(preferences, Constant.TIME, time);
				}
			}
		}
	}
	
	private void firstUse(){
		requestTool.getMap().clear();
		
	}
}
