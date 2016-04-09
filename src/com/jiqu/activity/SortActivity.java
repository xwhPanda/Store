package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.jiqu.adapter.SortAdapter;
import com.jiqu.object.SortItem;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.view.TitleView;

public class SortActivity extends BaseActivity {
	private TitleView titleView;
	private GridView sortGridView;
	
	private SortAdapter adapter;
	private List<SortItem> sortItems = new ArrayList<SortItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
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
		
		for(int i = 0 ; i < 9 ; i++){
			SortItem sortItem = new SortItem();
			sortItem.setTitle("射击游戏");
			sortItem.setNewAddCount(2);
			sortItem.setTotal(23);
			sortItems.add(sortItem);
		}
		adapter = new SortAdapter(this, sortItems);
		sortGridView.setAdapter(adapter);
		
		
		sortGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(SortActivity.this, SortInfoActivity.class));
			}
		});
	}

}
