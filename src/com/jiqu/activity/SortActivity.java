package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.SortAdapter;
import com.jiqu.object.CategoryInfo;
import com.jiqu.object.SortItem;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.view.TitleView;

public class SortActivity extends BaseActivity implements Listener<JSONObject> , ErrorListener{
	private TitleView titleView;
	private GridView sortGridView;
	
	private SortAdapter adapter;
	private List<SortItem> sortItems = new ArrayList<SortItem>();
	
	private RequestTool requestTool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		initView();
		requestTool.startCategoryRequest(this, this);
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.sort_layout;
	}
	
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		sortGridView = (GridView) findViewById(R.id.sortGridView);
		
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		titleView.setActivity(this);
		titleView.tip.setText(getResources().getString(R.string.sort));
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		
		sortGridView.setVerticalSpacing((int)(75 * MetricsTool.Rx));
		
		titleView.editBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		adapter = new SortAdapter(this, sortItems);
		sortGridView.setAdapter(adapter);
		
		
		sortGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SortItem sortItem = sortItems.get(position);
				startActivity(new Intent(SortActivity.this, SortInfoActivity.class)
				.putExtra("fromWhere", 0)
				.putExtra("categoryTitle", sortItem.getCategory_name())
				.putExtra("categoryId", sortItem.getCategory_id()));
			}
		});
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(JSONObject arg0) {
		// TODO Auto-generated method stub
		CategoryInfo categoryInfo = JSON.parseObject(arg0.toString(), CategoryInfo.class);
		if (categoryInfo != null && categoryInfo.getItem().length > 0) {
			Collections.addAll(sortItems, categoryInfo.getItem());
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		requestTool.stopCategoryRequest();
	}
}
