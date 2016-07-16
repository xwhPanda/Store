package com.jiqu.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.jiqu.adapter.GameAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.StringUtil;
import com.jiqu.interfaces.SearcheListener;
import com.jiqu.object.GameDetailInfo;
import com.jiqu.object.GameInfo;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.SearchIndexInfo;
import com.jiqu.object.SearchIndexItem;
import com.jiqu.object.SearchIndexItemInfo;
import com.jiqu.object.SearchInfo;
import com.jiqu.object.SearchKeyword;
import com.jiqu.store.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.SearchItemView;

import de.greenrobot.dao.query.QueryBuilder;

public class SearchActivity extends BaseActivity implements OnClickListener,Listener<String>,ErrorListener,SearcheListener{
	private final int DEFAULT_PAGE_SIZE = 10;
	private final String SEARCH_INDEX_REQUEST = "searchIndexRequest";
	private final String SEARCH_REQUEST = "searchRequest";
	private RelativeLayout searchLayout;
	private Button backBtn;
	private RelativeLayout searchEditLayout;
	private Button searchBtn;
	private EditText searchEdit;
	private LinearLayout keywordLin;
	private RelativeLayout searchResultRel;
	private ListView searchListView;
	private LoadStateView searchLoadView;
	private TextView noResultTx;
	
	private RequestTool requestTool;
	private GameAdapter adapter;
	private List<GameInfo> gameInfos = new ArrayList<GameInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		
		initView();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("SearchActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.search_list_item;
	}

