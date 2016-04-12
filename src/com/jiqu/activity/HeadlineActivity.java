package com.jiqu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.EvaluationBottomView;
import com.jiqu.view.EvaluationItemView;
import com.jiqu.view.TitleView;

public class HeadlineActivity extends BaseActivity {
	private TitleView titleView;
	private TextView evaluationTitle;
	private LinearLayout informationLin;
	private TextView evaluationTime;
	private TextView evaluationAuthor;
	private TextView browseCount;
	private ImageView gameImg;
	private TextView information;
	private LinearLayout detailedEvaLin;
	private EvaluationBottomView evaGameView;
	private ImageView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.headline_layout;
	}

	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		evaluationTitle = (TextView) findViewById(R.id.evaluationTitle);
		informationLin =(LinearLayout) findViewById(R.id.informationLin);
		evaluationTime = (TextView) findViewById(R.id.evaluationTime);
		evaluationAuthor = (TextView) findViewById(R.id.evaluationAuthor);
		browseCount = (TextView) findViewById(R.id.browseCount);
		gameImg = (ImageView) findViewById(R.id.gameImg);
		information = (TextView) findViewById(R.id.information);
		detailedEvaLin = (LinearLayout) findViewById(R.id.detailedEvaLin);
		evaGameView = (EvaluationBottomView) findViewById(R.id.evaGameView);
		view = (ImageView) findViewById(R.id.view);
		
		titleView.setActivity(this);
		titleView.tip.setText(getResources().getString(R.string.newHeadlines));
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang);
		titleView.editBtn.setVisibility(View.VISIBLE);
		
		for(int i = 0 ; i < 5 ; i++){
			EvaluationItemView itemView = new EvaluationItemView(this);
			
			ImageView view = new ImageView(this);
			view.setBackgroundResource(R.drawable.xian);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int)(2 * Ry));
			lp.topMargin = (int) (25 * MetricsTool.Ry);
			view.setLayoutParams(lp);
			
			detailedEvaLin.addView(itemView);
			if (i < 4) {
				detailedEvaLin.addView(view);
			}
		}
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setTextSize(evaluationTitle, 45);
		UIUtil.setTextSize(evaluationTime, 25);
		UIUtil.setTextSize(evaluationAuthor, 25);
		UIUtil.setTextSize(browseCount, 25);
		UIUtil.setTextSize(information, 35);
		
		UIUtil.setViewHeight(evaGameView, 215 * Ry);
		UIUtil.setViewSize(gameImg, 1030 * Rx, 515 * Ry);
		
		try {
			UIUtil.setViewSizeMargin(view, 0, 45 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(evaluationTitle, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(informationLin, 0, 20 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(gameImg, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(information, 0, 45 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(evaluationAuthor, 15 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(browseCount, 15 * Rx, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}