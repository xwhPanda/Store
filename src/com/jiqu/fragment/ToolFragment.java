package com.jiqu.fragment;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.activity.AboutUsActivity;
import com.jiqu.activity.AppUninstallActivity;
import com.jiqu.activity.ClearCacheActivity;
import com.jiqu.activity.CommomProblemActivity;
import com.jiqu.activity.DeepClearActivity;
import com.jiqu.activity.DownloadManagerActivity;
import com.jiqu.activity.MessageCenterActivity;
import com.jiqu.activity.PowerManagerActivity;
import com.jiqu.activity.ResourceManagementActivity;
import com.jiqu.activity.ShareActivity;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.Account;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.CircleImageView;
import com.jiqu.view.ToolItemView;

import de.greenrobot.dao.query.QueryBuilder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToolFragment extends BaseFragment implements OnClickListener{
	private float Rx,Ry;
	private View view;
	private CircleImageView accountIcon;
	private LinearLayout accountLin;
	private TextView accountName;
	private ImageView messageImg;
	private TextView level;
	private RelativeLayout itemRel;
	private RelativeLayout toolView;
	
	private ToolItemView downloadItem,resourcesItem,uninstallItem;
	private ToolItemView clearCacheItem,clearUpItem,powerItem;
	private ToolItemView commomProblemItem,shareItem,aboutUsItem;
	
	private Account account;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		StoreApplication.setLoginOutObserver(this);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		QueryBuilder qb = StoreApplication.daoSession.getAccountDao().queryBuilder();
		account = (Account) qb.unique();
		setData(account);
	}
	
	@Override
	public View initView(){
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		view = LayoutInflater.from(activity).inflate(R.layout.tool, null, false);
		toolView = (RelativeLayout) view.findViewById(R.id.toolView);
		accountIcon = (CircleImageView) view.findViewById(R.id.accountIcon);
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
		
		downloadItem.setImgBgRes(R.drawable.xiazai_tool, R.drawable.xiazai_tool_focus);
		downloadItem.setItemBgRes(R.drawable.gongju_item, R.drawable.gongju_item_focus);
		downloadItem.setTitleText(R.string.toolDownload);
		resourcesItem.setImgBgRes(R.drawable.ziyuan, R.drawable.ziyuan_focus);
		resourcesItem.setItemBgRes(R.drawable.gongju_item, R.drawable.gongju_item_focus);
		resourcesItem.setTitleText(R.string.toolResources);
		uninstallItem.setImgBgRes(R.drawable.xiezai, R.drawable.xiezai_focus);
		uninstallItem.setItemBgRes(R.drawable.gongju_item, R.drawable.gongju_item_focus);
		uninstallItem.setTitleText(R.string.toolUninstall);
		clearCacheItem.setImgBgRes(R.drawable.huancun, R.drawable.huancun_focus);
		clearCacheItem.setItemBgRes(R.drawable.gongju_item, R.drawable.gongju_item_focus);
		clearCacheItem.setTitleText(R.string.toolClearCache);
		clearUpItem.setImgBgRes(R.drawable.jiasu, R.drawable.jiasu_focus);
		clearUpItem.setItemBgRes(R.drawable.gongju_item, R.drawable.gongju_item_focus);
		clearUpItem.setTitleText(R.string.toolClearUp);
		powerItem.setImgBgRes(R.drawable.dianliang, R.drawable.dianliang_focus);
		powerItem.setItemBgRes(R.drawable.gongju_item, R.drawable.gongju_item_focus);
		powerItem.setTitleText(R.string.toolPower);
		commomProblemItem.setImgBgRes(R.drawable.wenti, R.drawable.wenti_focus);
		commomProblemItem.setItemBgRes(R.drawable.gongju_item, R.drawable.gongju_item_focus);
		commomProblemItem.setTitleText(R.string.toolCommonProblem);
		shareItem.setImgBgRes(R.drawable.fenxiang, R.drawable.fenxiang_focus);
		shareItem.setItemBgRes(R.drawable.gongju_item, R.drawable.gongju_item_focus);
		shareItem.setTitleText(R.string.toolShare);
		aboutUsItem.setImgBgRes(R.drawable.lianxi, R.drawable.lianxi_focus);
		aboutUsItem.setItemBgRes(R.drawable.gongju_item, R.drawable.gongju_item_focus);
		aboutUsItem.setTitleText(R.string.toolAbout);
		
		initViewSize();
		registerClick();
		
		return view;
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(accountIcon, 230 * Rx, 230 * Rx);
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
		
		toolView.setBackgroundDrawable(StoreApplication.BG_IMG);
		
		messageImg.setImageBitmap(UIUtil.readBitmap(activity, R.drawable.xinxi));
		level.setBackgroundDrawable(UIUtil.readBitmapDrawable(activity, R.drawable.dengji));
	}
	
	private void registerClick(){
		messageImg.setOnClickListener(this);
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
		case R.id.messageImg:
			startActivity(new Intent(getActivity(), MessageCenterActivity.class));
			break;
		case R.id.downloadItem:
			startActivity(new Intent(getActivity(), DownloadManagerActivity.class));
			break;

		case R.id.resourcesItem:
			startActivity(new Intent(getActivity(), ResourceManagementActivity.class));
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
			startActivity(new Intent(getActivity(), PowerManagerActivity.class));
			break;
			
		case R.id.commomProblemItem:
			startActivity(new Intent(getActivity(), CommomProblemActivity.class));
			break;

		case R.id.shareItem:
			startActivity(new Intent(getActivity(), ShareActivity.class));
			break;
			
		case R.id.aboutUsItem:
			startActivity(new Intent(getActivity(), AboutUsActivity.class));
			break;
		}
	}
	
	private void setData(Account account){
		if (account != null) {
			ImageListener listener = ImageLoader.getImageListener(accountIcon, R.drawable.yonghuicon, R.drawable.yonghuicon);
			StoreApplication.getInstance().getImageLoader().get(account.getPhoto(), listener);
			if (account.getUsername() != null && !TextUtils.isEmpty(account.getUsername())) {
				accountName.setText(account.getUsername());
			}else {
				accountName.setText(account.getNickname());
			}
			messageImg.setVisibility(View.VISIBLE);
			level.setVisibility(View.VISIBLE);
			level.setText("LV " + account.getLevel());
		}else {
			accountIcon.setBackgroundResource(R.drawable.yonghuicon);
			accountName.setText(R.string.notLogin);
			messageImg.setVisibility(View.GONE);
			level.setVisibility(View.INVISIBLE);
			level.setText("LV " + "");
		}
	}
	
	@Override
	public void onLoginOut() {
		// TODO Auto-generated method stub
		account = null;
		accountName.setText(R.string.notLogin);
		messageImg.setVisibility(View.GONE);
		level.setVisibility(View.INVISIBLE);
		level.setText("LV " + "");
		
		accountIcon.setImageBitmap(null);
		accountIcon.setBackgroundResource(R.drawable.yonghuicon);
	}

	@Override
	public void onRefresh(Account account) {
		// TODO Auto-generated method stub
		this.account = account;
		setData(account);
	}
}
