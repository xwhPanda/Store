package com.jiqu.view;


import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecommedGameView extends LinearLayout {
	public ImageView gameIcon;
	public TextView gameName;
	private TextView gameDescription;
	private RelativeLayout parentView;
	public View view;

	public RecommedGameView(Context context) {
		super(context);
		init(context);
	}
	
	public RecommedGameView(Context context,AttributeSet attributeSet){
		super(context, attributeSet);
		init(context);
	}
	
	
	public void init(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.recommend_game_layout,this);
		
		parentView = (RelativeLayout) view.findViewById(R.id.parentView);
		gameIcon = (ImageView) view.findViewById(R.id.gameIcon);
		gameName = (TextView) view.findViewById(R.id.gameName);
		gameDescription = (TextView) view.findViewById(R.id.gameDescription);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(parentView, 340 * MetricsTool.Rx, 550 * MetricsTool.Ry);
		UIUtil.setViewSize(gameIcon, LayoutParams.MATCH_PARENT, 415 * MetricsTool.Ry);
		
		UIUtil.setTextSize(gameName, 45);
		UIUtil.setTextSize(gameDescription, 25);
		
		try {
			UIUtil.setViewSizeMargin(gameName, 0, 20 * MetricsTool.Ry, 0, 0);
			UIUtil.setViewSizeMargin(gameDescription, 0, 10 * MetricsTool.Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ImageView getIconView(){
		return gameIcon;
	}
	
	public TextView getNameView(){
		return gameName;
	}
	
	public TextView getDesView(){
		return gameDescription;
	}
	
}
