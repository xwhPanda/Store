package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.activity.GameEvaluationInformationActivity;
import com.jiqu.activity.GameEvaluationWebInfoActivity;
import com.jiqu.adapter.EvaluationGridViewAdapter;
import com.jiqu.object.EvaluationInfo;
import com.jiqu.object.EvaluationItemInfo;
import com.jiqu.object.GameInformation;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class EvaluationFragment extends BaseFragment implements OnRefreshListener,OnClickListener{
	private final String EVALUATION_REQUEST = "evaluationRequest";
	private View view;
	private LoadStateView loadView;
	private PullToRefreshLayout pullToRefreshLayout;
	private GridView evaluationGridView;
	private List<EvaluationItemInfo> informations = new ArrayList<EvaluationItemInfo>();
	private EvaluationGridViewAdapter adapter;
	private RequestTool requestTool;
	private int pageNum = 1;
	private boolean isRefreshing = false;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		requestTool = RequestTool.getInstance();
	}
	
	@Override
	public View initView(){
		view = LayoutInflater.from(activity).inflate(R.layout.evaluation, null);
		loadView = (LoadStateView) view.findViewById(R.id.loadview);
		pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		evaluationGridView = (GridView) view.findViewById(R.id.content_view);
		pullToRefreshLayout.setOnRefreshListener(this);
		loadView.loadAgain(this);
		
		initViewSize();
		
		adapter = new EvaluationGridViewAdapter(getActivity(), informations);
		evaluationGridView.setAdapter(adapter);
		
		evaluationGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), GameEvaluationWebInfoActivity.class)
				.putExtra("info", informations.get(position)));
			}
		});
		
		return view;
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		loadData(RequestTool.EVALUATION_URL);
	}
	
	private void loadData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataSuccess();
				loadView.setVisibility(View.GONE);
				pullToRefreshLayout.setVisibility(View.VISIBLE);
				EvaluationInfo evaluationInfo = JSON.parseObject(arg0, EvaluationInfo.class);
				if (evaluationInfo != null) {
					if (evaluationInfo.getStatus() == 1) {
						pageNum++;
						Collections.addAll(informations, evaluationInfo.getData());
						adapter.notifyDataSetChanged();
					}else if (evaluationInfo.getStatus() == 0) {
						Toast.makeText(activity, R.string.notMore, Toast.LENGTH_SHORT).show();
					}
				}
				if (isRefreshing) {
					isRefreshing = false;
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataFail();
				if (isRefreshing) {
					isRefreshing = false;
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
				}
			}
			
		}, requestTool.getMap(), EVALUATION_REQUEST);
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
		isRefreshing = true;
		loadData(RequestTool.EVALUATION_URL + "?pageNum=" + pageNum);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == loadView.getLoadBtn()) {
			loadData(RequestTool.EVALUATION_URL);
		}
	}
}
