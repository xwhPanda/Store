package com.jiqu.store;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.jiqu.tools.MetricsTool;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(appInstallReceiver, filter);
	}
	
	@Override
	protected void attachBaseContext(Context newBase) {
		// TODO Auto-generated method stub
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
	
	public abstract int getContentView();
	
	private BroadcastReceiver appInstallReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
				String addPkg = intent.getDataString().split(":")[1];
				installEvent(addPkg);
			}else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
				String removePkg = intent.getDataString().split(":")[1];
				unInstallEvent(removePkg);
			}
		}
	};
	
	protected void installEvent(String installPackageName){
		
	}
	
	protected void unInstallEvent(String uninstallPackageName){
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(appInstallReceiver);
	}
}
