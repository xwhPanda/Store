package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jiqu.adapter.RecommendGameAdapter;
import com.jiqu.object.GameInformation;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.TitleView;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.SortTitleView;

public class SortInfoActivity extends BaseActivity implements OnRefreshListener{
	private View headView;
	private SortTitleView sortTitle;
	private TitleView titleView;
	private ListView sortListView;
	private PullToRefreshLayout refreshLayout;
	private RecommendGameAdapter adapter;
	
	private ImageView sortHeadNewAddGameImg;
	private TextView sortHeadNewAddGameContent;
	
	private List<GameInformation> gameInformations = new ArrayList<GameInformation>();
	//分类的类型
	private int type = 0;
	
	private String title = "极趣网络";
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			init();
		}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.sort_info_layout;
	}
	
	private void init(){
//		sortTitle = (SortTitleView) findViewById(R.id.sortTitle);
//		sortTitle.setActivity(this);
		
		Intent intent = getIntent();
		if (intent.hasExtra("title")) {
			title = intent.getStringExtra("title");
		}
		
		titleView = (TitleView) findViewById(R.id.titleView);
		sortListView = (ListView) findViewById(R.id.sortListView);
		refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
		refreshLayout.setOnRefreshListener(this);
		
		titleView.setActivity(this);
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(title);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		
		
		//根据分类的不同加载不同的头部
//		LayoutInflater inflater = LayoutInflater.from(this);
//		headView = inflater.inflate(R.layout.sort_head_new_add_game, null);
//		sortHeadNewAddGameImg = (ImageView) headView.findViewById(R.id.sortHeadNewAddGameImg);
//		sortHeadNewAddGameContent = (TextView) headView.findViewById(R.id.sortHeadNewAddGameContent);
//		sortListView.addHeaderView(headView);
		
		for(int i = 0; i<20;i++){
			GameInformation gameInformation = new GameInformation();
			gameInformations.add(gameInformation);
		}
		
		adapter = new RecommendGameAdapter(gameInformations, this);
		sortListView.setAdapter(adapter);
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