	private void initView(){
		searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
		backBtn = (Button) findViewById(R.id.backBtn);
		searchEditLayout = (RelativeLayout) findViewById(R.id.searchEditLayout);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchEdit = (EditText) findViewById(R.id.searchEdit);
		
		searchResultRel = (RelativeLayout) findViewById(R.id.searchResultRel);
		searchLoadView = (LoadStateView) findViewById(R.id.searchLoadView);
		noResultTx = (TextView) findViewById(R.id.noResultTx);
		
		keywordLin = (LinearLayout) findViewById(R.id.keywordLin);
		searchListView = (ListView) findViewById(R.id.searchListView);
		
		backBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		
		initViewSize();
		
		adapter = new GameAdapter(this, gameInfos,false,false);
		searchListView.setAdapter(adapter);
		
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(SearchActivity.this, DetailActivity.class).putExtra("p_id", gameInfos.get(position).getP_id()));
			}
		});
		
		loadData(RequestTool.SEARCHE_INDEX_URL);
	}
	
	private void setData(SearchIndexItem item){
		SearchIndexItemInfo[] hots = item.getHot();
		SearchIndexItemInfo[] lists = item.getList();
		int hotLength = hots.length;
		int listLength = lists.length;
		List<SearchKeyword> hotKeywords = new ArrayList<SearchKeyword>();
		List<SearchKeyword> listKeywords = new ArrayList<SearchKeyword>();
		SearchItemView hotItemView = new SearchItemView(this);
		SearchItemView listItemView = new SearchItemView(this);
		hotItemView.setSearchListener(this);
		listItemView.setSearchListener(this);
		keywordLin.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < hotLength; i++) {
			SearchKeyword keyword = new SearchKeyword();
			keyword.setKeyword(hots[i].getContent());
			keyword.setBackgroundType(1);
			hotKeywords.add(keyword);
		}
		hotItemView.titleImg.setBackgroundResource(R.drawable.sousuo2);
		hotItemView.titleContent.setText("热门");
		hotItemView.setData(hotKeywords);
		keywordLin.addView(hotItemView);
		
		for (int i = 0; i < listLength; i++) {
			SearchKeyword keyword = new SearchKeyword();
			keyword.setKeyword(lists[i].getContent());
			listKeywords.add(keyword);
		}
		listItemView.titleContent.setText("");
		listItemView.titleImg.setVisibility(View.GONE);
		listItemView.setData(listKeywords);
		ImageView imageView = new ImageView(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, (int)(4 * Ry));
		lp.topMargin = (int)(30 * Ry);
		lp.bottomMargin = (int)(30 * Ry);
		imageView.setLayoutParams(lp);
		imageView.setBackgroundResource(R.drawable.xian);
		keywordLin.addView(imageView);
		keywordLin.addView(listItemView);
	}
	
	private void loadData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				SearchIndexInfo searchInfo = JSON.parseObject(arg0, SearchIndexInfo.class);
				if (searchInfo != null) {
					if (searchInfo.getStatus() == 1) {
						setData(searchInfo.getData());
					}
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		}, requestTool.getMap(), SEARCH_INDEX_REQUEST);
	}
	
	private void search(String url){
		requestTool.stopRequest(SEARCH_INDEX_REQUEST);
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, this, url, this, requestTool.getMap(), SEARCH_REQUEST);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(searchLayout, LayoutParams.MATCH_PARENT, 180 * Ry);
		UIUtil.setViewSize(backBtn, 36 * Rx, 36 * Rx);
		UIUtil.setViewSize(searchEditLayout, 840 * Rx, 70 * Ry);
		UIUtil.setViewSize(searchBtn, 50 * Rx, 50 * Rx);
		
		UIUtil.setTextSize(searchEdit, 30);
		
		
		try {
			UIUtil.setViewSizeMargin(backBtn, 30 * Rx, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getSearchText(){
		return searchEdit.getText().toString();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adapter != null) {
			adapter.startObserver();
		}
		MobclickAgent.onPageStart("SearchActivity");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (adapter != null) {
			adapter.stopObserver();
		}
		requestTool.stopRequest(SEARCH_INDEX_REQUEST);
		requestTool.stopRequest(SEARCH_REQUEST);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backBtn) {
			finish();
		}else if (v.getId() == R.id.searchBtn) {
			String text = getSearchText();
			if (!TextUtils.isEmpty(text)) {
				searchLoadView.setVisibility(View.VISIBLE);
				searchLoadView.showLoading();
				keywordLin.setVisibility(View.GONE);
				searchResultRel.setVisibility(View.VISIBLE);
				noResultTx.setVisibility(View.GONE);
				searchListView.setVisibility(View.GONE);
				try {
					text = URLEncoder.encode(text, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				search(RequestTool.SEARCH_URL + "?keyword=" + text);
			}else {
				Toast.makeText(this, "请输入要搜索的内容", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		keywordLin.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onResponse(String arg0) {
		// TODO Auto-generated method stub
		searchLoadView.loadDataSuccess();
		searchLoadView.setVisibility(View.GONE);
		keywordLin.setVisibility(View.INVISIBLE);
		searchResultRel.setVisibility(View.VISIBLE);
		SearchInfo searchInfo = JSON.parseObject(arg0, SearchInfo.class);
		if (searchInfo != null) {
			if (searchInfo.getStatus() == 1) {
				noResultTx.setVisibility(View.GONE);
				searchListView.setVisibility(View.VISIBLE);
				gameInfos.clear();
				Collections.addAll(gameInfos, searchInfo.getData());
				setState(gameInfos, DEFAULT_PAGE_SIZE);
				adapter.notifyDataSetChanged();
				searchListView.setVisibility(View.VISIBLE);
			}else if (searchInfo.getStatus() == 0) {
				noResultTx.setVisibility(View.VISIBLE);
				searchListView.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onSearch(String keyword) {
		// TODO Auto-generated method stub
		searchLoadView.setVisibility(View.VISIBLE);
		searchLoadView.showLoading();
		keywordLin.setVisibility(View.GONE);
		searchResultRel.setVisibility(View.VISIBLE);
		noResultTx.setVisibility(View.GONE);
		searchListView.setVisibility(View.GONE);
		search(RequestTool.SEARCH_URL + "?keyword=" + keyword);
	}
	
	@Override
	protected void unInstallEvent(String uninstallPackageName) {
		// TODO Auto-generated method stub
		for(GameInfo info : gameInfos){
			if (info.getPackage_name().equals(uninstallPackageName)) {
				info.setState(DownloadManager.STATE_NONE);
				QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
				DownloadAppinfo downloadAppinfo = qb.where(Properties.Id.eq(info.getId())).unique();
				if (downloadAppinfo != null) {
					DownloadManager.DBManager.getDownloadAppinfoDao().delete(downloadAppinfo);
				}
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void installEvent(String installPackageName) {
		// TODO Auto-generated method stub
		for(GameInfo info : gameInfos){
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
		adapter.notifyDataSetChanged();
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
