package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.adapter.RecommendGameAdapter;
import com.jiqu.object.GameInformation;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;

public class BoutiqueActivity extends BaseActivity {
	private TitleView titleView;
	private PullToRefreshLayout refreshLayout;
	private ListView boutiqueListView;
	
	private View headView;
	private RelativeLayout pagerRel;
	private ViewPager viewPager;
	private LinearLayout viewGroup;
	private RelativeLayout allGameRel;
	private ImageView totalGameImg;
	private TextView totalGameTx;
	private TextView addedTime;
	
	private RecommendGameAdapter adapter;
	private List<GameInformation> informations = new ArrayList<GameInformation>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.boutique_layout;
	}
	
	private void initView(){
		initHeadView();
		titleView = (TitleView) findViewById(R.id.titleView);
		refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshView);
		boutiqueListView = (PullableListView) findViewById(R.id.boutiqueListView);
		
		boutiqueListView.addHeaderView(headView);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(getResources().getString(R.string.boutiqueTitle));
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		
		initViewSize();
		
		for(int i = 0 ; i < 10 ; i++){
			GameInformation information = new GameInformation();
			informations.add(information);
		}
		adapter = new RecommendGameAdapter(informations, this);
		boutiqueListView.setAdapter(adapter);
	}
	
	private void initHeadView(){
		LayoutInflater inflater = LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.boutique_head_layout, null);
		
		pagerRel = (RelativeLayout) headView.findViewById(R.id.pagerRel);
		viewPager = (ViewPager) headView.findViewById(R.id.viewPager);
		viewGroup = (LinearLayout) headView.findViewById(R.id.viewGroup);
		allGameRel = (RelativeLayout) headView.findViewById(R.id.allGameRel);
		totalGameImg = (ImageView) headView.findViewById(R.id.totalGameImg);
		totalGameTx = (TextView) headView.findViewById(R.id.totalGameTx);
		addedTime = (TextView) headView.findViewById(R.id.addedTiem);
	}
	
	private void initViewSize(){
		UIUtil.setViewHeight(viewPager, 460 * Ry);
		UIUtil.setViewSize(allGameRel, 1050 * Rx, 75 * Ry);
		UIUtil.setViewSize(totalGameImg, 56 * Rx, 56 * Rx);
		
		UIUtil.setTextSize(totalGameTx, 45);
		UIUtil.setTextSize(addedTime, 30);
		
		try {
			UIUtil.setViewSizeMargin(totalGameImg, 20 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(totalGameTx, 10 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(addedTime, 0, 0, 20 * Rx, 0);
			UIUtil.setViewSizeMargin(allGameRel, 0, 25 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
