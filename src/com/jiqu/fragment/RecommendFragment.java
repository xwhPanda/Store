package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jiqu.activity.RankingActivity;
import com.jiqu.activity.SortActivity;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.download.AppInfo;
import com.jiqu.object.GameInformation;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.MyImageView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.RecommedGameView;
import com.jiqu.view.RecommendGameItemView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class RecommendFragment extends Fragment implements OnPageChangeListener,OnRefreshListener,Listener<String>,ErrorListener,OnClickListener{
	private float Rx,Ry;
	private View view;
	private View headView;
	
	private LinearLayout boutiqueLin,thematicLin,rankingLin,sortLin;
	private ImageView boutiqueImg,thematicImg,rankingImg,sortImg;
	private TextView boutiqueTx,thematicTx,rankingTx,sortTx;
	private LinearLayout recommendGameList;
	private LinearLayout recommendCategory;
	private LinearLayout headlinesLin;
	private TextView headlinesTx;
	private ImageView headlineContenImg;
	private TextView headlinesInformation;
	private ImageView recommendGameInformationImg;
	private HorizontalScrollView scrollView;
	private TextView emptyView;
	
	private RelativeLayout recommendImgRel;
	private ViewPager recommendImgViewPager;
	private List<View> views = new ArrayList<View>();
	private View view1,view2,view3;
	private int currentItem = 0;
	
	private PullToRefreshLayout pullToRefreshLayout;
	private ListView recommendListView;
	private GameAdapter adapter;
	private List<AppInfo> resultList = new ArrayList<AppInfo>();
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.recommend_1, container, false);
		headView = inflater.inflate(R.layout.recommend_head, null);
		
		initView();
		
		initAdapter();
		
		String url_qihoo360 = "http://recommend.api.sj.360.cn/inew/getRecomendApps?iszip=1&logo_type=2&deflate_field=1&apiversion=2&os=19&model=G620S-UL00&sn=4.589389937671455&cu=qualcomm+technologies%2C+inc+msm8916&bannertype=1&withext=1&vc=300030184&zjbb=1&datatype=adgame&page=1&fm=home&m=b033525c2a96a00c2bfd48c673522449&m2=3363e38bf819414c6c81d886ff878e2a&v=3.1.84&re=1&ch=432403&ppi=720x1280&startCount=1&snt=-1";
		final StringRequest stringRequest = new StringRequest(url_qihoo360, this, this);
		StoreApplication.getInstance().getRequestQueue().add(stringRequest);
		
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
	
	private void initAdapter(){
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
	
	private void initView(){
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		recommendListView = (ListView) view.findViewById(R.id.recommendListView);
		
		
		pullToRefreshLayout.setOnRefreshListener(this);
		
		initHeadView();
		
		recommendListView.addHeaderView(headView);
		
		List<GameInformation> gameInformations = new ArrayList<GameInformation>();
		
		for (int i = 0; i < 30; i++)
		{
			GameInformation game = new GameInformation();
			if (i % 4 == 1) {
				game.adapterType = 0;
			}else {
				game.adapterType = 1;
			}
			gameInformations.add(game);
		}
		
//		GameAdapter adapter = new GameAdapter(getActivity(), gameInformations);
//		recommendListView.setAdapter(adapter);
		
		initViewSize();
	}
	
	private void initViewSize(){
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
	
	private void initHeadView(){
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
		headlinesTx = (TextView) headView.findViewById(R.id.headlinesTx);
		headlineContenImg = (ImageView) headView.findViewById(R.id.headlineContenImg);
		headlinesInformation = (TextView) headView.findViewById(R.id.headlinesInformation);
		
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
		
		
		for(int i = 0;i< 3;i++){
			RecommedGameView gameView = new RecommedGameView(getActivity());
			gameView.gameName.setText("英雄联盟");
			gameView.getDesView().setText("哈哈哈哈哈哈哈");
			recommendGameList.addView(gameView);
			if (i < 2) {
				TextView view = new TextView(getActivity());
				LayoutParams lp = new LayoutParams((int)(30 * Rx),  LayoutParams.MATCH_PARENT);
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
		
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(String response) {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject(response);
			int errNo = obj.getInt("errno");
			if (errNo == 0) {
				if (resultList.size() > 0) {
					resultList.clear();
				}
				JSONObject object = (JSONObject) obj.get("data");
				JSONArray array = (JSONArray) object.get("fixed");
				resultList = JSON.parseArray(array.toString(), AppInfo.class);
				
				adapter.clearAllItem();
				adapter.addItems(resultList);
			}
			for(AppInfo appInfos:resultList){
				appInfos.setAdapterType(1);
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
			
			break;

		case R.id.thematicLin:
			break;
			
		case R.id.rankingLin:
			startActivity(new Intent(getActivity(), RankingActivity.class));
			break;
			
		case R.id.sortLin:
			startActivity(new Intent(getActivity(), SortActivity.class));
			break;
		}
	}
}
