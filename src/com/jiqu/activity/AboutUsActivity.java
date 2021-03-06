package com.jiqu.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.application.StoreApplication;
import com.jiqu.store.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

public class AboutUsActivity extends BaseActivity {
	private RelativeLayout parent;
	private TitleView titleView;
	private RelativeLayout appInfoRel;
	private ImageView companyLogo;
	private LinearLayout appInfoLin;
	private TextView appName,copyrightInfo,releaseDate;
	private RelativeLayout websiteRel,weiboRel,noPublicRel;
	private TextView websiteTx,website;
	private TextView weiboTx,weibo;
	private TextView noPublicTx,noPublic;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.about_us_layout;
	}
	
	private void initView(){
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		appInfoRel = (RelativeLayout) findViewById(R.id.appInfoRel);
		companyLogo = (ImageView) findViewById(R.id.companyLogo);
		appInfoLin = (LinearLayout) findViewById(R.id.appInfoLin);
		appName = (TextView) findViewById(R.id.appName);
		copyrightInfo = (TextView) findViewById(R.id.copyrightInfo);
		releaseDate = (TextView) findViewById(R.id.releaseDate);
		websiteRel = (RelativeLayout) findViewById(R.id.websiteRel);
		weiboRel = (RelativeLayout) findViewById(R.id.weiboRel);
		noPublicRel = (RelativeLayout) findViewById(R.id.noPublicRel);
		websiteTx = (TextView) findViewById(R.id.websiteTx);
		website = (TextView) findViewById(R.id.website);
		weiboTx = (TextView) findViewById(R.id.weiboTx);
		weibo = (TextView) findViewById(R.id.weibo);
		noPublicTx = (TextView) findViewById(R.id.noPublicTx);
		noPublic = (TextView) findViewById(R.id.noPublic);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.aboutUs);
		
		initViewSize();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("AboutUsActivity");
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("AboutUsActivity");
		MobclickAgent.onPause(this);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(companyLogo, 150 * Rx, 150 * Rx);
		UIUtil.setViewHeight(websiteRel, 180 * Ry);
		UIUtil.setViewHeight(weiboRel, 180 * Ry);
		UIUtil.setViewHeight(noPublicRel, 180 * Ry);
		
		UIUtil.setTextSize(appName, 50);
		UIUtil.setTextSize(copyrightInfo, 35);
		UIUtil.setTextSize(releaseDate, 35);
		UIUtil.setTextSize(websiteTx, 40);
		UIUtil.setTextSize(website, 40);
		UIUtil.setTextSize(weiboTx, 40);
		UIUtil.setTextSize(weibo, 40);
		UIUtil.setTextSize(noPublicTx, 40);
		UIUtil.setTextSize(noPublic, 40);
		
		try {
			UIUtil.setViewSizeMargin(appInfoLin, 40 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(appInfoRel, 120 * Rx, 285 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(websiteRel, 0, 120 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(weiboRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(noPublicRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(websiteTx, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(weiboTx, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(noPublicTx, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(website, 0, 0, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(weibo, 0, 0, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(noPublic, 0, 0, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(copyrightInfo, 0, 15 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(releaseDate, 0, 10 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
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
}
