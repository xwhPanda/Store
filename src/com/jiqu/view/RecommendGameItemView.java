package com.jiqu.view;

import com.vr.store.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RecommendGameItemView extends View {
	private View view;
	private TextView subscriptTx;
	private ImageView gameIcon;
	public TextView gameName,gameDes,gameSize;
	private RatingBar gameScoreBar;
	private Button downloadBtn;

	public RecommendGameItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.recommend_game_item, null);
		
		subscriptTx = (TextView) view.findViewById(R.id.subscriptTx);
		gameIcon = (ImageView) view.findViewById(R.id.gameIcon);
		gameName = (TextView) view.findViewById(R.id.gameName);
		gameDes = (TextView) view.findViewById(R.id.gameDes);
		gameSize = (TextView) view.findViewById(R.id.gameSize);
		gameScoreBar = (RatingBar) view.findViewById(R.id.gameScore);
		downloadBtn = (Button) view.findViewById(R.id.downloadBtn);
	}
	
	
}
