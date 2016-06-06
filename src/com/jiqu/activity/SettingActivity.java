package com.jiqu.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.jiqu.application.StoreApplication;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.SharePreferenceTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.SettingsItem;
import com.jiqu.view.TitleView;

public class SettingActivity extends BaseActivity implements OnCheckedChangeListener,OnClickListener{
	private RelativeLayout parent;
	private TitleView titleView;
	private SettingsItem threadItem,loadLargImg,autoCheckVersion,checkVersion,modifyPassword,loginOut;
	private SharedPreferences preferences;
	private int threadNumbers = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences(Constants.SETTINGS_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.setting_layout;
	}

	private void initView(){
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		threadItem = (SettingsItem) findViewById(R.id.threadItem);
		loadLargImg = (SettingsItem) findViewById(R.id.loadLargImg);
		autoCheckVersion = (SettingsItem) findViewById(R.id.autoCheckVersion);
		checkVersion = (SettingsItem) findViewById(R.id.checkVersion);
		modifyPassword = (SettingsItem) findViewById(R.id.modifyPassword);
		loginOut = (SettingsItem) findViewById(R.id.loginOut);
		
		titleView.tip.setText(R.string.setting);
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		
		checkVersion.getVersionNameTextView().setText(UIUtil.getVersionName(this));
		threadItem.getTitleTextView().setText(R.string.changeThreadPool);
		loadLargImg.getTitleTextView().setText(R.string.loadLargImg);
		autoCheckVersion.getTitleTextView().setText(R.string.autoCheckVersion);
		checkVersion.getTitleTextView().setText(R.string.checkVersion);
		modifyPassword.getTitleTextView().setText(R.string.modifyPassword);
		loginOut.getTitleTextView().setText(R.string.loginOut);
		
		autoCheckVersion.getToggleButton().setOnCheckedChangeListener(this);
		
		autoCheckVersion.getToggleButton().setChecked(SharePreferenceTool.getBooleanFromPreferences(preferences, Constants.AUTO_CHECK_VERSION, true));
		threadItem.getThreadSizeTextView().setText(SharePreferenceTool.getIntFromPreferences(preferences, Constants.DOWNLOAD_THREAD_COUNTS, Constants.DEFAULT_DOWANLOAD_THREAD_COUNTS) + "");
		threadItem.getAddButton().setOnClickListener(this);
		threadItem.getSubtractButton().setOnClickListener(this);
		
		threadNumbers = SharePreferenceTool.getIntFromPreferences(preferences, Constants.DOWNLOAD_THREAD_COUNTS, Constants.DEFAULT_DOWANLOAD_THREAD_COUNTS);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(threadItem, 1040 * Rx, 150 * Ry);
		UIUtil.setViewHeight(loadLargImg, 150 * Ry);
		UIUtil.setViewHeight(autoCheckVersion, 150 * Ry);
		UIUtil.setViewHeight(checkVersion, 150 * Ry);
		UIUtil.setViewHeight(modifyPassword, 150 * Ry);
		UIUtil.setViewHeight(loginOut, 150 * Ry);
		
		try {
			UIUtil.setViewSizeMargin(threadItem, 0, 195 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(loadLargImg, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(autoCheckVersion, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(checkVersion, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(modifyPassword, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(loginOut, 0, 30 * Ry, 0, 0);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView == autoCheckVersion.getToggleButton()) {
			SharePreferenceTool.setValuePreferences(preferences, Constants.AUTO_CHECK_VERSION, isChecked);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == threadItem.getSubtractButton()) {
			if (threadNumbers > 1 && threadNumbers <= Constants.MAX_DOWANLOAD_THREAD_NUMBER) {
				threadNumbers -= 1;
			}
			
		}else if (v == threadItem.getAddButton()) {
			if (threadNumbers >= 1 && threadNumbers < 5) {
				threadNumbers += 1;
				
			}
		}
		SharePreferenceTool.setValuePreferences(preferences, Constants.DOWNLOAD_THREAD_COUNTS, threadNumbers);
		threadItem.getThreadSizeTextView().setText(threadNumbers +"");
	}
	
}
