package com.jiqu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.application.StoreApplication;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.EvaluationBottomView;
import com.jiqu.view.EvaluationItemView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.RatingBarView;
import com.jiqu.view.TitleView;

public class GameEvaluationInformationActivity extends BaseActivity implements OnRefreshListener{
	private RelativeLayout parent;
	private TitleView titleView;
	private TextView evaluationTitle;
	private LinearLayout informationLin;
	private TextView evaluationTime;
	private TextView evaluationAuthor;
	private TextView browseCount;
	private RelativeLayout coefficientRel;
	private LinearLayout coefficientLin;
	private TextView comprehensiveEvaluation;
	private TextView evaluationScore;
	private RatingBarView comprehensiveBar;
	private ImageView gameImg;
	private TextView information;
	private ImageView view1,view3;
	private RelativeLayout comprehensiveRel;
	private LinearLayout scoreLin;
	private RatingBarView screenSenseBar,immersionBar,gameplayBar,difficultyBar;
	private TextView screenSense,immersion,gameplay,difficultyNumber;
	private RelativeLayout screenSenseRel,immersionRel,gameplayRel,difficultyNumberRel;
	private TextView detailedTitle;
	private LinearLayout detailedEvaLin;
	private EvaluationBottomView evaGameView;
	private TextView emptyView;
	
	private int[] IDs = new int[3];
	private int[] blueIDs = new int[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.game_evaluation_information_head;
	}
	
	private void initStar(){
		IDs[0] = R.drawable.ratingbg;
		IDs[1] = R.drawable.rating_sencond_progress;
		IDs[2] = R.drawable.rating_progress;
		
		blueIDs[0] = R.drawable.star3_blue;
		blueIDs[1] = R.drawable.star2_blue;
		blueIDs[2] = R.drawable.star1_blue;
	}
	
	private void initView(){
		initStar();
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		evaluationTitle = (TextView) findViewById(R.id.evaluationTitle);
		informationLin =(LinearLayout) findViewById(R.id.informationLin);
		evaluationTime = (TextView) findViewById(R.id.evaluationTime);
		evaluationAuthor = (TextView) findViewById(R.id.evaluationAuthor);
		browseCount = (TextView) findViewById(R.id.browseCount);
		gameImg = (ImageView) findViewById(R.id.gameImg);
		information = (TextView) findViewById(R.id.information);
		view1 = (ImageView) findViewById(R.id.view1);
		view3 = (ImageView) findViewById(R.id.view3);
		scoreLin = (LinearLayout) findViewById(R.id.scoreLin);
		
		screenSense = (TextView) findViewById(R.id.screenSense);
		immersion = (TextView) findViewById(R.id.immersion);
		gameplay = (TextView) findViewById(R.id.gameplay);
		difficultyNumber = (TextView) findViewById(R.id.difficultyNumber);
		
		screenSenseBar = (RatingBarView) findViewById(R.id.screenSenseBar);
		immersionBar = (RatingBarView) findViewById(R.id.immersionBar);
		gameplayBar = (RatingBarView) findViewById(R.id.gameplayBar);
		difficultyBar = (RatingBarView) findViewById(R.id.difficultyBar);
		
		screenSenseRel = (RelativeLayout) findViewById(R.id.screenSenseRel);
		immersionRel = (RelativeLayout) findViewById(R.id.immersionRel);
		gameplayRel = (RelativeLayout) findViewById(R.id.gameplayRel);
		difficultyNumberRel = (RelativeLayout) findViewById(R.id.difficultyNumberRel);
		
		comprehensiveRel = (RelativeLayout) findViewById(R.id.comprehensiveRel);
		comprehensiveEvaluation = (TextView) findViewById(R.id.comprehensiveEvaluation);
		evaluationScore = (TextView) findViewById(R.id.evaluationScore);
		comprehensiveBar = (RatingBarView) findViewById(R.id.comprehensiveBar);
		
		detailedTitle = (TextView) findViewById(R.id.detailedTitle);
		
		detailedEvaLin = (LinearLayout) findViewById(R.id.detailedEvaLin);
		evaGameView = (EvaluationBottomView) findViewById(R.id.evaGameView);
		
		emptyView = (TextView) findViewById(R.id.emptyView);

		screenSenseBar.setResID(IDs);
		immersionBar.setResID(IDs);
		gameplayBar.setResID(IDs);
		difficultyBar.setResID(IDs);
		
		
		comprehensiveBar.setResID(blueIDs);
		
		titleView.setActivity(this);
		titleView.tip.setText(R.string.gameEvaluation);
		titleView.tip.setTextColor(getResources().getColor(R.color.blue));
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
		UIUtil.setTextSize(screenSense, 40);
		UIUtil.setTextSize(immersion, 40);
		UIUtil.setTextSize(gameplay, 40);
		UIUtil.setTextSize(difficultyNumber, 40);
		UIUtil.setTextSize(evaluationTime, 25);
		UIUtil.setTextSize(evaluationAuthor, 25);
		UIUtil.setTextSize(browseCount, 25);
		UIUtil.setTextSize(information, 35);
		UIUtil.setViewHeight(scoreLin, 350 * Ry);
		UIUtil.setTextSize(comprehensiveEvaluation, 70);
		UIUtil.setTextSize(evaluationScore, 100);
		comprehensiveBar.setStarSize(47, 47);
		UIUtil.setTextSize(detailedTitle, 45);
		UIUtil.setViewHeight(detailedTitle, 80 * Ry);
		UIUtil.setViewHeight(evaGameView, 215 * Ry);
		UIUtil.setViewHeight(emptyView, 215 * Ry);
		
		UIUtil.setViewPadding(detailedTitle, (int)(55 * Rx), 0, 0, 0);
		
		UIUtil.setViewSize(gameImg, 1030 * Rx, 515 * Ry);
		
		try {
			UIUtil.setViewSizeMargin(evaluationTitle, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(informationLin, 0, 20 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(gameImg, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(information, 0, 45 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(view1, 0, 45 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(immersionRel, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(gameplayRel, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(difficultyNumberRel, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(evaluationAuthor, 15 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(browseCount, 15 * Rx, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
