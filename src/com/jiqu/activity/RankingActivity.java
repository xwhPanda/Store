package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.adapter.RecommendGameAdapter;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.GameInformation;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.RankInfo;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;

public class RankingActivity extends BaseActivity implements OnClickListener,OnRefreshListener{
	private static final int DEFAULT_PAGE_SIZE = 20;
	private TitleView titleView;
	private LinearLayout rankLin;
	private Button favorableComment,hot;
	private PullToRefreshLayout favorableRefreshView,hotRefreshView;
	private PullableListView favorableListView,hotListView;
	
	private GameAdapter favorableAdapter;
	private GameAdapter hotAdapter;
	private List<GameInfo> favorableGameInformations = new ArrayList<GameInfo>();
	private List<GameInfo> hotGameInformations = new ArrayList<GameInfo>();
	
	private RequestTool requestTool;
	
	private boolean favorableRefreshViewShowing = false;
	private boolean hotRefreshViewShowing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		initView();
		
		favorableRequest(0,DEFAULT_PAGE_SIZE);
		hotRequest(0,DEFAULT_PAGE_SIZE);
	}
	
	private void favorableRequest(int start,int end){
		requestTool.initParam();
		requestTool.setParam("start_position", start);
		requestTool.setParam("size", end);
		requestTool.setParam("orderby", "3");
		requestTool.startRankRequest(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				// TODO Auto-generated method stub
				Log.i("TAG", "onResponse : " + arg0.toString());
//				RankInfo rankInfo = JSON.parseObject(arg0.toString(), RankInfo.class);
//				if (rankInfo != null) {
//					Collections.addAll(favorableGameInformations, rankInfo.getItem());
//					int count = DEFAULT_PAGE_SIZE;
//					if (favorableGameInformations.size() < DEFAULT_PAGE_SIZE) {
//						count = favorableGameInformations.size();
//					}
//					List<InstalledApp> apps = InstalledAppTool.getPersonalApp(RankingActivity.this);
//					for(int i = favorableGameInformations.size() - count;i<favorableGameInformations.size();i++){
//						GameInfo gameInfo = favorableGameInformations.get(i);
//						DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(gameInfo.getP_id()));
//						gameInfo.setAdapterType(1);
//						int state = InstalledAppTool.contain(apps,gameInfo.getPackagename(), Integer.parseInt(gameInfo.getVersion_code()));
//						if (state != -1) {
//							favorableGameInformations.get(i).setState(state);
//						}else {
//							if (info != null 
//									&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED
//									|| info.getDownloadState() == DownloadManager.STATE_NEED_UPDATE)) {
//								DownloadManager.DBManager.delete(info);
//							}
//						}
//					}
//					if (favorableRefreshViewShowing) {
//						favorableRefreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
//						favorableRefreshViewShowing = false;
//					}
//					favorableAdapter.notifyDataSetChanged();
//				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Log.i("TAG", "onErrorResponse");
				favorableRefreshView.refreshFinish(PullToRefreshLayout.FAIL);
				if (favorableRefreshViewShowing) {
					favorableRefreshView.refreshFinish(PullToRefreshLayout.FAIL);
					favorableRefreshViewShowing = false;
				}
			}
		});
	}
	
	private void hotRequest(int start , int end){
		requestTool.initParam();
		requestTool.setParam("start_position", start);
		requestTool.setParam("size", end);
		requestTool.setParam("orderby", "4");
		requestTool.startRankRequest(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				// TODO Auto-generated method stub
//				RankInfo rankInfo = JSON.parseObject(arg0.toString(), RankInfo.class);
//				if (rankInfo != null) {
//					Collections.addAll(hotGameInformations, rankInfo.getItem());
//					int count = DEFAULT_PAGE_SIZE;
//					if (hotGameInformations.size() < DEFAULT_PAGE_SIZE) {
//						count = hotGameInformations.size();
//					}
//					List<InstalledApp> apps = InstalledAppTool.getPersonalApp(RankingActivity.this);
//					for(int i = favorableGameInformations.size() - count;i<hotGameInformations.size();i++){
//						GameInfo gameInfo = hotGameInformations.get(i);
//						DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(gameInfo.getP_id()));
//						gameInfo.setAdapterType(1);
//						int state = InstalledAppTool.contain(apps,gameInfo.getPackagename(), Integer.parseInt(gameInfo.getVersion_code()));
//						if (state != -1) {
//							hotGameInformations.get(i).setState(state);
//						}else {
//							if (info != null 
//									&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED
//									|| info.getDownloadState() == DownloadManager.STATE_NEED_UPDATE)) {
//								DownloadManager.DBManager.delete(info);
//							}
//						}
//					}
//					if (hotRefreshViewShowing) {
//						hotRefreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
//						hotRefreshViewShowing = false;
//					}
//					hotAdapter.notifyDataSetChanged();
//				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				if (hotRefreshViewShowing) {
					hotRefreshView.refreshFinish(PullToRefreshLayout.FAIL);
					hotRefreshViewShowing = false;
				}
			}
		});
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.ranking_layout;
	}

	private void initView(){
		titleView  = (TitleView) findViewById(R.id.titleView);
		rankLin = (LinearLayout) findViewById(R.id.rankLin);
		favorableComment = (Button) findViewById(R.id.favorableComment);
		hot = (Button) findViewById(R.id.hot);
		favorableRefreshView = (PullToRefreshLayout) findViewById(R.id.favorableRefreshView);
		hotRefreshView = (PullToRefreshLayout) findViewById(R.id.hotRefreshView);
		favorableListView = (PullableListView) findViewById(R.id.favorableListView);
		hotListView = (PullableListView) findViewById(R.id.hotListView);
		
		favorableRefreshView.setOnRefreshListener(this);
		hotRefreshView.setOnRefreshListener(this);
		
		titleView.setActivity(this);
		titleView.tip.setText(getResources().getString(R.string.rank));
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		
		favorableComment.setOnClickListener(this);
		hot.setOnClickListener(this);
		
		initViewSize();
		
		favorableListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(RankingActivity.this, DetailActivity.class).putExtra("p_id", favorableGameInformations.get(position).getP_id()));
			}
		});
		
		hotListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(RankingActivity.this, DetailActivity.class).putExtra("p_id", hotGameInformations.get(position).getP_id()));
			}
		});
		
		favorableAdapter = new GameAdapter(this,favorableGameInformations,false,false);
		favorableListView.setAdapter(favorableAdapter);
		favorableAdapter.setItemBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		
		hotAdapter = new GameAdapter(this,hotGameInformations,true,false);
		hotListView.setAdapter(hotAdapter);
		hotAdapter.setItemBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		
		favorableAdapter.startObserver();
		hotAdapter.startObserver();
	}
	
	
	
	private void initViewSize(){
		UIUtil.setTextSize(favorableComment, 40);
		UIUtil.setTextSize(hot, 40);
		UIUtil.setViewSize(favorableComment, 515 * Rx, 80 * Ry);
		UIUtil.setViewSize(hot, 515 * Rx, 80 * Ry);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.favorableComment:
			changeBg(favorableComment);
			favorableRefreshView.setVisibility(View.VISIBLE);
			hotRefreshView.setVisibility(View.INVISIBLE);
			break;

		case R.id.hot:
			changeBg(hot);
			hotRefreshView.setVisibility(View.VISIBLE);
			favorableRefreshView.setVisibility(View.INVISIBLE);
			break;
		}
	}
	
	private void changeBg(Button button){
		if (button == favorableComment) {
			favorableComment.setBackgroundColor(getResources().getColor(R.color.blue));
			hot.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
		}else if (button == hot) {
			favorableComment.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
			hot.setBackgroundColor(getResources().getColor(R.color.blue));
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		if (pullToRefreshLayout.getId() == R.id.favorableRefreshView) {
			favorableRefreshViewShowing = true;
			favorableRequest(favorableGameInformations.size(), DEFAULT_PAGE_SIZE);
		}else if (pullToRefreshLayout.getId() == R.id.hotRefreshView) {
			hotRefreshViewShowing = true;
			hotRequest(hotGameInformations.size(), DEFAULT_PAGE_SIZE);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		requestTool.stopRankRequest();
		favorableAdapter.stopObserver();
		hotAdapter.stopObserver();
	}
}
