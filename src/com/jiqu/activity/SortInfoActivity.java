package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jiqu.object.SortItem;
import com.jiqu.object.ThematicItem;
import com.jiqu.object.ThematicSortInfo;
import com.jiqu.object.ThematicSortInfoItem;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.TitleView;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;

public class SortInfoActivity extends BaseActivity implements OnRefreshListener,OnClickListener{
	private final int DEFAULT_PAGE_SIZE = 10;
	private final String THEMATIC_DETAIL_REQUEST = "thematicDetailRequest";
	private final String CATEGORY_APPS_REQUEST = "categoryAppsRequest";
	private View headView;
	private TitleView titleView;
	private ListView sortListView;
	private PullToRefreshLayout refreshLayout;
	private GameAdapter adapter;
	private LoadStateView loadView;
	
	private ImageView sortHeadNewAddGameImg;
	private TextView sortHeadNewAddGameContent;
	
	private List<GameInfo> gameInformations = new ArrayList<GameInfo>();
	//分类的类型: 1:新游榜；2：热游榜；3：必玩榜
	private int gameType = 0;
	//0：从分类页跳转；1：从专题页跳转;2:从首页跳转
	private int fromWhere = 0;
	private ThematicItem thematicItem;
	private SortItem sortItem;
	
	private String categoryTitle = "";
	private int categoryId;
	private boolean refreshViewShowing;
	
	private int sortPageNum = 1;
	private int thematicPageNum = 1;
	private int gamePageNum = 1;
	private String loadUrl = "";
	private RequestTool requestTool;
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestTool = RequestTool.getInstance();
			fromWhere = getIntent().getIntExtra("fromWhere", 0);
			initView();
			
			if (fromWhere == 0) {
				sortItem = (SortItem) getIntent().getSerializableExtra("categoryItem");
				if (sortItem != null) {
					if (sortItem.getName() != null && !TextUtils.isEmpty(sortItem.getName())) {
						titleView.tip.setText(sortItem.getName());
					}
					loadCategoryData(RequestTool.CATEGORY_APPS_URL + "?columnId=" + sortItem.getId());
				}
			}else if (fromWhere == 1) {
				thematicItem = (ThematicItem) getIntent().getSerializableExtra("thematicItem");
				if (thematicItem != null) {
					if (thematicItem.getName() != null && !TextUtils.isEmpty(thematicItem.getName())) {
						titleView.tip.setText(thematicItem.getName());
					}else {
						if (thematicItem.getTitle() != null && !TextUtils.isEmpty(thematicItem.getTitle())) {
							titleView.tip.setText(thematicItem.getTitle());
						}
					}
					thematicAppsRequest(RequestTool.SPECIAL_DETAIL_URL + "?id=" + thematicItem.getId());
				}
			}else if (fromWhere == 2) {
				gameType = getIntent().getIntExtra("gameType", 0);
				switch (gameType) {
				case 1:
					titleView.tip.setText(getResources().getString(R.string.newGameList));
					loadUrl = RequestTool.NEW_GAME_LIST_URL;
					break;
				case 2:
					titleView.tip.setText(getResources().getString(R.string.popularGameList));
					loadUrl = RequestTool.POPULAR_GAME_LIST_URL;
					break;
				case 3:
					titleView.tip.setText(getResources().getString(R.string.willGameList));
					loadUrl = RequestTool.WILL_GAME_LIST_URL;
					break;
				}
				loadCategoryData(loadUrl);
			}
		}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.sort_info_layout;
	}
	
	private void loadCategoryData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataSuccess();
				loadView.setVisibility(View.GONE);
				refreshLayout.setVisibility(View.VISIBLE);
				CategoryAppsInfo categoryAppsInfo = JSON.parseObject(arg0.toString(), CategoryAppsInfo.class);
				if (categoryAppsInfo != null) {
					if (categoryAppsInfo.getStatus() == 1) {
						sortPageNum++;
						if (categoryAppsInfo.getData() != null) {
							setThematicData(categoryAppsInfo.getData());
						}
					}else if (categoryAppsInfo.getStatus() == 0) {
						Toast.makeText(SortInfoActivity.this, R.string.notMore, Toast.LENGTH_SHORT).show();
					}
				}
				if (refreshViewShowing) {
					refreshViewShowing = false;
					refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
			
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataFail();
				if (refreshViewShowing) {
					refreshViewShowing = false;
					refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
				}
			}
		}, requestTool.getMap(), CATEGORY_APPS_REQUEST);
	}
	
	private void thematicAppsRequest(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataSuccess();
				loadView.setVisibility(View.GONE);
				refreshLayout.setVisibility(View.VISIBLE);
				ThematicSortInfo thematicSortInfo = JSON.parseObject(arg0, ThematicSortInfo.class);
				if (thematicSortInfo != null) {
					if (thematicSortInfo.getStatus() == 1) {
						thematicPageNum++;
						if (thematicSortInfo.getData() != null) {
							setThematicData(thematicSortInfo.getData());
						}
					}else if (thematicSortInfo.getStatus() == 0) {
						Toast.makeText(SortInfoActivity.this, R.string.notMore, Toast.LENGTH_SHORT).show();
					}
				}
				if (refreshViewShowing) {
					refreshViewShowing = false;
					refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataFail();
				if (refreshViewShowing) {
					refreshViewShowing = false;
					refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
				}
			}
		}, requestTool.getMap(), THEMATIC_DETAIL_REQUEST);
	}
	
	private void setThematicData(GameInfo[] infos){
		Collections.addAll(gameInformations, infos);
		adapter.notifyDataSetChanged();
	}
	
	private void initView(){
		Intent intent = getIntent();
		categoryTitle = intent.getStringExtra("categoryTitle");
		categoryId = intent.getIntExtra("categoryId", 0);
		
		titleView = (TitleView) findViewById(R.id.titleView);
		sortListView = (ListView) findViewById(R.id.sortListView);
		loadView = (LoadStateView) findViewById(R.id.loadView);
		refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
		refreshLayout.setOnRefreshListener(this);
		loadView.loadAgain(this);
		
		titleView.setActivity(this);
		titleView.parentView.setBackgroundResource(R.drawable.zhuanti_bg);
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
		requestTool.stopRequest(THEMATIC_DETAIL_REQUEST);
		requestTool.stopRequest(CATEGORY_APPS_REQUEST);
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		refreshViewShowing = true;
		if (fromWhere == 0) {
			if (sortItem != null) {
				loadCategoryData(RequestTool.CATEGORY_APPS_URL + "?columnId=" + sortItem.getId() + "&pageNum=" + sortPageNum);
			}
		}else if (fromWhere == 1) {
			if (thematicItem != null) {
				thematicAppsRequest(RequestTool.SPECIAL_DETAIL_URL + "?id=" + thematicItem.getId() + "&pageNum=" + thematicPageNum);
			}
		}else if (fromWhere == 2) {
			loadCategoryData(loadUrl + "?pageNum=" + gamePageNum);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == loadView.getLoadBtn()) {
			if (fromWhere == 0) {
				
			}else if (fromWhere == 1) {
				thematicAppsRequest(RequestTool.SPECIAL_DETAIL_URL);
			}
		}
	}
}
