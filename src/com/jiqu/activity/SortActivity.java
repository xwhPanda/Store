package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.SortAdapter;
import com.jiqu.object.CategoryInfo;
import com.jiqu.object.SortItem;
import com.jiqu.store.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.TitleView;

public class SortActivity extends BaseActivity implements Listener<String> , ErrorListener{
	private final String CATEGORY_REQUEST = "categoryRequest";
	private TitleView titleView;
	private GridView sortGridView;
	private LoadStateView loadView;
	
	private SortAdapter adapter;
	private List<SortItem> sortItems = new ArrayList<SortItem>();
	
	private RequestTool requestTool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		initView();
		
		loadData(RequestTool.CATEGORY_URL);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("SortActivity");
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("SortActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.sort_layout;
	}
	
	private void loadData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, this, url, this, requestTool.getMap(), CATEGORY_REQUEST);
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		sortGridView = (GridView) findViewById(R.id.sortGridView);
		loadView = (LoadStateView) findViewById(R.id.loadView);
		
		titleView.parentView.setBackgroundResource(R.drawable.zhuanti_bg);
		titleView.setActivity(this);
		titleView.tip.setText(getResources().getString(R.string.sort));
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		
		sortGridView.setVerticalSpacing((int)(75 * MetricsTool.Rx));
		
		
		adapter = new SortAdapter(this, sortItems);
		sortGridView.setAdapter(adapter);
		
		
		sortGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(SortActivity.this, SortInfoActivity.class)
				.putExtra("fromWhere", 0)
				.putExtra("categoryItem", sortItems.get(position)));
			}
		});
		
		try {
			UIUtil.setViewSizeMargin(sortGridView, 0, 75 * MetricsTool.Rx, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(String arg0) {
		// TODO Auto-generated method stub
		loadView.loadDataSuccess();
		loadView.setVisibility(View.GONE);
		sortGridView.setVisibility(View.VISIBLE);
		CategoryInfo categoryInfo = JSON.parseObject(arg0, CategoryInfo.class);
		if (categoryInfo != null && categoryInfo.getData().length > 0) {
			Collections.addAll(sortItems, categoryInfo.getData());
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		requestTool.stopRequest(CATEGORY_REQUEST);
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
