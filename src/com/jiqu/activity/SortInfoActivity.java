package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.CategoryAppsInfo;
import com.jiqu.object.GameInfo;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.ThematicItem;
import com.jiqu.object.ThematicSortInfo;
import com.jiqu.object.ThematicSortInfoItem;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.TitleView;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.SortTitleView;

public class SortInfoActivity extends BaseActivity implements OnRefreshListener,Listener<JSONObject>,ErrorListener{
	private static final int DEFAULT_PAGE_SIZE = 20;
	private View headView;
	private TitleView titleView;
	private ListView sortListView;
	private PullToRefreshLayout refreshLayout;
	private GameAdapter adapter;
	
	private ImageView sortHeadNewAddGameImg;
	private TextView sortHeadNewAddGameContent;
	
	private List<GameInfo> gameInformations = new ArrayList<GameInfo>();
	//分类的类型
	private int type = 0;
	//0：从分类页跳转；1：从专题页跳转
	private int fromWhere = 0;
	private ThematicItem thematicItem;
	
	private String categoryTitle = "";
	private int categoryId;
	private boolean refreshViewShowing;
	
	private RequestTool requestTool;
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestTool = RequestTool.getInstance();
			fromWhere = getIntent().getIntExtra("fromWhere", 0);
			initView();
			
			if (fromWhere == 0) {
				categoryAppsRequest(0,DEFAULT_PAGE_SIZE);
			}else if (fromWhere == 1) {
				thematicItem = (ThematicItem) getIntent().getSerializableExtra("thematicItem");
				if (thematicItem != null) {
					thematicAppsRequest();
				}
			}
		}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.sort_info_layout;
	}
	
	private void thematicAppsRequest(){
		titleView.tip.setText(thematicItem.getTitle());
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				ThematicSortInfo thematicSortInfo = JSON.parseObject(arg0, ThematicSortInfo.class);
				setThematicData(thematicSortInfo.getAlldata());
			}
		}, RequestTool.thematicUrl + thematicItem.getId(), new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
		}, requestTool.getMap(), "thematic");
	}
	
	private void setThematicData(ThematicSortInfoItem[] items){
		int length = items.length;
		for (int i = 0; i < length; i++) {
			gameInformations.add(ThematicSortInfoItem.toGameInfo(items[i]));
		}
		adapter.notifyDataSetChanged();
	}
	
	private void categoryAppsRequest(int start,int end){
		requestTool.initParam();
		requestTool.setParam("start_position", start);
		requestTool.setParam("size", end);
		requestTool.setParam("category_id", categoryId);
		requestTool.startCategoryAppsRequest(this, this);
	}
	
	private void initView(){
		Intent intent = getIntent();
		categoryTitle = intent.getStringExtra("categoryTitle");
		categoryId = intent.getIntExtra("categoryId", 0);
		
		titleView = (TitleView) findViewById(R.id.titleView);
		sortListView = (ListView) findViewById(R.id.sortListView);
		refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
		refreshLayout.setOnRefreshListener(this);
		
		titleView.setActivity(this);
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(categoryTitle);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		
		
		//根据分类的不同加载不同的头部
//		LayoutInflater inflater = LayoutInflater.from(this);
//		headView = inflater.inflate(R.layout.sort_head_new_add_game, null);
//		sortHeadNewAddGameImg = (ImageView) headView.findViewById(R.id.sortHeadNewAddGameImg);
//		sortHeadNewAddGameContent = (TextView) headView.findViewById(R.id.sortHeadNewAddGameContent);
//		sortListView.addHeaderView(headView);
		
//		for(int i = 0; i<20;i++){
//			GameInformation gameInformation = new GameInformation();
//			gameInformations.add(gameInformation);
//		}
		
//		adapter = new RecommendGameAdapter(gameInformations, this);
//		sortListView.setAdapter(adapter);
		
		adapter = new GameAdapter(this, gameInformations, false, false);
		adapter.setItemBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		sortListView.setAdapter(adapter);
		
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(SortInfoActivity.this, DetailActivity.class).putExtra("p_id", gameInformations.get(position).getP_id()));
			}
		});
		
		adapter.startObserver();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adapter.stopObserver();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		refreshViewShowing = true;
		categoryAppsRequest(gameInformations.size(),DEFAULT_PAGE_SIZE);
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		Log.i("TAG", "onErrorResponse :" + arg0);
		if (refreshViewShowing) {
			refreshViewShowing = false;
			refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
		}
	}

	@Override
	public void onResponse(JSONObject arg0) {
		// TODO Auto-generated method stub
		Log.i("TAG", "onResponse :" + arg0);
		CategoryAppsInfo categoryAppsInfo = JSON.parseObject(arg0.toString(), CategoryAppsInfo.class);
		if (categoryAppsInfo != null && categoryAppsInfo.getItem() != null) {
			Collections.addAll(gameInformations, categoryAppsInfo.getItem());
			List<InstalledApp> apps = InstalledAppTool.getPersonalApp(this);
			int count = DEFAULT_PAGE_SIZE;
			if (gameInformations.size() < DEFAULT_PAGE_SIZE) {
				count = gameInformations.size();
			}
			
//			for(int i = gameInformations.size() - count;i<gameInformations.size();i++){
//				GameInfo gameInfo = gameInformations.get(i);
//				DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(gameInfo.getP_id()));
//				gameInfo.setAdapterType(1);
//				int state = InstalledAppTool.contain(apps,gameInfo.getPackagename(), Integer.parseInt(gameInfo.getVersion_code()));
//				if (state != -1) {
//					gameInfo.setState(state);
//				}else {
//					if (info != null 
//							&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED
//							|| info.getDownloadState() == DownloadManager.STATE_NEED_UPDATE)) {
//						DownloadManager.DBManager.delete(info);
//					}
//				}
//			}
			if (refreshViewShowing) {
				refreshViewShowing = false;
				refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
			adapter.notifyDataSetChanged();
		}
	}
}
