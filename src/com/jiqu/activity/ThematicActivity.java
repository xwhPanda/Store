package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.adapter.ThematicAdapter;
import com.jiqu.object.ThematicInfo;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.HeaderGridView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableGridView;
import com.jiqu.view.TitleView;

public class ThematicActivity extends BaseActivity {
	private TitleView titleView;
	private PullToRefreshLayout refreshView;
	private HeaderGridView thematicGridView;
	
	private View headView;
	private ViewPager viewPager;
	private RelativeLayout groupRel;
	private TextView title;
	private LinearLayout viewGroup;
	
	private List<ThematicInfo> thematicInfos = new ArrayList<ThematicInfo>();
	private ThematicAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.thematic_layout;
	}

	private void initView(){
		initHeadView();
		titleView = (TitleView) findViewById(R.id.titleView);
		refreshView = (PullToRefreshLayout) findViewById(R.id.refreshView);
		thematicGridView = (HeaderGridView) findViewById(R.id.thematicGridView);
		
		thematicGridView.addHeaderView(headView);
		
		titleView.tip.setText(getResources().getString(R.string.thematic));
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		
		for(int i = 0 ; i < 20 ; i++){
			ThematicInfo thematicInfo = new ThematicInfo();
			thematicInfos.add(thematicInfo);
		}
		adapter = new ThematicAdapter(this, thematicInfos);
		thematicGridView.setAdapter(adapter);
		
		initViewSize();
	}
	
	private void initHeadView(){
		LayoutInflater inflater = LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.thematic_head_layout, null);
		
		viewPager = (ViewPager) headView.findViewById(R.id.viewPager);
		groupRel = (RelativeLayout) headView.findViewById(R.id.groupRel);
		title = (TextView) headView.findViewById(R.id.title);
		viewGroup = (LinearLayout) headView.findViewById(R.id.viewGroup);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(groupRel, MetricsTool.width, 85 * Ry);
		UIUtil.setViewHeight(viewPager, 460 * Ry);
		
		
		UIUtil.setTextSize(title, 35);
		
		thematicGridView.setVerticalSpacing((int)(40 * Ry));
		
		try {
			UIUtil.setViewSizeMargin(title, 40 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(viewGroup, 0, 0, 40 * Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
