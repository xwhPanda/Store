package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.jiqu.adapter.DownloadedAdapter;
import com.jiqu.adapter.DownloadingAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.UnZipManager;
import com.jiqu.interfaces.UninstallStateObserver;
import com.jiqu.object.InstalledApp;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

import de.greenrobot.dao.query.QueryBuilder;

public class DownloadManagerActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		downloadingAdapter.startObserver();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		downloadingAdapter.stopObserver();
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
		
		initViewSize();
		initData();
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
		downloadingAdapter = new DownloadingAdapter(this, downloadingApps,handler);
		downloadingList.setAdapter(downloadingAdapter);
		
		QueryBuilder<DownloadAppinfo> qb1 = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
		List<DownloadAppinfo> infos2 = qb1.where(Properties.HasFinished.eq(true)).list();
		for (DownloadAppinfo info : infos2) {
				if (info.getDownloadState() != DownloadManager.STATE_INSTALLED) {
					int state = InstalledAppTool.contain(info.getPackageName(), Integer.parseInt(info.getVersionCode()));
					if (state == DownloadManager.STATE_INSTALLED) {
						info.setDownloadState(state);
						DownloadManager.DBManager.insertOrReplace(info);
					}
				}else if (info.getDownloadState() == DownloadManager.STATE_INSTALLED) {
					int state = InstalledAppTool.contain(info.getPackageName(), Integer.parseInt(info.getVersionCode()));
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
		downloadedAdapter = new DownloadedAdapter(this, downloadedApps);
		downloadedList.setAdapter(downloadedAdapter);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {//有应用下载完成
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
				downloadingAdapter.notifyDataSetChanged();
				
				List<DownloadAppinfo> apps2 = StoreApplication.daoSession.getDownloadAppinfoDao()
						.queryBuilder().where(Properties.HasFinished.eq(true)).list();
				for (DownloadAppinfo info : apps2) {
					if (info.getDownloadState() != DownloadManager.STATE_INSTALLED) {
						int state = InstalledAppTool.contain(info.getPackageName(), Integer.parseInt(info.getVersionCode()));
						if (state == DownloadManager.STATE_INSTALLED) {
							info.setDownloadState(state);
							DownloadManager.DBManager.insertOrReplace(info);
						}
					}else if (info.getDownloadState() == DownloadManager.STATE_INSTALLED) {
						int state = InstalledAppTool.contain(info.getPackageName(), Integer.parseInt(info.getVersionCode()));
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
				downloadedAdapter.clearHolders();
				downloadedApps.addAll(apps2);
				downloadedAdapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView == allStartCB) {
			downloadingAdapter.refreshHolderForCheck(isChecked);
		}else if (buttonView == allDeleteCB) {
			downloadedAdapter.refreshHolderForChecked(isChecked);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.downloading:
			changeButtonState(downloading);
			downloadedList.setVisibility(View.INVISIBLE);
			allDeleteRel.setVisibility(View.INVISIBLE);
			allStartRel.setVisibility(View.VISIBLE);
			downloadingList.setVisibility(View.VISIBLE);
			break;

		case R.id.downloaded:
			changeButtonState(downloaded);
			downloadingList.setVisibility(View.INVISIBLE);
			allStartRel.setVisibility(View.INVISIBLE);
			allDeleteRel.setVisibility(View.VISIBLE);
			downloadedList.setVisibility(View.VISIBLE);
			break;
			
		case R.id.allStartBtn:
			downloadingAdapter.startDownloadAll();
			break;
			
		case R.id.allDeleteBtn:
			downloadedAdapter.deleteAll();
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
}
