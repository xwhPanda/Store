package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jiqu.adapter.RecommendGameAdapter;
import com.jiqu.object.GameInformation;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;

public class RankingActivity extends BaseActivity implements OnClickListener{
	private TitleView titleView;
	private LinearLayout rankLin;
	private Button favorableComment,hot;
	private PullToRefreshLayout favorableRefreshView,hotRefreshView;
	private PullableListView favorableListView,hotListView;
	
	private RecommendGameAdapter favorableAdapter;
	private RecommendGameAdapter hotAdapter;
	private List<GameInformation> favorableGameInformations = new ArrayList<GameInformation>();
	private List<GameInformation> hotGameInformations = new ArrayList<GameInformation>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.ranking_layout;
	}

	private void initView(){
		titleView  = (TitleView) findViewById(R.id.titleView);
		rankLin = (LinearLayout) findViewById(R.id.rankLin);
		favorableComment = (Button) findViewById(R.id.favorableComment);
		hot = (Button) findViewById(R.id.hot);
		favorableRefreshView = (PullToRefreshLayout) findViewById(R.id.favorableRefreshView);
		hotRefreshView = (PullToRefreshLayout) findViewById(R.id.hotRefreshView);
		favorableListView = (PullableListView) findViewById(R.id.favorableListView);
		hotListView = (PullableListView) findViewById(R.id.hotListView);
		
		titleView.setActivity(this);
		titleView.tip.setText(getResources().getString(R.string.rank));
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		
		favorableComment.setOnClickListener(this);
		hot.setOnClickListener(this);
		
		initViewSize();
		
		
		
		for(int i = 0; i< 20; i++){
			GameInformation information = new GameInformation();
			favorableGameInformations.add(information);
		}
		favorableAdapter = new RecommendGameAdapter(favorableGameInformations, this);
		favorableListView.setAdapter(favorableAdapter);
		
		for(int i = 0; i< 10; i++){
			GameInformation information = new GameInformation();
			hotGameInformations.add(information);
		}
		hotAdapter = new RecommendGameAdapter(hotGameInformations, this);
		hotListView.setAdapter(hotAdapter);
	}
	
	private void initViewSize(){
		UIUtil.setTextSize(favorableComment, 40);
		UIUtil.setTextSize(hot, 40);
		UIUtil.setViewSize(favorableComment, 515 * Rx, 80 * Ry);
		UIUtil.setViewSize(hot, 515 * Rx, 80 * Ry);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.favorableComment:
			changeBg(favorableComment);
			favorableRefreshView.setVisibility(View.VISIBLE);
			hotRefreshView.setVisibility(View.INVISIBLE);
			break;

		case R.id.hot:
			changeBg(hot);
			hotRefreshView.setVisibility(View.VISIBLE);
			favorableRefreshView.setVisibility(View.INVISIBLE);
			break;
		}
	}
	
	private void changeBg(Button button){
		if (button == favorableComment) {
			favorableComment.setBackgroundColor(getResources().getColor(R.color.blue));
			hot.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
		}else if (button == hot) {
			favorableComment.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
			hot.setBackgroundColor(getResources().getColor(R.color.blue));
		}
	}
}
