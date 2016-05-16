package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.activity.DetailActivity;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.RankInfo;
import com.jiqu.object.SpecialInfo;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.MyScrollView;
import com.jiqu.view.PullUpListView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableListView;
import com.jiqu.view.PullUpListView.MyPullUpListViewCallBack;
import com.jiqu.view.ViewPagerLinView;
import com.vr.store.R;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameNewFragment extends BaseFragment implements MyPullUpListViewCallBack,OnClickListener{
	private View view;
	private final String PAGER_REQUEST = "pagerRequest";
	private final String LATSEST_REQUEST = "latestGameRequest";
	private final String HOT_REQUEST = "hotGameRequest";
	private RequestTool requestTool;
	
	private LoadStateView loadView;
	private MyScrollView contentScroll;
	private ViewPagerLinView pagerView;
	private LinearLayout explosiveHeadlinesLin;
	private LinearLayout allHeadlinesLin;
	private LinearLayout headlineLin;
	private ImageView explosiveHeadlinesImg,allHeadlinesImg;
	private TextView explosiveHeadlinesTx,allHeadlinesTx;

	private RelativeLayout hotGameRel;
	private RelativeLayout listViewRel;
	private LoadStateView hotLoadView;
	private PullUpListView latestListView;
	private PullUpListView hotListView;
	
	private GameAdapter lastGameAdapter;
	private GameAdapter hotGameAdapter;
	private List<GameInfo> lastGameInfos = new ArrayList<GameInfo>();
	private List<GameInfo> hotGameInfos = new ArrayList<GameInfo>();
	
	private int latestPageNum = 1;
	private int hotPageNum = 1;
	private boolean latestLoading = false;
	private boolean hotLoading = false;
	private boolean isFirst = true;
	private boolean isFirstLoad = true;
	private GameInfo[] viewPagerData;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		requestTool = RequestTool.getInstance();
	}

	@Override
	public View initView() {
		// TODO Auto-generated method stub
		view = LayoutInflater.from(activity).inflate(R.layout.game_new_fragment, null);
		
		loadView = (LoadStateView) view.findViewById(R.id.loadView);
		contentScroll = (MyScrollView) view.findViewById(R.id.contentScroll);
		pagerView = (ViewPagerLinView) view.findViewById(R.id.pagerView);
		pagerView.setClass(DetailActivity.class);
		pagerView.setDefaultImgId(R.drawable.recommend_viewpager_default);
		explosiveHeadlinesLin = (LinearLayout) view.findViewById(R.id.explosiveHeadlinesLin);
		allHeadlinesLin = (LinearLayout) view.findViewById(R.id.allHeadlinesLin);
		headlineLin = (LinearLayout) view.findViewById(R.id.headlineLin);
		explosiveHeadlinesImg = (ImageView) view.findViewById(R.id.explosiveHeadlinesImg);
		allHeadlinesImg = (ImageView) view.findViewById(R.id.allHeadlinesImg);
		explosiveHeadlinesTx = (TextView) view.findViewById(R.id.explosiveHeadlinesTx);
		allHeadlinesTx = (TextView) view.findViewById(R.id.allHeadlinesTx);
		latestListView = (PullUpListView) view.findViewById(R.id.latestListView);
		hotListView = (PullUpListView) view.findViewById(R.id.hotListView);
		hotGameRel = (RelativeLayout) view.findViewById(R.id.hotGameRel);
		hotLoadView = (LoadStateView) view.findViewById(R.id.hotLoadView);
		listViewRel = (RelativeLayout) view.findViewById(R.id.listViewRel);
		
		allHeadlinesImg.setBackgroundResource(R.drawable.zuire);
		explosiveHeadlinesImg.setBackgroundResource(R.drawable.zuibao);
		explosiveHeadlinesTx.setText(R.string.latestGame);
		allHeadlinesTx.setText(R.string.newGame);
		
		latestListView.initBottomView();
		latestListView.setMyPullUpListViewCallBack(this);
		hotListView.initBottomView();
		hotListView.setMyPullUpListViewCallBack(this);
		
		explosiveHeadlinesLin.setOnClickListener(this);
		allHeadlinesLin.setOnClickListener(this);
		loadView.loadAgain(this);
		hotLoadView.loadAgain(this);
		
		lastGameAdapter = new GameAdapter(activity, lastGameInfos, false, false);
		hotGameAdapter = new GameAdapter(activity, hotGameInfos, false, false);
		latestListView.setAdapter(lastGameAdapter);
		hotListView.setAdapter(hotGameAdapter);
		
		initViewSize();
		
		return view;
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(pagerView, MetricsTool.width, 455 * MetricsTool.Ry);
		UIUtil.setViewSize(headlineLin, MetricsTool.width, 75 * MetricsTool.Ry);
		UIUtil.setViewSize(explosiveHeadlinesImg, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		UIUtil.setViewSize(allHeadlinesImg, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		UIUtil.setViewHeight(latestListView, MetricsTool.height - 75 * MetricsTool.Ry - 180 * MetricsTool.Rx - 180 * MetricsTool.Rx );
		UIUtil.setViewHeight(hotListView, MetricsTool.height - 75 * Ry - 180 * Rx - 180 * Rx );
		UIUtil.setViewHeight(hotLoadView, MetricsTool.height - 75 * Ry - 180 * Rx - 180 * Rx );
		UIUtil.setViewHeight(listViewRel, MetricsTool.height - 75 * Ry - 180 * Rx - 180 * Rx );
		
		UIUtil.setTextSize(explosiveHeadlinesTx, 40);
		UIUtil.setTextSize(allHeadlinesTx, 40);
		
		try {
			UIUtil.setViewSizeMargin(headlineLin, 0, 20 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		loadPagerData(RequestTool.SPREAD_URL);
	}
	
	private void loadPagerData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {
			
			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				RankInfo spreadInfo = JSON.parseObject(arg0.toString(), RankInfo.class);
				if (spreadInfo != null) {
					if (spreadInfo.getStatus() == 1) {
						viewPagerData = spreadInfo.getData();
					}
				}
				loadLastGameData(RequestTool.LATEST_GAME_URL);
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataFail();
			}
		}, requestTool.getMap(), PAGER_REQUEST);
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				
			}
		};
	};
	
	private void loadLastGameData(String url){
		if (latestLoading) {
			return;
		}
		latestLoading = true;
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataSuccess();
				loadView.setVisibility(View.GONE);
				contentScroll.setVisibility(View.VISIBLE);
				RankInfo rankInfo = JSON.parseObject(arg0.toString(), RankInfo.class);
				if (rankInfo != null && rankInfo.getStatus() == 1 && rankInfo.getData() != null) {
					latestPageNum++;
					Collections.addAll(lastGameInfos, rankInfo.getData());
					int count = 10;
					if (lastGameInfos.size() < 10) {
						count = lastGameInfos.size();
					}
					List<InstalledApp> apps = InstalledAppTool.getPersonalApp(getActivity());
					for(int i = lastGameInfos.size() - count;i<lastGameInfos.size();i++){
						GameInfo gameInfo = lastGameInfos.get(i);
						DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(gameInfo.getId()));
						int state = InstalledAppTool.contain(apps,gameInfo.getProduct_name());
						if (state != -1) {
							lastGameInfos.get(i).setState(state);
						}else {
							if (info != null 
									&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED)) {
								DownloadManager.DBManager.delete(info);
							}
						}
					}
				}else if (rankInfo != null && rankInfo.getStatus() == 0) {
					Toast.makeText(activity, R.string.notMore, Toast.LENGTH_SHORT).show();
				}
				if (isFirstLoad) {
					isFirstLoad = false;
					pagerView.setData(viewPagerData);
				}
				lastGameAdapter.notifyDataSetChanged();
				latestListView.refreshFinish();
				latestLoading = false;
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataFail();
				latestListView.refreshFinish();
				latestLoading = false;
			}
			
		}, requestTool.getMap(), LATSEST_REQUEST);
	}
	
	private void loadHotGameData(String url){
		if (hotLoading) {
			return;
		}
		hotLoading = true;
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				hotLoadView.loadDataSuccess();
				hotLoadView.setVisibility(View.GONE);
				hotListView.setVisibility(View.VISIBLE);
				RankInfo rankInfo = JSON.parseObject(arg0.toString(), RankInfo.class);
				if (rankInfo != null && rankInfo.getStatus() == 1 && rankInfo.getData() != null) {
					hotPageNum++;
					Collections.addAll(hotGameInfos, rankInfo.getData());
					int count = 10;
					if (hotGameInfos.size() < 10) {
						count = hotGameInfos.size();
					}
					List<InstalledApp> apps = InstalledAppTool.getPersonalApp(getActivity());
					for(int i = hotGameInfos.size() - count;i<hotGameInfos.size();i++){
						GameInfo gameInfo = hotGameInfos.get(i);
						DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(gameInfo.getId()));
						int state = InstalledAppTool.contain(apps,gameInfo.getProduct_name());
						if (state != -1) {
							hotGameInfos.get(i).setState(state);
						}else {
							if (info != null 
									&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED)) {
								DownloadManager.DBManager.delete(info);
							}
						}
					}
				}else if (rankInfo != null && rankInfo.getStatus() == 0) {
					Toast.makeText(activity, R.string.notMore, Toast.LENGTH_SHORT).show();
				}
				hotListView.refreshFinish();
				hotGameAdapter.notifyDataSetChanged();
				hotLoading = false;
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				hotLoadView.loadDataFail();
				hotListView.refreshFinish();
				hotLoading = false;
			}
			
		}, requestTool.getMap(), HOT_REQUEST);
	}
	
	@Override
	public void scrollBottomState(int viewId) {
		// TODO Auto-generated method stub
		if (viewId == R.id.latestListView) {
			loadLastGameData(RequestTool.LATEST_GAME_URL + "?pageNum=" + latestPageNum);
		}else if (viewId == R.id.hotListView) {
			loadHotGameData(RequestTool.HOT_GAME_URL + "?pageNum=" + hotPageNum);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.explosiveHeadlinesLin:
			hotGameRel.setVisibility(View.GONE);
			latestListView.setVisibility(View.VISIBLE);
			explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.sortTitleColor));
			allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
			break;

		case R.id.allHeadlinesLin:
			latestListView.setVisibility(View.GONE);
			hotGameRel.setVisibility(View.VISIBLE);
			explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
			allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.sortTitleColor));
			if (isFirst) {
				isFirst = false;
				loadHotGameData(RequestTool.HOT_GAME_URL);
			}
			break;
		}
		if (loadView.getLoadBtn() == v) {
			loadView.showLoading();
			loadPagerData(RequestTool.SPREAD_URL);
		}else if (hotLoadView.getLoadBtn() == v) {
			hotLoadView.showLoading();
			loadHotGameData(RequestTool.HOT_GAME_URL);
		}
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		StoreApplication.getInstance().getRequestQueue().cancelAll(LATSEST_REQUEST);
		StoreApplication.getInstance().getRequestQueue().cancelAll(HOT_REQUEST);
	}
}
