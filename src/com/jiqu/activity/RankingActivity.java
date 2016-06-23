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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.adapter.RecommendGameAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.RankInfo;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;

import de.greenrobot.dao.query.QueryBuilder;

public class RankingActivity extends BaseActivity implements OnClickListener,OnRefreshListener{
	private final int DEFAULT_PAGE_SIZE = 10;
	private final String PRAISE_REQUEST = "praiseRequest";
	private final String HOT_REQUEST = "hotRequest";
	private TitleView titleView;
	private LinearLayout rankLin;
	private RelativeLayout praiseRel;
	private RelativeLayout hotRel;
	private LoadStateView hotLoadView;
	private LoadStateView praiseLoadView;
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
	private int praisePageNum = 1;
	private int hotPageNum = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		initView();
		
		loadPraiseData(RequestTool.PRAISE_URL);
	}
	
	private void loadPraiseData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				praiseLoadView.loadDataSuccess();
				praiseLoadView.setVisibility(View.GONE);
				favorableRefreshView.setVisibility(View.VISIBLE);
				RankInfo rankInfo = JSON.parseObject(arg0, RankInfo.class);
				if (rankInfo != null) {
					if (rankInfo.getStatus() == 1 && rankInfo.getData() != null) {
						Collections.addAll(favorableGameInformations, rankInfo.getData());
						setState(favorableGameInformations, DEFAULT_PAGE_SIZE);
						favorableAdapter.notifyDataSetChanged();
						praisePageNum++;
					}else if (rankInfo.getStatus() == 0) {
						Toast.makeText(RankingActivity.this, R.string.notMore, Toast.LENGTH_SHORT).show();
					}
				}
				if (favorableRefreshViewShowing) {
					favorableRefreshViewShowing = false;
					favorableRefreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				if (favorableRefreshViewShowing) {
					favorableRefreshViewShowing = false;
					favorableRefreshView.refreshFinish(PullToRefreshLayout.FAIL);
				}
			}
		}, requestTool.getMap(), PRAISE_REQUEST);
	}
	
	private void loadHotData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				hotLoadView.loadDataSuccess();
				hotLoadView.setVisibility(View.GONE);
				hotRefreshView.setVisibility(View.VISIBLE);
				RankInfo rankInfo = JSON.parseObject(arg0, RankInfo.class);
				if (rankInfo != null) {
					if (rankInfo.getStatus() == 1 && rankInfo.getData() != null) {
						Collections.addAll(hotGameInformations, rankInfo.getData());
						setState(hotGameInformations, DEFAULT_PAGE_SIZE);
						hotAdapter.notifyDataSetChanged();
						hotPageNum++;
					}else if (rankInfo.getStatus() == 0) {
						Toast.makeText(RankingActivity.this, R.string.notMore, Toast.LENGTH_SHORT).show();
					}
				}
				if (hotRefreshViewShowing) {
					hotRefreshViewShowing = false;
					hotRefreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				if (hotRefreshViewShowing) {
					hotRefreshViewShowing = false;
					hotRefreshView.refreshFinish(PullToRefreshLayout.FAIL);
				}
			}
		}, requestTool.getMap(), PRAISE_REQUEST);
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.ranking_layout;
	}

	private void initView(){
		titleView  = (TitleView) findViewById(R.id.titleView);
		rankLin = (LinearLayout) findViewById(R.id.rankLin);
		hotLoadView = (LoadStateView) findViewById(R.id.hotLoadView);
		praiseLoadView = (LoadStateView) findViewById(R.id.praiseLoadView);
		favorableComment = (Button) findViewById(R.id.favorableComment);
		hot = (Button) findViewById(R.id.hot);
		favorableRefreshView = (PullToRefreshLayout) findViewById(R.id.favorableRefreshView);
		hotRefreshView = (PullToRefreshLayout) findViewById(R.id.hotRefreshView);
		favorableListView = (PullableListView) findViewById(R.id.favorableListView);
		hotListView = (PullableListView) findViewById(R.id.hotListView);
		praiseRel = (RelativeLayout) findViewById(R.id.praiseRel);
		hotRel = (RelativeLayout) findViewById(R.id.hotRel);
		
		favorableRefreshView.setOnRefreshListener(this);
		hotRefreshView.setOnRefreshListener(this);
		
		titleView.setActivity(this);
		titleView.tip.setText(getResources().getString(R.string.rank));
		titleView.back.setBackgroundResource(R.drawable.fanhui);
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
			praiseRel.setVisibility(View.VISIBLE);
			hotRel.setVisibility(View.INVISIBLE);
			favorableAdapter.notifyDataSetChanged();
			break;

		case R.id.hot:
			changeBg(hot);
			hotRel.setVisibility(View.VISIBLE);
			praiseRel.setVisibility(View.INVISIBLE);
			if (hotGameInformations.size() == 0) {
				loadHotData(RequestTool.HOT_URL);
			}else {
				hotAdapter.notifyDataSetChanged();
			}
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
			loadPraiseData(RequestTool.PRAISE_URL + "?pageNum=" + praisePageNum);
		}else if (pullToRefreshLayout.getId() == R.id.hotRefreshView) {
			hotRefreshViewShowing = true;
			loadHotData(RequestTool.HOT_URL + "?pageNum=" + hotPageNum);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		requestTool.stopRequest(HOT_REQUEST);
		requestTool.stopRequest(PRAISE_REQUEST);
		favorableAdapter.stopObserver();
		hotAdapter.stopObserver();
	}
	
	@Override
	protected void unInstallEvent(String uninstallPackageName) {
		// TODO Auto-generated method stub
		for(GameInfo info : favorableGameInformations){
			if (info.getPackage_name().equals(uninstallPackageName)) {
				info.setState(DownloadManager.STATE_NONE);
				QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
				DownloadAppinfo downloadAppinfo = qb.where(Properties.Id.eq(info.getId())).unique();
				if (downloadAppinfo != null) {
					DownloadManager.DBManager.getDownloadAppinfoDao().delete(downloadAppinfo);
				}
			}
		}
		favorableAdapter.notifyDataSetChanged();
		
		for(GameInfo info : hotGameInformations){
			if (info.getPackage_name().equals(uninstallPackageName)) {
				info.setState(DownloadManager.STATE_NONE);
				QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
				DownloadAppinfo downloadAppinfo = qb.where(Properties.Id.eq(info.getId())).unique();
				if (downloadAppinfo != null) {
					DownloadManager.DBManager.getDownloadAppinfoDao().delete(downloadAppinfo);
				}
			}
		}
		hotAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void installEvent(String installPackageName) {
		// TODO Auto-generated method stub
		for(GameInfo info : favorableGameInformations){
			if (info.getPackage_name().equals(installPackageName)) {
				info.setState(DownloadManager.STATE_INSTALLED);
				QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
				DownloadAppinfo downloadAppinfo = qb.where(Properties.Id.eq(info.getId())).unique();
				if (downloadAppinfo != null) {
					downloadAppinfo.setDownloadState(DownloadManager.STATE_INSTALLED);
					DownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(downloadAppinfo);
				}
			}
		}
		favorableAdapter.notifyDataSetChanged();
		
		for(GameInfo info : hotGameInformations){
			if (info.getPackage_name().equals(installPackageName)) {
				info.setState(DownloadManager.STATE_INSTALLED);
				QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
				DownloadAppinfo downloadAppinfo = qb.where(Properties.Id.eq(info.getId())).unique();
				if (downloadAppinfo != null) {
					downloadAppinfo.setDownloadState(DownloadManager.STATE_INSTALLED);
					DownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(downloadAppinfo);
				}
			}
		}
		hotAdapter.notifyDataSetChanged();
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
