package com.jiqu.view;

import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 游戏测评页下面的游戏下载view
 */
public class EvaluationBottomView extends RelativeLayout implements OnClickListener{
	
	public ImageView gameIcon;
	public TextView gameName;
	public RatingBarView gameScoreBar;
	public TextView gameInformation;
	private int[] blueIDs = new int[3];
	private LinearLayout downloadLin;
	private ImageView downloadImg;
	private TextView downloadTx;
	private LinearLayout inforLin;
	
	public EvaluationBottomView(Context context){
		super(context);
		init(context);
	}
	
	public EvaluationBottomView(Context context,AttributeSet attributeSet){
		super(context, attributeSet);
		init(context);
	}
	
	public EvaluationBottomView(Context context,AttributeSet attributeSet,int style){
		super(context, attributeSet, style);
		init(context);
	}
	
	private void initStar(){
		blueIDs[0] = R.drawable.star3_blue;
		blueIDs[1] = R.drawable.star2_blue;
		blueIDs[2] = R.drawable.star1_blue;
	}
	
	private void init(Context context){
		initStar();
		LayoutInflater inflater = LayoutInflater.from(context);
		RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.evaluation_bottom_view_layout, this);
		gameIcon = (ImageView) view.findViewById(R.id.gameIcon);
		gameName = (TextView) view.findViewById(R.id.gameName);
		gameScoreBar = (RatingBarView) view.findViewById(R.id.gameScoreBar);
		gameInformation = (TextView) view.findViewById(R.id.gameInformation);
		inforLin = (LinearLayout) view.findViewById(R.id.inforLin);
		
		downloadLin = (LinearLayout) view.findViewById(R.id.downloadLin);
		downloadImg = (ImageView) view.findViewById(R.id.downloadImg);
		downloadTx = (TextView) view.findViewById(R.id.downloadTx);
		
		gameScoreBar.setResID(blueIDs);
		gameScoreBar.setRating(3.5f);
		
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(gameIcon, 160 * MetricsTool.Rx, 160 * MetricsTool.Rx);
		gameScoreBar.setStarSize(40, 40);
		UIUtil.setViewSize(downloadImg, 52, 52);
		UIUtil.setViewSize(downloadLin, 360 * MetricsTool.Rx, 115 * MetricsTool.Ry);
		
		UIUtil.setTextSize(gameName, 55);
		UIUtil.setTextSize(gameInformation, 35);
		UIUtil.setTextSize(downloadTx, 50);
		
		try {
			UIUtil.setViewSizeMargin(downloadTx, 30 * MetricsTool.Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(downloadLin, 0, 0, 60 * MetricsTool.Rx, 0);
			UIUtil.setViewSizeMargin(gameIcon, 60 * MetricsTool.Rx, 0, 0, 0);
//			UIUtil.setViewSizeMargin(gameIcon, 60 * MetricsTool.Rx, 30 * MetricsTool.Ry, 0, 30 * MetricsTool.Ry);
			UIUtil.setViewSizeMargin(inforLin, 20 * MetricsTool.Rx, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.downloadBtn) {
			
		}
	}
}
