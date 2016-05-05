package com.jiqu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiqu.download.AppUtil;
import com.jiqu.interfaces.UninstallStateObserver;
import com.jiqu.object.InstalledApp;
import com.vr.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

public class UninstallAppAdatpter extends BaseAdapter implements UninstallStateObserver{
	private Context context;
	private List<InstalledApp> installedApps;
	private List<Holder> displayHolders;
	//0 安装；1 卸载
	private int action = -1;
	
	public UninstallAppAdatpter(Context context ,List<InstalledApp> installedApps) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.installedApps = installedApps;
		displayHolders = new ArrayList<UninstallAppAdatpter.Holder>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return installedApps.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return installedApps.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void startObserver() {
		InstalledAppTool.getInstance().registerObsverver(this);
	}

	public void stopObserver() {
		InstalledAppTool.getInstance().registerObsverver(this);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder(context);
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.setData(installedApps.get(position));
		displayHolders.add(holder);
		return holder.getRootView();
	}

	private class Holder{
		private View rootView;
		private Context context;
		
		private CheckBox checkBox;
		private ImageView appIcon;
		private TextView appName;
		private TextView appVersionName;
		private Button uninstall;
		
		private InstalledApp appData;
		private InstalledAppTool installedAppTool;
		
		public Holder(Context context){
			this.context = context;
			rootView = initView();
			rootView.setTag(this);
		}
		
		public View getRootView(){
			return rootView;
		}
		
		public InstalledAppTool getInstalledAppTool(){
			return installedAppTool;
		}
		
		public void setData(InstalledApp app){
			if (installedAppTool == null) {
				installedAppTool = InstalledAppTool.getInstance();
			}
			appData = app;
			refreshView(app);
		}
		
		public void refreshView(InstalledApp app){
			appIcon.setBackgroundDrawable(app.appIcon);
			appName.setText(app.name);
			appVersionName.setText(app.versionName);
			checkBox.setChecked(app.checkState);
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.uninstall_app_item, null);
			checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			appIcon = (ImageView) view.findViewById(R.id.appIcon);
			appName = (TextView) view.findViewById(R.id.appName);
			appVersionName = (TextView) view.findViewById(R.id.appVersionName);
			uninstall = (Button) view.findViewById(R.id.uninstall);
			
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					appData.checkState = isChecked;
					if (isChecked) {
						installedAppTool.addUninstall(appData);
					}else {
						installedAppTool.removeUninstall(appData);
					}
				}
			});
			
			uninstall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					installedAppTool.addUninstall(appData);
					installedAppTool.uninstall(context, appData);
				}
			});
			
			initViewSize(view);
			return view;
		}
		
		private void initViewSize(View view){
			UIUtil.setViewSize(checkBox, 56 * MetricsTool.Rx, 56 * MetricsTool.Ry);
			UIUtil.setViewSize(appIcon, 160 * MetricsTool.Rx, 160 * MetricsTool.Ry);
			UIUtil.setViewSize(uninstall, 60 * MetricsTool.Rx, 60 * MetricsTool.Rx);
			
			UIUtil.setTextSize(appName, 40);
			UIUtil.setTextSize(appVersionName, 30);
			
			AbsListView.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (255 * MetricsTool.Ry));
			view.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(checkBox, 20 * MetricsTool.Rx, 0, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(appName, 20 * MetricsTool.Rx, 60 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(appVersionName, 0, 10 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(uninstall, 0, 0, 60 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Holder> getDisplayedHolder(){
		synchronized (displayHolders) {
			return new ArrayList<Holder>(displayHolders);
		}
	}
	
	private void refreshHolder(final InstalledApp app){
		List<Holder> holders = getDisplayedHolder();
		for (final Holder holder : holders) {
			if (holder.appData.packageName.equals(app.packageName)) {
				AppUtil.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						installedApps.remove(app);
						displayHolders.remove(holder);
						notifyDataSetChanged();
					}
				});
			}
		}
	}

	@Override
	public void onUninstallStateChanged(InstalledApp app) {
		// TODO Auto-generated method stub
//		refreshHolder(app);
	}
	
	private void refreshHolder(final String pkg){
		List<Holder> holders = getDisplayedHolder();
		for (final Holder holder : holders) {
			if (holder.appData.packageName.equals(pkg)) {
				AppUtil.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						holder.installedAppTool.removeUninstallTask(pkg);
						synchronized (installedApps) {
							installedApps.remove(holder.appData);
						}
						synchronized (displayHolders){
							displayHolders.remove(holder);
						}
						notifyDataSetChanged();
					}
				});
			}
		}
	}
	
	public void changeAppPkg(String pkg,int action){
		this.action = action;
		if (action == 0) {//install
			final InstalledApp app = InstalledAppTool.getInstallApp(context, pkg);
			if (app != null) {
				AppUtil.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						synchronized (installedApps) {
							installedApps.add(app);
							notifyDataSetChanged();
						}
					}
				});
			}
		}else if (action == 1) {//uninstall
			refreshHolder(pkg);
		}
	}
	
	public void refreshHolder(final boolean isChecked){
		for (final InstalledApp app : installedApps) {
			AppUtil.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					app.checkState = isChecked;
					notifyDataSetChanged();
				}
			});
		}
	}
}
