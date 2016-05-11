package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.CountDownTimer;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;
import com.jiqu.view.ViewPagerLinView;

public class BoutiqueActivity extends BaseActivity implements OnPageChangeListener,OnItemClickListener,OnRefreshListener,OnClickListener{
	private final String BOUTIQUE_REQUEST = "boutiqueRequest";
	private final int DEFAULT_SIZE = 10;
	private TitleView titleView;
	private PullToRefreshLayout refreshLayout;
	private ListView boutiqueListView;
	private LoadStateView loadView;
	
	private View headView;
	private RelativeLayout allGameRel;
	private ImageView totalGameImg;
	private TextView totalGameTx;
	private TextView addedTime;
	private ViewPagerLinView pagerView;
	
	private GameAdapter adapter;
	private List<GameInfo> informations = new ArrayList<GameInfo>();
	
	private RequestTool requestTool;
	private String nextUrl;
	private ImageView[] radioImgs;
	private boolean refreshShowing = false;
	private int total = 0;
	private int pageNum = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestTool = RequestTool.getInstance();
		
		initView();
		loadData(RequestTool.BOUTIQUE_URL);
	}
	
	private void loadData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataSuccess();
				loadView.setVisibility(View.GONE);
				refreshLayout.setVisibility(View.VISIBLE);
				
				SpecialInfo specialInfo = JSON.parseObject(arg0, SpecialInfo.class);
				if (specialInfo != null) {
					if (specialInfo.getData1() != null && specialInfo.getData1().length > 0) {
						pagerView.setData(specialInfo.getData1());
					}
					if (specialInfo.getData2() != null && specialInfo.getData2().length > 0) {
						Collections.addAll(informations, specialInfo.getData2());
						adapter.notifyDataSetChanged();
					}
					totalGameTx.setText("共" + specialInfo.getTotal() + "款");
					total = specialInfo.getTotal();
					pageNum++;
				}
				if (refreshShowing) {
					refreshShowing = false;
					refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataFail();
				if (refreshShowing) {
					refreshShowing = false;
					refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
				}
			}
		}, requestTool.getMap(), BOUTIQUE_REQUEST);
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
		loadView = (LoadStateView) findViewById(R.id.loadView);
		
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
		
		loadView.loadAgain(this);
	}
	
	private void initHeadView(){
		LayoutInflater inflater = LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.boutique_head_layout, null);
		
		pagerView = (ViewPagerLinView) headView.findViewById(R.id.pagerView);
		allGameRel = (RelativeLayout) headView.findViewById(R.id.allGameRel);
		totalGameImg = (ImageView) headView.findViewById(R.id.totalGameImg);
		totalGameTx = (TextView) headView.findViewById(R.id.totalGameTx);
		addedTime = (TextView) headView.findViewById(R.id.addedTiem);
		
		pagerView.setClass(BoutiqueActivity.class);
		pagerView.setDefaultImgId(R.drawable.recommend_viewpager_default);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(pagerView, MetricsTool.width, 455 * Ry);
		UIUtil.setViewHeight(allGameRel, 75 * Rx);
		UIUtil.setViewSize(totalGameImg, 56 * Rx, 56 * Rx);
		
		UIUtil.setTextSize(totalGameTx, 45);
		UIUtil.setTextSize(addedTime, 30);
		
		try {
			UIUtil.setViewSizeMargin(totalGameImg, 20 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(totalGameTx, 10 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(addedTime, 0, 0, 20 * Rx, 0);
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
		startActivity(new Intent(this, DetailActivity.class)
		.putExtra("id", informations.get(position - 1).getId()));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (adapter != null) {
			adapter.stopObserver();
		}
		if (pagerView != null) {
			pagerView.cancleTimer();
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		Log.i("TAG", "onLoadMore");
		refreshShowing = true;
		if ((pageNum * DEFAULT_SIZE) < total) {
			loadData(RequestTool.BOUTIQUE_URL + "?pageNum=" + pageNum);
		}else {
			if ((pageNum - 1) * DEFAULT_SIZE < total) {
				loadData(RequestTool.BOUTIQUE_URL + "?pageNum=" + pageNum);
			}else {
				/**没有更多数据**/
				Toast.makeText(this, R.string.notMore, Toast.LENGTH_SHORT).show();
				if (refreshShowing = true) {
					refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == loadView.getLoadBtn()) {
			loadData(RequestTool.BOUTIQUE_URL);
		}
	}
}
