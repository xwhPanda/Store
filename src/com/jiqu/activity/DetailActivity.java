package com.jiqu.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.RatingBarView;
import com.jiqu.view.TitleView;

public class DetailActivity extends BaseActivity {
	private TitleView titleView;
	private LinearLayout gameIconLin;
	private ImageView gameIcon;
	private RelativeLayout titleLine;
	private TextView gameName;
	private TextView downloadCount;
	private TextView version;
	private TextView type;
	private TextView size;
	private LinearLayout controlWayLin;
	private RelativeLayout headControlRel;
	private ImageView headControlImg;
	private TextView headControlTx;
	private RelativeLayout handleRel;
	private ImageView handleImg;
	private TextView handleTx;
	private RelativeLayout vertigoLin;
	private TextView vertigo;
	private ImageView vertigoValue;
	private TextView low;
	private TextView high;
	private ImageView view;
	private ViewPager viewPager;
	private RelativeLayout pagerRel;
	private LinearLayout viewGroup;
	private Button download;
	
	private LinearLayout scoreLin;
	private RatingBarView screenSenseBar,immersionBar,gameplayBar,difficultyBar;
	private TextView screenSense,immersion,gameplay,difficultyNumber;
	private RelativeLayout screenSenseRel,immersionRel,gameplayRel,difficultyNumberRel;
	private TextView comprehensiveEvaluation;
	private TextView evaluationScore;
	private RatingBarView comprehensiveBar;
	
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
		return R.layout.detail_layout;
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
		
		titleView = (TitleView) findViewById(R.id.titleView);
		gameIconLin = (LinearLayout) findViewById(R.id.gameIconLin);
		gameIcon = (ImageView) findViewById(R.id.gameIcon);
		titleLine = (RelativeLayout) findViewById(R.id.titleLine);
		gameName = (TextView) findViewById(R.id.gameName);
		downloadCount = (TextView) findViewById(R.id.downloadCount);
		version = (TextView) findViewById(R.id.version);
		type = (TextView) findViewById(R.id.type);
		size = (TextView) findViewById(R.id.size);
		controlWayLin = (LinearLayout) findViewById(R.id.controlWayLin);
		headControlRel = (RelativeLayout) findViewById(R.id.headControlRel);
		headControlImg = (ImageView) findViewById(R.id.headControlImg);
		headControlTx = (TextView) findViewById(R.id.headControlTx);
		handleRel = (RelativeLayout) findViewById(R.id.handleRel);
		handleImg = (ImageView) findViewById(R.id.handleImg);
		handleTx = (TextView) findViewById(R.id.handleTx);
		vertigoLin = (RelativeLayout) findViewById(R.id.vertigoLin);
		vertigo = (TextView) findViewById(R.id.vertigo);
		vertigoValue = (ImageView) findViewById(R.id.vertigoValue);
		low = (TextView) findViewById(R.id.low);
		high = (TextView) findViewById(R.id.high);
		view = (ImageView) findViewById(R.id.view);
		pagerRel = (RelativeLayout) findViewById(R.id.pagerRel);
		viewGroup = (LinearLayout) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		download = (Button) findViewById(R.id.download);
		
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
		
		comprehensiveEvaluation = (TextView) findViewById(R.id.comprehensiveEvaluation);
		evaluationScore = (TextView) findViewById(R.id.evaluationScore);
		comprehensiveBar = (RatingBarView) findViewById(R.id.comprehensiveBar);
		
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText("的风景海湾");
		titleView.tip.setTextColor(getResources().getColor(R.color.blue));
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		
		screenSenseBar.setResID(IDs);
		immersionBar.setResID(IDs);
		gameplayBar.setResID(IDs);
		difficultyBar.setResID(IDs);
		
		screenSenseBar.setRating(2.5);
		immersionBar.setRating(2.5);
		gameplayBar.setRating(2.5);
		difficultyBar.setRating(2.5);
		
		comprehensiveBar.setResID(blueIDs);
		comprehensiveBar.setRating(2.5);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setTextSize(screenSense, 40);
		UIUtil.setTextSize(immersion, 40);
		UIUtil.setTextSize(gameplay, 40);
		UIUtil.setTextSize(difficultyNumber, 40);
		UIUtil.setTextSize(comprehensiveEvaluation, 70);
		UIUtil.setTextSize(evaluationScore, 100);
		
		UIUtil.setTextSize(gameName, 40);
		UIUtil.setTextSize(downloadCount, 35);
		UIUtil.setTextSize(version, 35);
		UIUtil.setTextSize(type, 35);
		UIUtil.setTextSize(size, 35);
		UIUtil.setTextSize(headControlTx, 45);
		UIUtil.setTextSize(handleTx, 45);
		UIUtil.setTextSize(vertigo, 35);
		UIUtil.setTextSize(low, 25);
		UIUtil.setTextSize(high, 25);
		
		UIUtil.setViewSize(gameIcon, 190 * Rx, 190 * Rx);
		UIUtil.setViewSize(headControlImg, 110 * Rx, 60 * Rx);
		UIUtil.setViewSize(handleImg, 110 * Rx, 60 * Rx);
		UIUtil.setViewSize(headControlRel, 460 * Rx, 90 * Ry);
		UIUtil.setViewSize(handleRel, 460 * Rx, 90 * Ry);
		UIUtil.setViewSize(vertigoValue, 750 * Rx, 30 * Rx);
		UIUtil.setViewSize(pagerRel, 1040 * Rx, 515 * Ry);
		UIUtil.setViewSize(download, 805 * Rx, 95 * Ry);
		
		
		UIUtil.setViewHeight(scoreLin, 350 * Ry);
		UIUtil.setViewHeight(vertigoLin, 120 * Ry);
		
		comprehensiveBar.setStarSize(47, 47);
		
		try {
			UIUtil.setViewSizeMargin(gameIcon, 50 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(titleLine, 30 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(gameIconLin, 0, 45 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(downloadCount, 0, 25 * Ry, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(type, 0, 25 * Ry, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(controlWayLin, 0, 50 * Ry, 0, 50 * Ry);
			UIUtil.setViewSizeMargin(headControlRel, 0, 0, 60 * Rx, 0);
			UIUtil.setViewSizeMargin(headControlTx, 15 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(handleTx, 15 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(vertigoValue, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(low, 0, 5 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(high, 0, 5 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(pagerRel, 0, 15 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(download, 0, 105 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
