package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.adapter.RecommendGameAdapter;
import com.jiqu.adapter.ViewPagerAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.GameInformation;
import com.jiqu.object.SpecialInfo;
import com.jiqu.object.SpecialRecommendsItem;
import com.jiqu.object.SpecialResultsItem;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.CountDownTimer;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;

public class BoutiqueActivity extends BaseActivity implements OnPageChangeListener,OnItemClickListener,OnRefreshListener{
	private TitleView titleView;
	private PullToRefreshLayout refreshLayout;
	private ListView boutiqueListView;
	
	private View headView;
	private RelativeLayout pagerRel;
	private ViewPager viewPager;
	private LinearLayout viewGroup;
	private RelativeLayout allGameRel;
	private ImageView totalGameImg;
	private TextView totalGameTx;
	private TextView addedTime;
	
	private ViewPagerAdapter viewPagerAdapter;
	private GameAdapter adapter;
	private List<GameInfo> informations = new ArrayList<GameInfo>();
	
	private RequestTool requestTool;
	private String nextUrl;
	private ImageView[] imgs;
	private ImageView[] radioImgs;
	private int currentIndex = 0;
	private List<SpecialResultsItem> resultsItems = new ArrayList<SpecialResultsItem>();
	private boolean refreshShowing = false;
	private CountDownTimer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestTool = RequestTool.getInstance();
		
		initView();
		loadData(RequestTool.specialsUrl);
	}
	
	private void loadData(String url){
		requestTool.getMap().clear();
		
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				SpecialInfo specialInfo = JSON.parseObject(arg0, SpecialInfo.class);
				nextUrl = specialInfo.getNext();
				if (specialInfo.getRecommends() != null && specialInfo.getRecommends().length > 0) {
					setRecommendImgs(specialInfo.getRecommends());
				}
				if (specialInfo.getResults() != null && specialInfo.getResults().length > 0) {
					setSpecialData(specialInfo.getResults());
				}
				
				if (refreshShowing) {
					refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
					refreshShowing = false;
				}
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				if (refreshShowing) {
					refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
					refreshShowing = false;
				}
			}
			
		}, requestTool.getMap(), "special");
		
	}
	
	private void setRecommendImgs(final SpecialRecommendsItem[] items){
		imgs = new ImageView[items.length];
		radioImgs = new ImageView[items.length];
		for(int i = 0; i < items.length;i++){
			ImageView img = new ImageView(BoutiqueActivity.this);
			img.setScaleType(ScaleType.FIT_XY);
			ImageListener listener = ImageLoader.getImageListener(img, R.drawable.ic_launcher, R.drawable.ic_launcher);
			StoreApplication.getInstance().getImageLoader().get(items[i].getPic_url(), listener);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			img.setLayoutParams(lp);
			imgs[i] = img;
			ImageView radio = new ImageView(this);
			if (i== 0) {
				radio.setBackgroundResource(R.drawable.dian_blue);
			}else {
				radio.setBackgroundResource(R.drawable.dian_white);
			}
			android.widget.LinearLayout.LayoutParams params  = new android.widget.LinearLayout.LayoutParams((int)(20 * Rx), (int)(20 * Rx));
			if (i != 0) {
				params.leftMargin = (int) (10 * Rx);
			}
			radio.setLayoutParams(params);
			radioImgs[i] = radio;
			viewGroup.addView(radio);
		}
		viewPagerAdapter = new ViewPagerAdapter(this, imgs);
		viewPager.setAdapter(viewPagerAdapter);
		
		timer = new CountDownTimer(Integer.MAX_VALUE, 5000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
				viewPager.setCurrentItem(currentIndex % items.length);
				currentIndex++;
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		};
		timer.start();
	}
	
	private void setSpecialData(SpecialResultsItem[] items){
		int length = items.length;
		for(int i = 0;i < length; i++){
			SpecialResultsItem item = items[i];
			resultsItems.add(item);
			GameInfo gameInfo = SpecialResultsItem.toGameInfo(item);
			informations.add(gameInfo);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.boutique_layout;
	}
	
	private void initView(){
		initHeadView();
		titleView = (TitleView) findViewById(R.id.titleView);
		refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshView);
		boutiqueListView = (PullableListView) findViewById(R.id.boutiqueListView);
		
		refreshLayout.setOnRefreshListener(this);
		boutiqueListView.addHeaderView(headView);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(getResources().getString(R.string.boutiqueTitle));
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		
		initViewSize();
		
		adapter = new GameAdapter(this, informations, false, false);
		adapter.startObserver();
		boutiqueListView.setAdapter(adapter);
		boutiqueListView.setOnItemClickListener(this);
		
	}
	
	private void initHeadView(){
		LayoutInflater inflater = LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.boutique_head_layout, null);
		
		pagerRel = (RelativeLayout) headView.findViewById(R.id.pagerRel);
		viewPager = (ViewPager) headView.findViewById(R.id.viewPager);
		viewGroup = (LinearLayout) headView.findViewById(R.id.viewGroup);
		allGameRel = (RelativeLayout) headView.findViewById(R.id.allGameRel);
		totalGameImg = (ImageView) headView.findViewById(R.id.totalGameImg);
		totalGameTx = (TextView) headView.findViewById(R.id.totalGameTx);
		addedTime = (TextView) headView.findViewById(R.id.addedTiem);
		
		viewPager.setOnPageChangeListener(this);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(pagerRel, MetricsTool.width, 455 * Ry);
		UIUtil.setViewSize(allGameRel, 1050 * Rx, 75 * Ry);
		UIUtil.setViewSize(totalGameImg, 56 * Rx, 56 * Rx);
		
		UIUtil.setTextSize(totalGameTx, 45);
		UIUtil.setTextSize(addedTime, 30);
		
		try {
			UIUtil.setViewSizeMargin(totalGameImg, 20 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(totalGameTx, 10 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(addedTime, 0, 0, 20 * Rx, 0);
			UIUtil.setViewSizeMargin(allGameRel, 0, 25 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(viewGroup, 0, 0, 0, 30 * Ry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		for(int i = 0;i<radioImgs.length;i++){
			if (arg0 == i) {
				radioImgs[i].setBackgroundResource(R.drawable.dian_blue);
			}else {
				radioImgs[i].setBackgroundResource(R.drawable.dian_white);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, DetailActivity.class).putExtra("requestType", 1).putExtra("data", resultsItems.get(position -1)));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (adapter != null) {
			adapter.stopObserver();
		}
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		refreshShowing = true;
		if (!TextUtils.isEmpty(nextUrl)) {
			loadData(nextUrl);
		}else {
			if (refreshShowing) {
				refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				refreshShowing = false;
			}
		}
	}
	
	
}
