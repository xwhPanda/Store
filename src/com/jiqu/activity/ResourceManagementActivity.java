package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.jiqu.adapter.ResourcesManagmentAdapter;
import com.jiqu.object.ResourcesItem;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;

public class ResourceManagementActivity extends BaseActivity {
	private TitleView titleView;
	private PullToRefreshLayout refreshView;
	private ListView resourceListView;
	private ResourcesManagmentAdapter adapter;
	private List<ResourcesItem> resourcesItems = new ArrayList<ResourcesItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.resource_management_layout;
	}

	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		refreshView = (PullToRefreshLayout) findViewById(R.id.refreshView);
		resourceListView = (PullableListView) findViewById(R.id.resourceListView);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.resourceManagement);
		
		for(int i = 0 ; i<30 ; i++){
			ResourcesItem item = new ResourcesItem();
			resourcesItems.add(item);
		}
		adapter = new ResourcesManagmentAdapter(this, resourcesItems);
		resourceListView.setAdapter(adapter);
		
		initViewSize();
	}
	
	private void initViewSize(){
		try {
			UIUtil.setViewSizeMargin(refreshView, 0, 165 * Ry, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
