package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.CommomProblemAdapter;
import com.jiqu.object.CommonProblemInfo;
import com.jiqu.object.CommonProblemItem;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.TitleView;

public class CommomProblemActivity extends BaseActivity implements OnClickListener{
	private final String PROBLEM_REQUEST = "problemRequest";
	private TitleView titleView;
	private Button feedback;
	private ListView problemListView;
	private LoadStateView loadView;
	private RelativeLayout contentRel;
	private List<CommonProblemItem> items = new ArrayList<CommonProblemItem>();
	private CommomProblemAdapter adapter;
	
	private RequestTool requestTool;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.commom_problem_layout;
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		feedback = (Button) findViewById(R.id.feedback);
		problemListView = (ListView) findViewById(R.id.problemListView);
		loadView = (LoadStateView) findViewById(R.id.loadView);
		contentRel = (RelativeLayout) findViewById(R.id.contentRel);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.commomProblem);
		
		initViewSize();
		
		adapter = new CommomProblemAdapter(this, items);
		problemListView.setAdapter(adapter);
		
		feedback.setOnClickListener(this);
		loadView.loadAgain(this);
		
		problemListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CommomProblemActivity.this, GameEvaluationWebInfoActivity.class)
				.putExtra("item", items.get(position)));
			}
		});
		
		loadData();
	}
	
	private void initViewSize(){
		UIUtil.setViewHeight(feedback, 115 * Ry);
		UIUtil.setTextSize(feedback, 40);
		problemListView.setDividerHeight((int)(25 * Ry));
	}

	private void loadData(){
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataSuccess();
				loadView.setVisibility(View.GONE);
				contentRel.setVisibility(View.VISIBLE);
				CommonProblemInfo problemInfo = JSON.parseObject(arg0, CommonProblemInfo.class);
				if (problemInfo != null && problemInfo.getStatus() == 1
						&& problemInfo.getData() != null) {
					Collections.addAll(items, problemInfo.getData());
					adapter.notifyDataSetChanged();
				}
			}
		}, RequestTool.PROBLEM_URL, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataFail();
			}
			
		}, map, PROBLEM_REQUEST);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		requestTool.stopRequest(PROBLEM_REQUEST);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == feedback) {
			startActivity(new Intent(this, FeedbackActivity.class).putExtra("title", "问题反馈"));
		}else if (v == loadView.getLoadBtn()) {
			loadView.showLoading();
			loadData();
		}
	}
}
