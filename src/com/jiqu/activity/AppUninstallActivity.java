package com.jiqu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jiqu.adapter.UninstallAppAdatpter;
import com.jiqu.adapter.UninstallSystemAppAdapter;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

public class AppUninstallActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
	private TitleView titleView;
	private LinearLayout btnLin;
	private Button personalAPP,systemAPP;
	private RelativeLayout allUninstallRel;
	private CheckBox allUninstallCB;
	private Button allUninstallBtn;
	private ListView appList,systemAppList;
	private UninstallAppAdatpter adapter;
	private UninstallSystemAppAdapter uninstallSystemAppAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(appInstallReceiver, filter);
		adapter.startObserver();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.app_uninstall_layout;
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		btnLin = (LinearLayout) findViewById(R.id.btnLin);
		personalAPP = (Button) findViewById(R.id.personalAPP);
		systemAPP = (Button) findViewById(R.id.systemAPP);
		allUninstallRel = (RelativeLayout) findViewById(R.id.allUninstallRel);
		allUninstallCB = (CheckBox) findViewById(R.id.allUninstallCB);
		allUninstallBtn = (Button) findViewById(R.id.allUninstallBtn);
		appList = (ListView) findViewById(R.id.appList);
		systemAppList = (ListView) findViewById(R.id.systemAppList);
		
		personalAPP.setOnClickListener(this);
		systemAPP.setOnClickListener(this);
		allUninstallBtn.setOnClickListener(this);
		allUninstallCB.setOnCheckedChangeListener(this);
		
		titleView.setActivity(this);
		titleView.tip.setText(R.string.appuninstall);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		
		initViewSize();
		initData();
	}
	
	private void initData(){
		adapter = new UninstallAppAdatpter(this, InstalledAppTool.getPersonalApp(this));
		appList.setAdapter(adapter);
		
		uninstallSystemAppAdapter = new UninstallSystemAppAdapter(this,InstalledAppTool.getSystemApp(this));
		systemAppList.setAdapter(uninstallSystemAppAdapter);
	}
	
	private void initViewSize(){
		UIUtil.setViewHeight(allUninstallRel, 170 * Ry);
		UIUtil.setViewSize(personalAPP, 525 * Rx, 85 * Ry);
		UIUtil.setViewSize(systemAPP, 525 * Rx, 85 * Ry);
		UIUtil.setViewSize(allUninstallCB, 66 * Rx, 66 * Rx);
		UIUtil.setViewSize(allUninstallBtn, 320 * Rx, 115 * Rx);
		
		UIUtil.setTextSize(personalAPP, 45);
		UIUtil.setTextSize(systemAPP, 45);
		UIUtil.setTextSize(allUninstallBtn, 55);
		
		try {
			UIUtil.setViewSizeMargin(btnLin, 0, 190 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(allUninstallCB, 23 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(allUninstallBtn, 0, 0, 60 * Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void changeButtonState(Button button){
		if (button == personalAPP) {
			personalAPP.setTextColor(getResources().getColor(R.color.white));
			personalAPP.setBackgroundResource(R.drawable.xinxi_sel);
			systemAPP.setTextColor(getResources().getColor(R.color.blue));
			systemAPP.setBackgroundResource(R.drawable.xinxi_nor);
		}else if (button == systemAPP) {
			systemAPP.setTextColor(getResources().getColor(R.color.white));
			systemAPP.setBackgroundResource(R.drawable.xinxi_sel);
			personalAPP.setTextColor(getResources().getColor(R.color.blue));
			personalAPP.setBackgroundResource(R.drawable.xinxi_nor);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.personalAPP) {
			changeButtonState(personalAPP);
			systemAppList.setVisibility(View.INVISIBLE);
			appList.setVisibility(View.VISIBLE);
			allUninstallRel.setVisibility(View.VISIBLE);
		}else if (v.getId() == R.id.systemAPP) {
			changeButtonState(systemAPP);
			appList.setVisibility(View.INVISIBLE);
			systemAppList.setVisibility(View.VISIBLE);
			allUninstallRel.setVisibility(View.GONE);
		}else if (v.getId() == R.id.allUninstallBtn) {
			InstalledAppTool.getInstance().uninstallAll(this);
		}
	}
	
	private BroadcastReceiver appInstallReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
				String addPkg = intent.getDataString().split(":")[1];
				//应用安装
				Log.i("TAG", "安装应用 ： " + addPkg);
				if (adapter != null) {
					adapter.changeAppPkg(addPkg, 0);
				}
			}else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
				String removePkg = intent.getDataString().split(":")[1];
				//应用卸载
				Log.i("TAG", "卸载完成 ： " + removePkg);
				if (adapter != null) {
					adapter.changeAppPkg(removePkg, 1);
				}
			}
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(appInstallReceiver);
		adapter.stopObserver();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView == allUninstallCB) {
			if (adapter != null) {
				adapter.refreshHolder(isChecked);
			}
		}
	};
}
