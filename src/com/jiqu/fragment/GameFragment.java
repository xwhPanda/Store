package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.activity.RankingActivity;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.adapter.InformationAdapter;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.GameInformation;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.RankInfo;
import com.vr.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameFragment extends Fragment implements OnClickListener,OnRefreshListener,OnTouchListener{
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final String LATEST_REQUEST_TAG = "latestRequestTag";
	private static final String HOT_REQUEST_TAG = "hotRequestTag";
	private ListView gameListView;
	private View view,headView;
	private ViewPager informationImgViewPager;
	private RelativeLayout informationImgRel;
	private LinearLayout imgList;
	private LinearLayout explosiveHeadlinesLin;
	private LinearLayout allHeadlinesLin;
	private LinearLayout headlineLin;
	private PullToRefreshLayout ptrl;
	private ImageView explosiveHeadlinesImg,allHeadlinesImg;
	private TextView explosiveHeadlinesTx,allHeadlinesTx;
	
	private PullToRefreshLayout refresh_view;
	
	private List<GameInfo> newGameList = new ArrayList<GameInfo>();
	private List<GameInfo> hotGameList = new ArrayList<GameInfo>();
	private GameAdapter newGameAdapter;
	
	private RequestTool requestTool;
	private int type = 1;
	private int latestPageNum = 1;
	private int hotPageNum = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.game, null);
		headView = inflater.inflate(R.layout.information_head, null);
		requestTool = RequestTool.getInstance();
		initView();
		
		loadNew(RequestTool.LATEST_GAME_URL);
		loadHot(RequestTool.HOT_GAME_URL);
		return view;
	}
	
	private void loadNew(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				RankInfo rankInfo = JSON.parseObject(arg0.toString(), RankInfo.class);
				if (rankInfo != null) {
					latestPageNum++;
					Collections.addAll(newGameList, rankInfo.getData());
					int count = DEFAULT_PAGE_SIZE;
					if (newGameList.size() < DEFAULT_PAGE_SIZE) {
						count = newGameList.size();
					}
					List<InstalledApp> apps = InstalledAppTool.getPersonalApp(getActivity());
					for(int i = newGameList.size() - count;i<newGameList.size();i++){
						GameInfo gameInfo = newGameList.get(i);
						DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(gameInfo.getId()));
						gameInfo.setAdapterType(1);
						int state = InstalledAppTool.contain(apps,gameInfo.getProduct_name());
						if (state != -1) {
							newGameList.get(i).setState(state);
						}else {
							if (info != null 
									&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED)) {
								DownloadManager.DBManager.delete(info);
							}
						}
					}
					refresh_view.refreshFinish(PullToRefreshLayout.SUCCEED);
					newGameAdapter.notifyDataSetChanged();
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				refresh_view.refreshFinish(PullToRefreshLayout.FAIL);
			}
		}, requestTool.getMap(), LATEST_REQUEST_TAG);
	}
	
	private void loadHot(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				RankInfo rankInfo = JSON.parseObject(arg0.toString(), RankInfo.class);
				if (rankInfo != null && rankInfo.getData() != null) {
					hotPageNum++;
					Collections.addAll(hotGameList, rankInfo.getData());
					int count = DEFAULT_PAGE_SIZE;
					if (newGameList.size() < DEFAULT_PAGE_SIZE) {
						count = hotGameList.size();
					}
					List<InstalledApp> apps = InstalledAppTool.getPersonalApp(getActivity());
					for(int i = hotGameList.size() - count;i<hotGameList.size();i++){
						GameInfo gameInfo = hotGameList.get(i);
						DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(gameInfo.getId()));
						gameInfo.setAdapterType(1);
						int state = InstalledAppTool.contain(apps,gameInfo.getProduct_name());
						if (state != -1) {
							hotGameList.get(i).setState(state);
						}else {
							if (info != null 
									&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED)) {
								DownloadManager.DBManager.delete(info);
							}
						}
					}
					refresh_view.refreshFinish(PullToRefreshLayout.SUCCEED);
					newGameAdapter.notifyDataSetChanged();
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				refresh_view.refreshFinish(PullToRefreshLayout.FAIL);
			}
		}, requestTool.getMap(), LATEST_REQUEST_TAG);
	}
	
	private void initView(){
		gameListView = (ListView) view.findViewById(R.id.gameListView);
		informationImgRel = (RelativeLayout) headView.findViewById(R.id.informationImgRel);
		informationImgViewPager = (ViewPager) headView.findViewById(R.id.informationImgViewPager);
		imgList = (LinearLayout) headView.findViewById(R.id.imgList);
		explosiveHeadlinesLin = (LinearLayout) headView.findViewById(R.id.explosiveHeadlinesLin);
		allHeadlinesLin = (LinearLayout) headView.findViewById(R.id.allHeadlinesLin);
		headlineLin = (LinearLayout) headView.findViewById(R.id.headlineLin);
		explosiveHeadlinesImg = (ImageView) headView.findViewById(R.id.explosiveHeadlinesImg);
		allHeadlinesImg = (ImageView) headView.findViewById(R.id.allHeadlinesImg);
		explosiveHeadlinesTx = (TextView) headView.findViewById(R.id.explosiveHeadlinesTx);
		allHeadlinesTx = (TextView) headView.findViewById(R.id.allHeadlinesTx);
		
		refresh_view = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		
		refresh_view.setOnRefreshListener(this);
		
		allHeadlinesImg.setBackgroundResource(R.drawable.zuire);
		explosiveHeadlinesImg.setBackgroundResource(R.drawable.zuibao);
		
		gameListView.addHeaderView(headView);
		
		explosiveHeadlinesTx.setText(R.string.latestGame);
		allHeadlinesTx.setText(R.string.newGame);
		
		explosiveHeadlinesLin.setOnClickListener(this);
		explosiveHeadlinesLin.setOnTouchListener(this);
		allHeadlinesLin.setOnClickListener(this);
		allHeadlinesLin.setOnTouchListener(this);
		
		newGameAdapter = new GameAdapter(getActivity(), newGameList,false,false);
		ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_view));
		gameListView.setAdapter(newGameAdapter);
		
		UIUtil.setViewSize(informationImgRel, MetricsTool.width, 455 * MetricsTool.Ry);
		UIUtil.setViewSize(headlineLin, MetricsTool.width, 75 * MetricsTool.Ry);
		UIUtil.setViewSize(explosiveHeadlinesImg, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		UIUtil.setViewSize(allHeadlinesImg, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		
		UIUtil.setTextSize(explosiveHeadlinesTx, 40);
		UIUtil.setTextSize(allHeadlinesTx, 40);
		
		try {
			UIUtil.setViewSizeMargin(headlineLin, 0, 20 * MetricsTool.Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		if (type == 1) {
			loadNew(RequestTool.LATEST_GAME_URL + "?pageNum=" + latestPageNum);
		}else if (type == 2) {
			loadHot(RequestTool.HOT_GAME_URL + "?pageNum=" + hotPageNum);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (v.getId() == R.id.explosiveHeadlinesLin) {
				type = 1;
				explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.sortTitleColor));
				allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
				newGameAdapter.setList(newGameList);
				newGameAdapter.notifyDataSetChanged();
			}else if (v.getId() == R.id.allHeadlinesLin) {
				type = 2;
				explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
				allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.sortTitleColor));
				newGameAdapter.setList(hotGameList);
				newGameAdapter.notifyDataSetChanged();
			}
		}
		return false;
	}
}
