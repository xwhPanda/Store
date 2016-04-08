package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jiqu.adapter.CommomProblemAdapter;
import com.jiqu.object.CommomProblemItem;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

public class CommomProblemActivity extends BaseActivity {
	private TitleView titleView;
	private LinearLayout feedbackLin;
	private Button support,feedback;
	private ImageView line;
	private ListView problemListView;
	private List<CommomProblemItem> items = new ArrayList<CommomProblemItem>();
	
	private CommomProblemAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.commom_problem_layout;
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		feedbackLin = (LinearLayout) findViewById(R.id.feedbackLin);
		support = (Button) findViewById(R.id.support);
		feedback = (Button) findViewById(R.id.feedback);
		line = (ImageView) findViewById(R.id.line);
		problemListView = (ListView) findViewById(R.id.problemListView);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.commomProblem);
		
		initViewSize();
		
		for(int i = 0;i<6;i++){
			CommomProblemItem item = new CommomProblemItem();
			items.add(item);
		}
		adapter = new CommomProblemAdapter(this, items);
		problemListView.setAdapter(adapter);
	}
	
	private void initViewSize(){
		UIUtil.setViewHeight(feedbackLin, 115 * Ry);
		UIUtil.setViewHeight(line, 90 * Ry);
		
		UIUtil.setTextSize(feedback, 40);
		UIUtil.setTextSize(support, 40);
		
		problemListView.setDividerHeight((int)(25 * Ry));
		
		try {
			UIUtil.setViewSizeMargin(problemListView, 0, 165 * Ry, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
