package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jiqu.activity.GameEvaluationInformationActivity;
import com.jiqu.adapter.EvaluationGridViewAdapter;
import com.jiqu.object.GameInformation;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class EvaluationFragment extends BaseFragment implements OnRefreshListener{
	private float Rx,Ry;
	private View view;
	private PullToRefreshLayout pullToRefreshLayout;
	private GridView evaluationGridView;
	private RequestTool requestTool;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		requestTool = RequestTool.getInstance();
	}
	
	@Override
	public View initView(){
		view = LayoutInflater.from(activity).inflate(R.layout.evaluation, null);
		pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		evaluationGridView = (GridView) view.findViewById(R.id.content_view);
		pullToRefreshLayout.setOnRefreshListener(this);
		
		initViewSize();
		
		List<GameInformation> informations = new ArrayList<GameInformation>();
		for(int i = 0; i < 30 ; i++){
			GameInformation information = new GameInformation();
			informations.add(information);
		}
		
		evaluationGridView.setAdapter(new EvaluationGridViewAdapter(getActivity(), informations));
		
		evaluationGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), GameEvaluationInformationActivity.class));
			}
		});
		
		return view;
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}
	
	private void initViewSize(){
		evaluationGridView.setVerticalSpacing((int)(30 * Ry));
		UIUtil.setViewWidth(evaluationGridView, 1060 * Rx);
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
