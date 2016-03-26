package com.jiqu.store;

import com.jiqu.tools.MetricsTool;

import android.app.Activity;
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
	
	public abstract int getContentView();
}
