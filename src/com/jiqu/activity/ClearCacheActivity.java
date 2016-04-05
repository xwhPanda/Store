package com.jiqu.activity;

import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.ClearTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

public class ClearCacheActivity extends BaseActivity implements OnClickListener{
	private TitleView titleView;
	private RelativeLayout clearImgRel;
	private ImageView clearImg;
	private ImageView clearImgBg;
	private TextView clearTip;
	private RelativeLayout appCacheRel,systemCacheRel;
	private ImageView appCacheImg,systemCacheImg;
	private TextView appCacheTitle,findAppCache,systemCacheTitle,findSystemCache;
	private Button clear;
	private Animation animation_1,animation_2;
	private ClearTool clearTool;
	private boolean isCleared = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.clear_cache_layout;
	}

	private void initView(){
		animation_1 = AnimationUtils.loadAnimation(ClearCacheActivity.this, R.anim.clear_img_anim);
		animation_2 = AnimationUtils.loadAnimation(ClearCacheActivity.this, R.anim.clear_small_img_anim);
		
		titleView = (TitleView) findViewById(R.id.titleView);
		clearImgRel = (RelativeLayout) findViewById(R.id.clearImgRel);
		clearImg = (ImageView) findViewById(R.id.clearImg);
		clearImgBg = (ImageView) findViewById(R.id.clearImgBg);
		clearTip = (TextView) findViewById(R.id.clearTip);
		appCacheRel = (RelativeLayout) findViewById(R.id.appCacheRel);
		systemCacheRel = (RelativeLayout) findViewById(R.id.systemCacheRel);
		appCacheImg = (ImageView) findViewById(R.id.appCacheImg);
		systemCacheImg = (ImageView) findViewById(R.id.systemCacheImg);
		appCacheTitle = (TextView) findViewById(R.id.appCacheTitle);
		findAppCache = (TextView) findViewById(R.id.findAppCache);
		systemCacheTitle = (TextView) findViewById(R.id.systemCacheTitle);
		findSystemCache = (TextView) findViewById(R.id.findSystemCache);
		clear = (Button) findViewById(R.id.clear);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.clearCache);
		
		clear.setOnClickListener(this);
		
		initViewSize();
		
	}
	
	private void initViewSize(){
		UIUtil.setTextSize(clearTip, 40);
		UIUtil.setTextSize(appCacheTitle, 35);
		UIUtil.setTextSize(findAppCache, 35);
		UIUtil.setTextSize(systemCacheTitle, 35);
		UIUtil.setTextSize(findSystemCache, 35);
		
		UIUtil.setViewSize(appCacheRel, 1040 * Rx, 150 * Ry);
		UIUtil.setViewSize(systemCacheRel, 1040 * Rx, 150 * Ry);
		UIUtil.setViewSize(clearImgRel, 345 * Rx, 345 * Rx);
		UIUtil.setViewSize(clearImgBg, 345 * Rx, 345 * Rx);
		UIUtil.setViewSize(clearImg, 155 * Rx, 155 * Ry);
		UIUtil.setViewSize(appCacheImg, 56 * Rx, 56 * Rx);
		UIUtil.setViewSize(systemCacheImg, 56 * Rx, 56 * Rx);
		UIUtil.setViewHeight(clear, 110 * Ry);
		
		try {
			UIUtil.setViewSizeMargin(appCacheImg, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(systemCacheImg, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(appCacheTitle, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(systemCacheTitle, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(findAppCache, 0, 0, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(findSystemCache, 0, 0, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(systemCacheRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(clearImgRel, 0, 290 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(clearTip, 0, 65 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(appCacheRel, 0, 85 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(clear, 0, 490 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.clear) {
			if (isCleared) {
				finish();
			}
			clearTool = ClearTool.getInstance();
			clearTool.setHandler(handler);
			clearTool.startClearCacheSize(this);
			handler.sendEmptyMessage(2);
			
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {//清理完成
				clearImgBg.clearAnimation();
				appCacheImg.clearAnimation();
				systemCacheImg.clearAnimation();
				
				clearTip.setText(R.string.clearCacheed);
				String[] strings = msg.obj.toString().split("@");
				findAppCache.setText("共清理缓存" + strings[0]);
				findSystemCache.setText("共清理缓存" + strings[1]);
				
				clear.setBackgroundResource(R.drawable.qingjie_btn);
				clear.setTextColor(getResources().getColor(R.color.white));
				clear.setClickable(true);
				clear.setText(R.string.cleared);
				
				isCleared = true;
			}else if (msg.what == 2) {//开始扫描清理
				clearTip.setText(R.string.clearCacheIng);
				clearTool.startClearCacheSize(ClearCacheActivity.this);
				
				clearImgBg.startAnimation(animation_1);
				
				appCacheImg.setBackgroundResource(R.drawable.shuaxin_sel);
				systemCacheImg.setBackgroundResource(R.drawable.shuaxin_sel);
				
				appCacheImg.startAnimation(animation_2);
				systemCacheImg.startAnimation(animation_2);
				
				clear.setBackgroundResource(R.drawable.tuichuzhanghao);
				clear.setTextColor(getResources().getColor(R.color.itemDesColor));
				clear.setClickable(false);
				
			}
		};
	};
	
	protected void onDestroy() {
		super.onDestroy();
		if (clearTool != null) {
			clearTool.stopClear();
		}
	};
}
