package com.jiqu.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;

import com.jiqu.object.CommonProblemItem;
import com.jiqu.object.EvaluationItemInfo;
import com.jiqu.store.BaseActivity;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.TitleView;
import com.vr.store.R;

public class GameEvaluationWebInfoActivity extends BaseActivity {
	private TitleView titleView;
	private WebView webView;
	private LoadStateView loadView;
	private EvaluationItemInfo info;
	private CommonProblemItem item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		info = (EvaluationItemInfo) getIntent().getSerializableExtra("info");
		item = (CommonProblemItem) getIntent().getSerializableExtra("item");
		initView();
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		loadView = (LoadStateView) findViewById(R.id.loadView);
		webView = (WebView) findViewById(R.id.webView);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang);
		titleView.editBtn.setVisibility(View.VISIBLE);
		
		webView.clearCache(false);
		
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		if (info != null) {
			loadData(info.getUrl());
			titleView.tip.setText(info.getTitle());
		}else if (item != null) {
			loadData(item.getContent());
			titleView.tip.setText(item.getTitle());
		}
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
}
