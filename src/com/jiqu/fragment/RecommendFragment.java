package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jiqu.activity.BoutiqueActivity;
import com.jiqu.activity.DetailActivity;
import com.jiqu.activity.GameEvaluationInformationActivity;
import com.jiqu.activity.HeadlineActivity;
import com.jiqu.activity.RankingActivity;
import com.jiqu.activity.SortActivity;
import com.jiqu.activity.ThematicActivity;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.AppInfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.UnZipManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.GameInformation;
import com.jiqu.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.PullableListView;
import com.jiqu.view.RecommedGameView;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class RecommendFragment extends Fragment implements OnPageChangeListener, OnRefreshListener, ErrorListener, OnClickListener, Listener<JSONObject> {
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

	private RelativeLayout recommendImgRel;
	private ViewPager recommendImgViewPager;
	private List<View> views = new ArrayList<View>();
	private View view1, view2, view3;
	private int currentItem = 0;

	private PullToRefreshLayout pullToRefreshLayout;
	private PullableListView recommendListView;
	private GameAdapter adapter;
	private List<GameInfo> resultList = new ArrayList<GameInfo>();
	
	private Map<String, String> map = new HashMap<String, String>();
	private String url = "http://xu8api.91xuxu.com/api/1.0/getHomeRecommend";
	
	private boolean refreshShow = false;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.recommend_1, container, false);
		headView = inflater.inflate(R.layout.recommend_head, null);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		getActivity().registerReceiver(appInstallReceiver, filter);
		
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("deleted_downloaded_files_action");
		getActivity().registerReceiver(deleteReceiver, filter2);

		initView();

		initAdapter();

		// String url_qihoo360 =
		// "http://recommend.api.sj.360.cn/inew/getRecomendApps?iszip=1&logo_type=2&deflate_field=1&apiversion=2&os=19&model=G620S-UL00&sn=4.589389937671455&cu=qualcomm+technologies%2C+inc+msm8916&bannertype=1&withext=1&vc=300030184&zjbb=1&datatype=adgame&page=1&fm=home&m=b033525c2a96a00c2bfd48c673522449&m2=3363e38bf819414c6c81d886ff878e2a&v=3.1.84&re=1&ch=432403&ppi=720x1280&startCount=1&snt=-1";
		// // final StringRequest stringRequest = new
		// StringRequest(url_qihoo360, this, this);
		// JsonObjectRequest objectRequest = new
		// JsonObjectRequest(Method.GET,url_qihoo360, this, this);
		// StoreApplication.getInstance().addToRequestQueue(objectRequest,
		// "recommend");
		// StoreApplication.getInstance().getRequestQueue().start();

		map.put("android_id", "a9f7234301030848");
		map.put("imei", "000000000000000");
		map.put("start_position", "0");
		map.put("versionCode", "127");
		map.put("versionName", "0.1.27");
		map.put("size", "20");
		map.put("device_id", "000000000000000");
		map.put("mac", "08:00:27:d3:53:ef");
		JSONObject object = new JSONObject(map);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, url, object, this, this);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "recommend");
		StoreApplication.getInstance().getRequestQueue().start();

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adapter != null) {
			adapter.startObserver();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (adapter != null) {
			adapter.stopObserver();
		}
	}

	private void initAdapter() {
		if (adapter == null) {
			adapter = new GameAdapter(getActivity(), resultList);
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
	}

	private void initView() {
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		recommendListView = (PullableListView) view.findViewById(R.id.recommendListView);

		pullToRefreshLayout.setOnRefreshListener(this);

		initHeadView();

		recommendListView.addHeaderView(headView);
		recommendListView.setHasHead(true);

		initViewSize();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHeadView() {
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

		for (int i = 0; i < 3; i++) {
			RecommedGameView gameView = new RecommedGameView(getActivity());
			gameView.gameName.setText("英雄联盟");
			gameView.getDesView().setText("哈哈哈哈哈哈哈");
			recommendGameList.addView(gameView);
			if (i < 2) {
				TextView view = new TextView(getActivity());
				LayoutParams lp = new LayoutParams((int) (30 * Rx), LayoutParams.MATCH_PARENT);
				view.setLayoutParams(lp);
				recommendGameList.addView(view);
			}
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
		map.put("size", String.valueOf(resultList.size() + 20));
		JSONObject object = new JSONObject(map);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, url, object, this, this);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "recommend");
		StoreApplication.getInstance().getRequestQueue().start();
		
		refreshShow = true;
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResponse(JSONObject arg0) {
		// TODO Auto-generated method stub
		Log.i("TAG", "onResponse");
		if (refreshShow) {
			refreshShow = false;
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
		}
		try {
			JSONArray array = (JSONArray) arg0.get("item");
			resultList = JSON.parseArray(array.toString(), GameInfo.class);

			adapter.clearAllItem();
			adapter.addItems(resultList);

			for (GameInfo gameInfo : resultList) {
				gameInfo.setAdapterType(1);
				if (gameInfo.getUrl().endsWith(".zip")) {
					Log.i("TAG", gameInfo.getName() + "/" + gameInfo.getApp_size() + "/" + gameInfo.getUrl());
				}
			}
			
			for(int i = resultList.size() - 20;i<resultList.size();i++){
				int state = InstalledAppTool.contain(resultList.get(i).getPackagename(), Integer.parseInt(resultList.get(i).getVersion_code()));
				if (state != -1) {
					resultList.get(i).setState(state);
				}
			}
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
			// startActivity(new Intent(getActivity(), HeadlineActivity.class));
			startActivity(new Intent(getActivity(), DetailActivity.class));
			break;
		}
	}
	
	private BroadcastReceiver deleteReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("deleted_downloaded_files_action".equals(action)) {
				String pkg = intent.getStringExtra("pkg");
				for(GameInfo info : resultList){
					if (info.getPackagename().equals(pkg)) {
						info.setState(DownloadManager.STATE_NONE);
					}
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
			}else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
				String removePkg = intent.getDataString().split(":")[1];
				for(GameInfo info : resultList){
					if (info.getPackagename().equals(removePkg)) {
						info.setState(DownloadManager.STATE_NONE);
					}
				}
				adapter.notifyDataSetChanged();
			}
		}
	};
	
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().unregisterReceiver(appInstallReceiver);
	};
}
