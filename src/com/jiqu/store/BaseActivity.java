package com.jiqu.store;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.InstalledApp;
import com.jiqu.tools.Constants;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.NetReceiver;
import com.jiqu.tools.NetReceiver.OnNetChangeListener;
import com.jiqu.view.NetChangeDialog;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseActivity extends Activity implements OnNetChangeListener{
	protected float Rx;
	protected float Ry;
	protected NetChangeDialog netChangeDialog;
	private NetReceiver netReceiver;
	private InstalledAppTool installedAppTool;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MetricsTool.initMetrics(getWindowManager());
		PushAgent.getInstance(this).onAppStart();
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		installedAppTool = new InstalledAppTool();
		if (getContentView() != 0) {
			setContentView(getContentView());
		}
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(appInstallReceiver, filter);
		
		netReceiver = NetReceiver.getInstance();
		netReceiver.setNetChangeListener(this);
		
		netChangeDialog = new NetChangeDialog(this);
		
		netChangeDialog.setNegativeListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DownloadManager.getInstance().startAll();
				netChangeDialog.dismiss();
			}
		});
		
		netChangeDialog.setPositiveListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				netChangeDialog.dismiss();
			}
		});
		
		addToActivityList();
		
	}
	
	@Override
	protected void attachBaseContext(Context newBase) {
		// TODO Auto-generated method stub
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
	
	public abstract int getContentView();
	public abstract void removeFromActivityList();
	public abstract void addToActivityList();
	
	
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
	
	protected void setState(List<GameInfo> infos,int count){
		List<InstalledApp> apps = installedAppTool.getPersonalApp(this);
		int size = count;
		if (infos.size() < count) {
			size = infos.size();
		}
		for (int i = infos.size() - size; i < infos.size(); i++) {
			if (infos.get(i).getAdapterType() == 0) {
				DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(infos.get(i).getId()));
				int state = InstalledAppTool.contain(apps, infos.get(i).getPackage_name(), Integer.parseInt(infos.get(i).getVersion()));
				if (state != -1) {
					infos.get(i).setState(state);
				} else {
					if (info != null && (info.getDownloadState() == DownloadManager.STATE_INSTALLED 
							|| info.getDownloadState() == DownloadManager.STATE_NEED_UPDATE)) {
						DownloadManager.DBManager.delete(info);
					}
				}
			}
		}
	}
	
	@Override
	public void onNetChange(int netType) {
		// TODO Auto-generated method stub
		if (netType != NetReceiver.NET_WIFI && DownloadManager.getInstance().hasDownloading()) {
			if (!netChangeDialog.isShowing()) {
				DownloadManager.getInstance().pauseAllExit();
				netChangeDialog.show();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(appInstallReceiver);
		removeFromActivityList();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ("true".equals(getIntent().getStringExtra("isPush"))
					&& Constants.ACTIVITY_LIST.size() <= 1) {
				startActivity(new Intent(this, SplashActivity.class));
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
