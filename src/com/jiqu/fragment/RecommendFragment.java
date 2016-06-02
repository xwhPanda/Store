package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.alibaba.fastjson.JSON;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.activity.BoutiqueActivity;
import com.jiqu.activity.DetailActivity;
import com.jiqu.activity.HeadlineActivity;
import com.jiqu.activity.RankingActivity;
import com.jiqu.activity.SortActivity;
import com.jiqu.activity.ThematicActivity;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.Account;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.RecommendDataInfo;
import com.jiqu.object.RecommendHeadlineInfo;
import com.jiqu.object.TopRecommendtInfo;
import com.vr.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.PullableListView;
import com.jiqu.view.RecommedGameView;
import com.jiqu.view.ViewPagerLinView;

import de.greenrobot.dao.query.QueryBuilder;


import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class RecommendFragment extends BaseFragment implements OnRefreshListener, 
	ErrorListener, OnClickListener, Listener<String>{
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final String RECOMMEND_REQUEST_TAG = "recommendRequestTag";
	private float Rx, Ry;
	private View view;
	private View headView;

	private LinearLayout boutiqueLin, thematicLin, rankingLin, sortLin;
	private ImageView boutiqueImg, thematicImg, rankingImg, sortImg;
	private TextView boutiqueTx, thematicTx, rankingTx, sortTx;
	private LinearLayout recommendGameList;
	private LinearLayout recommendCategory;
	private LinearLayout headlinesLin;
	private RelativeLayout headlineContenLin;
	private TextView headlinesTx;
	private ImageView headlineContenImg;
	private TextView headlinesInformation;
	private HorizontalScrollView scrollView;
	private TextView emptyView;

	private ViewPagerLinView viewPager;
    private ViewPagerLinView recommendGameInformationPager;
	private View view1, view2, view3;
	private int currentItem = 0;

	private LoadStateView loadStateView;
	private PullToRefreshLayout pullToRefreshLayout;
	private PullableListView recommendListView;
	private GameAdapter adapter;
	private List<GameInfo> resultList = new ArrayList<GameInfo>();
	
	private JsonObjectRequest objectRequest;
	private TopRecommendtInfo topRecommendtInfo = new TopRecommendtInfo();
	private RequestTool requestTool;
	
	private boolean refreshShow = false;
	private int currentIndex = 0;
	private RecommendHeadlineInfo headlineInfo;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		getActivity().registerReceiver(appInstallReceiver, filter);
		
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("deleted_downloaded_files_action");
		getActivity().registerReceiver(deleteReceiver, filter2);
		requestTool = RequestTool.getInstance();
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		loadRecommedData();
	}
	
	private void loadRecommedData(){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, 
				this, RequestTool.RECOMMEND_URL, this, requestTool.getMap(), true,RECOMMEND_REQUEST_TAG);
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initAdapter() {
		if (adapter == null) {
			adapter = new GameAdapter(getActivity(), resultList,false,false);
		}
		recommendListView.setAdapter(adapter);
	}

	@Override
	public View initView() {
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		view = LayoutInflater.from(activity).inflate(R.layout.recommend_1, null);
		headView = LayoutInflater.from(getActivity()).inflate(R.layout.recommend_head, null);
		loadStateView = (LoadStateView) view.findViewById(R.id.loadStateView);
		pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		recommendListView = (PullableListView) view.findViewById(R.id.recommendListView);
		
		pullToRefreshLayout.setOnRefreshListener(this);
		recommendListView.setCanPullUp(false);

		initHeadView();

		recommendListView.addHeaderView(headView);
		recommendListView.setHasHead(true);

		recommendListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), DetailActivity.class)
				.putExtra("id", resultList.get(position - 1).getId())
				.putExtra("name", resultList.get(position - 1).getApply_name()));
			}
		});
		
		initViewSize();
		
		initAdapter();
		if (adapter != null) {
			adapter.startObserver();
		}
		
		loadStateView.loadAgain(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadStateView.showLoading();
				loadRecommedData();
			}
		});
		return view;
	}

	private void initViewSize() {
		UIUtil.setViewSize(viewPager, MetricsTool.width, 455 * Rx);
		UIUtil.setViewSize(recommendCategory, MetricsTool.width, 150 * Ry);
		UIUtil.setViewSize(boutiqueImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(thematicImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(rankingImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(sortImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(headlinesLin, MetricsTool.width, 90 * Ry);
		UIUtil.setViewSize(headlineContenImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(emptyView, MetricsTool.width, 25 * Ry);

		UIUtil.setTextSize(headlinesInformation, 30);

		UIUtil.setViewSize(recommendGameInformationPager, MetricsTool.width, 235 * Ry);

		UIUtil.setTextSize(boutiqueTx, 40);
		UIUtil.setTextSize(thematicTx, 40);
		UIUtil.setTextSize(rankingTx, 40);
		UIUtil.setTextSize(sortTx, 40);
		UIUtil.setTextSize(headlinesTx, 45);

		try {
			UIUtil.setViewSizeMargin(scrollView, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(recommendGameInformationPager, 0, 25 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHeadView() {
		viewPager = (ViewPagerLinView) headView.findViewById(R.id.viewPagerLin);
		viewPager.setClass(DetailActivity.class);
		viewPager.setDefaultImgId(R.drawable.recommend_viewpager_default);
		
		boutiqueLin = (LinearLayout) headView.findViewById(R.id.boutiqueLin);
		thematicLin = (LinearLayout) headView.findViewById(R.id.thematicLin);
		rankingLin = (LinearLayout) headView.findViewById(R.id.rankingLin);
		sortLin = (LinearLayout) headView.findViewById(R.id.sortLin);
		
		view1 = (View) headView.findViewById(R.id.view1);
		view1.setBackgroundDrawable(UIUtil.readBitmapDrawable(activity, R.drawable.xiaotiao));
		
		boutiqueLin.setOnClickListener(this);
		thematicLin.setOnClickListener(this);
		rankingLin.setOnClickListener(this);
		sortLin.setOnClickListener(this);

		boutiqueImg = (ImageView) headView.findViewById(R.id.boutiqueImg);
		thematicImg = (ImageView) headView.findViewById(R.id.thematicImg);
		rankingImg = (ImageView) headView.findViewById(R.id.rankingImg);
		sortImg = (ImageView) headView.findViewById(R.id.sortImg);

		headlinesLin = (LinearLayout) headView.findViewById(R.id.headlinesLin);
		headlineContenLin = (RelativeLayout) headView.findViewById(R.id.headlineContenLin);
		headlinesTx = (TextView) headView.findViewById(R.id.headlinesTx);
		headlineContenImg = (ImageView) headView.findViewById(R.id.headlineContenImg);
		headlinesInformation = (TextView) headView.findViewById(R.id.headlinesInformation);
		
		headlineContenImg.setBackgroundDrawable(UIUtil.readBitmapDrawable(activity, R.drawable.bao));

		headlineContenLin.setOnClickListener(this);

		boutiqueTx = (TextView) headView.findViewById(R.id.boutiquTx);
		thematicTx = (TextView) headView.findViewById(R.id.thematicTx);
		rankingTx = (TextView) headView.findViewById(R.id.rankingTx);
		sortTx = (TextView) headView.findViewById(R.id.sortTx);

		scrollView = (HorizontalScrollView) headView.findViewById(R.id.scrollView);
		emptyView = (TextView) headView.findViewById(R.id.emptyView);

		recommendCategory = (LinearLayout) headView.findViewById(R.id.recommendCategory);
		recommendGameList = (LinearLayout) headView.findViewById(R.id.recommendGameList);

		recommendGameInformationPager = (ViewPagerLinView) headView.findViewById(R.id.recommendGameInformationPager);
		recommendGameInformationPager.setClass(DetailActivity.class);
		recommendGameInformationPager.setDefaultImgId(R.drawable.recomend_default_3);
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
//		requestTool.initParam();
//		requestTool.setParam("size", String.valueOf(resultList.size() + 20));
//		requestTool.startHomeRecommendRequest(this, this, false);
		
		refreshShow = true;
	}

	int i = 0;

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		loadStateView.loadDataFail();
		if (refreshShow) {
			refreshShow = false;
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
		}
	}

	private void initHeadline(RecommendHeadlineInfo headlineInfo){
		headlinesInformation.setText(headlineInfo.getTitle());
	}
	
	private void initRecommedGame(GameInfo[] gameInfos){
		if (gameInfos.length > 0) {
			int count = gameInfos.length;
			for (int i = 0; i < count; i++) {
				final GameInfo gameInfo = gameInfos[i];
				RecommedGameView gameView = new RecommedGameView(getActivity());
				gameView.setClickable(true);
				gameView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(getActivity(), DetailActivity.class)
						.putExtra("id", gameInfo.getId())
						.putExtra("name", gameInfo.getApply_name()));
					}
				});
				gameView.gameName.setText(gameInfo.getApply_name());
				gameView.getDesView().setText(gameInfo.getDescript());
				Bitmap bitmap = UIUtil.readBitmap(activity, R.drawable.recommend_default);
				ImageListener listener = ImageLoader.getImageListener(gameView.gameIcon, bitmap, bitmap);
				if (gameInfo.getIcon() != null) {
					StoreApplication.getInstance().getImageLoader().get(gameInfo.getIcon(),listener);
				}
				recommendGameList.addView(gameView);
				
					TextView view = new TextView(getActivity());
					LayoutParams lp = new LayoutParams((int) (30 * Rx), LayoutParams.MATCH_PARENT);
					view.setLayoutParams(lp);
					recommendGameList.addView(view);
			}
		}
	}
	
	public void initGame(RecommendDataInfo dataInfo) {
		if (dataInfo.getData5() != null && dataInfo.getData5().length > 0) {
			GameInfo info = new GameInfo();
			info.setTitle(getResources().getString(R.string.newGameList));
			info.setGameType(1);
			info.setAdapterType(1);
			resultList.add(info);
			Collections.addAll(resultList, dataInfo.getData5());
		}
		if (dataInfo.getData6() != null && dataInfo.getData6().length > 0) {
			GameInfo info = new GameInfo();
			info.setTitle(getResources().getString(R.string.popularGameList));
			info.setGameType(2);
			info.setAdapterType(1);
			resultList.add(info);
			Collections.addAll(resultList, dataInfo.getData6());
		}
		if (dataInfo.getData7() != null && dataInfo.getData7().length > 0) {
			GameInfo info = new GameInfo();
			info.setTitle(getResources().getString(R.string.willGameList));
			info.setGameType(3);
			info.setAdapterType(1);
			resultList.add(info);
			Collections.addAll(resultList, dataInfo.getData7());
		}
		
		setState(resultList, resultList.size());
		adapter.notifyDataSetChanged();
	}
	
	private void setState(List<GameInfo> infos,int count){
		List<InstalledApp> apps = InstalledAppTool.getPersonalApp(activity);
		int size = count;
		if (infos.size() < count) {
			count = infos.size();
		}
		for (int i = infos.size() - size; i < infos.size(); i++) {
			if (resultList.get(i).getAdapterType() == 0) {
				DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(infos.get(i).getId()));
				int state = InstalledAppTool.contain(apps, resultList.get(i).getPackage_name(), Integer.parseInt(infos.get(i).getVersion()));
				if (state != -1) {
					infos.get(i).setState(state);
				} else {
					if (info != null && (info.getDownloadState() == DownloadManager.STATE_INSTALLED || info.getDownloadState() == DownloadManager.STATE_NEED_UPDATE)) {
						DownloadManager.DBManager.delete(info);
					}
				}
			}
		}
	}
	
	@Override
	public void onResponse(String arg0) {
		// TODO Auto-generated method stub
		loadStateView.loadDataSuccess();
		loadStateView.setVisibility(View.GONE);
		pullToRefreshLayout.setVisibility(View.VISIBLE);
		RecommendDataInfo dataInfo = JSON.parseObject(arg0, RecommendDataInfo.class);
		if (dataInfo.getData1() != null && dataInfo.getData1().length > 0) {
			viewPager.setData(dataInfo.getData1());
		}
		if (dataInfo.getData2() != null && dataInfo.getData2().length > 0) {
			headlineInfo = dataInfo.getData2()[0];
			initHeadline(headlineInfo);
		}
		if (dataInfo.getData3() != null && dataInfo.getData3().length > 0) {
			initRecommedGame(dataInfo.getData3());
		}
		if (dataInfo.getData4() != null && dataInfo.getData4().length > 0) {
			recommendGameInformationPager.setData(dataInfo.getData4());
		}
		initGame(dataInfo);
//		if (refreshShow) {
//			refreshShow = false;
//			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//		}
//		try {
//			JSONArray array = (JSONArray) arg0.get("item");
//			resultList = JSON.parseArray(array.toString(), GameInfo.class);
//
//			adapter.clearAllItem();
//			adapter.addItems(resultList);
//
//			for (GameInfo gameInfo : resultList) {
//				gameInfo.setAdapterType(1);
//			}
//			
//			List<InstalledApp> apps = InstalledAppTool.getPersonalApp(getActivity());
//			
//			int count = DEFAULT_PAGE_SIZE;
//			if (resultList.size() < DEFAULT_PAGE_SIZE) {
//				count = resultList.size();
//			}
//			
//			for(int i = resultList.size() - count;i<resultList.size();i++){
//				DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(resultList.get(i).getP_id()));
//				int state = InstalledAppTool.contain(apps,resultList.get(i).getPackagename(), Integer.parseInt(resultList.get(i).getVersion_code()));
//				if (resultList.get(i).getUrl().endsWith(".zip")) {
//					Log.i("TAG", resultList.get(i).getName() + " / " + resultList.get(i).getUrl());
//				}
//				if (state != -1) {
//					resultList.get(i).setState(state);
//				}else {
//					if (info != null 
//							&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED
//							|| info.getDownloadState() == DownloadManager.STATE_NEED_UPDATE)) {
//						DownloadManager.DBManager.delete(info);
//					}
//				}
//			}
//			adapter.notifyDataSetChanged();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.boutiqueLin:
			startActivity(new Intent(getActivity(), BoutiqueActivity.class));
			break;

		case R.id.thematicLin:
			startActivity(new Intent(getActivity(), ThematicActivity.class));
			break;

		case R.id.rankingLin:
			startActivity(new Intent(getActivity(), RankingActivity.class));
			break;

		case R.id.sortLin:
			startActivity(new Intent(getActivity(), SortActivity.class));
			break;

		case R.id.headlineContenLin:
			if (headlineInfo.getUrl() != null) {
				startActivity(new Intent(getActivity(), HeadlineActivity.class)
				.putExtra("isWeb", true)
				.putExtra("url", headlineInfo.getUrl()));
			}
			break;
		}
	}
	
	private BroadcastReceiver deleteReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("deleted_downloaded_files_action".equals(action)) {
				String pkg = intent.getStringExtra("pkg");
				for(GameInfo info : resultList){
//					if (info.getPackagename().equals(pkg)) {
//						info.setState(DownloadManager.STATE_NONE);
//					}
				}
				adapter.notifyDataSetChanged();
			}
		};
	};

	private BroadcastReceiver appInstallReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
				String addPkg = intent.getDataString().split(":")[1];
				for(GameInfo info : resultList){
					if (!TextUtils.isEmpty(info.getPackage_name()) 
							&& info.getPackage_name().equals(addPkg)) {
						info.setState(DownloadManager.STATE_INSTALLED);
						QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
						DownloadAppinfo downloadAppinfo = qb.where(Properties.Id.eq(info.getId())).unique();
						if (downloadAppinfo != null) {
							downloadAppinfo.setDownloadState(DownloadManager.STATE_INSTALLED);
							DownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(downloadAppinfo);
						}
					}
				}
			}else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
				String removePkg = intent.getDataString().split(":")[1];
				for(GameInfo info : resultList){
					if (!TextUtils.isEmpty(info.getPackage_name()) 
							&& info.getPackage_name().equals(removePkg)) {
						info.setState(DownloadManager.STATE_NONE);
						QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
						DownloadAppinfo downloadAppinfo = qb.where(Properties.Id.eq(info.getId())).unique();
						if (downloadAppinfo != null) {
							DownloadManager.DBManager.getDownloadAppinfoDao().delete(downloadAppinfo);
						}
					}
				}
			}
			adapter.notifyDataSetChanged();
		}
	};
	
	public void onDestroyView() {
		super.onDestroyView();
		if (adapter != null) {
			adapter.stopObserver();
		}
		viewPager.cancleTimer();
		recommendGameInformationPager.cancleTimer();
		getActivity().unregisterReceiver(appInstallReceiver);
		getActivity().unregisterReceiver(deleteReceiver);
	};
	
	class LoadDataTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
