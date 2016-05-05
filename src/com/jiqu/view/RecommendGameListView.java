package com.jiqu.view;

import com.vr.store.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 推荐页新游榜、网游榜view
 *
 */
public class RecommendGameListView extends View {
	private Context context;
	private TextView listTitle;
	private Button moreBtn;
	private LinearLayout recommendGameLin;

	public RecommendGameListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}
	
	public void init(){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.game_list_view_layout, null, false);
		listTitle = (TextView) view.findViewById(R.id.listTitle);
		moreBtn = (Button) view.findViewById(R.id.moreBtn);
		recommendGameLin = (LinearLayout) view.findViewById(R.id.recommendGameLin);
	}

}
