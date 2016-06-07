package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.activity.HeadlineActivity;
import com.jiqu.adapter.InformationAdapter;
import com.jiqu.object.InformationInfo;
import com.jiqu.object.InformationItemInfo;
import com.jiqu.object.InformationPagerInfo;
import com.jiqu.object.InformationPagerItemInfo;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.MyScrollView;
import com.jiqu.view.PullUpListView;
import com.jiqu.view.PullUpListView.MyPullUpListViewCallBack;
import com.jiqu.view.ViewPagerLinView;
import com.vr.store.R;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InformationNewFragment extends BaseFragment implements OnClickListener,MyPullUpListViewCallBack,OnItemClickListener{
	private final String HEADLINE_PAGER_REQUEST = "headlinePagerRequest";
	private final String HEADLINE_NEWS_REQUEST = "headlineNewsRequest";
	private final String HEADLINE_ALL_REQUEST = "headlineAllRequest";
	private RequestTool requestTool;
	private View view;
	
	private LoadStateView loadView;
	private MyScrollView contentScroll;
	private ViewPagerLinView pagerView;
	private LinearLayout explosiveHeadlinesLin;
	private LinearLayout allHeadlinesLin;
	private LinearLayout headlineLin;
	private ImageView explosiveHeadlinesImg,allHeadlinesImg;
	private TextView explosiveHeadlinesTx,allHeadlinesTx;
	
	private RelativeLayout allHeadlineRel;
	private RelativeLayout listViewRel;
	private LoadStateView allHeadlineLoadView;
	private PullUpListView hotHeadlineListView;
	private PullUpListView allHeadlineListView;
	
	private List<InformationItemInfo> hotInformations = new ArrayList<InformationItemInfo>();
	private List<InformationItemInfo> allInformations = new ArrayList<InformationItemInfo>();
	private InformationAdapter hotHeadlineAdapter;
	private InformationAdapter allHeadlineAdapter;
	
	private int hotHeadlinePageNum = 1;
	private int allHeadlinePageNum = 1;
	private boolean hotHeadlineLoading = false;
	private boolean allHeadlineLoading = false;
	private boolean isFirst = true;
	private boolean isFirstLoad = true;
	private InformationPagerItemInfo[] viewPagerData;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		requestTool = RequestTool.getInstance();
	}
	
	@Override
	public View initView() {
		// TODO Auto-generated method stub
		view = LayoutInflater.from(activity)
				.inflate(R.layout.information_new_fragment, null);
		
		loadView = (LoadStateView) view.findViewById(R.id.loadView);
		contentScroll = (MyScrollView) view.findViewById(R.id.contentScroll);
		pagerView = (ViewPagerLinView) view.findViewById(R.id.pagerView);
		pagerView.setDefaultImgId(R.drawable.recommend_viewpager_default);
		pagerView.setClass(HeadlineActivity.class);
		explosiveHeadlinesLin = (LinearLayout) view.findViewById(R.id.explosiveHeadlinesLin);
		allHeadlinesLin = (LinearLayout) view.findViewById(R.id.allHeadlinesLin);
		headlineLin = (LinearLayout) view.findViewById(R.id.headlineLin);
		explosiveHeadlinesImg = (ImageView) view.findViewById(R.id.explosiveHeadlinesImg);
		allHeadlinesImg = (ImageView) view.findViewById(R.id.allHeadlinesImg);
		explosiveHeadlinesTx = (TextView) view.findViewById(R.id.explosiveHeadlinesTx);
		allHeadlinesTx = (TextView) view.findViewById(R.id.allHeadlinesTx);
		hotHeadlineListView = (PullUpListView) view.findViewById(R.id.hotHeadlineListView);
		allHeadlineListView = (PullUpListView) view.findViewById(R.id.allHeadlineListView);
		allHeadlineRel = (RelativeLayout) view.findViewById(R.id.allHeadlineRel);
		allHeadlineLoadView = (LoadStateView) view.findViewById(R.id.allHeadlineLoadView);
		listViewRel = (RelativeLayout) view.findViewById(R.id.listViewRel);
		
		explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.blue));
		
		hotHeadlineListView.initBottomView();
		hotHeadlineListView.setMyPullUpListViewCallBack(this);
		allHeadlineListView.initBottomView();
		allHeadlineListView.setMyPullUpListViewCallBack(this);
		
		explosiveHeadlinesLin.setOnClickListener(this);
		allHeadlinesLin.setOnClickListener(this);
		loadView.loadAgain(this);
		allHeadlineLoadView.loadAgain(this);
		
		hotHeadlineListView.setOnItemClickListener(this);
		allHeadlineListView.setOnItemClickListener(this);
		
		hotHeadlineAdapter = new InformationAdapter(activity, hotInformations, 0);
		allHeadlineAdapter = new InformationAdapter(activity, allInformations, 0);
		hotHeadlineListView.setAdapter(hotHeadlineAdapter);
		allHeadlineListView.setAdapter(allHeadlineAdapter);
		
		initViewSize();
		
		return view;
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(pagerView, MetricsTool.width, 455 * MetricsTool.Ry);
		UIUtil.setViewSize(headlineLin, MetricsTool.width, 75 * MetricsTool.Ry);
		UIUtil.setViewSize(explosiveHeadlinesImg, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		UIUtil.setViewSize(allHeadlinesImg, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		UIUtil.setViewHeight(hotHeadlineListView, MetricsTool.height - 75 * MetricsTool.Ry - 180 * MetricsTool.Rx - 180 * MetricsTool.Rx );
		UIUtil.setViewHeight(allHeadlineListView, MetricsTool.height - 75 * Ry - 180 * Rx - 180 * Rx );
		UIUtil.setViewHeight(allHeadlineLoadView, MetricsTool.height - 75 * Ry - 180 * Rx - 180 * Rx );
		UIUtil.setViewHeight(listViewRel, MetricsTool.height - 75 * Ry - 180 * Rx - 180 * Rx );
		
		UIUtil.setTextSize(explosiveHeadlinesTx, 40);
		UIUtil.setTextSize(allHeadlinesTx, 40);
		
		try {
			UIUtil.setViewSizeMargin(headlineLin, 0, 20 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		loadPagerData(RequestTool.HEADLINE_TOP_URL);
	}
	
	private void loadPagerData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				InformationPagerInfo info = JSON.parseObject(arg0, InformationPagerInfo.class);
				if (info != null) {
					if (info.getStatus() == 1) {
						viewPagerData = info.getData();
					}
				}
				loadHotHeadlineData(RequestTool.HOT_HEADLINE_URL);
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataFail();
			}
			
		}, requestTool.getMap(), HEADLINE_PAGER_REQUEST);
	}
	
	private void loadHotHeadlineData(String url){
		if (hotHeadlineLoading) {
			return;
		}
		hotHeadlineListView.startRefresh();
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataSuccess();
				loadView.setVisibility(View.GONE);
				contentScroll.setVisibility(View.VISIBLE);
				InformationInfo info = JSON.parseObject(arg0, InformationInfo.class);
				if (info != null) {
					if (info.getStatus() == 1) {
						hotHeadlinePageNum++;
						Collections.addAll(hotInformations, info.getData());
					}else if (info.getStatus() == 0) {
						Toast.makeText(activity, R.string.notMore, Toast.LENGTH_SHORT).show();
					}
				}
				if (isFirstLoad) {
					isFirstLoad = false;
					pagerView.setData(viewPagerData);
				}
				hotHeadlineAdapter.notifyDataSetChanged();
				hotHeadlineListView.refreshFinish();
				hotHeadlineLoading = false;
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataFail();
				hotHeadlineLoading = false;
				hotHeadlineListView.refreshFinish();
			}
		}, requestTool.getMap(), HEADLINE_NEWS_REQUEST);
	}
	
	private void loadAllData(String url){
		if (allHeadlineLoading) {
			return;
		}
		allHeadlineListView.startRefresh();
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				allHeadlineLoadView.loadDataSuccess();
				allHeadlineLoadView.setVisibility(View.GONE);
				allHeadlineListView.setVisibility(View.VISIBLE);
				InformationInfo info = JSON.parseObject(arg0, InformationInfo.class);
				if (info != null) {
					if (info.getStatus() == 1) {
						allHeadlinePageNum++;
						Collections.addAll(allInformations, info.getData());
					}else if (info.getStatus() == 0) {
						Toast.makeText(activity, R.string.notMore, Toast.LENGTH_SHORT).show();
					}
				}
				allHeadlineListView.refreshFinish();
				allHeadlineAdapter.notifyDataSetChanged();
				allHeadlineLoading = false;
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				allHeadlineLoading = false;
				allHeadlineLoadView.loadDataFail();
				allHeadlineListView.refreshFinish();
			}
		}, requestTool.getMap(), HEADLINE_ALL_REQUEST);
	}

	@Override
	public void scrollBottomState(int viewId) {
		// TODO Auto-generated method stub
		if (viewId == R.id.hotHeadlineListView) {
			loadHotHeadlineData(RequestTool.HOT_HEADLINE_URL + "?pageNum=" + hotHeadlinePageNum); 
		}else if (viewId == R.id.allHeadlineListView) {
			loadAllData(RequestTool.ALL_HEADLINE_URL + "?pageNum=" + allHeadlinePageNum);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (explosiveHeadlinesLin == v) {
			allHeadlineRel.setVisibility(View.GONE);
			hotHeadlineListView.setVisibility(View.VISIBLE);
			explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.blue));
			allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
		}else if (allHeadlinesLin == v) {
			hotHeadlineListView.setVisibility(View.GONE);
			allHeadlineRel.setVisibility(View.VISIBLE);
			explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
			allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.blue));
			if (isFirst) {
				isFirst = false;
				loadAllData(RequestTool.ALL_HEADLINE_URL);
			}
		}else if (loadView.getLoadBtn() == v) {
			loadView.showLoading();
			loadPagerData(RequestTool.HEADLINE_TOP_URL);
		}else if (allHeadlineLoadView.getLoadBtn() == v) {
			
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (parent.getId() == R.id.hotHeadlineListView) {
			if (position < hotInformations.size()) {
				startActivity(new Intent(getActivity(), HeadlineActivity.class)
				.putExtra("info", hotInformations.get(position))
				.putExtra("isWeb", true)
				.putExtra("url", hotInformations.get(position).getUrl()));
			}
		}else if (parent.getId() == R.id.allHeadlineListView) {
			if (position < allInformations.size()) {
				startActivity(new Intent(getActivity(), HeadlineActivity.class)
				.putExtra("info", hotInformations.get(position))
				.putExtra("isWeb", true)
				.putExtra("url", allInformations.get(position).getUrl()));
			}
		}
	}
}
