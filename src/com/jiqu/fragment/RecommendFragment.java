package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.activity.BoutiqueActivity;
import com.jiqu.activity.DetailActivity;
import com.jiqu.activity.GameEvaluationInformationActivity;
import com.jiqu.activity.HeadlineActivity;
import com.jiqu.activity.RankingActivity;
import com.jiqu.activity.SortActivity;
import com.jiqu.activity.ThematicActivity;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.adapter.ViewPagerAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.download.AppInfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.FileUtil;
import com.jiqu.download.UnZipManager;
import com.jiqu.download.Upgrade;
import com.jiqu.object.GameInfo;
import com.jiqu.object.GameInformation;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.RecommendAppsInfo;
import com.jiqu.object.RecommendDataInfo;
import com.jiqu.object.RecommendHeadlineInfo;
import com.jiqu.object.TopRecommendItem;
import com.jiqu.object.TopRecommendtInfo;
import com.vr.store.R;
import com.jiqu.tools.CountDownTimer;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadDataDialog.OnLoadAgain;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.LoadDataDialog;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.PullableListView;
import com.jiqu.view.RecommedGameView;

import de.greenrobot.dao.query.QueryBuilder;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class RecommendFragment extends BaseFragment implements OnPageChangeListener, OnRefreshListener, 
	ErrorListener, OnClickListener, Listener<String>{
	private static final int DEFAULT_PAGE_SIZE = 20;
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
	private ImageView recommendGameInformationImg;
	private HorizontalScrollView scrollView;
	private TextView emptyView;

	private LinearLayout imgList;
	private RelativeLayout recommendImgRel;
	private ViewPager recommendImgViewPager;
	private ImageView[] radioImgs,contentImgs;
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
	private CountDownTimer timer;
	private RecommendHeadlineInfo headlineInfo;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
//		getActivity().registerReceiver(appInstallReceiver, filter);
		
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
	
	/**
	 * 顶部轮换图片
	 */
	private void loadTopData() {
		requestTool.initParam();
		requestTool.startTopRecommendRequest(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				// TODO Auto-generated method stub
				topRecommendtInfo = JSON.parseObject(arg0.toString(), TopRecommendtInfo.class);
				if (topRecommendtInfo != null) {
//					initTop();
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * 获取推荐游戏
	 */
	private void loadRecommendApps(){
		requestTool.initParam();
		requestTool.setParam("start_position", "0");
		requestTool.setParam("size", "5");
		requestTool.startRecommendAppsRequest(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				// TODO Auto-generated method stub
				RecommendAppsInfo info = JSON.parseObject(arg0.toString(), RecommendAppsInfo.class);
//				initRecommendApps(info);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
//	private void initRecommendApps(RecommendAppsInfo info){
//		if (info != null && info.getItem().length > 0) {
//			int count = info.getItem().length;
//			for (int i = 0; i < count; i++) {
//				final GameInfo gameInfo = info.getItem()[i];
//				RecommedGameView gameView = new RecommedGameView(getActivity());
//				gameView.setClickable(true);
//				gameView.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						startActivity(new Intent(getActivity(), DetailActivity.class).putExtra("p_id", String.valueOf(gameInfo.getP_id())));
//					}
//				});
//				gameView.gameName.setText(gameInfo.getName());
//				gameView.getDesView().setText(gameInfo.getShort_description());
//				ImageListener listener = ImageLoader.getImageListener(gameView.gameIcon,R.drawable.ic_launcher, R.drawable.ic_launcher);
//				StoreApplication.getInstance().getImageLoader().get(gameInfo.getLdpi_icon_url(),listener);
//				recommendGameList.addView(gameView);
//				
//					TextView view = new TextView(getActivity());
//					LayoutParams lp = new LayoutParams((int) (30 * Rx), LayoutParams.MATCH_PARENT);
//					view.setLayoutParams(lp);
//					recommendGameList.addView(view);
//			}
//		}
//	}
	
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
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		recommendImgViewPager.getCurrentItem();
		
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
	public View initView() {
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		view = LayoutInflater.from(activity).inflate(R.layout.recommend_1, null);
		headView = LayoutInflater.from(getActivity()).inflate(R.layout.recommend_head, null);
		loadStateView = (LoadStateView) view.findViewById(R.id.loadStateView);
		pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		recommendListView = (PullableListView) view.findViewById(R.id.recommendListView);

		pullToRefreshLayout.setOnRefreshListener(this);

		initHeadView();

		recommendListView.addHeaderView(headView);
		recommendListView.setHasHead(true);

		recommendListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(getActivity(), DetailActivity.class).putExtra("p_id", resultList.get(position - 1).getP_id()));
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
		UIUtil.setViewSize(recommendImgRel, MetricsTool.width, 455 * Ry);
		UIUtil.setViewSize(recommendCategory, MetricsTool.width, 150 * Ry);
		UIUtil.setViewSize(boutiqueImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(thematicImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(rankingImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(sortImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(headlinesLin, MetricsTool.width, 90 * Ry);
		UIUtil.setViewSize(headlineContenImg, 72 * Rx, 72 * Rx);
		UIUtil.setViewSize(emptyView, MetricsTool.width, 25 * Ry);

		UIUtil.setTextSize(headlinesInformation, 30);

		UIUtil.setViewSize(recommendGameInformationImg, MetricsTool.width, 235 * Ry);

		UIUtil.setTextSize(boutiqueTx, 40);
		UIUtil.setTextSize(thematicTx, 40);
		UIUtil.setTextSize(rankingTx, 40);
		UIUtil.setTextSize(sortTx, 40);
		UIUtil.setTextSize(headlinesTx, 45);

		try {
			UIUtil.setViewSizeMargin(scrollView, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(recommendGameInformationImg, 0, 25 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(imgList, 0, 0, 0, 30 * Ry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHeadView() {
		imgList = (LinearLayout) headView.findViewById(R.id.imgList);
		recommendImgViewPager = (ViewPager) headView.findViewById(R.id.recommendImgViewPager);
		
		recommendImgViewPager.setOnPageChangeListener(this);
		
		boutiqueLin = (LinearLayout) headView.findViewById(R.id.boutiqueLin);
		thematicLin = (LinearLayout) headView.findViewById(R.id.thematicLin);
		rankingLin = (LinearLayout) headView.findViewById(R.id.rankingLin);
		sortLin = (LinearLayout) headView.findViewById(R.id.sortLin);

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

		headlineContenLin.setOnClickListener(this);

		boutiqueTx = (TextView) headView.findViewById(R.id.boutiquTx);
		thematicTx = (TextView) headView.findViewById(R.id.thematicTx);
		rankingTx = (TextView) headView.findViewById(R.id.rankingTx);
		sortTx = (TextView) headView.findViewById(R.id.sortTx);

		scrollView = (HorizontalScrollView) headView.findViewById(R.id.scrollView);
		emptyView = (TextView) headView.findViewById(R.id.emptyView);

		recommendCategory = (LinearLayout) headView.findViewById(R.id.recommendCategory);
		recommendImgRel = (RelativeLayout) headView.findViewById(R.id.recommendImgRel);
		recommendGameList = (LinearLayout) headView.findViewById(R.id.recommendGameList);

		recommendGameInformationImg = (ImageView) headView.findViewById(R.id.recommendGameInformationImg);
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

	private void initTop(GameInfo[] infos){
		final int count = infos.length;
		radioImgs = new ImageView[count];
		contentImgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			final GameInfo item = infos[i];
			ImageView view = new ImageView(getActivity());
			LayoutParams lp = new LayoutParams((int)(20 * Rx), (int) (20 * Rx));
			if (i != 0) {
				lp.leftMargin = (int) (10 * Rx);
			}
			 view.setLayoutParams(lp);
			if (i== 0) {
				view.setBackgroundResource(R.drawable.dian_blue);
			}else {
				view.setBackgroundResource(R.drawable.dian_white);
			}
			radioImgs[i] = view;
			imgList.addView(view);
			
			ImageView contentImg = new ImageView(getActivity());
			contentImg.setClickable(true);
			contentImg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), DetailActivity.class).putExtra("id", item.getId()));
				}
			});
			LayoutParams params = new LayoutParams(android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
					android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
			contentImg.setLayoutParams(params);
			contentImg.setScaleType(ScaleType.FIT_XY);
			ImageListener listener = ImageLoader.getImageListener(contentImg,R.drawable.ic_launcher, R.drawable.ic_launcher);
			StoreApplication.getInstance().getImageLoader().get(item.getPic(),listener);
			contentImgs[i] = contentImg;
		}
		ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), contentImgs);
		recommendImgViewPager.setAdapter(adapter);
		
		timer = new CountDownTimer(Integer.MAX_VALUE, 5000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if (recommendImgViewPager != null && count != 0) {
					recommendImgViewPager.setCurrentItem(currentIndex % count);
					currentIndex++;
				}
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		};
		timer.start();
	}
	
	private void initHeadline(RecommendHeadlineInfo headlineInfo){
		headlinesInformation.setText(headlineInfo.getTitle());
	}
	
	@Override
	public void onResponse(String arg0) {
		// TODO Auto-generated method stub
		loadStateView.loadDataSuccess();
		loadStateView.setVisibility(View.GONE);
		pullToRefreshLayout.setVisibility(View.VISIBLE);
		RecommendDataInfo dataInfo = JSON.parseObject(arg0, RecommendDataInfo.class);
		if (dataInfo.getData1() != null && dataInfo.getData1().length > 0) {
			initTop(dataInfo.getData1());
		}
		if (dataInfo.getData2() != null && dataInfo.getData2().length > 0) {
			headlineInfo = dataInfo.getData2()[0];
			initHeadline(headlineInfo);
		}
		if (dataInfo.getData4() != null && dataInfo.getData4().getPic() != null) {
			ImageListener listener = ImageLoader.getImageListener(recommendGameInformationImg, R.drawable.ic_launcher, R.drawable.ic_launcher);
			StoreApplication.getInstance().getImageLoader().get(dataInfo.getData4().getPic(), listener);
		}
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

//	private BroadcastReceiver appInstallReceiver = new BroadcastReceiver(){
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			String action = intent.getAction();
//			if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
//				String addPkg = intent.getDataString().split(":")[1];
//				for(GameInfo info : resultList){
//					if (info.getPackagename().equals(addPkg)) {
//						info.setState(DownloadManager.STATE_INSTALLED);
//						QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
//						DownloadAppinfo downloadAppinfo = qb.where(Properties.Id.eq(info.getP_id())).unique();
//						if (downloadAppinfo != null) {
//							downloadAppinfo.setDownloadState(DownloadManager.STATE_INSTALLED);
//							DownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(downloadAppinfo);
//						}
//					}
//				}
//			}else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
//				String removePkg = intent.getDataString().split(":")[1];
//				for(GameInfo info : resultList){
//					if (info.getPackagename().equals(removePkg)) {
//						info.setState(DownloadManager.STATE_NONE);
//						QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
//						DownloadAppinfo downloadAppinfo = qb.where(Properties.Id.eq(info.getP_id())).unique();
//						if (downloadAppinfo != null) {
//							DownloadManager.DBManager.getDownloadAppinfoDao().delete(downloadAppinfo);
//						}
//					}
//				}
//			}
//			adapter.notifyDataSetChanged();
//		}
//	};
	
	public void onDestroyView() {
		super.onDestroyView();
		if (adapter != null) {
			adapter.stopObserver();
		}
		if (timer != null) {
			timer.cancel();
		}
//		getActivity().unregisterReceiver(appInstallReceiver);
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
