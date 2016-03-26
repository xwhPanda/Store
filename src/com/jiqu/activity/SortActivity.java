package com.jiqu.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jiqu.adapter.RecommendGameAdapter;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.SortTitleView;

public class SortActivity extends BaseActivity implements OnRefreshListener{
	private View headView;
	private SortTitleView sortTitle;
	private ListView sortListView;
	private PullToRefreshLayout refreshLayout;
	private RecommendGameAdapter adapter;
	
	private ImageView sortHeadNewAddGameImg;
	private TextView sortHeadNewAddGameContent;
	//分类的类型
	private int type = 0;
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			init();
		}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.sort_layout;
	}
	
	private void init(){
		sortTitle = (SortTitleView) findViewById(R.id.sortTitle);
		sortTitle.setActivity(this);
		sortListView = (ListView) findViewById(R.id.sortListView);
		refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
		refreshLayout.setOnRefreshListener(this);
		
		//根据分类的不同加载不同的头部
		LayoutInflater inflater = LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.sort_head_new_add_game, null);
		sortHeadNewAddGameImg = (ImageView) headView.findViewById(R.id.sortHeadNewAddGameImg);
		sortHeadNewAddGameContent = (TextView) headView.findViewById(R.id.sortHeadNewAddGameContent);
		sortListView.addView(headView);
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}

}
