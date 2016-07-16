package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.jiqu.application.StoreApplication;
import com.jiqu.object.InstalledApp;
import com.jiqu.store.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;


public class AppUninstallActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
	private RelativeLayout parent;
	private TitleView titleView;
	private LinearLayout btnLin;
	private Button personalAPP,systemAPP;
	private RelativeLayout allUninstallRel;
	private CheckBox allUninstallCB;
	private Button allUninstallBtn;
	private ListView appList,systemAppList;
	private UninstallAppAdatpter adapter;
	private UninstallSystemAppAdapter uninstallSystemAppAdapter;
	private InstalledAppTool installedAppTool;
	
	private List<InstalledApp> personalApps = new ArrayList<InstalledApp>();
	private List<InstalledApp> systemApps = new ArrayList<InstalledApp>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.app_uninstall_layout;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("AppUninstallActivity");
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("AppUninstallActivity");
		MobclickAgent.onPause(this);
	}
	
	private void initView(){
		installedAppTool = new InstalledAppTool();
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
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
		adapter = new UninstallAppAdatpter(this, personalApps);
		appList.setAdapter(adapter);
		new PersonalAppTask().execute();
		
		uninstallSystemAppAdapter = new UninstallSystemAppAdapter(this,systemApps);
		systemAppList.setAdapter(uninstallSystemAppAdapter);
		new SystemAppTask().execute();
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
//			allUninstallRel.setVisibility(View.VISIBLE);
		}else if (v.getId() == R.id.systemAPP) {
			changeButtonState(systemAPP);
			appList.setVisibility(View.INVISIBLE);
			systemAppList.setVisibility(View.VISIBLE);
//			allUninstallRel.setVisibility(View.GONE);
		}else if (v.getId() == R.id.allUninstallBtn) {
//			InstalledAppTool.getInstance().uninstallAll(this);
		}
	}
	
	
	protected void onDestroy() {
		super.onDestroy();
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
	
	@Override
	protected void installEvent(String installPackageName) {
		// TODO Auto-generated method stub
		if (adapter != null) {
			adapter.changeAppPkg(installPackageName, 0);
		}
	}
	
	@Override
	protected void unInstallEvent(String uninstallPackageName) {
		// TODO Auto-generated method stub
		if (adapter != null) {
			adapter.changeAppPkg(uninstallPackageName, 1);
		}
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
	
	/** 获取第三方APP
	 * 用AsyncTask不然会导致打开此Activity缓慢，影响体验 **/
	class PersonalAppTask extends AsyncTask<Void, Void, List<InstalledApp>>{

		@Override
		protected List<InstalledApp> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return installedAppTool.getPersonalApp(AppUninstallActivity.this);
		}
		
		@Override
		protected void onPostExecute(List<InstalledApp> result) {
			// TODO Auto-generated method stub
			personalApps.clear();
			personalApps.addAll(result);
			adapter.notifyDataSetChanged();
		}
	}
	
	/** 获取第系统APP
	 * 用AsyncTask不然会导致打开此Activity缓慢，影响体验 **/
	class SystemAppTask extends AsyncTask<Void, Void, List<InstalledApp>>{

		@Override
		protected List<InstalledApp> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return installedAppTool.getSystemApp(AppUninstallActivity.this);
		}
		
		@Override
		protected void onPostExecute(List<InstalledApp> result) {
			// TODO Auto-generated method stub
			systemApps.clear();
			systemApps.addAll(result);
			uninstallSystemAppAdapter.notifyDataSetChanged();
		}
	}
}
