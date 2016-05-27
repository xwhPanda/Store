package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jiqu.adapter.DownloadedAdapter;
import com.jiqu.adapter.DownloadingAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.UnZipManager;
import com.jiqu.object.InstalledApp;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

import de.greenrobot.dao.query.QueryBuilder;

public class DownloadManagerActivity extends BaseActivity implements OnClickListener
	,OnCheckedChangeListener,OnItemLongClickListener{
	private TitleView titleView;
	private LinearLayout btnLin;
	private Button downloading,downloaded;
	private RelativeLayout allStartRel;
	private CheckBox allStartCB,allDeleteCB;
	private Button allStartBtn,allDeleteBtn;
	private RelativeLayout allDeleteRel;
	private ListView downloadingList,downloadedList;
	private DownloadingAdapter downloadingAdapter;
	private DownloadedAdapter downloadedAdapter;
	private List<DownloadAppinfo> downloadingApps = new ArrayList<DownloadAppinfo>();
	private List<DownloadAppinfo> downloadedApps = new ArrayList<DownloadAppinfo>();
	
	private boolean downloadingListShowing = false;
	private boolean downloadedListShowing = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		downloadingAdapter = new DownloadingAdapter(this, downloadingApps,handler);
		downloadingList.setAdapter(downloadingAdapter);
		downloadedAdapter = new DownloadedAdapter(this, downloadedApps);
		downloadedList.setAdapter(downloadedAdapter);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		new DataTask().execute("");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (downloadingAdapter != null) {
			downloadingAdapter.stopObserver();
		}
		if (downloadedAdapter != null) {
			downloadedAdapter.stopObserver();
		}
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.download_manager_layout;
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		btnLin = (LinearLayout) findViewById(R.id.btnLin);
		downloading = (Button) findViewById(R.id.downloading);
		downloaded = (Button) findViewById(R.id.downloaded);
		allStartRel = (RelativeLayout) findViewById(R.id.allStartRel);
		allDeleteCB = (CheckBox) findViewById(R.id.allDeleteCB);
		allStartCB = (CheckBox) findViewById(R.id.allStartCB);
		allStartBtn = (Button) findViewById(R.id.allStartBtn);
		allDeleteBtn = (Button) findViewById(R.id.allDeleteBtn);
		allDeleteRel = (RelativeLayout) findViewById(R.id.allDeleteRel);
		downloadingList = (ListView) findViewById(R.id.downloadingList);
		downloadedList = (ListView) findViewById(R.id.downloadedList);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.downloadManager);
		
		downloaded.setOnClickListener(this);
		downloading.setOnClickListener(this);
		allDeleteBtn.setOnClickListener(this);
		allStartBtn.setOnClickListener(this);
		allDeleteCB.setOnCheckedChangeListener(this);
		allStartCB.setOnCheckedChangeListener(this);
		downloadingList.setOnItemLongClickListener(this);
		downloadedList.setOnItemLongClickListener(this);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewHeight(allDeleteRel, 170 * Ry);
		UIUtil.setViewHeight(allStartRel, 170 * Ry);
		UIUtil.setViewSize(downloading, 525 * Rx, 85 * Ry);
		UIUtil.setViewSize(downloaded, 525 * Rx, 85 * Ry);
		UIUtil.setViewSize(allDeleteCB, 66 * Rx, 66 * Rx);
		UIUtil.setViewSize(allDeleteBtn, 320 * Rx, 115 * Rx);
		UIUtil.setViewSize(allStartCB, 66 * Rx, 66 * Rx);
		UIUtil.setViewSize(allStartBtn, 320 * Rx, 115 * Rx);
		
		UIUtil.setTextSize(downloaded, 45);
		UIUtil.setTextSize(downloading, 45);
		UIUtil.setTextSize(allDeleteBtn, 55);
		UIUtil.setTextSize(allStartBtn, 55);
		
		try {
			UIUtil.setViewSizeMargin(btnLin, 0, 190 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(allStartCB, 23 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(allStartBtn, 0, 0, 60 * Rx, 0);
			UIUtil.setViewSizeMargin(allDeleteCB, 23 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(allDeleteBtn, 0, 0, 60 * Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void changeButtonState(Button button){
		if (button == downloading) {
			downloading.setTextColor(getResources().getColor(R.color.white));
			downloading.setBackgroundResource(R.drawable.xinxi_sel);
			downloaded.setTextColor(getResources().getColor(R.color.blue));
			downloaded.setBackgroundResource(R.drawable.xinxi_nor);
		}else if (button == downloaded) {
			downloaded.setTextColor(getResources().getColor(R.color.white));
			downloaded.setBackgroundResource(R.drawable.xinxi_sel);
			downloading.setTextColor(getResources().getColor(R.color.blue));
			downloading.setBackgroundResource(R.drawable.xinxi_nor);
		}
	}
	
	class DataTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			initData();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			downloadingAdapter.startObserver();
			downloadedAdapter.startObserver();
		}
		
	}
	
	private void initData(){
		QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
		List<DownloadAppinfo> infos1 = qb.where(Properties.HasFinished.eq(false)).list();
		for(DownloadAppinfo info : infos1){
			if (info.getDownloadState() == DownloadManager.STATE_DOWNLOADING
					|| info.getDownloadState() == DownloadManager.STATE_WAITING) {
				if (!DownloadManager.getInstance().isDownloading(info.getId())) {
					info.setDownloadState(DownloadManager.STATE_PAUSED);
					DownloadManager.DBManager.insertOrReplace(info);
				}
			}
		}
		downloadingApps.clear();
		downloadingApps.addAll(infos1);
		downloadingAdapter.putAllMap(false);
		handler.sendEmptyMessage(2);
		
		QueryBuilder<DownloadAppinfo> qb1 = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
		List<DownloadAppinfo> infos2 = qb1.where(Properties.HasFinished.eq(true)).list();
		List<InstalledApp> apps = InstalledAppTool.getPersonalApp(this);
		for (DownloadAppinfo info : infos2) {
				if (info.getDownloadState() != DownloadManager.STATE_INSTALLED) {
					int state = InstalledAppTool.contain(apps,info.getPackageName());
					if (state == DownloadManager.STATE_INSTALLED) {
						info.setDownloadState(state);
						DownloadManager.DBManager.insertOrReplace(info);
					}
				}else if (info.getDownloadState() == DownloadManager.STATE_INSTALLED) {
					int state = InstalledAppTool.contain(apps,info.getPackageName(), Integer.parseInt(info.getVersionCode()));
					if (state != DownloadManager.STATE_INSTALLED) {
						if (info.getIsZip()) {
							info.setDownloadState(DownloadManager.STATE_UNZIPED);
						}else {
							info.setDownloadState(DownloadManager.STATE_DOWNLOADED);
						}
						DownloadManager.DBManager.insertOrReplace(info);
					}
				}
			if (info.getDownloadState() == DownloadManager.STATE_UNZIPING 
					&& !UnZipManager.getInstance().isUnZiping(info.getPackageName())) {
				info.setDownloadState(DownloadManager.STATE_UNZIP_FAILED);
				DownloadManager.DBManager.insertOrReplace(info);
			}
		}
		downloadedApps.clear();
		downloadedApps.addAll(infos2);
		downloadedAdapter.putAllMap(false);
		handler.sendEmptyMessage(3);
	}
	
	class DataRefresh extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			refreshData();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
		}
		
	}
	
	private void refreshData(){
		List<InstalledApp> installedApps = InstalledAppTool.getPersonalApp(DownloadManagerActivity.this);
		QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
		List<DownloadAppinfo> apps = qb.where(Properties.HasFinished.eq(false)).list();
		for(DownloadAppinfo info : apps){
			if (info.getDownloadState() == DownloadManager.STATE_DOWNLOADING
					|| info.getDownloadState() == DownloadManager.STATE_WAITING) {
				if (!DownloadManager.getInstance().isDownloading(info.getId())) {
					info.setDownloadState(DownloadManager.STATE_PAUSED);
					DownloadManager.DBManager.insertOrReplace(info);
				}
			}
		}
		downloadingAdapter.clearHolders();
		downloadingApps.clear();
		downloadingApps.addAll(apps);
//		handler.sendEmptyMessage(2);
		
		List<DownloadAppinfo> apps2 = StoreApplication.daoSession.getDownloadAppinfoDao()
				.queryBuilder().where(Properties.HasFinished.eq(true)).list();
		for (DownloadAppinfo info : apps2) {
			if (info.getDownloadState() != DownloadManager.STATE_INSTALLED && info.getVersionCode() != null) {
				int state = InstalledAppTool.contain(installedApps,info.getPackageName(), Integer.parseInt(info.getVersionCode()));
				if (state == DownloadManager.STATE_INSTALLED) {
					info.setDownloadState(state);
					DownloadManager.DBManager.insertOrReplace(info);
				}
			}else if (info.getDownloadState() == DownloadManager.STATE_INSTALLED) {
				int state = InstalledAppTool.contain(installedApps,info.getPackageName(), Integer.parseInt(info.getVersionCode()));
				if (state != DownloadManager.STATE_INSTALLED) {
					if (info.getIsZip()) {
						info.setDownloadState(DownloadManager.STATE_UNZIPED);
					}else {
						info.setDownloadState(DownloadManager.STATE_DOWNLOADED);
					}
					DownloadManager.DBManager.insertOrReplace(info);
				}
			}
			if (info.getDownloadState() == DownloadManager.STATE_UNZIPING 
					&& !UnZipManager.getInstance().isUnZiping(info.getPackageName())) {
				info.setDownloadState(DownloadManager.STATE_UNZIP_FAILED);
				DownloadManager.DBManager.insertOrReplace(info);
			}
		}
		downloadedApps.clear();
		downloadedApps.addAll(apps2);
		handler.sendEmptyMessage(3);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {//有应用下载完成
				new DataRefresh().execute("");
			}else if (msg.what == 2) {
				downloadingAdapter.notifyDataSetChanged();
			}else if (msg.what == 3) {
				downloadedAdapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView == allStartCB) {
			downloadingAdapter.putAllMap(isChecked);
			downloadingAdapter.notifyDataSetChanged();
		}else if (buttonView == allDeleteCB) {
			downloadedAdapter.putAllMap(isChecked);
			downloadedAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.downloading:
			downloadedListShowing = false;
			changeButtonState(downloading);
			downloadedList.setVisibility(View.GONE);
			downloadedAdapter.showAllCheckbox(false);
			allDeleteRel.setVisibility(View.GONE);
			downloadingList.setVisibility(View.VISIBLE);
			break;

		case R.id.downloaded:
			downloadingListShowing = false;
			changeButtonState(downloaded);
			downloadingList.setVisibility(View.GONE);
			downloadingAdapter.showAllCheckbox(false);
			allStartRel.setVisibility(View.GONE);
			downloadedList.setVisibility(View.VISIBLE);
			break;
			
		case R.id.allStartBtn:
			if (downloadedAdapter != null) {
				downloadingAdapter.startDownloadAll();
			}
			break;
			
		case R.id.allDeleteBtn:
			if (downloadedAdapter != null) {
				downloadedAdapter.deleteAll();
			}
			break;
		}
	}

	@Override
	protected void installEvent(String installPackageName) {
		// TODO Auto-generated method stub
		for (DownloadAppinfo info : downloadedApps) {
			if (installPackageName.equals(info.getPackageName())) {
				info.setDownloadState(DownloadManager.STATE_INSTALLED);
			}
		}
		downloadedAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void unInstallEvent(String uninstallPackageName) {
		// TODO Auto-generated method stub
		for (DownloadAppinfo info : downloadedApps) {
			if (uninstallPackageName.equals(info.getPackageName())) {
				if (info.getIsZip()) {
					info.setDownloadState(DownloadManager.STATE_UNZIPED);
				}else {
					info.setDownloadState(DownloadManager.STATE_DOWNLOADED);
				}
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (parent.getId() == R.id.downloadingList) {
			allStartRel.setVisibility(View.VISIBLE);
			downloadingListShowing = true;
			downloadingAdapter.showAllCheckbox(true);
			downloadingAdapter.notifyDataSetChanged();
		}else if (parent.getId() == R.id.downloadedList) {
			downloadedListShowing = true;
			allDeleteRel.setVisibility(View.VISIBLE);
			downloadedAdapter.showAllCheckbox(true);
			downloadedAdapter.notifyDataSetChanged();
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (downloadingListShowing) {
				downloadingListShowing = false;
				downloadingAdapter.showAllCheckbox(false);
				downloadingAdapter.notifyDataSetChanged();
				allStartRel.setVisibility(View.GONE);
				return true;
			}else if (downloadedListShowing) {
				downloadedListShowing = false;
				downloadedAdapter.showAllCheckbox(false);
				downloadedAdapter.notifyDataSetChanged();
				allDeleteRel.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
