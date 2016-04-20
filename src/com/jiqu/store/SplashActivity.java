package com.jiqu.store;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
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
}
