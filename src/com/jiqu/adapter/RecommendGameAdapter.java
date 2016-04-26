package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.GameInformation;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.RatingBarView;

import android.content.Context;
import android.provider.Contacts.Intents.UI;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

/**
 * 推荐页新游榜、网游榜adapter
 */
public class RecommendGameAdapter extends BaseAdapter {
	private List<GameInformation> games;
	private LayoutInflater inflater;
	private Context context;
	private float Rx,Ry;
	private int[] resIds = new int[3];

	public RecommendGameAdapter(List<GameInformation> gameInformations,Context context){
		games = gameInformations;
		this.context = context;
		inflater = LayoutInflater.from(context);
		
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		
		initIds();
	}
	
	private void initIds(){
		resIds[0] = R.drawable.ratingbg;
		resIds[1] = R.drawable.rating_sencond_progress;
		resIds[2] = R.drawable.rating_progress;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return games.size();
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
			holder = new Holder();
			convertView = holder.getRootView();
			
		}else {
			holder = (Holder)convertView.getTag();
		}

		holder.setData(games.get(position));
		return convertView;
	}

	private class Holder{
		private LinearLayout subscriptLin;
		private RelativeLayout gameInforRel;
		private TextView subscriptTx;
		private ImageView gameIcon;
		private TextView gameName,gameDes,gameSize;
		private RatingBarView gameScoreBar;
		private Button downloadBtn;
		private ImageView hotIcon;
		
		private View rootView;
		
		public Holder(){
			rootView = initView();
			rootView.setTag(this);
			
			initViewSize();
		}
		
		public View getRootView(){
			return rootView;
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.recommend_game_item, null);
			
			subscriptLin = (LinearLayout) view.findViewById(R.id.subscriptLin);
			gameInforRel = (RelativeLayout) view.findViewById(R.id.gameInforRel);
			subscriptTx = (TextView) view.findViewById(R.id.subscriptTx);
			gameIcon = (ImageView) view.findViewById(R.id.icon);
			gameName = (TextView) view.findViewById(R.id.gameName);
			gameDes = (TextView) view.findViewById(R.id.gameDes);
			gameSize = (TextView) view.findViewById(R.id.gameSize);
			gameScoreBar = (RatingBarView) view.findViewById(R.id.gameScore);
			downloadBtn = (Button) view.findViewById(R.id.downloadBtn);
			hotIcon = (ImageView) view.findViewById(R.id.hotIcon);
			
			subscriptLin.setVisibility(View.INVISIBLE);
			
			gameScoreBar.setResID(resIds);
			
			view.setBackgroundColor(context.getResources().getColor(R.color.bottomBgColor));
			return view;
		}
		
		private void setData(GameInformation data){
			gameIcon.setBackgroundColor(context.getResources().getColor(R.color.red));
			gameName.setText("德玛西亚");
			gameDes.setText("谁复活我如何尽快恢复快回复");
			gameSize.setText("40M");
			downloadBtn.setBackgroundColor(context.getResources().getColor(R.color.blue));
			hotIcon.setBackgroundResource(R.drawable.hot_icon);
			hotIcon.setVisibility(View.VISIBLE);
		}
		
		private void initViewSize(){
			UIUtil.setViewSize(gameIcon, 170 * Rx, 170 * Rx);
			UIUtil.setViewSize(hotIcon, 55 * Rx, 38 * Ry);
			UIUtil.setViewSize(downloadBtn, 75 * Rx, 75 * Rx);
			
			UIUtil.setTextSize(gameName, 35);
			UIUtil.setTextSize(gameDes, 30);
			UIUtil.setTextSize(gameSize, 25);
			
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (230 * Ry));
			rootView.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(gameIcon, 35 * Rx, 0, 35 * Rx, 0);
				UIUtil.setViewSizeMargin(downloadBtn, 0, 0, 35 * Rx, 0);
				UIUtil.setViewSizeMargin(gameDes, 0, 15 * Ry, 0, 0);
				UIUtil.setViewSizeMargin(gameName, 5 * Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(gameInforRel, 0, 20 * Ry, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
