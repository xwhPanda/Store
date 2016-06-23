package com.jiqu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.application.StoreApplication;
import com.jiqu.object.InformationGallaryItem;
import com.jiqu.object.InformationItemInfo;
import com.jiqu.store.BaseActivity;
import com.umeng.socialize.media.UMImage;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.EvaluationBottomView;
import com.jiqu.view.EvaluationItemView;
import com.jiqu.view.TitleView;

public class HeadlineActivity extends BaseActivity {
	private RelativeLayout parentView;
	private TitleView titleView;
	private TextView evaluationTitle;
	private LinearLayout informationLin;
	private TextView evaluationTime;
	private TextView evaluationAuthor;
	private TextView browseCount;
	private ImageView gameImg;
	private TextView information;
	private LinearLayout detailedEvaLin;
	private EvaluationBottomView evaGameView;
	private ImageView view;
	private RelativeLayout parent;
	private WebView webView;

	private RequestTool requestTool;
	private InformationGallaryItem item;
	private InformationItemInfo info;
	/** 是否是网页 **/
	private boolean isWeb = false;
	private String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		item = (InformationGallaryItem) getIntent().getSerializableExtra("data");
		info = (InformationItemInfo) getIntent().getSerializableExtra("info");
		isWeb = getIntent().getBooleanExtra("isWeb", false);
		initView();

		if (isWeb) {
			url = getIntent().getStringExtra("url");
			parent.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);

			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setAllowFileAccess(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setSupportZoom(true);
			webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			webView.loadUrl(url);
			// 加载数据
			webView.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					if (newProgress == 100) {
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
	}

	@Override
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (webView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			webView.goBack();
			return true;
		}
		finish();
		return false;
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.headline_layout;
	}

	private void loadData(String url) {
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}

		}, requestTool.getMap(), "informationDetail");
	}

	private void initView() {
		parentView = (RelativeLayout) findViewById(R.id.parentView);
		parentView.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		evaluationTitle = (TextView) findViewById(R.id.evaluationTitle);
		informationLin = (LinearLayout) findViewById(R.id.informationLin);
		evaluationTime = (TextView) findViewById(R.id.evaluationTime);
		evaluationAuthor = (TextView) findViewById(R.id.evaluationAuthor);
		browseCount = (TextView) findViewById(R.id.browseCount);
		gameImg = (ImageView) findViewById(R.id.gameImg);
		information = (TextView) findViewById(R.id.information);
		detailedEvaLin = (LinearLayout) findViewById(R.id.detailedEvaLin);
		evaGameView = (EvaluationBottomView) findViewById(R.id.evaGameView);
		view = (ImageView) findViewById(R.id.view);
		parent = (RelativeLayout) findViewById(R.id.parent);
		webView = (WebView) findViewById(R.id.webView);

		titleView.setActivity(this);
		titleView.tip.setText(getResources().getString(R.string.newHeadlines));
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang);
		titleView.editBtn.setVisibility(View.VISIBLE);

		titleView.setShareListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (info == null) {
					return;
				}
				Bundle bundle = new Bundle();
				if (info.getTitle() != null) {
					bundle.putString("title", info.getTitle());
				}
				if (info.getComments() != null) {
					bundle.putString("content", info.getComments());
				}
				if (url != null) {
					bundle.putString("url", url);
				}
				if (info.getPic() != null && !TextUtils.isEmpty(info.getPic())) {
					bundle.putString("image", info.getPic());
				}
				startActivity(new Intent(HeadlineActivity.this, ShareActivity.class)
				.putExtra("bundle", bundle));
			}
		});

		initViewSize();
	}

	private void initViewSize() {
		UIUtil.setTextSize(evaluationTitle, 45);
		UIUtil.setTextSize(evaluationTime, 25);
		UIUtil.setTextSize(evaluationAuthor, 25);
		UIUtil.setTextSize(browseCount, 25);
		UIUtil.setTextSize(information, 35);

		UIUtil.setViewHeight(evaGameView, 215 * Ry);
		UIUtil.setViewSize(gameImg, 1030 * Rx, 515 * Ry);

		try {
			UIUtil.setViewSizeMargin(view, 0, 45 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(evaluationTitle, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(informationLin, 0, 20 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(gameImg, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(information, 0, 45 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(evaluationAuthor, 15 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(browseCount, 15 * Rx, 0, 0, 0);
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
