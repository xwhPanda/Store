package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.GameInformation;
import com.jiqu.store.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 推荐页新游榜、网游榜adapter
 */
public class RecommendGameAdapter extends BaseAdapter {
	private List<GameInformation> games;
	private LayoutInflater inflater;
	private Context context;

	public RecommendGameAdapter(List<GameInformation> gameInformations,Context context){
		games = gameInformations;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return games.size() < 3 ? games.size() : 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return games.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.recommend_game_item, null);
			holder = new Holder();
			
			holder.subscriptTx = (TextView) convertView.findViewById(R.id.subscriptTx);
			holder.gameIcon = (ImageView) convertView.findViewById(R.id.gameIcon);
			holder.gameName = (TextView) convertView.findViewById(R.id.gameName);
			holder.gameDes = (TextView) convertView.findViewById(R.id.gameDes);
			holder.gameSize = (TextView) convertView.findViewById(R.id.gameSize);
			holder.gameScoreBar = (RatingBar) convertView.findViewById(R.id.gameScore);
			holder.downloadBtn = (Button) convertView.findViewById(R.id.downloadBtn);
			holder.hotIcon = (ImageView) convertView.findViewById(R.id.hotIcon);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		
		return convertView;
	}

	private class Holder{
		private TextView subscriptTx;
		private ImageView gameIcon;
		private TextView gameName,gameDes,gameSize;
		private RatingBar gameScoreBar;
		private Button downloadBtn;
		private ImageView hotIcon;
	}
}
