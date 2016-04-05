package com.jiqu.store;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.jiqu.tools.MetricsTool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
	protected float Rx;
	protected float Ry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		setContentView(getContentView());
	}
	
	@Override
	protected void attachBaseContext(Context newBase) {
		// TODO Auto-generated method stub
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
	
	public abstract int getContentView();
}
