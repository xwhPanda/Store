package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jiqu.adapter.GameAdapter;
import com.jiqu.adapter.InformationAdapter;
import com.jiqu.object.GameInformation;
import com.jiqu.store.R;
import com.jiqu.view.PullToRefreshLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class GameFragment extends Fragment {
	private ListView gameListView;
	private View view,headView;
	private ViewPager informationImgViewPager;
	private LinearLayout imgList;
	private LinearLayout explosiveHeadlinesLin;
	private LinearLayout allHeadlinesLin;
	private PullToRefreshLayout ptrl;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.game, null);
		headView = inflater.inflate(R.layout.information_head, null);
		
		init();
		
		return view;
	}
	
	private void init(){
		gameListView = (ListView) view.findViewById(R.id.gameListView);
		informationImgViewPager = (ViewPager) headView.findViewById(R.id.informationImgViewPager);
		imgList = (LinearLayout) headView.findViewById(R.id.imgList);
		explosiveHeadlinesLin = (LinearLayout) headView.findViewById(R.id.explosiveHeadlinesLin);
		allHeadlinesLin = (LinearLayout) headView.findViewById(R.id.allHeadlinesLin);
		
		gameListView.addHeaderView(headView);
		
		List<GameInformation> gameInformations = new ArrayList<GameInformation>();
		
		for (int i = 0; i < 30; i++)
		{
			GameInformation game = new GameInformation();
			if (i % 4 == 1) {
				game.adapterType = 0;
			}else {
				game.adapterType = 1;
			}
			gameInformations.add(game);
		}
//		GameAdapter adapter = new GameAdapter(getActivity(), gameInformations);
		ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_view));
//		gameListView.setAdapter(adapter);
	}
}
