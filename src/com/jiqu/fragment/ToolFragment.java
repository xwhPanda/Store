package com.jiqu.fragment;

import com.jiqu.activity.AppUninstallActivity;
import com.jiqu.activity.ClearCacheActivity;
import com.jiqu.activity.DeepClearActivity;
import com.jiqu.activity.DownloadManagerActivity;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.ToolItemView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToolFragment extends Fragment implements OnClickListener{
	private float Rx,Ry;
	private View view;
	private ImageView accountIcon;
	private LinearLayout accountLin;
	private TextView accountName;
	private ImageView messageImg;
	private TextView level;
	private RelativeLayout itemRel;
	
	private ToolItemView downloadItem,resourcesItem,uninstallItem;
	private ToolItemView clearCacheItem,clearUpItem,powerItem;
	private ToolItemView commomProblemItem,shareItem,aboutUsItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.tool, null, false);
		
		init();
		return view;
	}
	
	private void init(){
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		accountIcon = (ImageView) view.findViewById(R.id.accountIcon);
		accountLin = (LinearLayout) view.findViewById(R.id.accountLin);
		accountName = (TextView) view.findViewById(R.id.accountName);
		messageImg = (ImageView) view.findViewById(R.id.messageImg);
		level = (TextView) view.findViewById(R.id.level);
		
		itemRel = (RelativeLayout) view.findViewById(R.id.itemRel);
		
		downloadItem = (ToolItemView) view.findViewById(R.id.downloadItem);
		resourcesItem = (ToolItemView) view.findViewById(R.id.resourcesItem);
		uninstallItem = (ToolItemView) view.findViewById(R.id.uninstallItem);
		clearCacheItem = (ToolItemView) view.findViewById(R.id.clearCacheItem);
		clearUpItem = (ToolItemView) view.findViewById(R.id.clearUpItem);
		powerItem = (ToolItemView) view.findViewById(R.id.powerItem);
		commomProblemItem = (ToolItemView) view.findViewById(R.id.commomProblemItem);
		shareItem = (ToolItemView) view.findViewById(R.id.shareItem);
		aboutUsItem = (ToolItemView) view.findViewById(R.id.aboutUsItem);
		
		downloadItem.setImgBgRes(R.drawable.tool_download_selector);
		downloadItem.setTitleText(R.string.toolDownload);
		resourcesItem.setImgBgRes(R.drawable.tool_resources_selector);
		resourcesItem.setTitleText(R.string.toolResources);
		uninstallItem.setImgBgRes(R.drawable.tool_uninstall_selector);
		uninstallItem.setTitleText(R.string.toolUninstall);
		clearCacheItem.setImgBgRes(R.drawable.tool_clearcache_selector);
		clearCacheItem.setTitleText(R.string.toolClearCache);
		clearUpItem.setImgBgRes(R.drawable.tool_clearup_selector);
		clearUpItem.setTitleText(R.string.toolClearUp);
		powerItem.setImgBgRes(R.drawable.tool_power_selector);
		powerItem.setTitleText(R.string.toolPower);
		commomProblemItem.setImgBgRes(R.drawable.tool_common_problem_selector);
		commomProblemItem.setTitleText(R.string.toolCommonProblem);
		shareItem.setImgBgRes(R.drawable.tool_share_selector);
		shareItem.setTitleText(R.string.toolShare);
		aboutUsItem.setImgBgRes(R.drawable.tool_about_selector);
		aboutUsItem.setTitleText(R.string.toolAbout);
		
		
		initViewSize();
		registerClick();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(accountIcon, 230 * Rx, 230 * Ry);
		UIUtil.setViewSize(messageImg, 60 * Rx, 40	 * Ry);
		UIUtil.setViewSize(level, 155 * Rx, 50 * Ry);
		
		UIUtil.setViewSize(downloadItem, 320 * Rx, 320 * Ry);
		UIUtil.setViewWidth(resourcesItem, 320 * Rx);
		UIUtil.setViewWidth(uninstallItem, 320 * Rx);
		
		UIUtil.setViewHeight(clearCacheItem, 320 * Ry);
		UIUtil.setViewHeight(commomProblemItem, 320 * Ry);
		
		UIUtil.setTextSize(accountName, 45);
		UIUtil.setTextSize(level, 30);
		
		try {
			UIUtil.setViewSizeMargin(accountIcon, 0, 65 *Ry, 0, 0);
			UIUtil.setViewSizeMargin(accountLin, 0, 30 *Ry, 0, 0);
			UIUtil.setViewSizeMargin(level, 0, 30 *Ry, 0, 0);
			
			UIUtil.setViewSizeMargin(resourcesItem, 20 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(uninstallItem, 20 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(clearCacheItem, 0, 20 * Rx, 0, 0);
			UIUtil.setViewSizeMargin(commomProblemItem, 0, 20 * Rx, 0, 0);
			UIUtil.setViewSizeMargin(itemRel, 0, 0, 0, 20 * Rx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void registerClick(){
		downloadItem.setOnClickListener(this);
		resourcesItem.setOnClickListener(this);
		uninstallItem.setOnClickListener(this);
		clearCacheItem.setOnClickListener(this);
		clearUpItem.setOnClickListener(this);
		powerItem.setOnClickListener(this);
		commomProblemItem.setOnClickListener(this);
		shareItem.setOnClickListener(this);
		aboutUsItem.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.downloadItem:
			startActivity(new Intent(getActivity(), DownloadManagerActivity.class));
			break;

		case R.id.resourcesItem:
			break;
			
		case R.id.uninstallItem:
			startActivity(new Intent(getActivity(), AppUninstallActivity.class));
			break;

		case R.id.clearCacheItem:
			startActivity(new Intent(getActivity(), ClearCacheActivity.class));
			break;
			
		case R.id.clearUpItem:
			startActivity(new Intent(getActivity(), DeepClearActivity.class));
			break;

		case R.id.powerItem:
			break;
			
		case R.id.commomProblemItem:
			
			break;

		case R.id.shareItem:
			break;
			
		case R.id.aboutUsItem:
			break;
		}
	}
}
