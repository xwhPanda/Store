package com.jiqu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;

import com.jiqu.object.CommonProblemItem;
import com.jiqu.object.EvaluationItemInfo;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.SplashActivity;
import com.jiqu.tools.Constants;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.TitleView;
import com.vr.store.R;

public class GameEvaluationWebInfoActivity extends BaseActivity {
	private TitleView titleView;
	private WebView webView;
	private LoadStateView loadView;
	private EvaluationItemInfo info;
	private CommonProblemItem item;
	
	private String url;
	private String title;
	private String content;
	private String image;
	/** 是否是推送通知跳转 **/
	private String isPush = "false";
	private boolean showShareBtn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		info = (EvaluationItemInfo) getIntent().getSerializableExtra("info");
		item = (CommonProblemItem) getIntent().getSerializableExtra("item");
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		showShareBtn = getIntent().getBooleanExtra("showShareBtn", true);
		isPush = getIntent().getStringExtra("isPush");
		if ("true".equals(isPush)) {
			content = getIntent().getStringExtra("content");
			image = getIntent().getStringExtra("image");
		}
		initView();
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		loadView = (LoadStateView) findViewById(R.id.loadView);
		webView = (WebView) findViewById(R.id.webView);
		
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		if (showShareBtn) {
			titleView.editBtn.setBackgroundResource(R.drawable.fenxiang);
			titleView.editBtn.setVisibility(View.VISIBLE);
		}
		
		webView.clearCache(false);
		
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		
		if (info != null) {//测评
			loadData(info.getUrl());
			titleView.tip.setText(info.getTitle());
		}else if (item != null) {//常见问题
			titleView.editBtn.setVisibility(View.GONE);
			loadData(item.getContent());
			titleView.tip.setText(item.getTitle());
		}else if(!TextUtils.isEmpty(url)){//消息通知
			titleView.editBtn.setVisibility(View.GONE);
			loadData(url);
			titleView.tip.setText(title);
		}
		
		titleView.setShareListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				if (info == null) {
					if (!"true".equals(isPush)) {
						return;
					}
				}else {
					content = info.getDescript();
					image = info.getPic();
				}
				bundle.putString("title", title);
				bundle.putString("url", url);
				bundle.putString("content", content);
				bundle.putString("image", image);
				startActivity(new Intent(GameEvaluationWebInfoActivity.this, ShareActivity.class)
				.putExtra("bundle", bundle));
			}
		});
		
		titleView.setBackListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("true".equals(isPush)
						&& Constants.ACTIVITY_LIST.size() <= 1) {
					startActivity(new Intent(GameEvaluationWebInfoActivity.this, SplashActivity.class));
				}
				finish();
			}
		});
	}
	
	private void loadData(String url){
		webView.loadUrl(url);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					loadView.loadDataSuccess();
					loadView.setVisibility(View.VISIBLE);
					webView.setVisibility(View.VISIBLE);
				} else {
					
				}
			}
		});
		// 这个是当网页上的连接被点击的时候
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
				webView.loadUrl(url);
				return true;
			}
		});
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.evaluation_web_info_layout;
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
