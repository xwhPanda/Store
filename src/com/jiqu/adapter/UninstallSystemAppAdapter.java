package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.InstalledApp;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class UninstallSystemAppAdapter extends BaseAdapter {
	private Context context;
	private List<InstalledApp> installedApps;
	
	public UninstallSystemAppAdapter(Context context,List<InstalledApp> installedApps) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.installedApps = installedApps;
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
		
		public Holder(Context context){
			this.context = context;
			rootView = initView();
			rootView.setTag(this);
		}
		
		public void setData(InstalledApp app){
			appData = app;
			refreshView(app);
		}
		
		public View getRootView(){
			return rootView;
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
			
			checkBox.setVisibility(View.INVISIBLE);
			
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					appData.checkState = isChecked;
				}
			});
			
			uninstall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "系统应用，不能卸载", Toast.LENGTH_SHORT).show();
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

}
